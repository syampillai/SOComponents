package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.SerializableBiFunction;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataGrid<T> extends Grid<T> {

    private final Class<T> objectClass;
    private Map<String, Renderer<T>> renderers = new HashMap<>();
    private Object methodHandlerHost;
    private int paramId = 0;
    private ButtonIcon configure;

    public DataGrid(Class<T> objectClass) {
        this.objectClass = objectClass;
        addAttachListener(e -> init());
        super.setDataProvider(new DataSupplier<>());
    }

    protected void constructed() {
    }

    private void init() {
        if(renderers == null) {
            return;
        }
        generateColumns();
        Stream<String> columnNames = getColumnNames();
        if (columnNames != null) {
            columnNames.forEach(n -> createColumn(n));
        }
        renderers.keySet().stream().filter(this::includeColumn).sorted(Comparator.comparingInt(this::getColumnOrder)).forEach(n -> constructColumn(n));
        renderers = null;
        getElement().setAttribute("theme", "row-stripes");
        constructHeader(createHeader());
        constructed();
    }

    protected Component createHeader() {
        return null;
    }

    private void constructHeader(Component component) {
        if(component == null) {
            return;
        }
        HeaderRow r = prependHeaderRow();
        List<Column<T>> columns = getColumns();
        Column<T>[] a = new Column[columns.size()];
        columns.toArray(a);
        HeaderRow.HeaderCell c = r.join(a);
        c.setComponent(component);
    }

    public ButtonIcon getConfigureButton() {
        if(configure == null) {
            configure = new ButtonIcon(VaadinIcon.COG_O, null);
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

    protected void generateColumns() {
        String name;
        for(Method m: objectClass.getMethods()) {
            name = getColumnName(m);
            if(name != null && includeColumn(name)) {
                createColumn(name, m);
            }
        }
    }

    protected Stream<String> getColumnNames() {
        return null;
    }

    public int size() {
        DataProvider<T, ?> p = provider();
        return p == null ? 0 : p.size(new Query<>());
    }

    public void refresh() {
        getDataProvider().refreshAll();
    }

    public void refresh(T item) {
        getDataProvider().refreshItem(item);
    }

    public T getItem(int index) {
        DataProvider<T, ?> p = provider();
        if(p != null) {
            Query<T, ?> q = new Query<>(0, 1, null, null, null);
            return ((DataSupplier<T, ?>)p).fetchInt(q).findAny().orElse(null);
        }
        return createObjectInstance();
    }

    public T createObjectInstance() {
        try {
            return objectClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    protected Method getColumnMethod(String columnName) {
        if(methodHandlerHost != null) {
            try {
                return methodHandlerHost.getClass().getMethod("get" + columnName);
            } catch (NoSuchMethodException e) {
            }
            try {
                return methodHandlerHost.getClass().getMethod("is" + columnName);
            } catch (NoSuchMethodException e) {
            }
        }
        try {
            return this.getClass().getMethod("get" + columnName);
        } catch (NoSuchMethodException e) {
        }
        try {
            return this.getClass().getMethod("is" + columnName);
        } catch (NoSuchMethodException e) {
        }
        try {
            return objectClass.getMethod("get" + columnName);
        } catch (NoSuchMethodException e) {
        }
        try {
            return objectClass.getMethod("is" + columnName);
        } catch (NoSuchMethodException e) {
        }
        return null;
    }

    public Function<T, ?> getColumnFunction(String columnName) {
        return null;
    }


    public boolean createColumn(String columnName) {
        if(columnName == null) {
            return false;
        }
        if(renderers != null && renderers.containsKey(columnName)) {
            return false;
        }
        Function<T, ?> function = getColumnFunction(columnName);
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

    public boolean createColumn(String columnName, Function<T, ?>... functions) {
        return createColumn(columnName, null, functions);
    }

    public boolean createColumn(String columnName, String template, Function<T, ?>... functions) {
        if (functions == null || functions.length == 0) {
            return createColumn(columnName);
        }
        Renderer<T> r = renderer(template, functions);
        if(renderers == null) {
            constructColumn(columnName, r);
        } else {
            renderers.put(columnName, r);
        }
        return true;
    }

    public boolean createColumn(String columnName, Method method) {
        if(method == null) {
            return createColumn(columnName);
        }
        return createColumn(columnName, getMethodFunction(method));
    }

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

    public String getColumnTemplate(String columnName) {
        return null;
    }

    private Renderer<T> renderer(String template, Function<T, ?>... functions) {
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
        i = 0;
        for(i = 0; i < ids.length; i++) {
            final Function<T, ?> function = functions[i];
            r.withProperty("so" + ids[i], object -> stringify(function.apply(object)));
        }
        return r;
    }

    protected String stringify(Object any) {
        if(any == null) {
            return "";
        }
        String s = any.toString();
        return s == null ? "" : s;
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
            return new Function<T, Object>() {
                @Override
                public Object apply(T t) {
                    try {
                        return method.invoke(methodHandlerHost);
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                    return null;
                }
            };
        }
        if(method.getDeclaringClass() == DataGrid.this.getClass()) {
            return new Function<T, Object>() {
                @Override
                public Object apply(T t) {
                    try {
                        return method.invoke(DataGrid.this);
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                    return null;
                }
            };
        }
        return new Function<T, Object>() {
            @Override
            public Object apply(T t) {
                try {
                    return method.invoke(t);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
                return null;
            }
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
        customizeColumn(columnName, c);
    }

    protected void customizeColumn(String columnName, Grid.Column<T> column) {
    }

    public int getColumnOrder(String columnName) {
        return Integer.MAX_VALUE;
    }

    public boolean includeColumn(String columnName) {
        return true;
    }

    public Component getHeaderComponent(String columnName) {
        return null;
    }

    public String getHeader(String columnName) {
        return Form.createLabel(columnName);
    }

    public void setMethodHandlerHost(Object host) {
        this.methodHandlerHost = host;
    }

    private DataProvider<T, ?> provider() {
        return ((DataSupplier<T, ?>)getDataProvider()).provider;
    }

    private class DataSupplier<T, F> extends AbstractDataProvider<T, F> implements BackEndDataProvider<T, F> {

        private DataProvider<T, F> provider;

        @Override
        public void setSortOrders(List<QuerySortOrder> list) {
            if(provider == null || !(provider instanceof  BackEndDataProvider)) {
                return;
            }
            ((BackEndDataProvider<T, F>)provider).setSortOrders(list);
        }

        @Override
        public void setSortOrder(QuerySortOrder sortOrder) {
            if(provider == null || !(provider instanceof  BackEndDataProvider)) {
                return;
            }
            ((BackEndDataProvider<T, F>)provider).setSortOrder(sortOrder);
        }

        @Override
        public void setSortOrders(QuerySortOrderBuilder builder) {
            if(provider == null || !(provider instanceof  BackEndDataProvider)) {
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
            return provider == null ? true : provider.isInMemory();
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
            return IntStream.range(start, end).mapToObj(i -> (T)DataGrid.this.getItem(i));
        }

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