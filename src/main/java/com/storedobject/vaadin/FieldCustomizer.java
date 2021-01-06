package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

/**
 * Field customizer may be set on {@link ObjectForm}.
 *
 * @author Syam
 */
@FunctionalInterface
public interface FieldCustomizer {
    /**
     * Customize the field.
     *
     * @param fieldName Name of the field
     * @param field Field
     */
    void customizeField(String fieldName, HasValue<?, ?> field);
}
