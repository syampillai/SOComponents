package com.storedobject.vaadin.util;

import com.vaadin.flow.component.textfield.TextField;

public abstract class PatternField extends TextField {

    public PatternField(String label, int width, String pattern) {
        super(label);
        super.setMinlength(width);
        super.setMaxlength(width);
        super.setPattern(pattern);
        setValue(getEmptyValue());
        addBlurListener(e -> {
            String v = getValue();
            if(v.isEmpty() || isInvalid()) {
                setValue(getEmptyValue());
            }
        });

    }

    @Override
    public void setPattern(String pattern) {
    }

    @Override
    protected void setMinlength(double minlength) {
    }

    @Override
    protected void setMaxlength(double maxlength) {
    }
}