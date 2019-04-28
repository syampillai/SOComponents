package com.storedobject.vaadin;

import com.vaadin.flow.component.HasElement;

public interface ApplicationMenuItem extends HasElement {

    /**
     * Enable or disable the menu item.
     *
     * @param enable Whether to enable or not
     */
    void setEnabled(boolean enable);

    /**
     * Check if the menu item is enabled or not.
     *
     * @return True if enabled.
     */
    boolean isEnabled();

    /**
     * Highlight the menu item so that it looks stand out among other menu items.
     */
    void hilite();

    /**
     * Set the caption for the menu item.
     *
     * @param caption Caption
     */
    void setLabel(String caption);

    /**
     * Show or hide the menu item.
     *
     * @param visibility Whether to make it visible or not
     */
    void setVisible(boolean visibility);

    /**
     * Check if the menu item is visible or not.
     *
     * @return True if visible.
     */
    boolean isVisible();
}