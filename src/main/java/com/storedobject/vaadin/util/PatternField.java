package com.storedobject.vaadin.util;

import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

public abstract class PatternField extends TextField {

    public PatternField(String label, int width, CustomStringBlockFormatter pattern) {
        super(label);
        this.getElement().setProperty("minlength", width);
        this.getElement().setProperty("maxlength", width);
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
}