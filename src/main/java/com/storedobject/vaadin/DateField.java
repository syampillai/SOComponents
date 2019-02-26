package com.storedobject.vaadin;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * A field to edit {@link Date}.
 *
 * @author Syam
 */
public class DateField extends TranslatedField<Date, LocalDate> {

    private static Date today = null;

    public DateField() {
        super(new DatePicker(), (f, d) -> create(d), (f, d) -> create(d));
    }

    public DateField(String label) {
        this(label, null);
    }

    public DateField(Date initialValue) {
        this(null, initialValue);
    }

    public DateField(String label, Date initialValue) {
        this();
        setLabel(label);
        if(initialValue != null) {
            setValue(initialValue);
        } else {
            setValue(today());
        }
    }

    private static Date today() {
        if(today == null) {
            GregorianCalendar c = new GregorianCalendar();
            today = new Date(c.getTimeInMillis());
        }
        return today;
    }

    public static void setToday(Date today) {
        DateField.today = today;
    }

    public static Date create(LocalDate date) {
        return create(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public static <D extends java.util.Date> LocalDate create(D date) {
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
        GregorianCalendar c = new GregorianCalendar(year, month - 1, day);
        return new Date(c.getTimeInMillis());
    }

    @Override
    public Date getEmptyValue() {
        return today();
    }
}
