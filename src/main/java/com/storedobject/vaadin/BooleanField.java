package com.storedobject.vaadin;

import com.storedobject.vaadin.util.CompositeField;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;

public class BooleanField
        extends CompositeField.SingleField<Boolean, BooleanField, Checkbox, AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>> {

    public BooleanField() {
        super(new Checkbox(), false);
        createField();
    }

    public BooleanField(String label) {
        this(label, null);
    }

    public BooleanField(Boolean initialValue) {
        this(null, initialValue);
    }

    public BooleanField(String label, Boolean initialValue) {
        this();
        setLabel(label);
        if(initialValue == null) {
            setPresentationValue(getEmptyValue());
        } else {
            setValue(initialValue);
        }
    }
}
