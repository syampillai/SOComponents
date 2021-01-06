package com.storedobject.vaadin;

/**
 * Custom interface to denote that value in a field is required or not. If any of the setRequired(...) methods of
 * {@link AbstractDataForm} or {@link Form} is set, this method will be invoked on the field that implements this
 * interface and the filed can set its status internally.
 *
 * @author Syam
 */
public interface RequiredField {
    /**
     * Indicate that value is required for this field.
     *
     * @param required True/false.
     */
    void setRequired(boolean required);
}
