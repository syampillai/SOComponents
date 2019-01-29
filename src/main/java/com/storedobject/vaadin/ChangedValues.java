package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

/**
 * A thin wrapper class that warps the details of value changed in a field ({@link HasValue}.
 * @see View#valueChanged(ChangedValues)
 * @see View#trackValueChange(HasValue)
 * @author Syam
 */
public class ChangedValues {

    private HasValue.ValueChangeEvent<?> event;

    /**
     * Constructor.
     * @param event Value change event
     */
    public ChangedValues(HasValue.ValueChangeEvent<?> event) {
        this.event = event;
    }

    /**
     * Is this is this the field that is chaged?
     * @param field Field to check
     * @return True or false.
     */
    public boolean isChanged(HasValue<?, ?> field) {
        return event.getHasValue() == field;
    }

    /**
     * Get the "value change" (appropriately type-casted).
     * @param field Field to check
     * @param <V> Field value type
     * @return Type-casted version of "value change". (Returns <code>null</code> if this is not the field that changed its value).
     */
    @SuppressWarnings("unchecked")
    public <V> ChangedValue<V> getChanged(HasValue<?, V> field) {
        return event.getHasValue() == field ? new ChangedValue<>((HasValue.ValueChangeEvent<V>) event) : null;
    }

    /**
     * Get the field whose value is changed.
     * @return Field.
     */
    public HasValue<?, ?> getChanged() {
        return event.getHasValue();
    }

    /**
     * Get the old value.
     * @return Old value.
     */
    public Object getOldValue() {
        return event.getOldValue();
    }

    /**
     * Get the current valie.
     * @return New value.
     */
    public Object getValue() {
        return event.getValue();
    }

    /**
     * Check if the value change happened due to user interaction.
     * @return True or false.
     */
    public boolean isFromClient() {
        return event.isFromClient();
    }
}