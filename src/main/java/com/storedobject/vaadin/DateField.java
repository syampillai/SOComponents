package com.storedobject.vaadin;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * A field to edit {@link Date}. Even though developers should start using {@link LocalDate}, almost all databases are still
 * using {@link Date}.
 *
 * @author Syam
 */
public class DateField extends TranslatedField<Date, LocalDate> {

    private static Date today = null;

    public DateField() {
        this(null, null);
    }

    public DateField(String label) {
        this(label, null);
    }

    public DateField(Date initialValue) {
        this(null, initialValue);
    }

    public DateField(String label, Date initialValue) {
        super(new DatePicker(), (f, d) -> create(d), (f, d) -> create(d), null);
        setLabel(label);
        setValue(initialValue);
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

    public static void setToday(Date today) {
        DateField.today = today;
    }

    public static Date create(LocalDate date) {
        if(date == null) {
            return today();
        }
        return create(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

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
}
