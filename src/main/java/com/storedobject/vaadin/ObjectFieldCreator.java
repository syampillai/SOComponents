package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A class that implements this interface determines how fields ({@link HasValue}) are created for editing an "object". All methods of this
 * interface have default implementation.
 * @param <T> Type type of "object" for which fields need to be created
 * @author Syam
 */
public interface ObjectFieldCreator<T> {

    /**
     * Create an instance of this interface for a particular "form".
     * @param objectForm Form
     * @return An instance of this interface. Default implementation returns the same instance.
     */
    default ObjectFieldCreator<T> create(@SuppressWarnings("unused") ObjectForm<T> objectForm) {
        return this;
    }

    /**
     * Get the "getXXX" methods of the object from which name of the "fields" can be determined.
     * @return The default implementation returns <code>null</code>. If <code>null</code> is returned, reflection will be used on the object's
     * class to determine the "getXXX" methods. (For example, if getFirstName() method exists, a field name of "FirstName" is assumed).
     */
    default Stream<Method> getFieldGetMethods() {
        return null;
    }

    /**
     * Get additional field namaes (in addition to the ones got from {@link #getFieldGetMethods()}.
     * @return Default implementation returns <code>null</code> (means no extra fields are added).
     */
    default Stream<String> getFieldNames() {
        return null;
    }

    /**
     * Create a "field" for the name, type and label provided.
     * @param fieldName Name of the field
     * @param fieldType Type of the field
     * @param label Label of the field
     * @return Field created.
     */
    default HasValue<?, ?> createField(@SuppressWarnings("unused") String fieldName, Class<?> fieldType, String label) {
        if(fieldType == null) {
            return null;
        }
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
        if(fieldType == java.util.Date.class) {
            return new com.storedobject.vaadin.util.DateField(label);
        }
        if(fieldType == LocalDate.class) {
            return new DatePicker(label);
        }
        return null;

    }

    /**
     * Get field name for a method (typically a getXXX or isXXX method).
     * @param getMethod Method
     * @return Default implementation returns "XXX" for method <code>getXXX()</code> or <code>isXXX()</code>.
     */
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

    /**
     * Get the function that can obtain the value for a field from the object.
     * @param fieldName Field name
     * @return Default implementation returns <code>null</code> and in that case, it will try to get it from its "get" method if one exists.
     * (If field name is "XXX", then <code>getXXX()</code> method is used to get the value).
     */
    default Function<T, ?> getValueGetter(@SuppressWarnings("unused") String fieldName) {
        return null;
    }

    /**
     * Get the function that can be used to set the value of a field to the object. If <code>null</code> is returned from this method,
     * set method (<code>setXXX()</code> for field "XXX") of the object is used for setting the value and if no such method exists, the field will be
     * set to "read only".
     * @param fieldName Field name
     * @return Default implementaton returns <code>null</code>.
     */
    default BiConsumer<T, ?> getValueSetter(@SuppressWarnings("unused") String fieldName) {
        return null;
    }

    /**
     * Determine the order in which field is displayed on the form. Fields with smaller "field orders" are displayed first.
     * @param fieldName Field name
     * @return Default implentation returns {@link Integer#MAX_VALUE}.
     */
    default int getFieldOrder(@SuppressWarnings("unused") String fieldName) {
        return Integer.MAX_VALUE;
    }

    /**
     * Determine the label to be used for a field.
     * @param fieldName Field name
     * @return Default implementation returns the value obtained from {@link ApplicationEnvironment#createLabel(String)}.
     */
    default String getLabel(String fieldName) {
        return Objects.requireNonNull(ApplicationEnvironment.get()).createLabel(fieldName);
    }

    /**
     * Close the field creator once the "field creation" process is over.
     * The default implemntation does nothing.
     */
    default void close() {
    }
}