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

    public DoubleField() {
        this(null);
    }

    public DoubleField(String label) {
        this(label, null);
    }

    public DoubleField(String label, Double initialValue) {
        this(label, initialValue, 6);
    }

    public DoubleField(int decimals) {
        this(null, null, decimals);
    }

    public DoubleField(Double initialValue, int decimals) {
        this(null, initialValue, decimals);
    }

    public DoubleField(String label, int decimals) {
        this(label, null, decimals);
    }

    public DoubleField(String label, Double initialValue, int decimals) {
        this(label, initialValue, 18, decimals);
    }

    public DoubleField(int width, int decimals) {
        this(null, null, width, decimals);
    }

    public DoubleField(Double initialValue, int width, int decimals) {
        this(null, initialValue, width, decimals);
    }

    public DoubleField(String label, int width, int decimals) {
        this(label, width, decimals, false, false);
    }

    public DoubleField(String label, Double initialValue, int width, int decimals) {
        this(label, initialValue, width, decimals, false, false);
    }

    public DoubleField(int width, int decimals, boolean grouping) {
        this(null, null, width, decimals, grouping);
    }

    public DoubleField(Double initialValue, int width, int decimals, boolean grouping) {
        this(null, initialValue, width, decimals, grouping);
    }

    public DoubleField(String label, int width, int decimals, boolean grouping) {
        this(label, null, width, decimals, grouping);
    }

    public DoubleField(String label, Double initialValue, int width, int decimals, boolean grouping) {
        this(label, initialValue, width, decimals, grouping, false);
    }

    public DoubleField(int width, int decimals, boolean grouping, boolean allowNegative) {
        this(null, null, width, decimals, grouping, allowNegative);
    }

    public DoubleField(Double initialValue, int width, int decimals, boolean grouping, boolean allowNegative) {
        this(null, initialValue, width, decimals, grouping, allowNegative);
    }

    public DoubleField(String label, int width, int decimals, boolean grouping, boolean allowNegative) {
        this(label, null, width, decimals, grouping, allowNegative);
    }

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

    public void setDecimals(int decimals) {
        if(decimals < 0 || decimals > (width - 2 - (allowNegative ? 1 : 0))) {
            decimals = 0;
        }
        this.decimals = decimals;
        setPattern();
    }
}
