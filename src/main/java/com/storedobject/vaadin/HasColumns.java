package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Common interface for DataGrid and DataTreeGrid. (Other implementations of {@link Grid} may also implement this).
 *
 * @param <T> Bean type of the Grid/TreeGrid.
 * @author Syam
 */
public interface HasColumns<T> extends ExecutableView {

    /**
     * Add a {@link ConstructedListener} so that we will be informed about when the columns are constructed.
     *
     * @param constructedListener Listener.
     * @return Registration.
     */
    Registration addConstructedListener(ConstructedListener constructedListener);

    /**
     * Get the stream of {@link ConstructedListener}s what were added to this.
     *
     * @return Stream of {@link ConstructedListener}s.
     */
    Stream<ConstructedListener> streamConstructedListeners();

    /**
     * This method is invoked once all the columns are built and the grid is ready to display.
     */
    default void constructed() {
    }

    /**
     * You can have a "header row" (as the first row) that covers the whole grid. Typically, such a row is to show your own buttons or components to
     * customize the grid. The default implementation returns null and thus, no such row is created. (Please note that {@link #createHeaders()}}
     * is invoked before this method for adding other header rows just above the main header row. This may be used for column grouping etc.)
     *
     * @return Component to be used as the "header row".
     */
    default Component createHeader() {
        return null;
    }

    /**
     * Create extra header rows if required here by invoking {@link #prependHeader()} or {@link #appendHeader()} (typically useful for
     * creating column grouping etc.). Default implementation does nothing. This is invoked before {@link #createHeader()}.
     */
    default void createHeaders() {
    }

    /**
     * Create footer rows if required here by invoking {@link #appendFooter()} or {@link #prependFooter()}. Default implementation does nothing.
     */
    default void createFooters() {
    }

    /**
     * Prepend a header row.
     *
     * @return Row created.
     */
    default GridRow prependHeader() {
        return GridRow.createHeader(getSOGrid().grid, false);
    }

    /**
     * Append a header row.
     *
     * @return Row created.
     */
    default GridRow appendHeader() {
        return GridRow.createHeader(getSOGrid().grid, true);
    }

    /**
     * Prepend a footer row.
     *
     * @return Row created.
     */
    default GridRow prependFooter() {
        return GridRow.createFooter(getSOGrid().grid, false);
    }

    /**
     * Append a footer row.
     *
     * @return Row created.
     */
    default GridRow appendFooter() {
        return GridRow.createFooter(getSOGrid().grid, true);
    }

    /**
     * This method is invoked to find out the names of the columns to be generated. However, this will not be invoked if the column names
     * are already passed in the constructor of the {@link SOGrid}. The default implementation returns null (however, this behaviour can be changed by setting up an
     * appropriate {@link ApplicationEnvironment} that can create a customized {@link ObjectColumnCreator#getColumnNames()})
     * and in that case, columns names will be determined
     * through getXXX and isXXX methods of the Bean type.
     *
     * @return Column names to be constructed.
     */
    default Stream<String> getColumnNames() {
        return null;
    }

    /**
     * This method is invoked when the column is actually constructed.
     *
     * @param columnName Column name
     * @param column Grid column that may be customized
     */
    default void customizeColumn(String columnName, Grid.Column<T> column) {
    }


    /**
     * Create a View to display the grid when executed. If this method returns null, a default View will be created.
     *
     * @return A View with this grid as the component. Default implementation returns <code>null</code>.
     */
    default View createView() {
        return null;
    }

    /**
     * Set the "theme" to make the appearance of this grid compact. It uses less space than normal and more rows will be visible.
     */
    default void compact() {
        getSOGrid().compact();
    }

    /**
     * Get the currently rendered object. This method is useful when you have your own methods mapped for column values and you want to do any special setup
     * or computation.
     *
     * @return Currently rendered object.
     */
    default T getObjectRendered() {
        return getSOGrid().objectRendered;
    }

    /**
     * This method is invoked just before each row is rendered. You can do special set up or computation at this stage.
     *
     * @param object Currently rendered object.
     */
    default void render(T object) {
    }

    /**
     * Get the currently rendered unwrapped object. See {@link #unwrap(Object)}.
     *
     * @return Currently rendered unwrapped object.
     */
    default T getObjectUnwrapped() {
        return getSOGrid().objectUnwrapped;
    }

    /**
     * Sometimes the row object being rendered may be embedded in another object and just before rendering the row's column details,
     * it may have to be unwrapped. This method is invoked when the row is rendered and just before the rendering function is applied.
     * The default implementation returns the same object.
     *
     * @param object Currently rendered object.
     * @return Unwrapped object.
     */
    default T unwrap(T object) {
        return object;
    }

    /**
     * Set the resizable attribute of a given column.
     *
     * @param columnName Name of the column
     * @param resizable Whether resizable or not
     */
    default void setColumnResizable(String columnName, boolean resizable) {
        getSOGrid().setColumnResizable(columnName, resizable);
    }

    /**
     * Check the resizable attribute of a given column.
     *
     * @param columnName Name of the column
     * @return Whether resizable or not
     */
    default boolean isColumnResizable(String columnName) {
        return getSOGrid().isColumnResizable(columnName);
    }

    /**
     * Set the visibility attribute of a given column.
     *
     * @param columnName Name of the column
     * @param visible Whether visible or not
     */
    default void setColumnVisible(String columnName, boolean visible) {
        getSOGrid().setColumnVisible(columnName, visible);
    }

    /**
     * Check the visibility attribute of a given column.
     *
     * @param columnName Name of the column
     * @return Whether visible or not
     */
    default boolean isColumnVisible(String columnName) {
        return getSOGrid().isColumnVisible(columnName);
    }

