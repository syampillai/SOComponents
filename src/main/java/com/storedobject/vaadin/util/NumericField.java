package com.storedobject.vaadin.util;

import com.storedobject.vaadin.CustomTextField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.textfieldformatter.NumeralFieldFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public abstract class NumericField<T extends Number> extends CustomTextField<T> {

    protected boolean grouping;
    protected boolean allowNegative;
    protected int width;
    private NumeralFieldFormatter formatter;
    private boolean formatted = false;

    protected NumericField(T defaultValue) {
        super(defaultValue);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(!formatted && formatter != null) {
            formatter.extend((TextField)getField());
            formatted = true;
        }
    }

    public final boolean isGrouping() {
        return grouping;
    }

    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
        setPattern();
    }

    public final boolean getAllowNegative() {
        return allowNegative;
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
        setPattern();
    }

    @Override
    public void setValue(T value) {
        if(value == null) {
            value = getEmptyValue();
        }
        super.setValue(value);
    }

    public final int getLength() {
        return width;
    }

    public int getDecimals() {
        return 0;
    }

    protected String format(T value) {
        if(value == null) {
            value = getEmptyValue();
        }
        DecimalFormat format = (DecimalFormat) DecimalFormat.getNumberInstance();
        format.setMaximumIntegerDigits(30);
        format.setRoundingMode(RoundingMode.HALF_UP);
        int decimals = getDecimals();
        if (decimals < 0) {
            format.setMinimumFractionDigits(decimals < -1 ? -decimals : 14);
            format.setMaximumFractionDigits(decimals < -1 ? -decimals : 14);
        } else {
            format.setMinimumFractionDigits(decimals);
            format.setMaximumFractionDigits(decimals);
        }
        String s = format.format(value);
        if(grouping) {
            return s;
        }
        return s.replace(",", "");
    }

    protected abstract int getDefaultLength();

    public final void setLength(int width) {
        if(width < 1) {
            width = 18;
        }
        int min = 1;
        if(allowNegative) {
            ++min;
        }
        int decimals = getDecimals();
        if(decimals > 0) {
            min += decimals + 1;
        }
        if(width < min) {
            width = min;
        }
        this.width = width;
        getField().setMaxLength(width);
    }

    protected final void setPattern() {
        if(formatter != null) {
            formatter.remove();
        }
        int w = width - getDecimals();
        if(allowNegative) {
            --w;
        }
        formatter = new NumeralFieldFormatter(",", ".", w, getDecimals(), !allowNegative);
        if(getUI().isPresent()) {
            formatter.extend((TextField) getField());
            formatted = true;
        }
        setPresentationValue(getValue());
    }
}