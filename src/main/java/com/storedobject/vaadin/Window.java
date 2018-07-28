package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;

public class Window extends Dialog {

    public Window() {
        this((Component[])null);
    }

    public Window(Component... components) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        if(components != null) {
            add(components);
        }
    }
}
