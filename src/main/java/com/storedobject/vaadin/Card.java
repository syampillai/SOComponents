package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

public class Card extends Composite {

    private Component component;

    public Card(Component component) {
        this(component, 4);
    }

    public Card(Component component, int padding) {
        this(component, padding + "px");
    }

    public Card(Component component, String paddings) {
        this.component = component;
        component.getElement().getStyle().set("box-shadow", "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)");
        component.getElement().getStyle().set("padding", paddings);
    }

    @Override
    protected Component initContent() {
        return component;
    }
}