    /**
     * Set the frozen attribute of a given column.
     *
     * @param columnName Name of the column
     * @param frozen Whether frozen or not
     */
    default void setColumnFrozen(String columnName, boolean frozen) {
        getSOGrid().setColumnFrozen(columnName, frozen);
    }

    /**
     * Check the frozen attribute of a given column.
     *
     * @param columnName Name of the column
     * @return Whether frozen or not
     */
    default boolean isColumnFrozen(String columnName) {
        return getSOGrid().isColumnFrozen(columnName);
    }

    /**
     * Get the "configure button" that can be added somewhere in your user interface, usually as the first component of the "header row". The grid columns
     * can be configured by clicking this button.
     *
     * @return Configure button.
     */
    default ButtonIcon getConfigureButton() {
        return getSOGrid().getConfigureButton();
    }

    /**
     * Set the minimum/maximum width of the column.
     *
     * @param perColumnWidthInPixels Minimum width (in pixels) per column
     * @param maxWidthInPixels Maximum width of the grid in pixels
     */
    default void setMinWidth(int perColumnWidthInPixels, int maxWidthInPixels) {
        getSOGrid().setMinWidth(perColumnWidthInPixels, maxWidthInPixels);
    }

    /**
     * Get the column count of the grid.
     *
     * @return Number of columns.
     */
    default int getColumnCount() {
        return getSOGrid().getColumnCount();
    }

    /**
     * Get the data class of the grid.
     *
     * @return Data class.
     */
    default Class<T> getDataClass() {
        return getSOGrid().objectClass;
    }

    /**
     * Refresh the whole grid.
     */
    default void refresh() {
        getSOGrid().grid.getDataProvider().refreshAll();
    }

    /**
     * Refresh a particular item in the grid.
     *
     * @param item Item
     */
    default void refresh(T item) {
        getSOGrid().grid.getDataProvider().refreshItem(item);
    }

    /**
     * Refresh a particular item in the tree grid. (This method is applicable only for tree grids).
     *
     * @param item Item
     * @param refreshChildren Whether refresh children or not
     */
    default void refresh(T item, boolean refreshChildren) {
        getSOGrid().grid.getDataProvider().refreshItem(item, refreshChildren);
    }

    /**
     * Return a Function for generating column data. If this method returns a non-null value, it will be used for
     * rendering the column values unless a getXXX(T object)/isXXX(T object) method exists in the grid itself.
     *
     * @param columnName Column name
     * @return Default implementation returns <code>null</code>.
     */
    default Function<T, ?> getColumnFunction(String columnName) {
        return null;
    }

    /**
     * Get the name of the method to determine column data. Normally, existence of getXXX(T object) or isXXX(T object) methods are checked to determine the
     * method. However, this method can specify that otherwise. If the method name doesn't start with a lowercase character, "get" and "is"
     * prefixes are added to the method name returned to check the existence of the method.
     * 
     * @param columnName Column name
     * @return Name of the method. By default it returns the same column name.
     */
    default String getColumnMethodName(String columnName) {
        return columnName;
    }

    /**
     * Create a column for the given column name. If the column name is "XXX", it first determines if a getXXX(T object) or isXXX(T object) method
     * is already defined in the grid or not for generating data for this column. If not, it calls the getColumnFunction("XXX")
     * to determine if any Function is defined for this purpose. If not found, it calls the getColumnMethod("XXX") to determine.
     *
     * @param columnName Column name
     * @return Whether a new column can be created or not.
     */
    default boolean createColumn(String columnName) {
        return getSOGrid().createColumn(columnName);
    }

    /**
     * Create a column that uses one or more functions to generate its column value. It generates a multi-line output with result of each function
     * in separate line unless a template is already defined for this column using getColumnTemplate method.
     *
     * @param columnName Column name
     * @param functions Functions that take object as a parameter and returns some sort of value. Values are stringified before displaying.
     *                  (Application Environment may be set up to control how the stringification process by defining it in the toDisplay(..) method).
     * @return Whether a new column can be created or not.
     */
    @SuppressWarnings("unchecked")
    default boolean createColumn(String columnName, Function<T, ?>... functions) {
        return getSOGrid().createColumn(columnName, null, functions);
    }

    /**
     * Create a HTML column that uses HTML emitted by the function as its column value.
     *
     * @param columnName Column name
     * @param htmlFunction Function that takes object as a parameter and returns HTML text.
     * @return Whether a new column can be created or not.
     */
    default boolean createHTMLColumn(String columnName, Function<T, ?> htmlFunction) {
        return getSOGrid().createColumn(columnName, true, null, htmlFunction);
    }

    /**
     * Create a column that uses one or more functions to generate its column value. The output is formatted using the "template" passed. The template
     * can contain any HTML text and placeholders such as &lt;1&gt;, &lt;2&gt;, &lt;3&gt; ... These placeholders will be replaced by the values returned by the functions.
     *
     * @param columnName Column name
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
     *
     * @param columnName Column name
     * @param method Method
     * @return Whether a new column can be created or not.
     */
    default boolean createColumn(String columnName, Method method) {
        return getSOGrid().createColumn(columnName, method);
    }

    /**
     * Create a column that generates data through a custom renderer.
     *
     * @param columnName Column name
     * @param renderer Renderer.
     * @return Whether a new column can be created or not.
     */
    default boolean createColumn(String columnName, Renderer<T> renderer) {
        return getSOGrid().createColumn(columnName, renderer);
    }

    /**
     * Get the template for the given column name. (This will not be invoked for HTML columns).
     *
     * @param columnName Column name
     * @return The default implementation returns null.
     */
    default String getColumnTemplate(String columnName) {
        return null;
    }

    /**
     * Return value from this method determines the order in which columns are displayed. Numbers do not have to be continuous, columns with lower
     * numbers are displayed first.
     *
     * @param columnName Column name.
     * @return An integer number that denotes the column order.
     */
    default int getColumnOrder(String columnName) {
        throw AbstractDataForm.FIELD_ERROR;
    }

