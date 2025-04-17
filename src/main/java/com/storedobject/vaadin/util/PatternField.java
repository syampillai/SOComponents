package com.storedobject.vaadin.util;

import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

/**
 * Represents an abstract text input field enforcing a specific pattern or format
 * for its content. The field is designed to validate input according to predefined
 * rules managed by a supplied {@link CustomStringBlockFormatter}.
 *
 * Subclasses of this class must define the behavior for input validation, the
 * representation of empty values, and any custom rule sets required for their
 * respective formats.
 *
 * This field automatically sets properties for minimum and maximum character
 * lengths based on the provided width during initialization. It also initializes
 * the input value to an empty representation and refocuses on itself if the input
 * is invalid on blur events.
 *
 * @author Syam
 */
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