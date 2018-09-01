package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DefaultObjectFieldCreator implements ObjectFieldCreator {

    @Override
    public HasValue<?, ?> createField(String fieldName, Class<?> fieldType, String label) {
        if(fieldType == String.class) {
            return new TextField(label);
        }
        if(fieldType == boolean.class || fieldType == Boolean.class) {
            return new BooleanField(label);
        }
        if(fieldType == int.class || fieldType == Integer.class) {
            return new IntegerField(label);
        }
        if(fieldType == long.class || fieldType == Long.class) {
            return new LongField(label);
        }
        if(fieldType == double.class || fieldType == Double.class) {
            return new DoubleField(label);
        }
        if(fieldType == BigDecimal.class) {
            return new BigDecimalField(label);
        }
        if(fieldType == java.sql.Date.class) {
            return new DateField(label);
        }
        if(fieldType == LocalDate.class) {
            return new DatePicker(label);
        }
        return null;
    }
}
