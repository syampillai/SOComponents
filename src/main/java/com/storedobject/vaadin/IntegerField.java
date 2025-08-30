package com.storedobject.vaadin;

import com.storedobject.vaadin.util.NumericField;

/**
 * Field to edit {@link Integer}.
 *
 * @author Syam
 */
public class IntegerField extends NumericField<Integer> {

    private static final Integer ZERO = 0;

    /**
     * Constructor.
     */
    public IntegerField() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public IntegerField(String label) {
        this(label, null);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     */
    public IntegerField(Integer initialValue) {
        this(null, initialValue);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public IntegerField(String label, Integer initialValue) {
        this(label, initialValue, 18);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     */
    public IntegerField(Integer initialValue, int width) {
        this(null, initialValue, width);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     */
    public IntegerField(String label, Integer initialValue, int width) {
        this(label, initialValue, width, false, false);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param grouping Whether grouping is required or not.
     */
    public IntegerField(Integer initialValue, int width, boolean grouping) {
        this(null, initialValue, width, grouping);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param grouping Whether grouping is required or not.
     */
    public IntegerField(String label, Integer initialValue, int width, boolean grouping) {
        this(label, initialValue, width, grouping, false);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param grouping Whether grouping is required or not.
     * @param allowNegative Whether negative values to be allowed or not.
     */
    public IntegerField(Integer initialValue, int width, boolean grouping, boolean allowNegative) {
        this(null, initialValue, width, grouping, allowNegative);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param grouping Whether grouping is required or not.
     * @param allowNegative Whether negative values to be allowed or not.
     */
    public IntegerField(String label, Integer initialValue, int width, boolean grouping, boolean allowNegative) {
        super(ZERO);
        this.grouping = grouping;
        this.allowNegative = allowNegative;
        createField();
        setLength(width);
        setValue(initialValue);
        setLabel(label);
    }

    @Override
    protected Integer getModelValue(String string) {
        try {
            return Integer.parseInt(string.replace(",", ""));
        } catch(NumberFormatException e) {
            setPresentationValue(ZERO);
            return ZERO;
        }
    }

    @Override
    protected int getDefaultLength() {
        return 8;
    }
}
