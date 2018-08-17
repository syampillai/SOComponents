package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

public class Scrollable extends Composite {

    private Component component;

    public Scrollable(Component component) {
        this(component, true);
    }

    public Scrollable(Component component, boolean scrollable) {
        this.component = component instanceof Composite ? ((Composite) component).getContent() : component;
        if(scrollable) {
            component.getElement().getStyle().set("overflow", "auto");
        } else if(isScrollable()) {
            component.getElement().getStyle().remove("overflow");
        }
    }

    @Override
    protected Component initContent() {
        return component;
    }

    public boolean isScrollable() {
        return isScrollable(component);
    }

    public static boolean isScrollable(Component component) {
        if(component instanceof Composite) {
            return isScrollable(((Composite) component).getContent());
        }
        return "auto".equals(component.getElement().getStyle().get("overflow"));
    }
}