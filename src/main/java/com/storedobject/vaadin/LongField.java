package com.storedobject.vaadin;

import com.storedobject.vaadin.util.NumericField;

/**
 * Field to edit {@link Long}.
 *
 * @author Syam
 */
public class LongField extends NumericField<Long> {

    private static final Long ZERO = 0L;

    /**
     * Constructor.
     */
    public LongField() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public LongField(String label) {
        this(label, null);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     */
    public LongField(Long initialValue) {
        this(null, initialValue);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public LongField(String label, Long initialValue) {
        this(label, initialValue, 18);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     */
    public LongField(Long initialValue, int width) {
        this(null, initialValue, width);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     */
    public LongField(String label, Long initialValue, int width) {
        this(label, initialValue, width, false, false);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     * @param width Width (Number of digits plus decimal character and group separators).
     * @param grouping Whether grouping is required or not.
     */
    public LongField(Long initialValue, int width, boolean grouping) {
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
    public LongField(String label, Long initialValue, int width, boolean grouping) {
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
    public LongField(Long initialValue, int width, boolean grouping, boolean allowNegative) {
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
    public LongField(String label, Long initialValue, int width, boolean grouping, boolean allowNegative) {
        super(ZERO);
        this.grouping = grouping;
        this.allowNegative = allowNegative;
        setLength(width);
        setValue(initialValue);
        setLabel(label);
    }

    @Override
    protected Long getModelValue(String string) {
        try {
            return Long.parseLong(string.replace(",", ""));
        } catch(NumberFormatException e) {
            setPresentationValue(ZERO);
            return ZERO;
        }
    }

    @Override
    protected int getDefaultLength() {
        return 10;
    }
}