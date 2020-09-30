package com.storedobject.vaadin;

import com.storedobject.vaadin.util.NumericField;

/**
 * A field to edit {@link Double}.
 *
 * @author Syam
 */
public class DoubleField extends NumericField<Double> {

    private static final Double ZERO = 0d;
    private int decimals;

    /**
     * Constructor.
     */
    public DoubleField() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public DoubleField(String label) {
        this(label, null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public DoubleField(String label, Double initialValue) {
        this(label, initialValue, 6);
    }

    /**
     * Constructor.
     *
     * @param decimals Number of decimal places.
     */
    public DoubleField(int decimals) {
        this(null, null, decimals);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param decimals Number of decimal places.
     */
    public DoubleField(Double initialValue, int decimals) {
        this(null, initialValue, decimals);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param decimals Number of decimal places.
     */
    public DoubleField(String label, int decimals) {
        this(label, null, decimals);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param decimals Number of decimal places.
     */
    public DoubleField(String label, Double initialValue, int decimals) {
        this(label, initialValue, 18, decimals);
    }

    /**
     * Constructor.
     *
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     */
    public DoubleField(int width, int decimals) {
        this(null, null, width, decimals);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     */
    public DoubleField(Double initialValue, int width, int decimals) {
        this(null, initialValue, width, decimals);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     */
    public DoubleField(String label, int width, int decimals) {
        this(label, width, decimals, false, false);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     */
    public DoubleField(String label, Double initialValue, int width, int decimals) {
        this(label, initialValue, width, decimals, false, false);
    }

    /**
     * Constructor.
     *
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     */
    public DoubleField(int width, int decimals, boolean grouping) {
        this(null, null, width, decimals, grouping);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     */
    public DoubleField(Double initialValue, int width, int decimals, boolean grouping) {
        this(null, initialValue, width, decimals, grouping);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     */
    public DoubleField(String label, int width, int decimals, boolean grouping) {
        this(label, null, width, decimals, grouping);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     */
    public DoubleField(String label, Double initialValue, int width, int decimals, boolean grouping) {
        this(label, initialValue, width, decimals, grouping, false);
    }

    /**
     * Constructor.
     *
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     * @param allowNegative Whether negative values to be allowed or not.
     */
    public DoubleField(int width, int decimals, boolean grouping, boolean allowNegative) {
        this(null, null, width, decimals, grouping, allowNegative);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     * @param allowNegative Whether negative values to be allowed or not.
     */
    public DoubleField(Double initialValue, int width, int decimals, boolean grouping, boolean allowNegative) {
        this(null, initialValue, width, decimals, grouping, allowNegative);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     * @param allowNegative Whether negative values to be allowed or not.
     */
    public DoubleField(String label, int width, int decimals, boolean grouping, boolean allowNegative) {
        this(label, null, width, decimals, grouping, allowNegative);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     * @param allowNegative Whether negative values to be allowed or not.
     */
    public DoubleField(String label, Double initialValue, int width, int decimals, boolean grouping, boolean allowNegative) {
        super(ZERO);
        this.grouping = grouping;
        this.allowNegative = allowNegative;
        this.decimals = decimals < 1 ? 0 : Math.min(decimals, 9);
        setLength(width);
        setValue(initialValue);
        setDecimals(this.decimals);
        setLabel(label);
    }

    @Override
    protected Double getModelValue(String string) {
        try {
            return Double.parseDouble(string.replace(",", ""));
        } catch(NumberFormatException e) {
            setPresentationValue(ZERO);
            return ZERO;
        }
    }

    @Override
    public final int getDecimals() {
        return decimals;
    }

    @Override
    protected int getDefaultLength() {
        return 18;
    }

    /**
     * Set the number of decimals.
     *
     * @param decimals Number of decimals.
     */
    public void setDecimals(int decimals) {
        if(decimals < 0 || decimals > (width - 2 - (allowNegative ? 1 : 0))) {
            decimals = 0;
        }
        this.decimals = decimals;
        setPattern();
    }
}
