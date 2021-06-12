package com.storedobject.vaadin;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.Calendar.*;

/**
 * A field to edit {@link Date}. Even though developers should start using {@link LocalDate}, almost all databases
 * are still using {@link Date}.
 *
 * @author Syam
 */
public class DateField extends TranslatedField<Date, LocalDate> {

    private static Date today = null;
    static Date nullValue = new Date(-5364662400000L);

    /**
     * Constructor.
     */
    public DateField() {
        this((String) null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public DateField(String label) {
        this(label, today());
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     */
    public DateField(Date initialValue) {
        this(null, initialValue);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public DateField(String label, Date initialValue) {
        super(new DatePicker(), (f, d) -> create(d), (f, d) -> create(d), null);
        setLabel(label);
        setValue(initialValue);
        getField().setClearButtonVisible(false);
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

    static Date today() {
        if(today == null) {
            today = create(LocalDate.now());
        }
        return today;
    }

    /**
     * Set the value of "today". (In a business application, it could be the "working date").
     *
     * @param today Today.
     */
    public static void setToday(Date today) {
        if(today != null) {
            boolean setNullToo = DateField.today == DateField.nullValue;
            DateField.today = today;
            if(setNullToo) {
                DateField.nullValue = today;
            }
        }
    }

    /**
     * Create an instance of a {@link Date} from a {@link LocalDate} instance.
     *
     * @param date Instance to convert.
     * @return Converted value.
     */
    public static Date create(LocalDate date) {
        if(date == null) {
            return null;
        }
        @SuppressWarnings("MagicConstant")
        GregorianCalendar c = new GregorianCalendar(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        return new Date(c.getTimeInMillis());
    }

    /**
     * Create an instance of a {@link LocalDate} from a {@link Date} instance.
     *
     * @param date Instance to convert.
     * @param <D> Type of instance to convert.
     * @return Converted value.
     */
    public static <D extends java.util.Date> LocalDate create(D date) {
        return date == null ? null : LocalDate.of(getYear(date), getMonth(date), getDay(date));
    }

    private static <D extends java.util.Date> int getYear(D date) {
        return get(date, YEAR);
    }

    private static <D extends java.util.Date> int getMonth(D date) {
        return get(date, MONTH) + 1;
    }

    private static <D extends java.util.Date> int getDay(D date) {
        return get(date, DATE);
    }

    private static <D extends java.util.Date> int get(D date, int field) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return c.get(field);
    }

    @Override
    public DatePicker getField() {
        return (DatePicker) super.getField();
    }

    /**
     * Set the minimum value allowed.
     *
     * @param value Minimum value allowed.
     */
    public void setMin(Date value) {
        getField().setMin(value == null ? null : create(value));
    }

    /**
     * Set the maximum value allowed.
     *
     * @param value Maximum value allowed.
     */
    public void setMax(Date value) {
        getField().setMax(value == null ? null : create(value));
    }

    /**
     * Get the allowed minimum value that is currently set. Null is returned of no minimum value was set.
     *
     * @return Minimum value.
     */
    public Date getMin() {
        LocalDate d = getField().getMin();
        return d == null ? null : create(d);
    }

    /**
     * Get the allowed maximum value that is currently set. Null is returned of no maximum value was set.
     *
     * @return Maximum value.
     */
    public Date getMax() {
        LocalDate d = getField().getMax();
        return d == null ? null : create(d);
    }

    /**
     * Does it allow empty?
     *
     * @return False if allows, otherwise true.
     */
    public boolean isRequired() {
        return getField().isRequired();
    }

    /**
     * Set whether empty value is allowed or not.
     *
     * @param required Set to false if empty value should be allowed.
     */
    public void setRequired(boolean required) {
        getField().setRequired(required);
    }

    /**
     * Set the visibility of "week numbers" while accepting the value.
     *
     * @param weekNumbersVisible True if visible.
     */
    public void setWeekNumbersVisible(boolean weekNumbersVisible) {
        getField().setWeekNumbersVisible(weekNumbersVisible);
    }

    /**
     * Check the visibility of "week numbers" while accepting the value.
     *
     * @return True if visible.
     */
    public boolean isWeekNumbersVisible() {
        return getField().isWeekNumbersVisible();
    }

    /**
     * Set the null value. This value will be considered as the null value. When this value is set to the
     * {@link DateField}, it will be considered as null. Also, this value will be returned when the field value is
     * null. The default value of this is set to Jan 1, 1800 but you could set it to <code>null</code> if desired.
     *
     * @param nullValue Null value to set.
     */
    public static void setNullValue(Date nullValue) {
        DateField.nullValue = nullValue;
        LegacyDateField.setNullValue();
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
}
