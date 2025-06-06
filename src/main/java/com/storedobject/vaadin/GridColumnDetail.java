package com.storedobject.vaadin;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;

import java.util.function.Function;

/**
 * This class keeps extra information about {@link com.vaadin.flow.component.grid.Grid.Column}.
 *
 * @param <T> Bean type of the grid.
 * @author Syam
 */
public final class GridColumnDetail<T> {

    private Grid.Column<T> column;
    private String caption, label;
    private Function<T, ?> valueFunction;
    private Class<?> valueType;
    private MenuItem contextMenu;

    /**
     * Constructor.
     */
    GridColumnDetail() {
    }

    /**
     * Set the associated column.
     *
     * @param column Column to set.
     */
    void setColumn(Grid.Column<T> column) {
        this.column = column;
    }

    /**
     * Get the associated grid column.
     *
     * @return Grid column.
     */
    public Grid.Column<T> getColumn() {
        return column;
    }

    /**
     * Get the header caption.
     *
     * @return Header caption. For component columns, this returns null.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Set the header caption.
     *
     * @param caption Header caption.
     */
    public void setCaption(String caption) {
        if(column != null) {
            column.setHeader(caption);
        }
        this.caption = caption;
    }

    /**
     * Get the function that can compute the rendering value associated with column. For multivalued columns,
     * the function returns an array of values.
     *
     * @return Value function.
     */
    public Function<T, ?> getValueFunction() {
        return valueFunction;
    }

    /**
     * Set the value functions.
     *
     * @param valueFunctions Value functions.
     */
    @SafeVarargs
    final void setValueFunction(Function<T, ?>... valueFunctions) {
        if(valueFunctions.length == 1) {
            this.valueFunction = valueFunctions[0];
            return;
        }
        this.valueFunction = o -> {
            Object[] values = new Object[valueFunctions.length];
            for(int i = 0; i < values.length; i++) {
                values[i] = valueFunctions[i].apply(o);
            }
            return values;
        };
    }

    /**
     * Get the value type.
     *
     * @return Value type.
     */
    public Class<?> getValueType() {
        return valueType;
    }

    /**
     * Set the value type.
     *
     * @param valueType Value type.
     */
    void setValueType(Class<?> valueType) {
        this.valueType = valueType;
    }

    /**
     * Set the context menu (used by the configurator). See {@link HasColumns#getConfigureButton()}.
     *
     * @param contextMenu Context menu item to set.
     */
    void setContextMenu(MenuItem contextMenu) {
        this.contextMenu = contextMenu;
    }

    /**
     * Get the context menu item associated with this. (Used by the configurator).
     *
     * @return The context menu item. It could be null if not yet set.
     */
    MenuItem getContextMenu() {
        return contextMenu;
    }

    /**
     * Set the column visible.
     * <p>Note: This is the recommended way to make the column visible because it will update the context menu
     * of the configurator too.</p>
     *
     * @param visible True/false.
     */
    public void setVisible(boolean visible) {
        if(column.isVisible() == visible) {
            if(contextMenu == null) {
                return;
            }
            if(contextMenu.isChecked() == visible) {
                return;
            }
            contextMenu.setChecked(visible);
            return;
        }
        column.setVisible(visible);
        setVisible(visible);
        column.getGrid().recalculateColumnWidths();
    }

    /**
     * Retrieves the label of the grid column. If the label is not set (null),
     * it returns the caption instead.
     * <p>Note: Label is used as the menu label inside the {@link HasColumns#getConfigureButton()}'s menu.</p>
     *
     * @return The label of the grid column, or the caption if the name is null.
     */
    public String getLabel() {
        return label == null ? caption : label;
    }

    /**
     * Sets the label of the grid column.
     * <p>Note: Label is used as the menu label inside the {@link HasColumns#getConfigureButton()}'s menu.</p>
     *
     * @param label The Label to set for the grid column.
     */
    public void setLabel(String label) {
        this.label = label;
        if(contextMenu != null) {
            contextMenu.setText(label);
        }
    }
}
