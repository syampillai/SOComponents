package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Application;
import com.storedobject.vaadin.CustomTextField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
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
        ((TextField)getField()).setAutoselect(true);
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
    protected void customizeTextField(HasTextValue textField) {
        super.customizeTextField(textField);
        ((TextField)getField()).addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
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

    @Override
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
            if(getCountry().equals("IN")) {
                return indianStyle(s);
            }
            return s;
        }
        return s.replace(",", "");
    }

    private static String indianStyle(String v) {
        v = v.replace(",", "");
        int i = v.indexOf('.');
        StringBuilder s;
        if(i >= 0) {
            s = new StringBuilder(v.substring(i));
            v = v.substring(0, i);
        } else {
            s = new StringBuilder();
        }
        if(v.length() <= 3) {
            return v + s;
        }
        s.insert(0, v.substring(v.length() - 3));
        v = v.substring(0, v.length() - 3);
        while(v.length() > 0) {
            s.insert(0, ",");
            if(v.length() <= 2) {
                s.insert(0, v);
                break;
            }
            s.insert(0, v.substring(v.length() - 2));
            v = v.substring(0, v.length() - 2);
        }
        return s.toString();
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
        formatter = new NumeralFieldFormatter(grouping ? "," : "", ".", w, getDecimals(), !allowNegative,
                getThousandGroupStyle(), false, true, null);
        if(getUI().isPresent()) {
            formatter.extend((TextField) getField());
            formatted = true;
        }
        setPresentationValue(getValue());
    }

    protected String getCountry() {
        Application a = Application.get();
        return a == null ? "US" : a.getCountry();
    }

    private NumeralFieldFormatter.ThousandsGroupStyle getThousandGroupStyle() {
        switch(getCountry()) {
            case "IN":
                return NumeralFieldFormatter.ThousandsGroupStyle.LAKH;
            case "CN":
                return NumeralFieldFormatter.ThousandsGroupStyle.WAN;
        }
        return NumeralFieldFormatter.ThousandsGroupStyle.THOUSAND;
    }
}