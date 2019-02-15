package com.storedobject.vaadin.util;

import com.storedobject.vaadin.ButtonLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.shared.Registration;

public class NullField extends ButtonLayout implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<NullField, String>, String> {

    @Override
    public void setValue(String s) {
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<NullField, String>> valueChangeListener) {
        return null;
    }
}
