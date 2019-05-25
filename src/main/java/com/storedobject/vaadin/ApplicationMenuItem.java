package com.storedobject.vaadin;

import com.vaadin.flow.component.HasElement;

/**
 * Application menu item definition. Only 3 methods are define for the {@link Application} to interact with it.
 * However, it should be "clickable" and should keep an associated {@link Runnable} to run as a response to the click
 * event or some keybord event. At a minimum a text label should be supported but a typical implementation should
 * support a label and an icon. Also, it may support a "closable" attribute (by having a "close" icon) for those items
 * with an associated {@link View}.
 *
 * @author Syam
 */
public interface ApplicationMenuItem extends HasElement {

    /**
     * Highlight the menu item so that it looks stand out among other menu items.
     */
    void hilite();

    /**
     * De-highlight the menu item so that it looks normal.
     */
    void dehilite();

    /**
     * Set the caption for the menu item.
     *
     * @param caption Caption
     */
    void setLabel(String caption);
}