package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.Tag;

/**
 * Basic implementation of the CSS Grid layout.
 * 
 * @author Syam
 */
@Tag("div")
public class CSSGrid extends Component implements HasOrderedComponents, HasStyle, HasSize {
    
    public enum Position { START, END, CENTER, STRETCH }

    /**
     * Constructor.
     */
    public CSSGrid() {
        style("display", "grid");
        style("align-items", "center");
        style("justify-items", "center");
        setGap("4px");
        style("transition", "all 1s");
    }

    /**
     * Set gap between components.
     *
     * @param size Gap
     */
    public void setGap(String size) {
        style("grid-row-gap", size == null ? "1px" : size);
        style("grid-column-gap", size == null ? "1px" : size);
    }

    /**
     * Set gap between components.
     *
     * @param size Gap in pixels
     */
    public void setGap(int size) {
        setGap((Math.max(size, 0)) + "px");
    }

    /**
     * Set gap between columns.
     * @param size Gap
     */
    public void setColumnGap(String size) {
        style("grid-column-gap", size == null ? "1px" : size);
    }

    /**
     * Set gap between columns.
     * @param size Gap in pixels
     */
    public void setColumnGap(int size) {
        setColumnGap(Math.max(size, 0) + "px");
    }

    /**
     * Set gap between rows.
     * @param size Gap
     */
    public void setRowGap(String size) {
        style("grid-row-gap", size == null ? "1px" : size);
    }

    /**
     * Set gap between rows.
     * @param size Gap in pixels
     */
    public void setRowGap(int size) {
        setRowGap((Math.max(size, 0)) + "px");
    }

    protected void style(String styleName, String styleValue) {
        getStyle().set(styleName, styleValue);
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

    /**
     * Set number of columns to span.
     *
     * @param component Component
     * @param columns Columns to span
     */
    public void setColumnSpan(Component component, int columns) {
        if(columns < 1) {
            columns = 1;
        }
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
        if(rows < 1) {
            rows = 1;
        }
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
}