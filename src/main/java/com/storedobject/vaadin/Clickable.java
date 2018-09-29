package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ClickNotifier;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.*;
import com.vaadin.flow.shared.Registration;

public class Clickable<T extends Component> extends Composite<T> implements ClickNotifier {

    private T component;
    private ElementClick click;

    public Clickable(@SuppressWarnings("unused") T component) {
    }

    public Clickable(T component, ComponentEventListener<ClickEvent<Component>> listener) {
        this.component = component;
        click = new ElementClick(component);
        if(listener != null) {
            addClickListener(listener);
        }
    }

    @Override
    protected T initContent() {
        return component;
    }

    @Override
    public Registration addClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        return click.addClickListener(listener);
    }

    @Override
    public void removeClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        click.removeClickListener(listener);
    }
}
