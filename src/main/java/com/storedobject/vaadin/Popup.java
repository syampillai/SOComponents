package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.contextmenu.ContextMenu;

import java.io.Serializable;

/**
 * Popup emulated using Vaadin's {@link ContextMenu}.
 *
 * @author Syam
 */
public class Popup extends Composite<ContextMenu> {

    private final ContextMenu menu = new ContextMenu();

    /**
     * Constructor.
     *
     * @param container Container component that gets popped up
     */
    public Popup(Component container) {
        menu.addItem(container, null);
        menu.setOpenOnClick(true);
    }

    @Override
    protected final ContextMenu initContent() {
        return menu;
    }

    /**
     * Set the target component for this popup.
     *
     * @param target Target component
     */
    public void setTarget(Component target) {
        menu.setTarget(target);
    }

    /**
     * Get the target component.
     *
     * @return Current target.
     */
    public Component getTarget() {
        return menu.getTarget();
    }

    /**
     * Close the popup.
     */
    public void close() {
        menu.close();
    }

    /**
     * Open the popup.
     */
    public void open() {
        menu.getElement().callJsFunction("open", new Serializable[0], null);
    }

    /**
     * Check if the popup is currently opened or not.
     *
     * @return True or false.
     */
    public boolean isOpened() {
        return menu.isOpened();
    }
}