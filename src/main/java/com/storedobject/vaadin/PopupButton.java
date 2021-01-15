package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * A button that can pop up some other set of components when clicked. Component set to be popped up can be added using the add method.
 *
 * @author Syam
 */
public class PopupButton extends Button {

    private ContextMenu menu;
    private final GridLayout container = new GridLayout(1);

    /**
     * Constructor.
     *
     * @param text Text label to display and the respective icon will be used
     */
    public PopupButton(String text) {
        super(text, null);
        init();
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     */
    public PopupButton(Component icon) {
        super(icon, null);
        init();
    }

    /**
     * Constructor.
     *
     * @param text Text label to display
     * @param icon Name of the icon to use
     */
    public PopupButton(String text, String icon) {
        super(text, icon, null);
        init();
    }

    /**
     * Constructor.
     *
     * @param text Text label to display
     * @param icon Icon to use
     */
    public PopupButton(String text, Component icon) {
        super(text, icon, null);
        init();
    }

    /**
     * Constructor.
     *
     * @param icon Icon to use
     */
    public PopupButton(VaadinIcon icon) {
        super(icon, null);
        init();
    }

    /**
     * Constructor.
     *
     * @param text Text label to display
     * @param icon Icon to use
     */
    public PopupButton(String text, VaadinIcon icon) {
        super(text, icon, null);
        init();
    }

    private void init() {
        getElement().appendChild(new Icon(VaadinIcon.CHEVRON_DOWN_SMALL).getElement());
        createMenu();
    }

    private void createMenu() {
        if(menu != null) {
            menu.setTarget(null);
            menu.removeAll();
        }
        menu = new ContextMenu();
        menu.setTarget(this);
        menu.setOpenOnClick(true);
        menu.addItem(container);
    }

    /**
     * Set equally sized columns. By default, components that are added will be vertically stacked up in one column.
     * This method can be used to make them stacked up as multiple equally sized columns.
     *
     * @param numberOfColumns Number of columns to set
     */
    public void setColumns(int numberOfColumns) {
        container.setColumns(numberOfColumns);
    }

    /**
     * Add components to the pop up.
     *
     * @param components Components to add
     */
    public void add(Component... components) {
        if(components != null) {
            container.add(components);
            for(Component c: components) {
                if(c instanceof HasSize) {
                    ((HasSize) c).setWidth("100%");
                }
            }
        }
    }

    /**
     * Remove components from the pop up.
     *
     * @param components Components to remove
     */
    public void remove(Component... components) {
        if(components != null) {
            container.remove(components);
        }
    }

    /**
     * Remove all components from the pop up.
     */
    public void removeAll() {
        container.removeAll();
    }

    /**
     * Set number of columns to span.
     *
     * @param component Component
     * @param columns Columns to span
     */
    public void setColumnSpan(Component component, int columns) {
        container.setColumnSpan(component, columns);
    }

    /**
     * Get number of columns the component takes up.
     *
     * @param component Component
     * @return Number of columns.
     */
    public int getColumnSpan(Component component) {
        return container.getColumnSpan(component);
    }

    /**
     * Set number of rows to span.
     *
     * @param component Component
     * @param rows Rows to span
     */
    public void setRowSpan(Component component, int rows) {
        container.setRowSpan(component, rows);
    }

    /**
     * Get number of rows the component takes up.
     *
     * @param component Component
     * @return Number of rows.
     */
    public int getRowSpan(Component component) {
        return container.getRowSpan(component);
    }

    /**
     * Justify (horizontally) a component within its grid cell.
     *
     * @param component Component
     * @param position Position
     */
    public void justify(Component component, GridLayout.Position position) {
        container.justify(component, position);
    }

    /**
     * Align (vertically) a component within its grid cell.
     *
     * @param component Component
     * @param position Position
     */
    public void align(Component component, GridLayout.Position position) {
        container.align(component, position);
    }

    /**
     * Center (horizontally and vertically) a component within its grid cell.
     *
     * @param component Component
     */
    public void center(Component component) {
        container.center(component);
    }
}