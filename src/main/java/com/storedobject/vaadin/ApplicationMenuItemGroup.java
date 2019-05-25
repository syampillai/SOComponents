package com.storedobject.vaadin;

/**
 * A special menu item that has sub-menus in it. The {@link Runnable} menu action for this should be to expand and
 * collapse the sub-menu.
 *
 * @author Syam
 */
public interface ApplicationMenuItemGroup extends ApplicationMenuItem {

    /**
     * Add a sub-menu item to this menu item,
     *
     * @param menuItem Sub-menu item
     */
    void add(ApplicationMenuItem menuItem);

    /**
     * Remove a sub-menu item from this menu item,
     *
     * @param menuItem Sub-menu item
     */
    void remove(ApplicationMenuItem menuItem);
}
