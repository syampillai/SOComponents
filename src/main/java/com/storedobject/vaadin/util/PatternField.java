package com.storedobject.vaadin.util;

import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

public abstract class PatternField extends TextField {

    public PatternField(String label, int width, CustomStringBlockFormatter pattern) {
        super(label);
        super.setMinlength(width);
        super.setMaxlength(width);
        pattern.extend(this);
        setValue(getEmptyValue());
        addBlurListener(e -> {
            String v = getValue();
            if(v == null || v.isEmpty()) {
                setValue(getEmptyValue());
            } else {
                if(isInvalid()) {
                    focus();
                }
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