package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Application;
import com.storedobject.vaadin.CustomTextField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.textfieldformatter.NumeralFieldFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * An abstract class representing a numeric text field with support for various configurations,
 * such as grouping, allowance of negative values, formatting, and specified width constraints.
 * This class serves as a base class for creating type-specific numeric fields.
 *
 * @param <T> The numeric type handled by the field, extends {@link Number}.
 *
 * @author Syam
 */
public abstract class NumericField<T extends Number> extends CustomTextField<T> {

    /**
     * Determines whether the numeric field should apply grouping of digits
     * (e.g., thousands separators) when displaying numbers.
     */
    protected boolean grouping;
    /**
     * Indicates whether negative values are allowed for the numeric input field.
     * This variable determines if the field should accept negative numeric input.
     */
    protected boolean allowNegative;
    /**
     * Represents the width of the numeric field in terms of the number of characters allowed.
     * This variable controls the maximum visible or editable width of the field.
     */
    protected int width;
    private NumeralFieldFormatter formatter;
    private boolean formatted = false;

    /**
     * Constructs a NumericField with a specified default value.
     *
     * @param defaultValue the default value of the numeric field
     */
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

    /**
     * Checks whether grouping is enabled for the numeric field.
     *
     * @return true if grouping is enabled, false otherwise
     */
    public final boolean isGrouping() {
        return grouping;
    }

    /**
     * Sets whether grouping is enabled for the numeric field. Grouping typically refers to the separation of digits
     * into groups, such as by thousands, for easier readability (e.g., 1,000,000 for one million).
     *
     * @param grouping a boolean value where {@code true} enables grouping, and {@code false} disables grouping
     */
    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
        setPattern();
    }

    /**
     * Retrieves the current configuration indicating whether negative numbers
     * are allowed in this numeric field.
     *
     * @return true if negative numbers are allowed; false otherwise
     */
    public final boolean getAllowNegative() {
        return allowNegative;
    }

    /**
     * Sets whether the numeric field allows negative values.
     *
     * @param allowNegative a boolean specifying if negative values are allowed.
     *                       If true, negative values are permitted; otherwise, they are not.
     */
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

    /**
     * Retrieves the length of the numeric field.
     *
     * @return the length of the numeric field as an integer.
     */
    public final int getLength() {
        return width;
    }

    /**
     * Retrieves the number of decimal places allowed for the numeric field.
     *
     * @return the number of decimals as an integer
     */
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

    /**
     * Retrieves the default length for the numeric field. The default length
     * typically represents a predefined or initial configuration for the field's width.
     *
     * @return the default length of the numeric field as an integer
     */
    protected abstract int getDefaultLength();

    /**
     * Sets the maximum length of the numeric field. The length is adjusted based on certain criteria such as
     * whether negative values are allowed and the number of decimal places.
     *
     * If the specified length is less than 1, a default value of 18 is used. The minimum length is dynamically
     * calculated based on whether negative values are allowed and the number of decimal places. If the specified
     * length is less than the calculated minimum length, the minimum length is used instead.
     *
     * @param width the desired maximum length for the numeric field. If the value is less than the calculated
     *              minimum or a default value, it will be adjusted accordingly.
     */
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

    /**
     * Configures the formatting pattern for the numeric field based on several parameters
     * such as grouping, decimal places, and whether negative values are allowed.
     *
     * This method ensures that the internal {@code formatter} instance is properly set up
     * or removed if it already exists. It calculates the maximum width of the numeric input
     * based on defined constraints such as whether negative numbers are allowed and the number
     * of decimal places. The {@code formatter} uses parameters such as grouping characters,
     * decimal separators, and the desired formatting style to ensure that the numeric field's
     * input and display follow the specified rules.
     *
     * The method also applies the configured formatter to the underlying text field and updates the
     * displayed value to match the current formatting rules.
     *
     * Preconditions:
     * - The numeric field's configuration parameters (e.g., grouping, decimal precision) must be set.
     * - The {@code getField()} method provides the internal text field for the numeric field.
     *
     * Postconditions:
     * - The numeric field's {@code formatter} is updated and applied to the text field.
     * - The displayed presentation value reflects the new formatting pattern.
     */
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

    /**
     * Retrieves the country code associated with the current application context.
     * If no application context is available, the default country code "US" is returned.
     *
     * @return the country code as a string, defaulting to "US" if the application context is unavailable.
     */
    protected String getCountry() {
        Application a = Application.get();
        return a == null ? "US" : a.getCountry();
    }

    private NumeralFieldFormatter.ThousandsGroupStyle getThousandGroupStyle() {
        return switch(getCountry()) {
            case "IN" -> NumeralFieldFormatter.ThousandsGroupStyle.LAKH;
            case "CN" -> NumeralFieldFormatter.ThousandsGroupStyle.WAN;
            default -> NumeralFieldFormatter.ThousandsGroupStyle.THOUSAND;
        };
    }
}