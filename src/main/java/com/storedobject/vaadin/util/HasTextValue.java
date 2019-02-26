package com.storedobject.vaadin.util;

public interface HasTextValue {
    void setValue(String value);
    String getValue();
    void setMaxLength(int width);
    void setPattern(String pattern);
}
