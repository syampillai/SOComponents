package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

public class ChangedValue<V> {

    private HasValue.ValueChangeEvent<V> event;

    public ChangedValue(HasValue.ValueChangeEvent<V> event) {
        this.event = event;
    }

    public V getValue() {
        return event.getValue();
    }

    public V getOldValue() {
        return event.getOldValue();
    }

    public boolean isFromClient() {
        return event.isFromClient();
    }
}