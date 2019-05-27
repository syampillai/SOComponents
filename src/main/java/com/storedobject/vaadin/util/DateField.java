package com.storedobject.vaadin.util;

import com.storedobject.vaadin.TranslatedField;

import java.util.Date;

/**
 * A field to edit old {@link Date}.
 *
 * @author Syam
 */
public class DateField extends TranslatedField<Date, java.sql.Date> {

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
        super(new com.storedobject.vaadin.DateField(), (f, d) -> create(d), (f, d) -> create(d), null);
        setLabel(label);
        setValue(initialValue);
    }

    public static Date create(java.sql.Date date) {
        if(date == null) {
            return new Date();
        }
        return new Date(date.getTime());
    }

    public static java.sql.Date create(Date date) {
        if(date == null) {
            date = new Date();
        }
        return new java.sql.Date(date.getTime());
    }
}