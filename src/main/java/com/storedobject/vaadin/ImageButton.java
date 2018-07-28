package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ImageButton extends Button {

    public ImageButton(String text, ClickHandler clickHandler) {
        super(new Icon(text), clickHandler);
    }

    public ImageButton(Component icon, ClickHandler clickHandler) {
        super(icon, clickHandler);
    }

    public ImageButton(VaadinIcon icon, ClickHandler clickHandler) {
        super(icon, clickHandler);
    }
}
