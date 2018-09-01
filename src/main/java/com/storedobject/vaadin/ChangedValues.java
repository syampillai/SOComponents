package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

public class ChangedValues {

    private HasValue.ValueChangeEvent<?> event;

    public ChangedValues(HasValue.ValueChangeEvent<?> event) {
        this.event = event;
    }

    public boolean isChanged(HasValue<?, ?> field) {
        return event.getHasValue() == field;
    }

    public <V extends Object> ChangedValue<V> getChanged(HasValue<?, V> field) {
        return event.getHasValue() == field ? new ChangedValue<V>((HasValue.ValueChangeEvent<V>) event) : null;
    }

    public Object getOldValue() {
        return event.getOldValue();
    }

    public Object getValue() {
        return event.getValue();
    }

    public boolean isFromClient() {
        return event.isFromClient();
    }
}