package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;

/**
 * A button that can pop up some other set of components when clicked. Component set to be popped up can be added using the add method.
 *
 * @author Syam
 */
@CssImport("./so/button/styles.css")
public class PopupButton extends Button {

    private final Popover popover = new Popover();
    private final GridLayout container = new GridLayout(1);
    private GridLayout.Position justify = GridLayout.Position.START, align = GridLayout.Position.CENTER;

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
        Div div = new Div();
        div.addClassName("expand-suffix");
        var i = VaadinIcon.CHEVRON_DOWN_SMALL.create();
        i.setSize("1em");
        div.add(i);
        setSuffixComponent(div);
        popover.setTarget(this);
        popover.setOpenOnClick(true);
        popover.setCloseOnOutsideClick(true);
        popover.setCloseOnEsc(true);
        popover.setPosition(PopoverPosition.BOTTOM_START);
        popover.addThemeVariants(PopoverVariant.ARROW);
        popover.setModal(false);
        popover.setAutofocus(true);
        popover.add(container);
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
     * Add components to the pop-up.
     *
     * @param components Components to add
     */
    public void add(Component... components) {
        if(components != null) {
            container.add(components);
            for(Component c: components) {
                justify(c, justify);
                align(c, align);
                if(c instanceof HasSize s) {
                    s.setWidthFull();
                }
                if(c instanceof com.vaadin.flow.component.button.Button b) {
                    if(!(b instanceof PopupButton)) b.addClickListener(e -> popover.close());
                    if(b.getSuffixComponent() == null) {
                        b.setSuffixComponent(new Div());
                    }
                    b.addClassName("expand-suffix");
                }
            }
        }
    }

    /**
     * Remove components from the pop-up.
     *
     * @param components Components to remove
     */
    public void remove(Component... components) {
        if(components != null) {
            container.remove(components);
        }
    }

    /**
     * Remove all components from the pop-up.
     */
    public void removeAll() {
        container.removeAll();
    }

    /**
     * Set the number of columns to span.
     *
     * @param component Component
     * @param columns Columns to span
     */
    public void setColumnSpan(Component component, int columns) {
        container.setColumnSpan(component, columns);
    }

    /**
     * Get the number of columns the component takes up.
     *
     * @param component Component
     * @return Number of columns.
     */
    public int getColumnSpan(Component component) {
        return container.getColumnSpan(component);
    }

    /**
     * Set the number of rows to span.
     *
     * @param component Component
     * @param rows Rows to span
     */
    public void setRowSpan(Component component, int rows) {
        container.setRowSpan(component, rows);
    }

    /**
     * Get the number of rows the component takes up.
     *
     * @param component Component
     * @return Number of rows.
     */
    public int getRowSpan(Component component) {
        return container.getRowSpan(component);
    }

    /**
     * Horizontally justify a component within its grid cell.
     *
     * @param component Component
     * @param position Position
     */
    public void justify(Component component, GridLayout.Position position) {
        container.justify(component, position);
    }

    /**
     * Vertically align a component within its grid cell.
     *
     * @param component Component
     * @param position Position
     */
    public void align(Component component, GridLayout.Position position) {
        container.align(component, position);
    }

    /**
     * (Horizontally and vertically) center a component within its grid cell.
     *
     * @param component Component
     */
    public void center(Component component) {
        container.center(component);
    }

    /**
     * Aligns the content of the popover button within its grid cell based on the specified position.
     *
     * @param position The vertical alignment position within the grid cell.
     */
    public void align(GridLayout.Position position) {
        justify = position;
    }

    /**
     * Justifies (horizontally aligns) the components in the grid layout associated with the popover.
     *
     * @param position The position to justify the components within the grid cell.
     */
    public void justify(GridLayout.Position position) {
        align = position;
    }

    /**
     * Centers the content within the grid layout horizontally and vertically by
     * setting the justification and alignment to the center position.
     */
    public void center() {
        justify = align = GridLayout.Position.CENTER;
    }

    /**
     * Retrieves the popover associated with this button.
     *
     * @return the {@code Popover} instance associated with this button.
     */
    public final Popover getPopover() {
        return popover;
    }

    /**
     * Retrieves the container for the components within the popover.
     *
     * @return The {@code GridLayout} container used to manage the layout of components.
     */
    public final GridLayout getContainer() {
        return container;
    }
}