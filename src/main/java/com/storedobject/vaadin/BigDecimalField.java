package com.storedobject.vaadin;

import com.storedobject.vaadin.util.NumericField;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Field to edit {@link BigDecimal} values.
 * @author Syam
 */
public class BigDecimalField extends NumericField<BigDecimal> {

    private final int decimals;

    /**
     * Constructor.
     */
    public BigDecimalField() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public BigDecimalField(String label) {
        this(label, null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public BigDecimalField(String label, BigDecimal initialValue) {
        this(label, initialValue, 6);
    }

    /**
     * Constructor.
     *
     * @param decimals Number of decimal places.
     */
    public BigDecimalField(int decimals) {
        this(null, null, decimals);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param decimals Number of decimal places.
     */
    public BigDecimalField(BigDecimal initialValue, int decimals) {
        this(null, initialValue, decimals);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param decimals Number of decimal places.
     */
    public BigDecimalField(String label, int decimals) {
        this(label, null, decimals);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param decimals Number of decimal places.
     */
    public BigDecimalField(String label, BigDecimal initialValue, int decimals) {
        this(label, initialValue, 18, decimals);
    }

    /**
     * Constructor.
     *
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     */
    public BigDecimalField(int width, int decimals) {
        this(null, null, width, decimals);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     */
    public BigDecimalField(BigDecimal initialValue, int width, int decimals) {
        this(null, initialValue, width, decimals);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     */
    public BigDecimalField(String label, int width, int decimals) {
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
    public BigDecimalField(String label, BigDecimal initialValue, int width, int decimals) {
        this(label, initialValue, width, decimals, false, false);
    }

    /**
     * Constructor.
     *
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param decimals Number of decimal places.
     * @param grouping Whether grouping is required or not.
     */
    public BigDecimalField(int width, int decimals, boolean grouping) {
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
    public BigDecimalField(BigDecimal initialValue, int width, int decimals, boolean grouping) {
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
    public BigDecimalField(String label, int width, int decimals, boolean grouping) {
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
    public BigDecimalField(String label, BigDecimal initialValue, int width, int decimals, boolean grouping) {
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
    public BigDecimalField(int width, int decimals, boolean grouping, boolean allowNegative) {
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
    public BigDecimalField(BigDecimal initialValue, int width, int decimals, boolean grouping, boolean allowNegative) {
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
    public BigDecimalField(String label, int width, int decimals, boolean grouping, boolean allowNegative) {
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
    public BigDecimalField(String label, BigDecimal initialValue, int width, int decimals, boolean grouping, boolean allowNegative) {
        super(toBigDecimal(BigDecimal.ZERO, decimals(decimals)));
        this.grouping = grouping;
        this.allowNegative = allowNegative;
        this.decimals = decimals(decimals);
        setLength(width);
        setValue(initialValue);
        setPattern();
        setLabel(label);
    }

    @Override
    protected BigDecimal generateModelValue() {
        return toBigDecimal(getField().getValue(), decimals);
    }

    private static int decimals(int decimals) {
        return decimals < 1 ? 2 : Math.min(decimals, 40);
    }

    @Override
    public void setValue(BigDecimal value) {
        super.setValue(toBigDecimal(value, decimals));
    }

    @Override
    protected BigDecimal getModelValue(String string) {
        return toBigDecimal(string, decimals);
    }

    /**
     * Get the number of decimals allowed.
     *
     * @return Number of decimals.
     */
    public final int getDecimals() {
        return decimals;
    }

    @Override
    protected int getDefaultLength() {
        return 18;
    }

    private static BigDecimal toBigDecimal(Object value) {
        String v;
        if(value == null) {
            return null;
        }
        if(value instanceof BigDecimal) {
            return (BigDecimal)value;
        }
        v = value.toString().trim();
        if(v.length() == 0) {
            return null;
        }
        try {
            return new BigDecimal(v.trim().replace(",", ""));
        } catch(Throwable ignored) {
        }
        return null;
    }

    private static BigDecimal toBigDecimal(Object value, int decimals) {
        BigDecimal bd = toBigDecimal(value);
        if(bd == null || decimals < 0 || bd.scale() == decimals) {
            return bd;
        }
        try {
            return bd.setScale(decimals, RoundingMode.DOWN);
        } catch(Throwable ignored) {
        }
        return null;
    }
}