    /**
     * Determines the respective column should be created or note. This is useful when columns are autogenerated but you want to eliminate
     * some columns.
     *
     * @param columnName Column name
     * @return Default implementation returns true for all columns.
     */
    default boolean includeColumn(String columnName) {
        throw AbstractDataForm.FIELD_ERROR;
    }

    /**
     * Check if the column is in-memory and back-end sortable.
     *
     * @param columnName Column name
     * @return Default implementation returns <code>true</code>.
     */
    default boolean isColumnSortable(String columnName) {
        return true;
    }

    /**
     * Get a "sorter" for the column. This method will be invoked only if {@link #isColumnSortable(String)} returns
     * <code>true</code>. Default implementation returns <code>null</code>. If this method returns
     * <code>non-null</code>, it will be used for sorting the column. Else, if the column's value is an instance of
     * {@link Comparable}, that will be used for sorting the column. Otherwise, a case-insensitive string sorting will
     * be carried out for the value returned by the {@link ApplicationEnvironment#toDisplay(Object)} method for the
     * corresponding column value.
     *
     * @param columnName Column name
     * @return A {@link Comparator} to compare column values.
     */
    default Comparator<T> getColumnSorter(String columnName) {
        return null;
    }

    /**
     * This will be invoked for those columns that are sortable and contains text values. If returned <code>true</code>
     * from this method, case-insensitive sorting will be carried out.
     *
     * @param columnName Column name
     * @return Default implementation returns <code>true</code>.
     */
    default boolean ignoreCaseForColumnSorting(String columnName) {
        return true;
    }

    /**
     * Determines the text alignment of the column.
     *
     * @param columnName Column name
     * @return Default implementation returns START for all columns.
     */
    default ColumnTextAlign getTextAlign(String columnName) {
        return null;
    }

    /**
     * Get the header component for the specified column. If no header component is defined, getHeader method will be invoked to create a text-based
     * header.
     *
     * @param columnName Column name
     * @return Default implementation returns null.
     */
    default Component getColumnHeaderComponent(String columnName) {
        return null;
    }

    /**
     * Get the text to display in the header. This will be invoked only if {@link #getColumnHeaderComponent} returns null.
     *
     * @param columnName Column name
     * @return Text to display as header. By default it tries to determine this from the Application Environment.
     */
    default String getColumnCaption(String columnName) {
        return null;
    }

    /**
     * Get the number of columns that are already defined/created.
     *
     * @return Number of columns already defined via some createColumn/addColumn method.
     */
    default int getDefinedColumnCount() {
        SOGrid<T> g = getSOGrid();
        return g.renderers == null ? g.getColumnCount() : g.renderers.size();
    }

    /**
     * Set some other object to supply the column methods. If such an object is set, for column method look up, this object also will be used
     * before checking in the gird itself.
     *
     * @param host An object containing getXXX / isXXX methods for the respective column names.
     */
    default void setMethodHandlerHost(Object host) {
        getSOGrid().methodHandlerHost = host;
    }

    @Override
    default View getView(boolean create) {
        return getSOGrid().getView(create);
    }

    /**
     * Caption used when displaying it in a View. If no caption was set using setCaption method, a label for the Bean's class
     * is determined from the Application Environment.
     *
     * @return Caption
     */
    default String getCaption() {
        return getSOGrid().caption == null || getSOGrid().caption.trim().isEmpty() ? Objects.requireNonNull(ApplicationEnvironment.get()).createLabel(getSOGrid().objectClass) : getSOGrid().caption;
    }

    /**
     * Closes the view if that exists.
     */
    @Override
    default void close() {
        getSOGrid().close();
    }

    /**
     * Closes the view by aborting if that exists.
     */
    @Override
    default void abort() {
        getSOGrid().abort();
    }

    /**
     * Close resources if any that are left opened.
     * This method is invoked when the grid is removed from the {@link Application}.
     * The default implementation invokes the clean method of the view if it exists.
     */
    default void clean() {
        getSOGrid().clean();
    }

    /**
     * Set caption used when displaying the grid in a View.
     *
     * @param caption Caption
     */
    default void setCaption(String caption) {
        getSOGrid().caption = caption;
        View v = getView();
        if(v != null) {
            v.setCaption(caption);
        }
    }

    /**
     * Get the menu item for this when displayed as view. This is the menu item displayed by the {@link Application}
     * when its view is activated.
     *
     * @return Menu item. May return <code>null</code> it the menu item is not yet created.
     */
    default ApplicationMenuItem getMenuItem() {
        return getSOGrid().view == null? null : getSOGrid().view.getMenuItem();
    }

    /**
     * Configure the columns.
     */
    default void configure() {
        getSOGrid().configure();
    }

    /**
     * Select rows.
     *
     * @param items Items to be selected.
     */
    default void select(Iterable<T> items) {
        getSOGrid().select(items);
    }

    /**
     * Select rows.
     *
     * @param items Items to be selected.
     */
    default void select(Iterator<T> items) {
        getSOGrid().select(items);
    }

    /**
     * Deselect rows.
     *
     * @param items Items to be deselected.
     */
    default void deselect(Iterable<T> items) {
        getSOGrid().deselect(items);
    }

    /**
     * Deselect rows.
     *
     * @param items Items to be deselected.
     */
    default void deselect(Iterator<T> items) {
        getSOGrid().deselect(items);
    }

    /**
     * Get the selected item. This will return the item only if a single item is selected.
     *
     * @return Item if that is the only row selected.
     */
    default T getSelected() {
        return getSOGrid().getSelected();
    }

    /**
     * Get the column for the given column name.
     *
     * @param columnName Name of the column
     * @return Column for the corresponding column name.
     */
    default Grid.Column<T> getColumn(String columnName) {
        return getSOGrid().getColumnByKey(columnName);
    }

