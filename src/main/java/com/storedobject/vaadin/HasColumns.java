package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Common interface for DataGrid and DataTreeGrid.
 * @param <T> Bean type of the Gird/TreeGrid.
 * @author Syam
 */
public interface HasColumns<T> extends ExecutableView {

    /**
     * Set the "theme" to make the appearence of this grid compact. It uses less space than normal and more rows will be visible.
     */
    default void compact() {
        getSOGrid().compact();
    }

    /**
     * Get the currently rendered object. This method is useful when you have your own methods mapped for column values and you want to do any special setup
     * or computation.
     * @return Currently rendered object.
     */
    default T getObjectRendered() {
        return getSOGrid().objectRendered;
    }

    /**
     * This method is invoked just before each row is rendered. You can do special set up or comuptation at this stage.
     * @param object Currently rendered object.
     */
    default void render(@SuppressWarnings("unused") T object) {
    }

    default void setColumnResizable(String columnName, boolean resizable) {
        getSOGrid().setColumnResizable(columnName, resizable);
    }

    default boolean isColumnResizable(String columnName) {
        return getSOGrid().isColumnResizable(columnName);
    }

    default void setColumnVisible(String columnName, boolean visible) {
        getSOGrid().setColumnVisible(columnName, visible);
    }

    default boolean isColumnVisible(String columnName) {
        return getSOGrid().isColumnVisible(columnName);
    }

    default void setColumnFrozen(String columnName, boolean frozen) {
        getSOGrid().setColumnFrozen(columnName, frozen);
    }

    default boolean isColumnFrozen(String columnName) {
        return getSOGrid().isColumnFrozen(columnName);
    }

    /**
     * Get the "configure button" that can be added somewhere in your user interface, usually as the first component of the "header row". The grid columns
     * can be configured by clicking this button.
     * @return Configure button.
     */
    default ButtonIcon getConfigureButton() {
        return getSOGrid().getConfigureButton();
    }

    default void setMinWidth(String width) {
        getSOGrid().setMinWidth(width);
    }

    default void setMinWidth(int perColumnWidthInPixels, int maxWidthInPixels) {
        getSOGrid().setMinWidth(perColumnWidthInPixels, maxWidthInPixels);
    }

    default int getColumnCount() {
        return getSOGrid().getColumnCount();
    }

    default Class<T> getObjectClass() {
        return getSOGrid().objectClass;
    }

    /**
     * Refresh the whole grid.
     */
    default void refresh() {
        getSOGrid().grid.getDataProvider().refreshAll();
    }

    /**
     * Rfresh a particular item in the grid.
     * @param item Item
     */
    default void refresh(T item) {
        getSOGrid().grid.getDataProvider().refreshItem(item);
    }

    /**
     * Return a Function for generating column data.
     * @param columnName Column name.
     * @return Default implementation is to find out this from the Application Environment but the detault Application Environment also returns null.
     */
    default Function<T, ?> getColumnFunction(@SuppressWarnings("unused") String columnName) {
        return null;
    }


    /**
     * Create a column for the given column name. If the column name is "XXX", it first determines if a getXXX(T object) or isXXX(T object) method
     * is already defined in the grid or not for generating data for this column. If not, it calls the getColumnFunction("XXX")
     * to determine if any Function is defined for this purpose. If not found, it calls the getColumnMethod("XXX") to determine.
     * @param columnName Column name.
     * @return Whether a new column can be created or not.
     */
    default boolean createColumn(String columnName) {
        return getSOGrid().createColumn(columnName);
    }

    /**
     * Crate a column that uses one or more functions to generate its column value. It generates a multi-line output with result of each function
     * in separate line unless a template is already define for this column using getColumnTemplate method.
     * @param columnName Column name.
     * @param functions Functions that take object as a parameter and returns some sort of value. Values are stringified before displaying.
     *                  (Application Environment may be set up to control how the stringification process by defining it in the toDisplay(..) method).
     * @return Whether a new column can be created or not.
     */
    @SuppressWarnings("unchecked")
    default boolean createColumn(String columnName, Function<T, ?>... functions) {
        return getSOGrid().createColumn(columnName, null, functions);
    }

