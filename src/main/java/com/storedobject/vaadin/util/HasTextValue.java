package com.storedobject.vaadin.util;

import com.vaadin.flow.component.HasSize;

public interface HasTextValue extends HasSize {
    void setValue(String value);
    String getValue();
    void setMinLength(int width);
    void setMaxLength(int width);
    void setPattern(String pattern);
}
