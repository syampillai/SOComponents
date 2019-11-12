package com.storedobject.vaadin;

/**
 * An interface used by {@link AbstractForm} and {@link AbstractDataForm} classes to determine if a field can be added to the form or not.
 *
 * @see AbstractDataForm#setIncludeFieldChecker(IncludeField)
 * @see AbstractForm#setIncludeFieldChecker(IncludeField)
 * @author Syam
 */
@FunctionalInterface
public interface IncludeField {
    /**
     * Check if the given field name should be included in the form or not.
     *
     * @param fieldName Field name
     * @return True if the field to be included.
     */
    boolean includeField(String fieldName);
}
