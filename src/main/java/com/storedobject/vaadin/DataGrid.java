package com.storedobject.vaadin;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Enhancement to Vaadin's Grid to handle Java Beans in a specialized way. Please note that this is not supporting the functionality supported
 * by Vaadin's Bean Grid but has similar functionality. The main difference in use is that instead of addColumn methods, one should use the
 * createColumn methods. createColumn methods just return whether column can be created or not but, columns are created at a later stage
 * when all columns are defined and the ordinal of columns is determined. If you want to customize any Column, it can be done in
 * customizeColumn(String, Column) method or by invoking methods provided in this class
 * (See the implementation of the interface {@link HasColumns}).
 * Each column has a "column name" and it gets mapped to the Bean's getXXX
 * method just like in Vaadin's Bean Grid. However, if a getXXX method is available in the DataGrid itself, that will be used for sourcing the
 * data for the respective column. Each column uses its respective column name as the key. By default, "multi-sorting" is on.
 *
 * @param <T> Bean type
 * @author Syam
 */
@CssImport(value = "./so/grid/styles.css", themeFor = "vaadin-grid")
public class DataGrid<T> extends Grid<T> implements HasColumns<T> {

    private final SOGrid<T> soGrid;
    private boolean firstFooter = true;
    private List<ConstructedListener> constructedListeners;

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
        setMultiSort(true);
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
    public Registration addConstructedListener(ConstructedListener constructedListener) {
        if(soGrid.rendered()) {
            constructedListener.constructed(this);
            return () -> {};
        }
        if(constructedListeners == null) {
            constructedListeners = new ArrayList<>();
        }
        constructedListeners.add(constructedListener);
        return () -> constructedListeners.remove(constructedListener);
    }

    @Override
    public Stream<ConstructedListener> streamConstructedListeners() {
        return constructedListeners == null ? Stream.empty() : constructedListeners.stream();
    }

    @Override
    public void clearConstructedListeners() {
        constructedListeners = null;
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

    @Override
    public GridRow appendFooter() {
        if(firstFooter) {
            HasColumns.super.appendFooter();
            firstFooter = false;
        }
        return HasColumns.super.appendFooter();
    }
}