package com.storedobject.vaadin;

import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasSize;

/**
 * Field interface to define certain basic properties of editable component.
 *
 * @param <T> Value type
 * @author Syam
 */
public interface Field<T> extends HasElement, HasEnabled, HasSize {

    /**
     * Get the current value.
     * @return Current value.
     */
    T getValue();

    /**
     * Set the current value.
     * @param value Current value
     */
    void setValue(T value);

    /**
     * Set the label for the field.
     * @param label Label
     */
    void setLabel(String label);

    /**
     * Get the label of the field.
     * @return Label.
     */
    String getLabel();

    /**
     * Set the field "read only".
     * @param readOnly Whether "read only" or not
     */
    void setReadOnly(boolean readOnly);

    /**
     * Check whether the field is "read only" or not.
     * @return True or false.
     */
    boolean isReadOnly();

    /**
     * Focus the field.
     */
    void focus();

    /**
     * Take away focus from the field.
     */
    void blur();

    /**
     * Set the field to auto-focus mode.
     * @param autofocus Whether to set to auto-focus mode or not
     */
    default void setAutofocus(boolean autofocus) {
        this.getElement().setProperty("autofocus", autofocus);
    }

    /**
     * Checks if the field is set to auto-focus or not.
     * @return True or false
     */
    default boolean isAutofocus() {
        return this.getElement().getProperty("autofocus", false);
    }
}