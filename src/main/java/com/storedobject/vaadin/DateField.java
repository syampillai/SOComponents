package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicDateField;
import com.storedobject.vaadin.util.TranslatedField;

import java.sql.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;

/**
 * A field to edit {@link Date}.
 *
 * @author Syam
 */
public class DateField extends TranslatedField<Date, LocalDate, BasicDateField> {

    private static Date today = null;

    public DateField() {
        super(today(), new BasicDateField(),
                (f, d) -> BasicDateField.create(d), (f, d) -> BasicDateField.create(d));
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
}
