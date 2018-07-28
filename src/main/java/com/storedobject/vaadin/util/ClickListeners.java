package com.storedobject.vaadin.util;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;

import java.util.EventListener;
import java.util.EventObject;

public class ClickListeners extends Listeners {

    @Override
    protected void fire(EventListener listener, EventObject event) {
        ((ComponentEventListener) listener).onComponentEvent((ComponentEvent)event);
    }
}
