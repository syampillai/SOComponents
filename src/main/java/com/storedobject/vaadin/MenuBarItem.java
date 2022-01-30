package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

/**
 * Represents a "menu bar item" or its "sub-menu".
 *
 * @author Syam
 */
public abstract class MenuBarItem extends Composite<Component> {

    /**
     * Add a menu with its click-handler.
     * @param caption Caption to display.
     * @param clickHandler Click-handler.
     * @return The menu that is added. This menu can not add further items because it already defines its own
     * click handler.
     */
    public abstract MenuBarItem addMenuItem(String caption, ClickHandler clickHandler);

    /**
     * Add a menu with its click-handler.
     * @param component Component to display.
     * @param clickHandler Click-handler.
     * @return The menu that is added. This menu can not add further items because it already defines its own
     * click handler.
     */
    public abstract MenuBarItem addMenuItem(Component component, ClickHandler clickHandler);

    /**
     * Add a menu so that a sub-menu can be created under that.
     * @param caption Caption to display.
     * @return The menu that is added. It will be a sub-menu so that you can add further menu items under it.
     */
    public abstract MenuBarItem addMenuItem(String caption);

    /**
     * Add a menu so that a sub-menu can be created under that.
     * @param component Component to display.
     * @return The menu that is added. It will be a sub-menu so that you can add further menu items under it.
     */
    public abstract MenuBarItem addMenuItem(Component component);

    /**
     * Removes the given menu items from this.
     * @param items Menu items to remove.
     */
    public abstract void remove(MenuBarItem... items);

    /**
     * Removes all menu items from this.
     */
    public abstract void removeAll();


    /**
     * Sets the checkable state of this menu item. A checkable item toggles a checkmark icon when clicked.
     * Changes in the checked state can be handled in the item's click handler with {@link #isChecked()}.
     * Setting a checked item un-checkable also makes it un-checked.
     * @param checkable <code>true</code> to enable toggling the checked-state of this menu item by clicking,
     *                  <code>false</code> to disable it.
     */
    public void setCheckable(boolean checkable) {
    }

    /**
     * Gets whether this item toggles a checkmark icon when clicked.
     * @return the checkable state of the item.
     */
    public boolean isCheckable() {
        return false;
    }

    /**
     * Sets the checked state of this item. A checked item displays a checkmark icon next to it. The checked state is
     * also toggled by clicking the item.
     * @param checked <code>true</code> to check this item, <code>false</code> to un-check it.
     */
    public void setChecked(boolean checked) {
    }

    /**
     * Gets the checked state of this item. The item can be checked and un-checked with {@link #setChecked(boolean)}
     * or by clicking it when it is checkable. A checked item displays a checkmark icon inside it.
     * @return <code>true</code> if the item is checked, <code>false</code> otherwise.
     */
    public boolean isChecked() {
        return false;
    }
}
