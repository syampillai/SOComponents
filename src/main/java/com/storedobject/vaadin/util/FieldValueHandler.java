package com.storedobject.vaadin.util;

import com.vaadin.flow.component.HasValue;

public interface FieldValueHandler {
    boolean isBasic();
    boolean canHandle(String fieldName);
    boolean canSet(String fieldName);
    Object getValue(String fieldName);
    void setValue(String fieldName, Object value);
    boolean isVisible(String fieldName);
    boolean isVisible(HasValue<?, ?> field);
    boolean isEditable(String fieldName);
    boolean isEditable(HasValue<?, ?> field);
}