    /**
     * Crate a column that uses one or more functions to generate its column value. The output is formatted using the "template" passed. The template
     * can contain any HTML text and placeholders such as <1>, <2>, <3> ... These placeholders will be replaced from the output from the functions.
     * @param columnName Column name.
     * @param template Template
     * @param functions Functions that take object as a parameter and returns some sort of value. Values are stringified before displaying.
     *                  (Application Environment may be set up to control how the stringification process by defining it in the toDisplay(..) method).
     * @return Whether a new column can be created or not.
     */
    @SuppressWarnings("unchecked")
    default boolean createColumn(String columnName, String template, Function<T, ?>... functions) {
        return getSOGrid().createColumn(columnName, template, functions);
    }

    /**
     * Create a column uses a method to determine its data. The method must be either available in the grid itself or must be available in the object.
     * If it is available in the grid itself, it should take object as the only parameter.
     * @param columnName Column name.
     * @param method Method
     * @return Whether a new column can be created or not.
     */
    default boolean createColumn(String columnName, Method method) {
        return getSOGrid().createColumn(columnName, method);
    }

    /**
     * Create a column that generates data through a custom renderer.
     * @param columnName Column name.
     * @param renderer Renderer.
     * @return Whether a new column can be created or not.
     */
    default boolean createColumn(String columnName, Renderer<T> renderer) {
        return getSOGrid().createColumn(columnName, renderer);
    }

