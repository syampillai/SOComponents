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
    default void add(ApplicationMenuItem menuItem) {
        getMenuPane().getElement().appendChild(menuItem.getElement().removeFromTree());
    }

    /**
     * Insert a menu item at a particular position in the menu.
     * @param position Position
     * @param menuItem Menu item
     */
    default void insert(int position, ApplicationMenuItem menuItem) {
        getMenuPane().getElement().insertChild(position, menuItem.getElement().removeFromTree());
    }

    /**
     * Remove a menu item from the menu.
     * @param menuItem Menu item
     */
    default void remove(ApplicationMenuItem menuItem) {
        getMenuPane().getElement().removeChild(menuItem.getElement());
    }
}
