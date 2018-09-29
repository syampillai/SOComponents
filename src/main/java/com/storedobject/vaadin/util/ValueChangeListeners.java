package com.storedobject.vaadin.util;

import com.vaadin.flow.component.HasValue;

import java.util.EventListener;
import java.util.EventObject;

public class ValueChangeListeners extends Listeners {

    @Override
    @SuppressWarnings("unchecked")
    protected void fire(EventListener listener, EventObject event) {
        ((HasValue.ValueChangeListener)listener).valueChanged((HasValue.ValueChangeEvent<?>) event);
    }
}
