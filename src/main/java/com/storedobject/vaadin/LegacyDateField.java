package com.storedobject.vaadin;

import java.util.Date;

/**
 * A field to edit legacy {@link Date}.
 *
 * @author Syam
 */
public class LegacyDateField extends TranslatedField<Date, java.sql.Date> {

    private static Date nullValue = new Date(-5364662400000L);

    /**
     * Constructor.
     */
    public LegacyDateField() {
        this((String) null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public LegacyDateField(String label) {
        this(label, create(DateField.today()));
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     */
    public LegacyDateField(Date initialValue) {
        this(null, initialValue);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public LegacyDateField(String label, Date initialValue) {
        super(new com.storedobject.vaadin.DateField(), (f, d) -> create(d), (f, d) -> create(d), null);
        setLabel(label);
        setValue(initialValue);
    }

    /**
     * Create legacy date from date.
     *
     * @param date Date.
     * @return Legacy date.
     */
    public static Date create(java.sql.Date date) {
        return date == null ? null : new Date(date.getTime());
    }

    /**
     * Create date from legacy date.
     *
     * @param date Legacy date.
     * @return Date.
     */
    public static java.sql.Date create(Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

    @Override
    public void setValue(Date value) {
        super.setValue(value == null || value.equals(nullValue) ? null : value);
    }

    @Override
    public Date getValue() {
        Date d = super.getValue();
        return d == null ? nullValue : d;
    }

    /**
     * Auto-set the null value.
     */
    static void setNullValue() {
        java.sql.Date date = DateField.nullValue;
        if(date == null) {
            nullValue = null;
        } else {
            nullValue = new Date(date.getTime());
        }
    }

    @Override
    protected boolean valueEquals(Date value1, Date value2) {
        if(nullValue != null) {
            if(value1 != null && value1.getTime() == nullValue.getTime()) {
                value1 = null;
            }
            if(value2 != null && value2.getTime() == nullValue.getTime()) {
                value2 = null;
            }
        }
        return super.valueEquals(value1, value2);
    }


    /**
     * Epoch value to set. Epoch value determines how a 2 digit year value is interpreted. Epoch value is added to
     * the 2 digit year value. The default value of epoch is the first year of the century. For example, for the 21st
     * century, the default epoch value is 2000.
     *
     * @param epoch Epoch value to set.
     */
    public void setEpoch(int epoch) {
        ((DateField)getField()).setEpoch(epoch);
    }

    /**
     * Get the current epoch value. (Please see {@link #setEpoch(int)}).
     *
     * @return Current the current epoch value.
     */
    public int getEpoch() {
        return ((DateField)getField()).getEpoch();
    }
}
