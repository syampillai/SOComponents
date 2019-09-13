package com.storedobject.vaadin;

import com.vaadin.flow.component.*;

/**
 * A layout for arragning components in a grid style. Only columns (just the count or sizes) can be specified.
 * When more components are added, it will wrap to the next rows if the column count exceeds the specified limit.
 *
 * @author Syam
 */
@Tag("div")
public class GridLayout extends Component implements HasOrderedComponents<Component>, HasStyle, HasSize {

    public enum Position { START, END, CENTER, STRETCH }

    /**
     * Constructor.
     * @param numberOfColumns Number of equally sized columns
     */
    public GridLayout(int numberOfColumns) {
        init();
        setColumns(numberOfColumns);
    }

    /**
     * Constructor.
     * @param columnSizes Sizes of proportionately sized columns (A zero value set the size to "auto" meaning the size will be
     *                    automatically determined from the component's size)
     */
    public GridLayout(int... columnSizes) {
        init();
        setColumnSizes(columnSizes);
    }

    /**
     * Constructor.
     * @param columnSizes Sizes of columns
     */
    public GridLayout(String... columnSizes) {
        init();
        setColumnSizes(columnSizes);
    }

    private void init() {
        style("display", "grid");
        style("align-items", "center");
        setColumnGap("4px");
        style("transition", "all 1s");
    }

    /**
     * Set column sizes.
     * @param sizes Sizes of columns
     */
    public void setColumnSizes(String... sizes) {
        if(sizes == null || sizes.length == 0) {
            return;
        }
        StringBuilder s = new StringBuilder();
        for(String size: sizes) {
            if(s.length() > 0) {
                s.append(' ');
            }
            if(size != null) {
                try {
                    if(Integer.parseInt(size) > 0) {
                        size += "fr";
                    } else {
                        size = null;
                    }
                } catch(Throwable ignored) {
                }
            }
            s.append(size == null || size.isEmpty() ? "auto" : size);
        }
        style("grid-template-columns", s.toString());
    }

    /**
     * Set column sizes.
     * @param sizes Sizes of proportionately sized columns (A zero value set the size to "auto" meaning the size will be
     *              automatically determined from the component's size)
     */
    public void setColumnSizes(int... sizes) {
        if(sizes == null || sizes.length == 0) {
            return;
        }
        StringBuilder s = new StringBuilder();
        for(int size: sizes) {
            if(s.length() > 0) {
                s.append(' ');
            }
            s.append(size <= 0 ? "auto" : (size + "fr"));
        }
        style("grid-template-columns", s.toString());
    }

    /**
     * Set equally sized columns.
     *
     * @param numberOfColumns Number of columns to set
     */
    public void setColumns(int numberOfColumns) {
        setColumnSizes(sizes(numberOfColumns, 0));
    }

    /**
     * Set gap between columns.
     * @param size Gap
     */
    public void setColumnGap(String size) {
        style("grid-column-gap", size == null ? "1px" : size);
    }

    /**
     * Set gap between rows.
     * @param size Gap
     */
    public void setRowGap(String size) {
        style("grid-row-gap", size == null ? "1px" : size);
    }
    
    private void style(String styleName, String styleValue) {
        getStyle().set(styleName, styleValue);
    }

    /**
     * For internal use only.
     * @param numberOfColumns Number of columns
     * @param value Value
     * @return An integer array with "number of columns" as size and each element set to "value".
     */
    static int[] sizes(int numberOfColumns, int value) {
        if(numberOfColumns < 1) {
            numberOfColumns = 4;
        }
        int[] sizes = new int[numberOfColumns];
        for(int i = 0; i < numberOfColumns; i++) {
            sizes[i] = value;
        }
        return sizes;
    }

    /**
     * Set number of columns to span.
     *
     * @param component Component
     * @param columns Columns to span
     */
    public void setColumnSpan(Component component, int columns) {
        component.getElement().getStyle().set("grid-column", "span " + columns);
    }

    /**
     * Get number of columns the component takes up.
     *
     * @param component Component
     * @return Number of columns.
     */
    public int getColumnSpan(Component component) {
        return getSpan(component, "column");
    }

    /**
     * Set number of rows to span.
     *
     * @param component Component
     * @param rows Rows to span
     */
    public void setRowSpan(Component component, int rows) {
        component.getElement().getStyle().set("grid-row", "span " + rows);
    }

    /**
     * Get number of rows the component takes up.
     *
     * @param component Component
     * @return Number of rows.
     */
    public int getRowSpan(Component component) {
        return getSpan(component, "row");
    }

    private int getSpan(Component component, String of) {
        try {
            return Integer.parseInt(component.getElement().getStyle().get("grid-" + of).replace(" ", "").replace("span", ""));
        } catch (Throwable ignored) {
        }
        return 1;
    }

    /**
     * Justify (horizontally) a component within its grid cell.
     *
     * @param component Component
     * @param position Position
     */
    public void justify(Component component, Position position) {
        if(position == null) {
            position = Position.STRETCH;
        }
        component.getElement().getStyle().set("justify-self", position.toString().toLowerCase());
    }

    /**
     * Align (vertically) a component within its grid cell.
     *
     * @param component Component
     * @param position Position
     */
    public void align(Component component, Position position) {
        if(position == null) {
            position = Position.STRETCH;
        }
        component.getElement().getStyle().set("align-self", position.toString().toLowerCase());
    }

    /**
     * Center (horizontally and vertically) a component within its grid cell.
     *
     * @param component Component
     */
    public void center(Component component) {
        justify(component, Position.CENTER);
        align(component, Position.CENTER);
    }
}