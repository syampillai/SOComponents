package com.storedobject.vaadin.util;

import com.storedobject.vaadin.SpellCheck;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasText;

public interface HasTextValue extends HasSize, HasText, SpellCheck {

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

    /**
     * Convert input into uppercase characters.
     */
    default  void uppercase() {
        getClassNames().remove("lowercase");
        getClassNames().remove("capitalize");
        getClassNames().add("uppercase");
    }

    /**
     * Convert input into lowercase characters.
     */
    default void lowercase() {
        getClassNames().add("lowercase");
        getClassNames().remove("capitalize");
        getClassNames().remove("uppercase");
    }

    /**
     * Capitalize words while inputting.
     */
    default void capitalize() {
        getClassNames().remove("lowercase");
        getClassNames().add("capitalize");
        getClassNames().remove("uppercase");
    }
}
