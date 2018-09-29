package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.SerializableBiFunction;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Enhancement to Vaadin's Grid to handle Java Beans in a specialized way. Please note that this is not supporting the functionality supported
 * by Vaadin's Bean Grid but has similar functionality. The main difference in use is that instead of addColumn methods, one should use the
 * createColumn methods. createColumn methods just return whether column can be created or not but, columns are created at a later stage
 * when all columns are defined and the ordinality of columns are determined. If you want to customize any Column, it can be done in
 * customizeColumn method or by invoking methods provided in this claas. Each column has a "column name" and it gets mapped to the Bean's getXXX
 * method just like in Vaadin's Bean Grid. However, if a getXXX method is available in the DataGrid itself, that will be used for sourcing the
 * data for the respective column. Each column uses its respective column name as the key.</BR>
 * Public/protected methods that do not have documentation are self-explanatory from the method names itself.
 *
 * @param <T> Bean type
 * @author Syam
 */
public class DataGrid<T> extends Grid<T> implements ExecutableView {

    private final Class<T> objectClass;
    private Map<String, Renderer<T>> renderers = new HashMap<>();
    private Map<String, Boolean> columnResizable = new HashMap<>();
    private Map<String, Boolean> columnVisible = new HashMap<>();
    private Map<String, Boolean> columnFrozen = new HashMap<>();
    private Object methodHandlerHost;
    private int paramId = 0;
    private ButtonIcon configure;
    private ObjectColumnCreator<T> columnCreator;
    private Iterable<String> columns;
    private String caption;
    private boolean allowColumnReordering = true;
    private T objectRendered;
    private View view;

    /**
     * Constructor that will generate columns from the Bean's properties.
     * @param objectClass Bean type
     */
    public DataGrid(Class<T> objectClass) {
        this(objectClass, null);
    }

    /**
     * Constructor that will generate columns from the column names passed
     * @param objectClass Bean type
     * @param columns Column names
     */
    public DataGrid(Class<T> objectClass, Iterable<String> columns) {
        this.objectClass = objectClass;
        this.columns = columns;
        addAttachListener(e -> init());
        super.setDataProvider(new DataSupplier<>());
    }

    /**
     * This method is invoked once all the columns are built and the grid is ready to display.
     */
    protected void constructed() {
    }

    private void init() {
        if(renderers == null) {
            return;
        }
        if(columns != null) {
            columns.forEach(n -> {
                if(includeColumn(n)) {
                    createColumn(n);
                }
            });
            columns = null;
        } else {
            Stream<String> columnNames = getColumnNames();
            if(columnNames != null) {
                columnNames.filter(this::includeColumn).forEach(this::createColumn);
            } else {
                generateColumns();
            }
        }
        renderers.keySet().stream().filter(this::includeColumn).
                sorted(Comparator.comparingInt(this::getColumnOrder)).forEach(this::constructColumn);
        renderers = null;
        columnResizable = null;
        columnVisible = null;
        columnFrozen = null;
        getElement().setAttribute("theme", "row-stripes wrap-cell-content");
        constructHeader(createHeader());
        if(columnCreator != null) {
            columnCreator.close();
            columnCreator = null;
        }
        super.setColumnReorderingAllowed(allowColumnReordering);
        constructed();
    }

    /**
     * Set the "theme" to make the appearence of this grid compact. It uses less space than normal and more rows will be visible.
     */
    public void compact() {
        getElement().setAttribute("theme", "compact row-stripes wrap-cell-content");
    }

    /**
     * Get the currently rendered object. This method is useful when you have your own methods mapped for column values and you want to do any special setup
     * or computation.
     * @return Currently rendered object.
     */
    public T getObjectRendered() {
        return objectRendered;
    }

    /**
     * This method is invoked just before each row is rendered. You can do special set up or comuptation at this stage.
     * @param object Currently rendered object.
     */
    public void render(@SuppressWarnings("unused") T object) {
    }

    private void setRO(T object) {
        if(object == objectRendered) {
            return;
        }
        objectRendered = object;
        render(object);
    }

    @SuppressWarnings("unchecked")
    private ObjectColumnCreator cc() {
        if(columnCreator == null) {
            columnCreator = ((ObjectColumnCreator<T>)ApplicationEnvironment.get().getObjectColumnCreator()).create(this);
        }
        return columnCreator;
    }

    @Override
    public boolean isColumnReorderingAllowed() {
        return renderers == null ? super.isColumnReorderingAllowed() : allowColumnReordering;
    }