    /**
     * This method is invoked when renderer functions are created for each column.
     *
     * @param columnName Name of the column
     * @param html Whether the renderer function returns HTML text or not (For HTML texts, only one function will be available)
     * @param functions List of functions
     */
    @SuppressWarnings({"unchecked"})
    default void setRendererFunctions(String columnName, boolean html, Function<T, ?>... functions) {
    }

    /**
     * Get the current Application.
     *
     * @return Current Application.
     */
    @SuppressWarnings("unchecked")
    default <A extends Application> A getApplication() {
        return (A)getSOGrid().getApplication();
    }

    /**
     * Check if this grid is currently being displayed or not through the associated view.
     *
     * @return True if it is being displayed through the associated view, otherwise false.
     */
    default boolean executing() {
        View v = getSOGrid().view;
        return v != null && v.executing();
    }

    /**
     * Add the hierarchy column. If this or any of its cousin method is never called, the first column created will be made the hierarchy column.
     * Note: This method is invoked only for Tree type grids.
     *
     * @param columnName Name of the column
     * @param valueProvider Value provider for the column
     * @return Column created.
     */
    default Grid.Column<T> createHierarchyColumn(String columnName, ValueProvider<T, ?> valueProvider) {
        return null;
    }

    /**
     * Add a HTML hierarchy column. If this or any of its cousin method is never called, the first column created will be made the hierarchy column.
     * Note: This method is invoked only for Tree type grids.
     *
     * @param columnName Name of the column
     * @param htmlFunction Function that returns HTML content
     * @return Column created.
     */
    default Grid.Column<T> createHTMLHierarchyColumn(String columnName, Function<T, ?> htmlFunction) {
        return null;
    }

    @Override
    default void clearAlerts() {
        ExecutableView.super.clearAlerts();
        View v = getSOGrid().getView(false);
        if(v != null) {
            Application.clearAlerts(v);
        }
    }

    /**
     * This method is invoked multiple times. So, the {@link SOGrid} instance created must be assigned to a variable and
     * returned whenever asked for.
     *
     * @return SO Grid
     */
    SOGrid<T> getSOGrid();

    /**
     * Add an "item selected listener" to the grid. Whenever a row is selected, this listener
     * will be notified via {@link ItemSelectedListener#itemSelected(Component, Object)}.
     *
     * @param itemSelectedListener Listener
     */
    default void addItemSelectedListener(ItemSelectedListener<T> itemSelectedListener) {
        List<ItemSelectedListener<T>> itemSelectedListeners = getSOGrid().getItemSelectedListeners();
        if(itemSelectedListeners == null) {
            return;
        }
        itemSelectedListeners.add(itemSelectedListener);
    }

    /**
     * Add an "items selected listener" to the grid. Whenever one or more rows are selected or deselected, this listener
     * will be notified via {@link ItemsSelectedListener#itemsSelected(Component, Set)}.
     *
     * @param itemsSelectedListener Listener
     */
    default void addItemsSelectedListener(ItemsSelectedListener<T> itemsSelectedListener) {
        addItemSelectedListener(itemsSelectedListener);
    }

    /**
     * Remove an "item selected listener" that was previously added to the grid. See {@link #addItemSelectedListener(ItemSelectedListener)}.
     *
     * @param itemSelectedListener Listener
     */
    default void removeItemSelectedListener(ItemSelectedListener<T> itemSelectedListener) {
        if(getSOGrid().itemSelectedListeners != null) {
            getSOGrid().itemSelectedListeners.remove(itemSelectedListener);
        }
    }

    /**
     * Create "sort order" definition for the given column name. (It will be an "ascending order" definition). This
     * method is useful to use as parameters to {@link #sort(GridSortOrder[])}.
     *
     * @param columnName Column name
     * @return Sort order definition.
     */
    default GridSortOrder<T> sortOrder(String columnName) {
        return sortOrder(columnName, true);
    }

    /**
     * Create "sort order" definition for the given column name. This
     * method is useful to use as parameters to {@link #sort(GridSortOrder[])}.
     *
     * @param columnName Column name
     * @param ascending <code>True</code> for ascending order and <code>false</code> for descending order.
     * @return Sort order definition.
     */
    default GridSortOrder<T> sortOrder(String columnName, boolean ascending) {
        Grid.Column<T> column = columnName == null ? null : getSOGrid().getColumnByKey(columnName);
        return column == null ? null : new GridSortOrder<>(column, ascending ? SortDirection.ASCENDING : SortDirection.DESCENDING);
    }

    /**
     * Sort the columns as defined in the "sort order definitions" passed. Typically, it is easy to write something
     * like this to sort:<BR>
     * <code>sort(sort("Age", false), sort("FirstName"));</code><BR>
     * This will sort "Age" column in the descending order and then, the "FirstName" column in the ascending order.
     *
     * @param sortOrders Sort order definitions.
     */
    @SuppressWarnings("unchecked")
    default void sort(GridSortOrder<T>... sortOrders) {
        if(sortOrders != null) {
            List<GridSortOrder<T>> sortOrderList = new ArrayList<>();
            for(GridSortOrder<T> gso: sortOrders) {
                if(gso != null) {
                    sortOrderList.add(gso);
                }
            }
            if(!sortOrderList.isEmpty()) {
                getSOGrid().grid.sort(sortOrderList);
            }
        }
    }

    /**
     * Sort the given columns in ascending order.
     *
     * @param columnNames Column names.
     */
    default void sort(String... columnNames) {
        if(columnNames != null && columnNames.length > 0) {
            @SuppressWarnings("unchecked") GridSortOrder<T>[] sortOrders = new GridSortOrder[columnNames.length];
            for(int i = 0; i < columnNames.length; i++) {
                sortOrders[i] = sortOrder(columnNames[i]);
            }
            sort(sortOrders);
        }
    }

