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

    @SuppressWarnings("unchecked")
    public <V> ChangedValue<V> getChanged(HasValue<?, V> field) {
        return event.getHasValue() == field ? new ChangedValue<>((HasValue.ValueChangeEvent<V>) event) : null;
    }

    public HasValue<?, ?> getChanged() {
        return event.getHasValue();
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