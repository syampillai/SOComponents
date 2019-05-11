package com.storedobject.vaadin;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Enhancement to Vaadin's Grid to handle Java Beans in a specialized way. Please note that this is not supporting the functionality supported
 * by Vaadin's Bean Grid but has similar functionality. The main difference in use is that instead of addColumn methods, one should use the
 * createColumn methods. createColumn methods just return whether column can be created or not but, columns are created at a later stage
 * when all columns are defined and the ordinality of columns are determined. If you want to customize any Column, it can be done in
 * customizeColumn(String, Column) method or by invoking methods provided in this class
 * (See the implentation of the interface {@link HasColumns}).
 * Each column has a "column name" and it gets mapped to the Bean's getXXX
 * method just like in Vaadin's Bean Grid. However, if a getXXX method is available in the DataGrid itself, that will be used for sourcing the
 * data for the respective column. Each column uses its respective column name as the key.
 *
 * @param <T> Bean type
 * @author Syam
 */
@HtmlImport("so-grid-styles.html")
public class DataGrid<T> extends Grid<T> implements HasColumns<T> {

    private final SOGrid<T> soGrid;

    /**
     * Constructor that will generate columns from the Bean's properties.
     *
     * @param objectClass Bean type
     */
    public DataGrid(Class<T> objectClass) {
        this(objectClass, null);
    }

    /**
     * Constructor that will generate columns from the column names passed.
     *
     * @param objectClass Bean type
     * @param columns Column names
     */
    public DataGrid(Class<T> objectClass, Iterable<String> columns) {
        soGrid = new SOGrid<>(this, objectClass, columns);
    }

    /**
     * For internal use only.
     *
     * @return The embedded SO Grid.
     */
    @Override
    public final SOGrid<T> getSOGrid() {
        return soGrid;
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
}