package com.storedobject.vaadin.util;

import com.storedobject.vaadin.SpellCheck;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.textfield.HasAutocomplete;

public interface HasTextValue extends HasSize, HasText, SpellCheck, HasAutocomplete {

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
        inputTypeSet(2);
    }

    /**
     * Convert input into lowercase characters.
     */
    default void lowercase() {
        getClassNames().add("lowercase");
        getClassNames().remove("capitalize");
        getClassNames().remove("uppercase");
        inputTypeSet(1);
    }

    /**
     * Capitalize (camelcase) words while inputting.
     */
    default void capitalize() {
        getClassNames().remove("lowercase");
        getClassNames().add("capitalize");
        getClassNames().remove("uppercase");
        inputTypeSet(3);
    }

    /**
     * Remove text transformation if any.
     */
    default void normal() {
        getClassNames().remove("lowercase");
        getClassNames().remove("capitalize");
        getClassNames().remove("uppercase");
        inputTypeSet(0);
    }

    /**
     * This method is invoked whenever the type of input is set.
     *
     * @param type Type of input - 0: Normal, 1: Lowercase, 2: Uppercase, 3: Camelcase
     */
    void inputTypeSet(int type);

    /**
     * Convert a string to camelcase.
     * @param text Text to convert.
     * @return Converted text.
     */
    static String camelcase(String text) {
        StringBuilder builder = new StringBuilder();
        char p = ' ', c;
        for(int i = 0; i < text.length(); i++) {
            c = text.charAt(i);
            builder.append(Character.isLetterOrDigit(p) ? Character.toLowerCase(c) : Character.toUpperCase(c));
            p = c;
        }
        return builder.toString();
    }
}
