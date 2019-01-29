package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

/**
 * A thin wrapper class that warps the details of value changed in a field ({@link HasValue}. Please note that this is a type-casted
 * version of {@link ChangedValues}.
 *
 * @param <V> Value type of the field
 */
public class ChangedValue<V> {

    private HasValue.ValueChangeEvent<V> event;

    /**
     * Constructor.
     * @param event Value change event
     */
    public ChangedValue(HasValue.ValueChangeEvent<V> event) {
        this.event = event;
    }

    /**
     * Get the changed value of the field.
     * @return New value
     */
    public V getValue() {
        return event.getValue();
    }

    /**
     * Get the old value of the field.
     * @return Old value
     */
    public V getOldValue() {
        return event.getOldValue();
    }

    /**
     * Check if the value change happened due to user interaction.
     * @return True or false.
     */
    public boolean isFromClient() {
        return event.isFromClient();
    }

    /**
     * Get the field that is chnaged.
     * @return Field.
     */
    public HasValue<?, V> getChanged() {
        return event.getHasValue();
    }
}