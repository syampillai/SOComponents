package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.function.ValueProvider;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

/**
 * Enhancement to Vaadin's TreeGrid to handle Java Beans in a specialized way. Please note that this is not supporting the functionality supported
 * by Vaadin's Bean TreeGrid but has similar functionality. The main difference in use is that instead of addColumn methods, one should use the
 * createColumn methods. createColumn methods just return whether column can be created or not but, columns are created at a later stage
 * when all columns are defined and the ordinality of columns are determined. If you want to customize any Column, it can be done in
 * customizeColumn(String, Column) method or by invoking methods provided in this claas
 * (See the implentation of the interface {@link HasColumns}).
 * Each column has a "column name" and it gets mapped to the Bean's getXXX
 * method just like in Vaadin's Bean Grid. However, if a getXXX method is available in the DataGrid itself, that will be used for sourcing the
 * data for the respective column. Each column uses its respective column name as the key.
 *
 * @param <T> Bean type
 * @author Syam
 */
@HtmlImport("so-grid-styles.html")
public class DataTreeGrid<T> extends TreeGrid<T> implements HasColumns<T> {

    private final SOGrid<T> soGrid;

    /**
     * Constructor that will generate columns from the Bean's properties.
     * @param objectClass Bean type
     */
    public DataTreeGrid(Class<T> objectClass) {
        this(objectClass, null);
    }

    /**
     * Constructor that will generate columns from the column names passed
     * @param objectClass Bean type
     * @param columns Column names
     */
    public DataTreeGrid(Class<T> objectClass, Iterable<String> columns) {
        soGrid = new SOGrid<>(this, objectClass, columns);
    }

    /**
     * For internal use only.
     * @return SO grid.
     */
    @Override
    public final SOGrid<T> getSOGrid() {
        return soGrid;
    }

    /**
     * This method is invoked once all the columns are built and the grid is ready to display.
     */
    protected void constructed() {
    }

    /**
     * You can have a "header row" (as the first row) that covers the whole grid. Typically, such a row is to show your own buttons or components to
     * customize the grid. The default implentation returns null and thus, no such row is created.
     * @return Component to be used as the "header row".
     */
    protected Component createHeader() {
        return null;
    }

    @Override
    public boolean isColumnReorderingAllowed() {
        return soGrid.rendered() ? super.isColumnReorderingAllowed() : soGrid.isColumnReorderingAllowed();
    }

    @Override
    public void setColumnReorderingAllowed(boolean allowColumnReordering) {
        if(soGrid.rendered()) {
            super.setColumnReorderingAllowed(allowColumnReordering);
        } else {
            soGrid.setColumnReorderingAllowed(allowColumnReordering);
        }
    }

    @Override
    public final List<Column<T>> getColumns() {
        if(soGrid.rendered()) {
            return super.getColumns();
        }
        return soGrid.getColumns();
    }

    @Override
    public final Grid.Column<T> getColumnByKey(String columnKey) {
        if(soGrid.rendered()) {
            return super.getColumnByKey(columnKey);
        }
        return soGrid.getColumnByKey(columnKey);
    }

    /**
     * Add the hierarchy column. If this is never called, the first column created will be made the hierarchy column.
     * @param valueProvider Value provider for the column
     * @return Column created.
     */
    @Override
    public Column<T> addHierarchyColumn(ValueProvider<T, ?> valueProvider) {
        soGrid.treeBuilt();
        return super.addHierarchyColumn(valueProvider);
    }

    /**
     * This method is invoked to find out the names of the columns to be generated. However, this will not be invoked if the column names
     * are already passed in the constructor. The default implementation returns null (however, this behaviour can be changed by setting up an
     * appropriate {@link ApplicationEnvironment} that can create a customized {@link ObjectColumnCreator#getColumnNames()})
     * and in that case, columns names will be determined
     * through getXXX and isXXX methods of the Bean type.
     * @return Column names to be constructed.
     */
    protected Stream<String> getColumnNames() {
        return null;
    }

    /**
     * Return the method for generating column data from the Bean. By default, getXXX and isXXX methods are tried.
     * However, this method is invoked only if no getXXX or isXXX method is defined in the grid itself. (Also note that
     * {@link #getColumnFunction(String)}
     * is invoked before that and thus, this will not be invoked if data is already available through a Function returned by
     * the {@link #getColumnFunction(String)} method.
     * @param columnName Column name
     * @return method if available, otherwise null. Firstly, it sees if this can be retrieved from the
     * {@link ObjectColumnCreator#getColumnMethod(String)} returned by the {@link ApplicationEnvironment}.
     */
    protected Method getColumnMethod(String columnName) {
        return soGrid.getColumnMethod(columnName);
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
     * Create a View to display the grid when executed. If this method returns null, a default View will be created.
     * @return A View with this grid as the component.
     */
    protected View createView() {
        return null;
    }
}