    @Override
    public void setColumnReorderingAllowed(boolean allowColumnReordering) {
        if(renderers == null) {
            super.setColumnReorderingAllowed(allowColumnReordering);
        }
        this.allowColumnReordering = allowColumnReordering;
    }

    public void setColumnResizable(String columnName, boolean resizable) {
        if(renderers == null) {
            Column<T> c = getColumnByKey(columnName);
            if(c != null) {
                c.setResizable(resizable);
            }
            return;
        }
        columnResizable.put(columnName, resizable);
    }

    public boolean isColumnResizable(String columnName) {
        if(renderers == null) {
            Column<T> c = getColumnByKey(columnName);
            if(c != null) {
                return c.isResizable();
            }
            return false;
        }
        Boolean v = columnResizable.get(columnName);
        return v == null ? false : v;
    }

    public void setColumnVisible(String columnName, boolean visible) {
        if(renderers == null) {
            Column<T> c = getColumnByKey(columnName);
            if(c != null) {
                c.setVisible(visible);
            }
            return;
        }
        columnVisible.put(columnName, visible);
    }

    public boolean isColumnVisible(String columnName) {
        if(renderers == null) {
            Column<T> c = getColumnByKey(columnName);
            if(c != null) {
                return c.isVisible();
            }
            return false;
        }
        Boolean v = columnVisible.get(columnName);
        return v == null ? false : v;
    }

    public void setColumnFrozen(String columnName, boolean frozen) {
        if(renderers == null) {
            Column<T> c = getColumnByKey(columnName);
            if(c != null) {
                c.setFrozen(frozen);
            }
            return;
        }
        columnFrozen.put(columnName, frozen);
    }

    public boolean isColumnFrozen(String columnName) {
        if(renderers == null) {
            Column<T> c = getColumnByKey(columnName);
            if(c != null) {
                return c.isFrozen();
            }
            return false;
        }
        Boolean v = columnFrozen.get(columnName);
        return v == null ? false : v;
    }

    /**
     * You can have a "header row" (as the first row) that covers the whole grid. Typically, such a row is to show your own buttons or components to
     * customize the grid. The default implentation returns null and thus, no such row is created.
     * @return Component to be used as the "header row".
     */
    protected Component createHeader() {
        return null;
    }

    private void constructHeader(Component component) {
        if(component == null) {
            return;
        }
        HeaderRow r = prependHeaderRow();
        List<Column<T>> columns = getColumns();
        Column[] a = new Column[columns.size()];
        columns.toArray(a);
        HeaderRow.HeaderCell c = r.join(a);
        c.setComponent(component);
    }

    /**
     * Get the "configure button" that can be added somewhere in your user interface, usually as the first component of the "header row". The grid columns
     * can be configured by clicking this button.
     * @return Configure button.
     */
    public ButtonIcon getConfigureButton() {
        if(configure == null) {
            configure = new ButtonIcon(VaadinIcon.COG_O, e -> configure());
            configure.setPlaceholder("Configure columns");
        }
        return configure;
    }

    public void setMinWidth(String width) {
        getElement().getStyle().set("min-width", width);
    }

    public void setMinWidth(int perColumnWidthInPixels, int maxWidthInPixels) {
        if(maxWidthInPixels <= 10) {
            maxWidthInPixels = 800;
        }
        setMinWidth(Math.min(maxWidthInPixels, perColumnWidthInPixels * getColumnCount()) + "px");
    }

    @Override
    public List<Column<T>> getColumns() {
        init();
        return super.getColumns();
    }

    @Override
    public Column<T> getColumnByKey(String columnKey) {
        init();
        return super.getColumnByKey(columnKey);
    }

