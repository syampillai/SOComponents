package com.storedobject.vaadin;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;

import static java.util.Calendar.*;

/**
 * A field to edit {@link Date}. Even though developers should start using {@link LocalDate}, almost all databases are still
 * using {@link Date}.
 *
 * @author Syam
 */
public class DateField extends TranslatedField<Date, LocalDate> {

    private static Date today = null;

    /**
     * Constructor.
     */
    public DateField() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public DateField(String label) {
        this(label, null);
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
        super.setValue(value == null ? today() : value);
    }

    private static Date today() {
        if(today == null) {
            LocalDate d = LocalDate.now();
            today = create(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
        }
        return today;
    }

    /**
     * Set the value of "today". (In a business application, it could be the "working date").
     *
     * @param today Today.
     */
    public static void setToday(Date today) {
        DateField.today = today;
    }

    /**
     * Create an instance of a {@link Date} from a {@link LocalDate} instance.
     *
     * @param date Instance to convert.
     * @return Converted value.
     */
    public static Date create(LocalDate date) {
        if(date == null) {
            return today();
        }
        return create(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * Create an instance of a {@link LocalDate} from a {@link Date} instance.
     *
     * @param date Instance to convert.
     * @param <D> Type of instance to convert.
     * @return Converted value.
     */
    public static <D extends java.util.Date> LocalDate create(D date) {
        if(date == null) {
            return LocalDate.now();
        }
        return LocalDate.of(getYear(date), getMonth(date), getDay(date));
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

    private static Date create(int year, int month, int day) {
        @SuppressWarnings("MagicConstant") GregorianCalendar c = new GregorianCalendar(year, month - 1, day);
        return new Date(c.getTimeInMillis());
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
}
