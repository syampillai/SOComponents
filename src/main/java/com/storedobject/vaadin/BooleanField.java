package com.storedobject.vaadin;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.customfield.CustomField;

/**
 * Field to edit {@link Boolean} values.
 *
 * @author Syam
 */
public class BooleanField extends CustomField<Boolean> {

    private Checkbox checkbox = new Checkbox();

    public BooleanField() {
        this(null, null);
    }

    public BooleanField(String label) {
        this(label, null);
    }

    public BooleanField(Boolean initialValue) {
        this(null, initialValue);
    }

    public BooleanField(String label, Boolean initialValue) {
        setLabel(label);
        add(checkbox);
        setValue(initialValue);
    }

    @Override
    public void setValue(Boolean value) {
        super.setValue(value == null ? false : value);
    }

    @Override
    protected Boolean generateModelValue() {
        return checkbox.getValue();
    }

    @Override
    protected void setPresentationValue(Boolean value) {
        checkbox.setValue(value);
    }
}