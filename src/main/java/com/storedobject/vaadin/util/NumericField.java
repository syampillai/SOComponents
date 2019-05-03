package com.storedobject.vaadin.util;

import com.storedobject.vaadin.CustomTextField;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public abstract class NumericField<T extends Number> extends CustomTextField<T> {

    protected boolean grouping;
    protected boolean allowNegative;
    protected int width;

    protected NumericField(T defaultValue) {
        super(defaultValue);
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

    public void setLength(int width) {
        this.width = width;
        getField().setMaxLength(width);
    }

    public int getDecimals() {
        return 0;
    }

    protected abstract void setPattern();

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
}