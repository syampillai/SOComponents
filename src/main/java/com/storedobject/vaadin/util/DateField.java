package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Field;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DateField extends DatePicker implements Field<LocalDate> {

    public DateField() {
        setWidth("18ch");
    }

    @Override
    public boolean isAutofocus() {
        return super.isAutofocusBoolean();
    }

    @Override
    public void setAutofocus(boolean autofocus) {
        super.setAutofocus(autofocus);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return super.isReadonlyBoolean();
    }

    @Override
    public void focus() {
        super.focus();
    }

    @Override
    public void blur() {
        super.blur();
    }

    public void setDate(Date date) {
        setValue(create(date));
    }

    public Date getDate() {
        return create(getValue());
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
}