    public int getColumnCount() {
        return getColumns().size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void setDataProvider(DataProvider<T, ?> dataProvider) {
        DataProvider<T, ?> dp = getDataProvider();
        ((DataSupplier)dp).provider = dataProvider;
        super.setDataProvider(dp);
    }

    public final Class<T> getObjectClass() {
        return objectClass;
    }

    private String getColumnName(Method getMethod) {
        String name = getMethod.getName();
        if(name.equals("getClass")) {
            return null;
        } else if(name.startsWith("get")) {
            return name.substring(3);
        } else if(name.startsWith("is")) {
            return name.substring(2);
        }
        return null;
    }

    private void generateColumns() {
        String name;
        for(Method m: objectClass.getMethods()) {
            if(Modifier.isStatic(m.getModifiers()) || m.getParameterTypes().length > 0) {
                continue;
            }
            name = getColumnName(m);
            if(name != null && includeColumn(name)) {
                createColumn(name, m);
            }
        }
    }

    /**
     * This method is invoked to find out the names of the columns to be generated. However, this will not be invoked if the column names
     * are already passed in the constructor. The default implementation returns null (however, this behaviour can be changed by setting up an
     * appropriate Application Environment) and in that case, columns names will be determined
     * through getXXX and isXXX methods of the Bean type.
     * @return Column names to be constructed.
     */
    @SuppressWarnings("unchecked")
    protected Stream<String> getColumnNames() {
        return cc().getColumnNames();
    }

    /**
     * Get the number of rows in the grid.
     * @return Number of rows in the grid.
     */
    public int size() {
        DataProvider<T, ?> p = provider();
        return p == null ? 0 : p.size(new Query<>());
    }

    /**
     * Refresh the whole grid.
     */
    public void refresh() {
        getDataProvider().refreshAll();
    }

    /**
     * Rfresh a particular item in the grid.
     * @param item Item
     */
    public void refresh(T item) {
        getDataProvider().refreshItem(item);
    }

    /**
     * Get item at a particular index.
     * @param index Index of the item.
     * @return Item at the index.
     */
    @SuppressWarnings("unchecked")
    public T getItem(int index) {
        if(index < 0 || index >= size()) {
            return null;
        }
        if(provider() == null) {
            return createObjectInstance();
        }
        Query<T, ?> q = new Query<>(index, 1, null, null, null);
        return ((DataSupplier<?>)getDataProvider()).fetchInt(q).findAny().orElse(null);
    }

    /**
     * Create an instance of the Bean. By default, newInstance() method is tried.
     * @return A newly created instance of the Bean. Returns null if any error occurs or a default constrcuor is missing.
     */
    public T createObjectInstance() {
        try {
            return objectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        return null;
    }

    /**
     * Return the method for generating column data from the Bean. By default, getXXX and isXXX methods are tried.
     * However, this method is invoked only if no getXXX or isXXX method is defined in the grid itself. (Also note that getColumnFunction
     * is invoked before that and thus, this will not be invoked if data is already available through a Function returned by
     * the getColumnFunction method.
     * @param columnName Column name
     * @return method if available, otherwise null. Firslty, it sees if this can be retrieved from the Applucation Environment.
     */
    protected Method getColumnMethod(String columnName) {
        Method m = cc().getColumnMethod(columnName);
        if(m != null) {
            return m;
        }
        try {
            return objectClass.getMethod("get" + columnName);
        } catch (NoSuchMethodException ignored) {
        }
        try {
            return objectClass.getMethod("is" + columnName);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    private Method getOutsideMethod(String columnName) {
        Class<?>[] params = new Class<?>[] { getObjectClass() };
        if(methodHandlerHost != null) {
            try {
                return methodHandlerHost.getClass().getMethod("get" + columnName, params);
            } catch (NoSuchMethodException ignore) {
            }
            try {
                return methodHandlerHost.getClass().getMethod("is" + columnName, params);
            } catch (NoSuchMethodException ignore) {
            }
        }
        try {
            return this.getClass().getMethod("get" + columnName, params);
        } catch (NoSuchMethodException ignore) {
        }
        try {
            return this.getClass().getMethod("is" + columnName, params);
        } catch (NoSuchMethodException ignore) {
        }
        return null;
    }

    /**
     * Return a Function for generating column data.
     * @param columnName Column name.
     * @return Default implementation is to find out this from the Application Environment but the detault Application Environment also returns null.
     */
    @SuppressWarnings("unchecked")
    public Function<T, ?> getColumnFunction(String columnName) {
        return cc().getColumnFunction(columnName);
    }


    /**
     * Create a column for the given column name. If the column name is "XXX", it first determines if a getXXX(T object) or isXXX(T object) method
     * is already defined in the grid or not for generating data for this column. If not, it calls the getColumnFunction("XXX")
     * to determine if any Function is defined for this purpose. If not found, it calls the getColumnMethod("XXX") to determine.
     * @param columnName Column name.
     * @return Whether a new column can be created or not.
     */
    @SuppressWarnings("unchecked")
    public boolean createColumn(String columnName) {
        if(columnName == null) {
            return false;
        }
        if(renderers != null && renderers.containsKey(columnName)) {
            return false;
        }
        Method m = getOutsideMethod(columnName);
        Function<T, ?> function = null;
        if(m != null) {
            function = getMethodFunction(m);
        }
        if(function == null) {
            function = getColumnFunction(columnName);
        }
        if(function == null) {
            function = getMethodFunction(columnName);
        }
        Renderer<T> r = renderer(getColumnTemplate(columnName), function);
        if(renderers == null) {
            constructColumn(columnName, r);
        } else {
            renderers.put(columnName, r);
        }
        return true;
    }

    /**
     * Crate a column that uses one or more functions to generate its column value. It generates a multi-line output with result of each function
     * in separate line unless a template is already define for this column using getColumnTemplate method.
     * @param columnName Column name.
     * @param functions Functions that take object as a parameter and returns some sort of value. Values are stringified before displaying.
     *                  (Application Environment may be set up to control how the stringification process by defining it in the toString(..) method).
     * @return Whether a new column can be created or not.
     */
    @SafeVarargs
    public final boolean createColumn(String columnName, Function<T, ?>... functions) {
        return createColumn(columnName, null, functions);
    }

    /**
     * Crate a column that uses one or more functions to generate its column value. The output is formatted using the "template" passed. The template
     * can contain any HTML text and placeholders such as <1>, <2>, <3> ... These placeholders will be replaced from the output from the functions.
     * @param columnName Column name.
     * @param template Template
     * @param functions Functions that take object as a parameter and returns some sort of value. Values are stringified before displaying.
     *                  (Application Environment may be set up to control how the stringification process by defining it in the toString(..) method).
     * @return Whether a new column can be created or not.
     */
    @SafeVarargs
    public final boolean createColumn(String columnName, String template, Function<T, ?>... functions) {
        if (functions == null || functions.length == 0) {
            return createColumn(columnName);
        }
        if(template == null) {
            template = getColumnTemplate(columnName);
        }
        Renderer<T> r = renderer(template, functions);
        if(renderers == null) {
            constructColumn(columnName, r);
        } else {
            renderers.put(columnName, r);
        }
        return true;
    }

    /**
     * Create a column uses a method to determine its data. The method must be either available in the grid itself or must be available in the object.
     * If it is available in the grid itself, it should take object as the only parameter.
     * @param columnName Column name.
     * @param method Method
     * @return Whether a new column can be created or not.
     */
    public boolean createColumn(String columnName, Method method) {
        if(method == null) {
            return createColumn(columnName);
        }
        return createColumn(columnName, getMethodFunction(method));
    }

    /**
     * Create a column that generates data through a custom renderer.
     * @param columnName Column name.
     * @param renderer Renderer.
     * @return Whether a new column can be created or not.
     */
    public boolean createColumn(String columnName, Renderer<T> renderer) {
        if(renderer == null) {
            return createColumn(columnName);
        }
        if(renderers == null) {
            constructColumn(columnName, renderer);
        } else {
            renderers.put(columnName, renderer);
        }
        return true;
    }

    /**
     * Get the template for the given column name.
     * @param columnName Column name.
     * @return The default implementation returns null.
     */
    public String getColumnTemplate(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    @SafeVarargs
    private final Renderer<T> renderer(String template, Function<T, ?>... functions) {
        if(template == null) {
            StringBuilder s = new StringBuilder();
            IntStream.range(0, functions.length - 1).forEach(i -> s.append('<').append(i).append('>').append("<br/>"));
            s.append('<').append(functions.length).append('>');
            template = s.toString();
        }
        int[] ids = new int[functions.length];
        int i;
        for(i = 0; i < ids.length; i++) {
            ++paramId;
            ids[i] = paramId;
        }
        for(i = 0; i < ids.length; i++) {
            template = template.replace("<" + (i + 1) + ">", "[[item.so" + ids[i] + "]]");
        }
        TemplateRenderer<T> r = TemplateRenderer.of(template);
        for(i = 0; i < ids.length; i++) {
            final Function<T, ?> function = functions[i];
            r.withProperty("so" + ids[i], o -> {
                setRO(o);
                return ApplicationEnvironment.get().toString(function.apply(o));
            });
        }
        return r;
    }

    private Function<T, ?> getMethodFunction(String columnName) {
        Method m = getColumnMethod(columnName);
        if(m == null) {
            return item -> "?";
        }
        return getMethodFunction(m);
    }

    private Function<T, ?> getMethodFunction(Method method) {
        if(methodHandlerHost != null && method.getDeclaringClass() == methodHandlerHost.getClass()) {
            return (Function<T, Object>) t -> {
                try {
                    return method.invoke(methodHandlerHost, t);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
                return null;
            };
        }
        if(method.getDeclaringClass() == DataGrid.this.getClass()) {
            return (Function<T, Object>) t -> {
                try {
                    return method.invoke(DataGrid.this, t);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
                return null;
            };
        }
        return (Function<T, Object>) t -> {
            try {
                return method.invoke(t);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
            return null;
        };
    }

    private void constructColumn(String columnName) {
        constructColumn(columnName, renderers.get(columnName));
    }

    private void constructColumn(String columnName, Renderer<T> renderer) {
        Grid.Column<T> c = super.addColumn(renderer);
        c.setKey(columnName);
        Component h = getHeaderComponent(columnName);
        if(h == null) {
            c.setHeader(getHeader(columnName));
        } else {
            c.setHeader(h);
        }
        Boolean v = columnResizable.get(columnName);
        if(v != null) {
            c.setResizable(v);
        }
        v = columnVisible.get(columnName);
        if(v != null) {
            c.setVisible(v);
        }
        customizeColumn(columnName, c);
    }

    /**
     * This mehod is invoked when the column is actually constructed.
     * @param columnName Column name
     * @param column Grid column that may be customized.
     */
    protected void customizeColumn(@SuppressWarnings("unused") String columnName,
                                   @SuppressWarnings("unused") Grid.Column<T> column) {
    }

    /**
     * Return value from this method determines the order in which columns are displayed. Numbers do not have to be continuous, columns with lower
     * numbers are displayed first.
     * @param columnName Column nmae.
     * @return An integer number that denotes the column order.
     */
    public int getColumnOrder(String columnName) {
        return cc().getColumnOrder(columnName);
    }

    /**
     * Determines the respective column should be created or note. This is useful when columns are autogenerated but you want to eliminate
     * some columns.
     * @param columnName Column name.
     * @return Default implementation returns true for all columns.
     */
    public boolean includeColumn(@SuppressWarnings("unused") String columnName) {
        return true;
    }

    /**
     * Get the header component for the specified column. If no header component is defined, getHeader method will be invoked to create a text-based
     * header.
     * @param columnName Column name.
     * @return Default implemnentation returns null.
     */
    public Component getHeaderComponent(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    /**
     * Get the text to display in the header. This will be invoked only if getHeaderComponent returns null.
     * @param columnName Column name.
     * @return Text to display as header. By default it tries to determine this from the Application Envornment.
     */
    public String getHeader(String columnName) {
        return cc().getHeader(columnName);
    }

    /**
     * Set some other object to supply the column methods. If such an object is set, for column method look up, this object also will be used
     * before checking in the gird itself.
     * @param host An object containing getXXX / isXXX methods for the respective column names.
     */
    public void setMethodHandlerHost(Object host) {
        this.methodHandlerHost = host;
    }

    @SuppressWarnings("unchecked")
    private DataProvider<T, ?> provider() {
        return ((DataSupplier<?>)getDataProvider()).provider;
    }

    public View getView(boolean create) {
        if(view == null && create) {
            view = createView();
        }
        if(view == null && create) {
            view = new View(getApplication(), this, getCaption());
        }
        return view;
    }

    /**
     * Create a View to display the grid when executed. If this method returns null, a default View will be created.
     * @return A View with this grid as the component.
     */
    protected View createView() {
        return null;
    }

    /**
     * Get the Application
     * @return The Application instance
     */
    public Application getApplication() {
        return Application.get();
    }

    /**
     * Caption used when displaying it in a View. If no caption was set using setCaption method, a label for the Bean's class
     * is determined from the Application Environment.
     * @return Caption
     */
    public String getCaption() {
        return caption == null || caption.trim().isEmpty() ? ApplicationEnvironment.get().createLabel(getObjectClass()) : caption;
    }

    /**
     * Closes the view if that exists
     */
    @Override
    public void close() {
        if(view != null) {
            view.close();
        }
    }

    /**
     * Closes the view by aborting if that exists
     */
    @Override
    public void abort() {
        if(view != null) {
            view.abort();
        }
    }

    /**
     * Set caption used when displaying the grid in a View.
     * @param caption Caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Skeleton implementation of ClickHandler interface.
     * @param c Component clicked.
     */
    @Override
    public void clicked(Component c) {
    }

    /**
     * Configure the columns.
     */
    public void configure() {
        Application.message("Not yet implemented");
    }

    /**
     * Select rows.
     * @param items Items to be selected.
     */
    public void select(Iterable<T> items) {
        items.forEach(this::select);
    }

    /**
     * Select rows.
     * @param items Items to be selected.
     */
    public void select(Iterator<T> items) {
        items.forEachRemaining(this::deselect);
    }

    /**
     * Deselect rows.
     * @param items Items to be deselected.
     */
    public void deselect(Iterable<T> items) {
        items.forEach(this::select);
    }

    /**
     * Deselect rows.
     * @param items Items to be deselected.
     */
    public void deselect(Iterator<T> items) {
        items.forEachRemaining(this::deselect);
    }

    /**
     * Get the selected item. This will return the item only if a single item is selected.
     * @return Item if that is the only row selected.
     */
    public T getSelected() {
        Set<T> set = getSelectedItems();
        if(set == null || set.size() > 1) {
            return null;
        }
        return set.stream().findAny().orElse(null);
    }

    /**
     * Scroll to a particular row.
     * @param rowIndex Zero based index of the row.
     */
    public void scrollTo(int rowIndex) {
        if(rowIndex < 0) {
            return;
        }
        UI.getCurrent().getPage().executeJavaScript("$0._scrollToIndex(" + rowIndex + ")", DataGrid.this.getElement());
    }

    /**
     * Skeleton implementation of ValueChangeHandler.
     * @param changedValues Changed values.
     */
    @Override
    public void valueChanged(ChangedValues changedValues) {
    }

    private class DataSupplier<F> extends AbstractDataProvider<T, F> implements BackEndDataProvider<T, F> {

        private DataProvider<T, F> provider;

        @Override
        public void setSortOrders(List<QuerySortOrder> list) {
            if(!(provider instanceof  BackEndDataProvider)) {
                return;
            }
            ((BackEndDataProvider<T, F>)provider).setSortOrders(list);
        }

        @Override
        public void setSortOrder(QuerySortOrder sortOrder) {
            if(!(provider instanceof  BackEndDataProvider)) {
                return;
            }
            ((BackEndDataProvider<T, F>)provider).setSortOrder(sortOrder);
        }

        @Override
        public void setSortOrders(QuerySortOrderBuilder builder) {
            if(!(provider instanceof  BackEndDataProvider)) {
                return;
            }
            ((BackEndDataProvider<T, F>)provider).setSortOrders(builder);
        }

        public <C> DataProvider<T, C> withConvertedFilter(final SerializableFunction<C, F> filterConverter) {
            if(provider == null) {
                return super.withConvertedFilter(filterConverter);
            }
            return provider.withConvertedFilter(filterConverter);
        }

        @Override
        public <Q, C> ConfigurableFilterDataProvider<T, Q, C> withConfigurableFilter(SerializableBiFunction<Q, C, F> filterCombiner) {
            if(provider == null) {
                return super.withConfigurableFilter(filterCombiner);
            }
            return provider.withConfigurableFilter(filterCombiner);
        }

        @Override
        public ConfigurableFilterDataProvider<T, Void, F> withConfigurableFilter() {
            if(provider == null) {
                return super.withConfigurableFilter();
            }
            return provider.withConfigurableFilter();
        }

        @Override
        public Object getId(T item) {
            return provider == null ? item : provider.getId(item);
        }

        @Override
        public boolean isInMemory() {
            return provider == null || provider.isInMemory();
        }

        @Override
        public int size(Query<T, F> query) {
            return provider == null ? DataGrid.this.size() : provider.size(query);
        }

        @Override
        public Stream<T> fetch(Query<T, F> query) {
            if(provider != null) {
                return provider.fetch(query);
            }
            int start = query.getOffset(), end = query.getLimit();
            int size = DataGrid.this.size();
            if(end > size) {
                end = size;
            }
            end += start;
            return IntStream.range(start, end).mapToObj(DataGrid.this::getItem);
        }

        @SuppressWarnings("unchecked")
        private Stream<T> fetchInt(Query<T, ?> query) {
            return fetch((Query<T, F>) query);
        }

        @Override
        public void refreshItem(T t) {
            if(provider != null) {
                provider.refreshItem(t);
            }
            super.refreshItem(t);
        }

        @Override
        public void refreshAll() {
            if(provider != null) {
                provider.refreshAll();
            }
            super.refreshAll();
        }

        @Override
        public Registration addDataProviderListener(DataProviderListener<T> dataProviderListener) {
            if(provider == null) {
                return super.addDataProviderListener(dataProviderListener);
            }
            return provider.addDataProviderListener(dataProviderListener);
        }
    }
}