package com.storedobject.vaadin.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;

/**
 * Slightly enhanced version of Vaadin's {@link ClickNotifier}.
 *
 * @author Syam
 */
public interface ClickNotifier extends com.vaadin.flow.component.ClickNotifier<Component> {

    /**
     * Remove a listener.
     * @param listener Listener
     */
    void removeClickListener(ComponentEventListener<ClickEvent<Component>> listener);

    /**
     * Replace a listener.
     * @param oldListener Old listener
     * @param newListener New listener
     * @return Registration.
     */
    default Registration replaceClickListener(ComponentEventListener<ClickEvent<Component>> oldListener, ComponentEventListener<ClickEvent<Component>> newListener) {
        removeClickListener(oldListener);
        return addClickListener(newListener);
    }
}