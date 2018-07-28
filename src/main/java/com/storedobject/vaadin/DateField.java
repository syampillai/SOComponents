package com.storedobject.vaadin;

import com.storedobject.vaadin.util.TranslatedField;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;

public class DateField extends TranslatedField<Date, LocalDate, com.storedobject.vaadin.util.DateField> {

    public DateField() {
        super(today(), new com.storedobject.vaadin.util.DateField(),
                (f, d) -> com.storedobject.vaadin.util.DateField.create(d), (f, d) -> com.storedobject.vaadin.util.DateField.create(d));
        setPresentationValue(getValue());
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
        }
    }

    private static Date today() {
        GregorianCalendar c = new GregorianCalendar();
        return new Date(c.getTimeInMillis());
    }
}