    /**
     * Get the component for creating the view - See {@link #getView(boolean)} and {@link #getView()}.
     *
     * @return The component for the view. By default, the grid itself is returned.
     */
    default Component getViewComponent() {
        return getSOGrid().grid;
    }

    /**
     * This class takes care of creation of the columns in the grid. In order to have behaviours of {@link HasColumns},
     * an instance of this class is required and should be returned in the {@link HasColumns#getSOGrid()} method. (See
     * the source code of {@link DataGrid} and {@link DataTreeGrid} classes to get an idea). Also, the following methods
     * of the grid must be overridden: {@link Grid#setColumnReorderingAllowed(boolean)},
     * {@link Grid#isColumnReorderingAllowed()}, {@link Grid#getColumns()}, @{@link Grid#getColumnByKey(String)}. These
     * methods should check whether the grid is rendered using {@link SOGrid#rendered()} method and if not rendered,
     * these method calls to be delegated into this class.
     *
     * @param <T> Bean type of the grid
     */
    class SOGrid<T> {

        private final Grid<T> grid;
        private final HasColumns<T> hc;
        private final Class<T> objectClass;
        private Map<String, Renderer<T>> renderers = new HashMap<>();
        private Map<String, Boolean> columnResizable = new HashMap<>();
        private Map<String, Boolean> columnVisible = new HashMap<>();
        private Map<String, Boolean> columnFrozen = new HashMap<>();
        private final Map<String, Class<?>> columnTypes = new HashMap<>();
        private final Map<String, String> columnCaptions = new HashMap<>();
        @SuppressWarnings("rawtypes")
        private Map<String, ValueProvider<T, Comparable>> columnComparators1 = new HashMap<>();
        private Map<String, Comparator<T>> columnComparators2 = new HashMap<>();
        private Map<String, Integer> columnOrders = new HashMap<>();
        private Object methodHandlerHost;
        private int paramId = 0;
        private ButtonIcon configure;
        private ObjectColumnCreator<T> columnCreator;
        private Iterable<String> columns;
        private String caption;
        private boolean allowColumnReordering = true;
        private T objectRendered, objectUnwrapped;
        private View view;
        private String treeColumnName = "_$_";
        private Application application;
        private List<ItemSelectedListener<T>> itemSelectedListeners;

        /**
         * Constructor.
         *
         * @param grid Grid for which SO grid is being created
         * @param objectClass Data class of the grid
         * @param columns Iterable list of column names (maybe null)
         */
        @SuppressWarnings("unchecked")
        public SOGrid(Grid<T> grid, Class<T> objectClass, Iterable<String> columns) {
            this.grid = grid;
            if(grid instanceof TreeGrid) {
                treeColumnName = null;
            }
            if(grid instanceof HasColumns) {
                this.hc = (HasColumns<T>) grid;
            } else {
                this.hc = null;
            }
            this.objectClass = objectClass;
            this.columns = columns;
            grid.addAttachListener(e -> init());
            grid.getElement().getClassList().add("so-grid");
            getApplication();
        }

        private void constructed() {
            if(grid instanceof HasColumns) {
                ((HasColumns<?>) grid).constructed();
                ((HasColumns<?>) grid).streamConstructedListeners().forEach(cl -> cl.constructed(grid));
            }
        }

        private void init() {
            if(renderers == null) {
                return;
            }
            if(columns != null) {
                columns.forEach(n -> {
                    n = trimCaption(n);
                    if(includeColumn(n)) {
                        createColumn(n);
                    }
                });
            } else {
                Stream<String> columnNames = getColumnNames();
                if(columnNames != null) {
                    columnNames.map(this::trimCaption).filter(this::includeColumn).forEach(this::createColumn);
                } else {
                    generateColumns();
                }
            }
            renderers.keySet().stream().filter(this::includeColumn).
                    sorted(Comparator.comparingInt(this::getColumnOrder)).forEach(this::constructColumn);
            columns = null;
            renderers = null;
            columnOrders = null;
            columnResizable = null;
            columnVisible = null;
            columnFrozen = null;
            columnComparators1 = null;
            columnComparators2 = null;
            grid.getElement().setAttribute("theme", getDefaultThemes());
            if(grid instanceof HasColumns) {
                hc.createHeaders();
                Component component = hc.createHeader();
                if(component != null) {
                    hc.prependHeader().join().setComponent(component);
                }
                hc.createFooters();
            }
            if(columnCreator != null) {
                columnCreator.close();
                columnCreator = null;
            }
            grid.setColumnReorderingAllowed(allowColumnReordering);
            constructed();
        }

        private String trimCaption(String columnName) {
            int p = columnName.toLowerCase().indexOf(" as ");
            if(p < 0) {
                return columnName;
            }
            String cn = columnName.substring(0, p).trim();
            columnCaptions.put(cn, columnName.substring(p + 4));
            return cn;
        }

        private void compact() {
            grid.getElement().setAttribute("theme", "compact " + getDefaultThemes());
        }

        /**
         * Get the default themes to be set. The default is "row-stripes wrap-cell-content". This could be overridden in
         * {@link HasColumns#constructed()} method if required.
         *
         * @return Default themes.
         */
        protected String getDefaultThemes() {
            return "row-stripes wrap-cell-content";
        }

        private Application getApplication() {
            if(application == null) {
                application = Application.get();
            }
            return application;
        }

        private void setRO(T object) {
            if(object == objectRendered) {
                return;
            }
            objectRendered = object;
            hc.render(object);
            objectUnwrapped = hc.unwrap(objectRendered);
        }

        @SuppressWarnings("unchecked")
        private ObjectColumnCreator<T> cc() {
            if(columnCreator == null) {
                columnCreator = ((ObjectColumnCreator<T>) Objects.requireNonNull(ApplicationEnvironment.get()).getObjectColumnCreator()).create(hc);
            }
            return columnCreator;
        }

