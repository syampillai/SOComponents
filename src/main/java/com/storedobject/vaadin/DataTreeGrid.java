package com.storedobject.vaadin;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
@CssImport("./so/grid/styles.css")
public class DataTreeGrid<T> extends TreeGrid<T> implements HasColumns<T> {

    private final SOGrid<T> soGrid;
    private ApplicationMenuItem menuItem;
    private boolean firstFooter = true;

    /**
     * Constructor that will generate columns from the Bean's properties.
     *
     * @param objectClass Bean type
     */
    public DataTreeGrid(Class<T> objectClass) {
        this(objectClass, null);
    }

    /**
     * Constructor that will generate columns from the column names passed.
     *
     * @param objectClass Bean type
     * @param columns Column names
     */
    public DataTreeGrid(Class<T> objectClass, Iterable<String> columns) {
        soGrid = new SOGrid<>(this, objectClass, columns);
    }

    /**
     * For internal use only.
     *
     * @return SO grid.
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

    /**
     * This method always return <code>null</code>. Instead of this method, use {@link #createHierarchyColumn(String, ValueProvider)}.
     *
     * @param valueProvider Value provider for column values
     * @return <code>null</code>
     */
    @Override
    public Column<T> addHierarchyColumn(ValueProvider<T, ?> valueProvider) {
        return null;
    }

    /**
     * Add the hierarchy column. If this or any of its cousin method is never called, the first column created will be made the hierarchy column.
     *
     * @param columnName Name of the column
     * @param valueProvider Value provider for the column
     * @return Column created.
     */
    @Override
    public Column<T> createHierarchyColumn(String columnName, ValueProvider<T, ?> valueProvider) {
        if(soGrid.treeCreated() || columnName == null) {
            return null;
        }
        soGrid.treeBuilt(columnName);
        Column<T> column = super.addHierarchyColumn(valueProvider);
        soGrid.acceptColumn(column, columnName);
        return column;
    }

    /**
     * Add a HTML hierarchy column. If this or any of its cousin method is never called, the first column created will be made the hierarchy column.
     *
     * @param columnName Name of the column
     * @param htmlFunction Function that returns HTML content
     * @return Column created.
     */
    @Override
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public Column<T> createHTMLHierarchyColumn(String columnName, Function<T, ?> htmlFunction) {
        if(soGrid.treeCreated() || columnName == null) {
            return null;
        }
        soGrid.treeBuilt(columnName);
        Column<T> column = addColumn((Renderer<T>)TemplateRenderer.
                of("<vaadin-grid-tree-toggle leaf='[[item.leaf]]' expanded='{{expanded}}' level='[[level]]'><span inner-h-t-m-l=\"[[item.html]]\"></span></vaadin-grid-tree-toggle>").
                withProperty("leaf", item -> !getDataCommunicator().hasChildren((T)item)).
                withProperty("html", item -> Objects.requireNonNull(ApplicationEnvironment.get()).toDisplay(htmlFunction.apply((T)item))));
        soGrid.acceptColumn(column, columnName);
        return column;
    }

    /**
     * Get the menu item for this view. This is the menu item displayed by the {@link Application} when the view is activated.
     * This method is final but {@link #createMenuItem(Runnable)} can be overridden for customizing it.
     *
     * @param menuAction Action for the menu item to be created
     * @return Menu item.
     */
    @Override
    public final ApplicationMenuItem getMenuItem(Runnable menuAction) {
        if(menuItem == null) {
            menuItem = HasColumns.super.getMenuItem(menuAction);
            if(menuItem == null) {
                menuItem = getApplication().getEnvironment().createMenuItem(this, getCaption(), menuAction);
            }
        }
        return menuItem;
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