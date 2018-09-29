package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Field;
import com.vaadin.flow.component.textfield.HasPrefixAndSuffix;

public interface BasicTextField extends Field<String>, HasPrefixAndSuffix {
    void setMaxLength(int width);
    void setPattern(String pattern);
}