        /**
         * Check whether the column reordering is allowed or not. (Delegated method).
         *
         * @return True if column reordering is allowed.
         */
        public boolean isColumnReorderingAllowed() {
            return allowColumnReordering;
        }

        /**
         * Set whether column reordering is allowed or not. (Delegated method).
         *
         * @param allowColumnReordering True if column reordering is allowed.
         */
        public void setColumnReorderingAllowed(boolean allowColumnReordering) {
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
            return v != null && v;
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
            return v != null && v;
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
            return v != null && v;
        }

        private ButtonIcon getConfigureButton() {
            if(configure == null) {
                configure = new ButtonIcon(VaadinIcon.COG_O, e -> configure());
                configure.setPlaceholder("Configure columns");
            }
            return configure;
        }

        private void configure() {
            grid.recalculateColumnWidths();
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

        /**
         * Get the columns of the grid. (Delegated method).
         *
         * @return Columns of the grid.
         */
        public List<Grid.Column<T>> getColumns() {
            init();
            return grid.getColumns();
        }

        /**
         * Get the column of the grid for the given key. (Delegated method).
         *
         * @param columnKey Column key
         * @return Column for the key.
         */
        public Grid.Column<T> getColumnByKey(String columnKey) {
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
            Stream<String> names = null;
            if(grid instanceof HasColumns) {
                names = ((HasColumns<T>) grid).getColumnNames();
            }
            return names == null ? cc().getColumnNames() : names;
        }

        private Method getColumnMethod(String columnName) {
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
            columnName = getColumnMethodName(columnName);
            Method m;
            Class<?> objectClass = this.objectClass;
            while (true) {
                m = getOutsideMethod(columnName, objectClass);
                if(m != null) {
                    return m;
                }
                if(objectClass == Object.class) {
                    break;
                }
                objectClass = objectClass.getSuperclass();
            }
            return null;
        }

        private Method getOutsideMethod(String columnName, Class<?> objectClass) {
            Class<?>[] params = new Class<?>[] { objectClass };
            boolean methodName = Character.isLowerCase(columnName.charAt(0)) && !columnName.equals(columnName.toLowerCase());
            if(methodHandlerHost != null) {
                if(methodName) {
                    try {
                        return methodHandlerHost.getClass().getMethod(columnName, params);
                    } catch (NoSuchMethodException ignore) {
                    }
                }
                try {
                    return methodHandlerHost.getClass().getMethod("get" + columnName, params);
                } catch (NoSuchMethodException ignore) {
                }
                try {
                    return methodHandlerHost.getClass().getMethod("is" + columnName, params);
                } catch (NoSuchMethodException ignore) {
                }
            }
            if(methodName) {
                try {
                    return grid.getClass().getMethod(columnName, params);
                } catch (NoSuchMethodException ignore) {
                }
            }
            try {
                return grid.getClass().getMethod("get" + columnName, params);
            } catch (NoSuchMethodException ignore) {
            }
            try {
                return grid.getClass().getMethod("is" + columnName, params);
            } catch (NoSuchMethodException ignore) {
            }
            if(columnName.equals(columnName.toLowerCase())) {
                return getOutsideMethod(Character.toUpperCase(columnName.charAt(0)) + columnName.substring(1), objectClass);
            }
            return null;
        }

        private String getColumnMethodName(String columnName) {
            String m = hc.getColumnMethodName(columnName);
            return m == null || m.isEmpty() ? columnName : m;
        }

        private Function<T, ?> getColumnFunction(String columnName) {
            Function<T, ?> f = hc.getColumnFunction(columnName);
            return f == null ? cc().getColumnFunction(columnName) : f;
        }

        @SuppressWarnings("unchecked")
        private boolean createColumn(String columnName) {
            if(columnName == null || columnName.equals(treeColumnName)) {
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
                function = getMethodFunction(columnName, m);
            }
            if(function == null) {
                function = getColumnFunction(columnName);
                if(function == null) {
                    m = getColumnMethod(columnName);
                    if(m == null) {
                        function = item -> "?";
                    } else {
                        function = getMethodFunction(columnName, m);
                    }
                }
            }
            if(createTreeColumn(columnName, function)) {
                return true;
            }
            Class<?> type = getColumnValueType(columnName);
            boolean html = type != null && (type == String.class || HTMLGenerator.class.isAssignableFrom(type));
            Renderer<T> r = html ? renderer(columnName, function) : renderer(columnName, hc.getColumnTemplate(columnName), function);
            hc.setRendererFunctions(columnName, html, function);
            if(renderers == null) {
                constructColumn(columnName, r);
            } else {
                renderers.put(columnName, r);
            }
            return true;
        }

        @SafeVarargs
        private boolean createColumn(String columnName, String template, Function<T, ?>... functions) {
            return createColumn(columnName, false, template, functions);
        }

        @SafeVarargs
        private boolean createColumn(String columnName, boolean html, String template, Function<T, ?>... functions) {
            if(functions == null || functions.length == 0) {
                return createColumn(columnName);
            }
            if(functions.length == 1 && createTreeColumn(columnName, functions[0])) {
                return true;
            }
            if(template == null) {
                template = hc.getColumnTemplate(columnName);
            }
            if(!html) {
                Class<?> type = getColumnValueType(columnName);
                html = type != null && (type == String.class || HTMLGenerator.class.isAssignableFrom(type));
            }
            Renderer<T> r = html ? renderer(columnName, functions[0]) : renderer(columnName, template, functions);
            hc.setRendererFunctions(columnName, html, functions);
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
            return createColumn(columnName, null, getMethodFunction(columnName, method));
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
        private Renderer<T> renderer(String columnName, String template, Function<T, ?>... functions) {
            boolean sortable = hc != null && hc.isColumnSortable(columnName);
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
            if(getColumnValueType(columnName) == String.class) {
                final Function<T, ?> function = functions[0];
                r.withProperty("so" + ids[0], o -> {
                    setRO(o);
                    o = objectUnwrapped;
                    return HTMLGenerator.encodeHTML((String)function.apply(o)).replace("\n", "<br>");
                });
            } else {
                for (i = 0; i < ids.length; i++) {
                    final Function<T, ?> function = functions[i];
                    r.withProperty("so" + ids[i], o -> {
                        setRO(o);
                        o = objectUnwrapped;
                        Object v = function.apply(o);
                        if (v == null && grid instanceof TreeGrid) {
                            v = "";
                        }
                        return Objects.requireNonNull(ApplicationEnvironment.get()).toDisplay(v);
                    });
                }
            }
            if(sortable) {
                Comparator<T> columnSorter = hc.getColumnSorter(columnName);
                if(columnSorter != null) {
                    columnComparators2.put(columnName, columnSorter);
                    return r;
                }
                final Function<T, ?> compareFunction = functions[0];
                @SuppressWarnings("rawtypes") ValueProvider<T, Comparable> valueProvider = o -> {
                    setRO(o);
                    o = objectUnwrapped;
                    Object v = compareFunction.apply(o);
                    if (!(v instanceof Comparable)) {
                        ApplicationEnvironment ae = ApplicationEnvironment.get();
                        if (ae == null) {
                            v = v.toString();
                        } else {
                            v = ae.toDisplay(v);
                        }
                    }
                    if (v instanceof String && hc.ignoreCaseForColumnSorting(columnName)) {
                        v = ((String) v).toLowerCase();
                    }
                    return (Comparable<?>) v;
                };
                columnComparators1.put(columnName, valueProvider);
            }
            return r;
        }

        private Renderer<T> renderer(String columnName, Function<T, ?> htmlFunction) {
            return renderer(columnName,"<span inner-h-t-m-l=\"<1>\"></span>", htmlFunction);
        }

        void treeBuilt(String columnName) {
            treeColumnName = columnName;
        }

        boolean treeCreated() {
            return treeColumnName != null;
        }

        private boolean createTreeColumn(String columnName, Method m) {
            if(treeCreated() || m == null) {
                return false;
            }
            if(hc != null) {
                Function<T, ?> function = wrap(getMethodFunction(columnName, m));
                Grid.Column<T> c;
                if (HTMLGenerator.class.isAssignableFrom(m.getReturnType())) {
                    c = hc.createHTMLHierarchyColumn(columnName, function);
                } else {
                    c = hc.createHierarchyColumn(columnName, function::apply);
                }
                if(c != null && !hc.isColumnSortable(columnName)) {
                    c.setSortable(false);
                }
                return c != null;
            }
            return false;
        }

        private boolean createTreeColumn(String columnName, Function<T, ?> function) {
            if(treeCreated() || function == null) {
                return false;
            }
            if(hc != null) {
                function = wrap(function);
                Grid.Column<T> c = hc.createHierarchyColumn(columnName, function::apply);
                if(c != null && !hc.isColumnSortable(columnName)) {
                    c.setSortable(false);
                }
                return c != null;
            }
            return true;
        }

        private Function<T, ?> wrap(Function<T, ?> function) {
            return item -> {
                setRO(item);
                return function.apply(objectUnwrapped);
            };
        }

        private Function<T, ?> getMethodFunction(String columnName, Method method) {
            columnTypes.put(columnName, method.getReturnType());
            method.setAccessible(true);
            if(methodHandlerHost != null && method.getDeclaringClass().isAssignableFrom(methodHandlerHost.getClass())) {
                return (Function<T, Object>) t -> {
                    try {
                        return method.invoke(methodHandlerHost, t);
                    } catch (IllegalAccessException | InvocationTargetException ignored) {
                    }
                    return null;
                };
            }
            if(method.getDeclaringClass().isAssignableFrom(grid.getClass())) {
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
            constructColumn(columnName, renderer, columnComparators1 == null ? null : columnComparators1.get(columnName), columnComparators2.get(columnName));
        }

        private void constructColumn(String columnName, Renderer<T> renderer, @SuppressWarnings("rawtypes") ValueProvider<T, Comparable> valueProviderForComparator, Comparator<T> comparator) {
            acceptColumn(constructColumn(columnName, grid, renderer, valueProviderForComparator, comparator), columnName);
        }

        /**
         * This is where the column is finally constructed. If you have another implementation, this method can be
         * overridden. (For example, an "editable" grid such as Vaadin's GridPro, may construct an
         * "editable" column). One of the parameters <code>valueProviderForComparator</code> and <code>comparator</code>
         * will be <code>null</code> because their values are mutually exclusive.
         *
         * @param grid Grid for which column needs to be created.
         * @param columnName Name of the column
         * @param renderer Renderer for the column
         * @param valueProviderForComparator Value provider for setting the comparator used for in-memory sorting (Could be <code>null</code>)
         * @param comparator Comparator used for sorting this column (Could be <code>null</code>)
         *
         * @return Column created. Default implementation use the {@link Grid#addColumn(Renderer)} method to create the
         * column and set the comparator if it is not <code>null</code>.
         */
        protected Grid.Column<T> constructColumn(String columnName, Grid<T> grid, Renderer<T> renderer,
                                                 @SuppressWarnings("rawtypes") ValueProvider<T, Comparable> valueProviderForComparator, Comparator<T> comparator) {
            if(valueProviderForComparator != null || comparator != null) {
                Grid.Column<T> column = grid.addColumn(renderer, columnName);
                if(valueProviderForComparator != null) {
                    //noinspection unchecked
                    column.setComparator(valueProviderForComparator);
                } else {
                    column.setComparator(comparator);
                }
                return column;
            }
            return grid.addColumn(renderer);
        }

        /**
         * This will be invoked when each column is constructed. (If you directly create any columns using
         * addColumn methods of the {@link Grid}, then it is highly recommended to invoke this method for those
         * columns. Also, make sure that a unique column name is used because column name will be used as the
         * column key of the grid).
         *
         * @param column Column that is constructed
         * @param columnName Name of the column
         */
        public void acceptColumn(Grid.Column<T> column, String columnName) {
            column.setKey(columnName);
            Component h = hc.getColumnHeaderComponent(columnName);
            if(h == null) {
                column.setHeader(getHeader(columnName));
            } else {
                column.setHeader(h);
            }
            if(columnResizable != null) {
                Boolean r = columnResizable.get(columnName);
                if (r != null) {
                    column.setResizable(r);
                }
            }
            if(columnVisible != null) {
                Boolean v = columnVisible.get(columnName);
                if (v != null) {
                    column.setVisible(v);
                }
            }
            column.setTextAlign(getTextAlign(columnName));
            column.setAutoWidth(true);
            column.setResizable(true);
            customizeColumn(columnName, column);
        }

        private void customizeColumn(String columnName, Grid.Column<T> column) {
            if(hc != null) {
                hc.customizeColumn(columnName, column);
            }
        }

        private int getColumnOrderCustom(String columnName) {
            try {
                if(hc != null) {
                    return hc.getColumnOrder(columnName);
                }
            } catch (AbstractDataForm.FieldError ignored) {
            }
            return cc().getColumnOrder(columnName);
        }

        private int getColumnOrder(String columnName) {
            if(columnOrders != null) {
                Integer o = columnOrders.get(columnName);
                if(o != null) {
                    return o;
                }
            }
            int order = getColumnOrderCustom(columnName);
            if(order == Integer.MIN_VALUE && columns != null) {
                order = 0;
                for(String name: columns) {
                    if(trimCaption(name).equals(columnName)) {
                        break;
                    }
                    ++order;
                }
            }
            if(columnOrders != null) {
                columnOrders.put(columnName, order);
            }
            return order;
        }

        private boolean includeColumn(String columnName) {
            try {
                return hc.includeColumn(columnName);
            } catch (AbstractDataForm.FieldError ignored) {
            }
            return true;
        }

        private ColumnTextAlign getTextAlign(String columnName) {
            ColumnTextAlign a = hc.getTextAlign(columnName);
            if(a == null) {
                Class<?> type = getColumnValueType(columnName);
                if(type == null) {
                    type = cc().getColumnValueType(columnName);
                }
                a = cc().getColumnTextAlign(columnName, type);
            }
            return a == null ? ColumnTextAlign.START : a;
        }

        private Class<?> getColumnValueType(String columnName) {
            Class<?> type = columnTypes.get(columnName);
            if(type == null) {
                type = cc().getColumnValueType(columnName);
            }
            return type == null ? Object.class : type;
        }

        private String getHeader(String columnName) {
            String h = columnCaptions.get(columnName);
            if(h != null) {
                return h;
            }
            h = hc.getColumnCaption(columnName);
            return h == null ? cc().getColumnCaption(columnName) : h;
        }

        private View getView(boolean create) {
            if(view == null && create) {
                if(hc != null) {
                    view = hc.createView();
                }
            }
            if(view == null && create) {
                String caption = hc != null ? hc.getCaption() : "Data View";
                view = new View(hc == null ? grid : hc.getViewComponent(), caption) {

                    @Override
                    public boolean isCloseable() {
                        if(hc != null) {
                            return hc.isCloseable();
                        }
                        return grid instanceof CloseableView;
                    }

                    @Override
                    public boolean isHomeView() {
                        if(hc != null) {
                            return hc.isHomeView();
                        }
                        return grid instanceof HomeView;
                    }

                    @Override
                    public void returnedFrom(View parent) {
                        if(hc != null) {
                            hc.returnedFrom(parent);
                        }
                    }

                    @Override
                    public void close() {
                        super.close();
                        View v = view;
                        view = null;
                        if(hc != null) {
                            hc.close();
                        }
                        view = v;
                    }

                    @Override
                    public void abort() {
                        super.abort();
                        View v = view;
                        view = null;
                        if(hc != null) {
                            hc.abort();
                        }
                        view = v;
                    }

                    @Override
                    public void clean() {
                        super.clean();
                        View v = view;
                        view = null;
                        if(hc != null) {
                            hc.clean();
                        }
                        view = v;
                    }

                    @Override
                    public String getMenuIconName() {
                        if(hc != null) {
                            return hc.getMenuIconName();
                        }
                        return super.getMenuIconName();
                    }

                    @Override
                    public ApplicationMenuItem createMenuItem(Runnable menuAction) {
                        if(hc != null) {
                            return hc.createMenuItem(menuAction);
                        }
                        return super.createMenuItem(menuAction);
                    }
                };
            }
            return view;
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

        private void clean() {
            if(view != null) {
                view.clean();
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

        /**
         * Check whether the rendering is already done.
         *
         * @return True if the rendering is done.
         */
        public boolean rendered() {
            return renderers == null;
        }

        private List<ItemSelectedListener<T>> getItemSelectedListeners() {
            if(itemSelectedListeners == null) {
                itemSelectedListeners = new ArrayList<>();
                grid.addSelectionListener(e -> selected(e.getAllSelectedItems()));
            }
            return itemSelectedListeners;
        }

        private void selected(Set<T> selection) {
            if(itemSelectedListeners == null || itemSelectedListeners.isEmpty()) {
                return;
            }
            itemSelectedListeners.forEach(isl -> {
                if(isl instanceof ItemsSelectedListener) {
                    ((ItemsSelectedListener<T>) isl).itemsSelected(grid, selection);
                } else {
                    isl.itemSelected(grid, selection.stream().findAny().orElse(null));
                }
            });
        }
    }
}