    /**
     * Get the template for the given column name.
     * @param columnName Column name.
     * @return The default implementation returns null.
     */
    default String getColumnTemplate(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    /**
     * Return value from this method determines the order in which columns are displayed. Numbers do not have to be continuous, columns with lower
     * numbers are displayed first.
     * @param columnName Column nmae.
     * @return An integer number that denotes the column order.
     */
    default int getColumnOrder(@SuppressWarnings("unused") String columnName) {
        throw AbstractDataForm.FIELD_ERROR;
    }

    /**
     * Determines the respective column should be created or note. This is useful when columns are autogenerated but you want to eliminate
     * some columns.
     * @param columnName Column name.
     * @return Default implementation returns true for all columns.
     */
    default boolean includeColumn(@SuppressWarnings("unused") String columnName) {
        throw AbstractDataForm.FIELD_ERROR;
    }

    /**
     * Get the header component for the specified column. If no header component is defined, getHeader method will be invoked to create a text-based
     * header.
     * @param columnName Column name.
     * @return Default implemnentation returns null.
     */
    default Component getHeaderComponent(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    /**
     * Get the text to display in the header. This will be invoked only if getHeaderComponent returns null.
     * @param columnName Column name.
     * @return Text to display as header. By default it tries to determine this from the Application Envornment.
     */
    default String getHeader(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    /**
     * Set some other object to supply the column methods. If such an object is set, for column method look up, this object also will be used
     * before checking in the gird itself.
     * @param host An object containing getXXX / isXXX methods for the respective column names.
     */
    default void setMethodHandlerHost(Object host) {
        getSOGrid().methodHandlerHost = host;
    }

    default View getView(boolean create) {
        return getSOGrid().getView(create);
    }
    
    /**
     * Caption used when displaying it in a View. If no caption was set using setCaption method, a label for the Bean's class
     * is determined from the Application Environment.
     * @return Caption
     */
    default String getCaption() {
        return getSOGrid().getCaption();
    }

    /**
     * Closes the view if that exists
     */
    @Override
    default void close() {
        getSOGrid().close();
    }

    /**
     * Closes the view by aborting if that exists
     */
    @Override
    default void abort() {
        getSOGrid().abort();
    }

    /**
     * Set caption used when displaying the grid in a View.
     * @param caption Caption
     */
    default void setCaption(String caption) {
        getSOGrid().caption = caption;
    }

    /**
     * Skeleton implementation of ClickHandler interface.
     * @param c Component clicked.
     */
    @Override
    default void clicked(@SuppressWarnings("unused") Component c) {
    }

    /**
     * Configure the columns.
     */
    default void configure() {
        getSOGrid().configure();
    }

    /**
     * Select rows.
     * @param items Items to be selected.
     */
    default void select(Iterable<T> items) {
        getSOGrid().select(items);
    }

    /**
     * Select rows.
     * @param items Items to be selected.
     */
    default void select(Iterator<T> items) {
        getSOGrid().select(items);
    }

    /**
     * Deselect rows.
     * @param items Items to be deselected.
     */
    default void deselect(Iterable<T> items) {
        getSOGrid().deselect(items);
    }

    /**
     * Deselect rows.
     * @param items Items to be deselected.
     */
    default void deselect(Iterator<T> items) {
        getSOGrid().deselect(items);
    }

    /**
     * Get the selected item. This will return the item only if a single item is selected.
     * @return Item if that is the only row selected.
     */
    default T getSelected() {
        return getSOGrid().getSelected();
    }

    /**
     * Scroll to a particular row.
     * @param rowIndex Zero based index of the row.
     */
    default void scrollTo(int rowIndex) {
        getSOGrid().scrollTo(rowIndex);
    }

    /**
     * Skeleton implementation of ValueChangeHandler.
     * @param changedValues Changed values.
     */
    @Override
    default void valueChanged(@SuppressWarnings("unused") ChangedValues changedValues) {
    }

    /**
     * For internal use only.
     * @return SO Grid
     */
    SOGrid<T> getSOGrid();

    class SOGrid<T> {
        
        private final Grid<T> grid;
        private final HasColumns<T> hc;
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
        private boolean buildTree;

        @SuppressWarnings("unchecked")
        SOGrid(Grid<T> grid, Class<T> objectClass, Iterable<String> columns) {
            this.grid = grid;
            buildTree = grid instanceof TreeGrid;
            this.hc = (HasColumns<T>) grid;
            this.objectClass = objectClass;
            this.columns = columns;
            grid.addAttachListener(e -> init());
            grid.getElement().getClassList().add("so-grid");
        }

        private void constructed() {
            if(grid instanceof DataGrid) {
                ((DataGrid<T>) grid).constructed();
            } else {
                ((DataTreeGrid<T>) grid).constructed();
            }
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
            grid.getElement().setAttribute("theme", "row-stripes wrap-cell-content");
            constructHeader(createHeader());
            if(columnCreator != null) {
                columnCreator.close();
                columnCreator = null;
            }
            grid.setColumnReorderingAllowed(allowColumnReordering);
            constructed();
        }

        private void compact() {
            grid.getElement().setAttribute("theme", "compact row-stripes wrap-cell-content");
        }

        private void setRO(T object) {
            if(object == objectRendered) {
                return;
            }
            objectRendered = object;
            hc.render(object);
        }

        @SuppressWarnings("unchecked")
        private ObjectColumnCreator cc() {
            if(columnCreator == null) {
                columnCreator = ((ObjectColumnCreator<T>) Objects.requireNonNull(ApplicationEnvironment.get()).getObjectColumnCreator()).create(hc);
            }
            return columnCreator;
        }

        boolean isColumnReorderingAllowed() {
            return allowColumnReordering;
        }

        void setColumnReorderingAllowed(boolean allowColumnReordering) {
            this.allowColumnReordering = allowColumnReordering;
        }

        private void setColumnResizable(String columnName, boolean resizable) {
            if(renderers == null) {
                Grid.Column<T> c = getColumnByKey(columnName);
                if(c != null) {
                    c.setResizable(resizable);
                }
                return;
            }
            columnResizable.put(columnName, resizable);
        }

        private boolean isColumnResizable(String columnName) {
            if(renderers == null) {
                Grid.Column<T> c = getColumnByKey(columnName);
                if(c != null) {
                    return c.isResizable();
                }
                return false;
            }
            Boolean v = columnResizable.get(columnName);
            return v == null ? false : v;
        }

        private void setColumnVisible(String columnName, boolean visible) {
            if(renderers == null) {
                Grid.Column<T> c = getColumnByKey(columnName);
                if(c != null) {
                    c.setVisible(visible);
                }
                return;
            }
            columnVisible.put(columnName, visible);
        }

        private boolean isColumnVisible(String columnName) {
            if(renderers == null) {
                Grid.Column<T> c = getColumnByKey(columnName);
                if(c != null) {
                    return c.isVisible();
                }
                return false;
            }
            Boolean v = columnVisible.get(columnName);
            return v == null ? false : v;
        }

        private void setColumnFrozen(String columnName, boolean frozen) {
            if(renderers == null) {
                Grid.Column<T> c = getColumnByKey(columnName);
                if(c != null) {
                    c.setFrozen(frozen);
                }
                return;
            }
            columnFrozen.put(columnName, frozen);
        }

        private boolean isColumnFrozen(String columnName) {
            if(renderers == null) {
                Grid.Column<T> c = getColumnByKey(columnName);
                if(c != null) {
                    return c.isFrozen();
                }
                return false;
            }
            Boolean v = columnFrozen.get(columnName);
            return v == null ? false : v;
        }

        private Component createHeader() {
            if(grid instanceof DataGrid) {
                return ((DataGrid<T>) grid).createHeader();
            }
            return ((DataTreeGrid<T>) grid).createHeader();
        }

        private void constructHeader(Component component) {
            if(component == null) {
                return;
            }
            HeaderRow r = grid.prependHeaderRow();
            HeaderRow.HeaderCell c;
            List<Grid.Column<T>> columns = getColumns();
            if(columns.size() == 1) {
                c = r.getCells().get(0);
            } else {
                Grid.Column[] a = new Grid.Column[columns.size()];
                columns.toArray(a);
                c = r.join(a);
            }
            c.setComponent(component);
        }

        private ButtonIcon getConfigureButton() {
            if(configure == null) {
                configure = new ButtonIcon(VaadinIcon.COG_O, e -> configure());
                configure.setPlaceholder("Configure columns");
            }
            return configure;
        }

        private void configure() {
            Application.message("Not yet implemented");
        }

        private void setMinWidth(String width) {
            grid.getElement().getStyle().set("min-width", width);
        }

        private void setMinWidth(int perColumnWidthInPixels, int maxWidthInPixels) {
            if(maxWidthInPixels <= 10) {
                maxWidthInPixels = 800;
            }
            setMinWidth(Math.min(maxWidthInPixels, perColumnWidthInPixels * getColumnCount()) + "px");
        }

        List<Grid.Column<T>> getColumns() {
            init();
            return grid.getColumns();
        }

        Grid.Column<T> getColumnByKey(String columnKey) {
            init();
            return grid.getColumnByKey(columnKey);
        }

        private int getColumnCount() {
            return getColumns().size();
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

        @SuppressWarnings("unchecked")
        private Stream<String> getColumnNames() {
            Stream<String> names;
            if(grid instanceof DataGrid) {
                names = ((DataGrid<T>) grid).getColumnNames();
            } else {
                names = ((DataTreeGrid<T>) grid).getColumnNames();
            }
            return names == null ? cc().getColumnNames() : names;
        }

        Method getColumnMethod(String columnName) {
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
            Class<?>[] params = new Class<?>[] { objectClass };
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

        @SuppressWarnings("unchecked")
        private Function<T, ?> getColumnFunction(String columnName) {
            Function<T, ?> f = hc.getColumnFunction(columnName);
            return f == null ? cc().getColumnFunction(columnName) : f;
        }


        @SuppressWarnings("unchecked")
        private boolean createColumn(String columnName) {
            if(columnName == null) {
                return false;
            }
            if(renderers != null && renderers.containsKey(columnName)) {
                return false;
            }
            Method m = getOutsideMethod(columnName);
            Function<T, ?> function = null;
            if(m != null) {
                if(createTreeColumn(columnName, m)) {
                    return true;
                }
                function = getMethodFunction(m);
            }
            if(function == null) {
                function = getColumnFunction(columnName);
            }
            if(function == null) {
                function = getMethodFunction(columnName);
            }
            if(createTreeColumn(columnName, function)) {
                return true;
            }
            Renderer<T> r = renderer(hc.getColumnTemplate(columnName), function);
            if(renderers == null) {
                constructColumn(columnName, r);
            } else {
                renderers.put(columnName, r);
            }
            return true;
        }

        @SafeVarargs
        private final boolean createColumn(String columnName, String template, Function<T, ?>... functions) {
            if (functions == null || functions.length == 0) {
                return createColumn(columnName);
            }
            if(functions.length == 1 && createTreeColumn(columnName, functions[0])) {
                return true;
            }
            if(template == null) {
                template = hc.getColumnTemplate(columnName);
            }
            Renderer<T> r = renderer(template, functions);
            if(renderers == null) {
                constructColumn(columnName, r);
            } else {
                renderers.put(columnName, r);
            }
            return true;
        }

        private boolean createColumn(String columnName, Method method) {
            if(method == null) {
                return createColumn(columnName);
            }
            if(createTreeColumn(columnName, method)) {
                return true;
            }
            return createColumn(columnName, null, getMethodFunction(method));
        }

        private boolean createColumn(String columnName, Renderer<T> renderer) {
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
                    return Objects.requireNonNull(ApplicationEnvironment.get()).toDisplay(function.apply(o));
                });
            }
            return r;
        }

        void treeBuilt() {
            buildTree =false;
        }

        private boolean createTreeColumn(String columnName, Method m) {
            if(!buildTree || m == null) {
                return false;
            }
            buildTree = false;
            Grid.Column<T> column = ((DataTreeGrid<T>)grid).addHierarchyColumn(t -> {
                try {
                    return m.invoke(t);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
                return "?";
            });
            acceptColumn(column, columnName);
            return true;
        }

        private boolean createTreeColumn(String columnName, Function<T, ?> function) {
            if(!buildTree || function == null) {
                return false;
            }
            buildTree = false;
            acceptColumn(((DataTreeGrid<T>)grid).addHierarchyColumn(function::apply), columnName);
            return true;
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
            if(method.getDeclaringClass() == grid.getClass()) {
                return (Function<T, Object>) t -> {
                    try {
                        return method.invoke(grid, t);
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
            acceptColumn(grid.addColumn(renderer), columnName);
        }

        private void acceptColumn(Grid.Column<T> column, String columnName) {
            column.setKey(columnName);
            Component h = hc.getHeaderComponent(columnName);
            if(h == null) {
                column.setHeader(getHeader(columnName));
            } else {
                column.setHeader(h);
            }
            Boolean v = columnResizable.get(columnName);
            if(v != null) {
                column.setResizable(v);
            }
            v = columnVisible.get(columnName);
            if(v != null) {
                column.setVisible(v);
            }
            customizeColumn(columnName, column);
        }

        private void customizeColumn(String columnName, Grid.Column<T> column) {
            if(grid instanceof DataGrid) {
                ((DataGrid<T>) grid).customizeColumn(columnName, column);
            } else {
                ((DataTreeGrid<T>) grid).customizeColumn(columnName, column);
            }
        }

        private int getColumnOrder(String columnName) {
            try {
                if (grid instanceof DataGrid) {
                    return ((DataGrid<T>) grid).getColumnOrder(columnName);
                } else {
                    return ((DataTreeGrid<T>) grid).getColumnOrder(columnName);
                }
            } catch (AbstractDataForm.FieldError ignored) {
            }
            return cc().getColumnOrder(columnName);
        }

        private boolean includeColumn(String columnName) {
            try {
                return hc.includeColumn(columnName);
            } catch (AbstractDataForm.FieldError ignored) {
            }
            return true;
        }

        private String getHeader(String columnName) {
            return cc().getHeader(columnName);
        }

        private View getView(boolean create) {
            if(view == null && create) {
                if(grid instanceof DataGrid) {
                    view = ((DataGrid<T>)grid).createView();
                } else {
                    view = ((DataTreeGrid<T>)grid).createView();
                }
            }
            if(view == null && create) {
                view = new View(grid, getCaption());
            }
            return view;
        }

        /**
         * Caption used when displaying it in a View. If no caption was set using setCaption method, a label for the Bean's class
         * is determined from the Application Environment.
         * @return Caption
         */
        private String getCaption() {
            return caption == null || caption.trim().isEmpty() ? Objects.requireNonNull(ApplicationEnvironment.get()).createLabel(objectClass) : caption;
        }

        private void close() {
            if(view != null) {
                view.close();
            }
        }

        private void abort() {
            if(view != null) {
                view.abort();
            }
        }

        private void select(Iterable<T> items) {
            items.forEach(grid::select);
        }

        private void select(Iterator<T> items) {
            items.forEachRemaining(grid::deselect);
        }

        private void deselect(Iterable<T> items) {
            items.forEach(grid::select);
        }

        private void deselect(Iterator<T> items) {
            items.forEachRemaining(grid::deselect);
        }

        private T getSelected() {
            Set<T> set = grid.getSelectedItems();
            if(set == null || set.size() > 1) {
                return null;
            }
            return set.stream().findAny().orElse(null);
        }

        private void scrollTo(int rowIndex) {
            if(rowIndex < 0) {
                return;
            }
            UI.getCurrent().getPage().executeJavaScript("$0._scrollToIndex(" + rowIndex + ")", grid.getElement());
        }

        boolean rendered() {
            return renderers == null;
        }

    }
}
