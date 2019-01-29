package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;

/**
 * Interface that defines the "menu" area of the {@link Application}.
 * @author Syam
 */
public interface ApplicationMenu extends HasComponents {

    /**
     * Get the menu pane.
     * @return Menu pane.
     */
    default HasComponents getMenuPane() {
        return this;
    }

    /**
     * Add a menu item to the menu.
     * @param menuItem Menu item
     */
    default void add(MenuItem menuItem) {
        getMenuPane().getElement().appendChild(menuItem.getComponent().getElement());
    }

    /**
     * Insert a menu item at a particular position in the menu.
     * @param position Position
     * @param menuItem Menu item
     */
    default void insert(int position, MenuItem menuItem) {
        getMenuPane().getElement().insertChild(position, menuItem.getComponent().getElement());
    }

    /**
     * Remove a menu item from the menu.
     * @param menuItem Menu item
     */
    default void remove(MenuItem menuItem) {
        getMenuPane().getElement().removeChild(menuItem.getComponent().getElement());
    }
}
