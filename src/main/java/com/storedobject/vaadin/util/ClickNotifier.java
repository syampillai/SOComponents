package com.storedobject.vaadin.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;

public interface ClickNotifier extends com.vaadin.flow.component.ClickNotifier<Component> {

    void removeClickListener(ComponentEventListener<ClickEvent<Component>> listener);

    default Registration replaceClickListener(ComponentEventListener<ClickEvent<Component>> oldListener, ComponentEventListener<ClickEvent<Component>> newListener) {
        removeClickListener(oldListener);
        return addClickListener(newListener);
    }
}