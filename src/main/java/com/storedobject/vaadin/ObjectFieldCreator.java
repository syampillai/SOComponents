package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

public interface ObjectFieldCreator<T> {

    default ObjectFieldCreator<T> create(ObjectForm<T> objectForm) {
        return this;
    }

    default Stream<Method> getFieldGetMethods() {
        return null;
    }

    default HasValue<?, ?> createField(String fieldName, Class<?> fieldType, String label) {
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

    default String getFieldName(Method getMethod) {
        String name = getMethod.getName();
        if(name.equals("getClass")) {
            return null;
        } else if(name.startsWith("get")) {
            return name.substring(3);
        } else if(name.startsWith("is")) {
            return name.substring(2);
        }
        return null;
    }

    default int getFieldOrder(String fieldName) {
        return Integer.MAX_VALUE;
    }

    default String getLabel(String fieldName) {
        return ApplicationEnvironment.get().createLabel(fieldName);
    }

    default void close() {
    }
}