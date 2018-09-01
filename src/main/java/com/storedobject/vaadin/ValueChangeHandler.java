package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

@FunctionalInterface
public interface ValueChangeHandler extends HasValue.ValueChangeListener {

    @Override
    default void valueChanged(HasValue.ValueChangeEvent valueChangeEvent) {
        valueChanged(new ChangedValues(valueChangeEvent));
    }

    void valueChanged(ChangedValues changedValues);
}