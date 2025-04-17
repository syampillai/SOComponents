package com.storedobject.vaadin.util;

import com.vaadin.flow.component.HasValue;

/**
 * Interface for handling field values dynamically. This interface defines
 * various operations for retrieving, updating, and checking properties of fields
 * such as visibility, editability, and basic handling capabilities.
 *
 * @author Syam
 */
public interface FieldValueHandler {
    /**
     * Determines whether the field value handler is considered basic.
     *
     * @return true if the handler is basic, false otherwise
     */
    boolean isBasic();
    /**
     * Determines whether the handler can process the provided field name.
     *
     * @param fieldName the name of the field to check
     * @return true if the handler can handle the field, false otherwise
     */
    boolean canHandle(String fieldName);
    /**
     * Determines whether the specified field can be set to a value.
     *
     * @param fieldName the name of the field to check
     * @return true if the field can be set, false otherwise
     */
    boolean canSet(String fieldName);
    /**
     * Retrieves the value associated with the specified field.
     *
     * @param fieldName the name of the field whose value is to be retrieved
     * @return the value of the specified field, or null if the field does not exist or is inaccessible
     */
    Object getValue(String fieldName);
    /**
     * Sets the value of the specified field.
     *
     * @param fieldName the name of the field to set the value for
     * @param value the value to be set for the specified field
     */
    void setValue(String fieldName, Object value);
    /**
     * Checks whether the specified field is visible.
     *
     * @param fieldName the name of the field to check visibility for
     * @return true if the field is visible, false otherwise
     */
    boolean isVisible(String fieldName);
    /**
     * Determines if the specified field is currently visible.
     *
     * @param field the field to check for visibility, provided as an instance implementing HasValue
     * @return true if the field is visible, false otherwise
     */
    boolean isVisible(HasValue<?, ?> field);
    /**
     * Checks whether the specified field is editable.
     *
     * @param fieldName the name of the field to check
     * @return true if the field is editable, false otherwise
     */
    boolean isEditable(String fieldName);
    /**
     * Checks if the given field is editable.
     *
     * @param field the field to be checked
     * @return true if the field is editable, false otherwise
     */
    boolean isEditable(HasValue<?, ?> field);
}
