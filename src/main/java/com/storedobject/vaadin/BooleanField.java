package com.storedobject.vaadin;

import com.vaadin.flow.component.checkbox.Checkbox;

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
        super(false);
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

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        checkbox.setReadOnly(readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        checkbox.setEnabled(enabled);
    }
}