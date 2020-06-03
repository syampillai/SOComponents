package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;

/**
 * A slightly enhanced version of {@link Dialog} that can not be closed by pressing Esc key or clicking outside.
 *
 * @author Syam
 */
public class Window extends Dialog {

    /**
     * Constructor.
     */
    public Window() {
        this((Component[])null);
    }

    /**
     * Constructor.
     *
     * @param components Components to add as content
     */
    public Window(Component... components) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        setResizable(true);
        if(components != null) {
            add(components);
        }
    }
}
