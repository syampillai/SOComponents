package com.storedobject.vaadin;

import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasSize;

public interface Field<T> extends HasElement, HasEnabled, HasSize {

    T getValue();

    void setValue(T value);

    void setLabel(String label);

    String getLabel();

    void setReadOnly(boolean readOnly);

    boolean isReadOnly();

    void focus();

    void blur();

    default void setAutofocus(boolean autofocus) {
        this.getElement().setProperty("autofocus", autofocus);
    }

    default boolean isAutofocus() {
        return this.getElement().getProperty("autofocus", false);
    }
}