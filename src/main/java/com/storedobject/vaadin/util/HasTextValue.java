package com.storedobject.vaadin.util;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasText;

public interface HasTextValue extends HasSize, HasText {

    void setValue(String value);

    String getValue();

    void setMinLength(int width);

    void setMaxLength(int width);

    void setPattern(String pattern);

    @Override
    default void setText(String text) {
        setValue(text);
    }

    @Override
    default String getText() {
        return getValue();
    }
}
