package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A view that is used for creating "data entry forms" for a particular type of object. The "form" embedded in this is an {@link ObjectForm}.
 * Please refer to {@link ObjectForm} for a detailed explanation of how the "form" is generated. Many of the methods in this are delegated to
 * the embedded "form".
 * @param <T> Type of object to be edited
 * @author Syam
 */
public abstract class AbstractDataEditor<T> extends AbstractDataForm<T> {

    private final HashMap<Method, Object> fixedValues = new HashMap<>();

    /**
     * Constructor
     * @param objectClass Type of class to be edited
     */
    public AbstractDataEditor(Class<T> objectClass) {
        this(objectClass, null);
    }

    /**
     * Constructor
     * @param objectClass Type of class to be edited
     * @param caption Caption of the view
     */
    public AbstractDataEditor(Class<T> objectClass, String caption) {
        this.form = new DForm(objectClass);
        setCaption(caption == null || caption.isEmpty() ? Objects.requireNonNull(Application.get()).getEnvironment().createLabel(getObjectClass()) : caption);
    }

    @Override
    public ObjectForm<T> getForm() {
        return (ObjectForm<T>) super.getForm();
    }

    /**
     * Get the class of the object being edited. (Same as {@link #getDataClass()}.
     * @return Object's class.
     */
    public Class<T> getObjectClass() {
        return ((ObjectForm<T>)form).getObjectClass();
    }

    /**
     * Get the class of the object being edited. (Same as {@link #getObjectClass()}.
     * @return Object's class.
     */
    public Class<T> getDataClass() {
        return ((ObjectForm<T>)form).getDataClass();
    }

    /**
     * Get the names of the fields to be generated in the form. This can be overridden to generate a custom list.
     * @return Stream of field names.
     */
    @Override
    public Stream<String> getFieldNames() {
        throw FIELD_ERROR;
    }

    /**
     * Get the field's "get" method. The default implementation checks for both getXXX and isXXX methods.
     * @param fieldName Name of the field
     * @return Field' "get" method (if method is found, it will returns <code>null</code>).
     */
    protected Method getFieldGetMethod(String fieldName) {
        throw FIELD_ERROR;
    }

    /**
     * Get the field's "set" method. The default implementation checks both availability of setXXX method.
     * @param fieldName Name of the field
     * @param getMethod "get" method of this field (determined through {@link #getFieldGetMethod(String)})
     * @return Field' "set" method (if method is found, it will returns <code>null</code>).
     */
    protected Method getFieldSetMethod(String fieldName, Method getMethod) {
        throw FIELD_ERROR;
    }

    /**
     * Add extra fields.
     * @param fieldNames Names of the fields to be added
     */
    protected void addField(Iterable<String> fieldNames) {
        getForm().addField(fieldNames);
    }

    /**
     * Add extra fields.
     * @param fieldNames Names of the fields to be added
     */
    protected void addField(String... fieldNames) {
        getForm().addField(fieldNames);
    }

    /**
     * Add an extra field
     * @param field Field.
     * @param valueGetter Function that determines how to get the value to load the field
     * @return A field name will be generated (starting with an underscore character followed by a random number)
     * and returned.
     * @param <V> Value type of the field.
     */
    protected <V> String addField(HasValue<?, V> field, Function<T, V> valueGetter) {
        return getForm().addField(field, valueGetter);
    }

    /**
     * Add an extra field
     * @param field Field.
     * @param valueGetter Function that determines how to get the value to load the field
     * @param valueSetter Function that determines how to commit value from the field to the object's instance
     * @return A field name will be generated (starting with an underscore character followed by a random number)
     * and returned.
     * @param <V> Value type of the field.
     */
    protected <V> String addField(HasValue<?, V> field, Function<T, V> valueGetter, BiConsumer<T, V> valueSetter) {
        return getForm().addField(field, valueGetter, valueSetter);
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param valueGetter Function that determines how to get the value to load the field.
     */
    protected void addField(String fieldName, Function<T, ?> valueGetter) {
        getForm().addField(fieldName, valueGetter);
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param valueGetter Function that determines how to get the value to load the field
     * @param valueSetter Function that determines how to commit value from the field to the object's instance
     */
    protected void addField(String fieldName, Function<T, ?> valueGetter, BiConsumer<T, ?> valueSetter) {
        getForm().addField(fieldName, valueGetter, valueSetter);
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param getMethod Method that determines how to get the value to load the field
     */
    protected void addField(String fieldName, Method getMethod) {
        getForm().addField(fieldName, getMethod);
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param getMethod Method that determines how to get the value to load the field
     * @param setMethod Method that determines how to commit the value from the field to the object's instance
     */
    protected void addField(String fieldName, Method getMethod, Method setMethod) {
        getForm().addField(fieldName, getMethod, setMethod);
    }

    /**
     * Check whether field should be included in the form or not.
     * @param fieldName Name of the field.
     * @return True or false. (Default return value is <code>true</code>).
     */
    protected boolean includeField(String fieldName) {
        return true;
    }

    /**
     * Customize a field. This method is invoked when a field is created. One may do some customization here.
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void customizeField(String fieldName, HasValue<?, ?> field) {
    }

    /**
     * Create the field for the particular name.. Default implementation try to obtain the value from the "field creator"
     * ({@link ObjectFieldCreator#createField(String, Class, String)}).
     * @param fieldName Name of the field
     * @param fieldType Type of the field's value
     * @param label Label
     * @return Field
     */
    protected HasValue<?, ?> createField(String fieldName, Class<?> fieldType, String label) {
        return null;
    }

    /**
     * Construct a field for the given field name. (For internal purpose only).
     * @param fieldName Field name
     * @return Field
     */
    protected final HasValue<?, ?> constructField(String fieldName) {
        return getForm().createField(fieldName);
    }

    /**
     * Get the "field creator" for this form. By default, it tries to obtain it from {@link ApplicationEnvironment#getObjectFieldCreator()}.
     * @return Field creator.
     */
    protected ObjectFieldCreator<T> getFieldCreator() {
        return ((ObjectForm<T>)form).getFieldCreator();
    }

    /**
     * Get the order in which a field to appear in the form. Any integer value can be returned and the field is placed in the form in
     * ascending order of the values returned by this method. Default implementation try to obtain the value from the "field creator"
     * ({@link ObjectFieldCreator#getFieldOrder(String)}).
     * @param fieldName Name of the field
     * @return Field order.
     */
    protected int getFieldOrder(String fieldName) {
        throw FIELD_ERROR;
    }

    /**
     * Create an instance of the object. Default implementation tries to invoke the default constructor to create an instance.
     * @return Newly created object.
     */
    protected T createObjectInstance() {
        throw FIELD_ERROR;
    }

    /**
     * Create an instance of the object (public method).
     *
     * @return Newly created object.
     */
    public final T newObject() {
        return getForm().createObjectInstance();
    }

    /**
     * Get the instance of the currently editing object. (A new object will be created by invoking {@link #createObjectInstance()} if there
     * is no current object instance set).
     * @return Instance of the currently loaded object.
     */
    public T getObject() {
        return form.getObject();
    }

    /**
     * Set the current object. Fields will be loaded.
     * @param object Object to set
     */
    public void setObject(T object) {
        setObject(object, true);
    }

    /**
     * Set the current object. Fields will be loaded.
     * @param object Object to set
     * @param load Whether to load the fields or not
     */
    public void setObject(T object, boolean load) {
        form.setObject(object, load);
    }

    /**
     * Set a fixed value for a field. If a fixed value is set, the field value will not be changed.
     * @param fieldName Name of the field
     * @param value Value to be set as fixed value
     */
    public void setFixedValue(String fieldName, Object value) {
        Arrays.stream(getObjectClass().getMethods()).filter(m -> checkSetMethod(m, fieldName, value)).forEach(m -> fixedValues.put(m, value));
    }

    private static boolean checkSetMethod(Method m, String fieldName, Object value) {
        if(!m.getName().equals("set" + fieldName) || m.getParameterCount() != 1 || !Modifier.isPublic(m.getModifiers()) ||
                Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        return value == null || m.getParameterTypes()[0].isAssignableFrom(value.getClass());
    }

    /**
     * Set a fixed value for a field. If a fixed value is set, the field value will not be changed.
     * @param field Field
     * @param value Value to be set as fixed value
     */
    public void setFixedValue(HasValue<?, ?> field, Object value) {
        setFixedValue(getFieldName(field), value);
    }

    /**
     * Handle errors while setting (committing) values.
     * Default implementation returns <code>true</code> without doing anything.
     * @param fieldName Name of the field
     * @param field Field
     * @param fieldValue Field's value that's being committed
     * @param objectValue Value currently being in the object
     * @param error Error occurred while setting the value
     * @return Whether the error situation is handled or not.
     */
    protected boolean handleValueSetError(String fieldName, HasValue<?, ?> field,
                                          Object fieldValue,
                                          Object objectValue,
                                          Throwable error) {
        return true;
    }

    /**
     * Form to be used internally.
     *
     * @author Syam
     */
    protected class DForm extends ObjectForm<T> {

        /**
         * Constructor.
         *
         * @param objectClass Object class.
         */
        public DForm(Class<T> objectClass) {
            super(objectClass);
            setView(AbstractDataEditor.this);
            setMethodHandlerHost(AbstractDataEditor.this);
        }

        @Override
        protected void generateFieldNames() {
            try {
                AbstractDataEditor.this.getFieldNames().filter(this::includeField).forEach(n -> addField(n, getFieldGetMethod(n), null));
            } catch (FieldError e) {
                super.generateFieldNames();
            }
        }

        @Override
        protected Method getFieldGetMethod(String fieldName) {
            Method m;
            try {
                m = getFieldCreator().getFieldGetMethod(fieldName);
                if (m != null && !m.getDeclaringClass().isAssignableFrom(AbstractDataEditor.class)) {
                    return m;
                }
            } catch (FieldError ignored) {
            }
            try {
                return AbstractDataEditor.this.getFieldGetMethod(fieldName);
            } catch (FieldError ignored) {
            }
            try {
                m = AbstractDataEditor.this.getClass().getMethod("get" + fieldName);
                if (!m.getDeclaringClass().isAssignableFrom(AbstractDataEditor.class)) {
                    return m;
                }
            } catch (NoSuchMethodException ignored) {
            }
            try {
                return AbstractDataEditor.this.getClass().getMethod("is" + fieldName);
            } catch (NoSuchMethodException ignored) {
            }
            return super.getFieldGetMethod(fieldName);
        }

        @Override
        protected Method getFieldSetMethod(String fieldName, Method getMethod) {
            Method m;
            try {
                m = getFieldCreator().getFieldSetMethod(fieldName, getMethod);
                if (m != null && !m.getDeclaringClass().isAssignableFrom(AbstractDataEditor.class)) {
                    return m;
                }
            } catch (FieldError ignored) {
            }
            try {
                return AbstractDataEditor.this.getFieldSetMethod(fieldName, getMethod);
            } catch (FieldError ignored) {
            }
            if(!"Caption".equals(fieldName)) {
                Class<?>[] params = new Class[]{getMethod.getReturnType()};
                try {
                    m = AbstractDataEditor.this.getClass().getMethod("set" + fieldName, params);
                    if(!m.getDeclaringClass().isAssignableFrom(AbstractDataEditor.class)) {
                        return m;
                    }
                } catch(NoSuchMethodException ignored) {
                }
            }
            return super.getFieldSetMethod(fieldName, getMethod);
        }

        @Override
        protected HasValue<?, ?> createField(String fieldName, Class<?> fieldType, String label) {
            HasValue<?, ?> field = AbstractDataEditor.this.createField(fieldName, fieldType, label);
            return field == null ? super.createField(fieldName, fieldType, label) : field;
        }

        @Override
        protected boolean includeField(String fieldName) {
            return AbstractDataEditor.this.includeField(fieldName) && super.includeField(fieldName);
        }

        @Override
        protected int getFieldOrder(String fieldName) {
            try {
                return AbstractDataEditor.this.getFieldOrder(fieldName);
            } catch (FieldError e) {
                return super.getFieldOrder(fieldName);
            }
        }

        @Override
        protected T createObjectInstance() {
            T object;
            try {
                object = AbstractDataEditor.this.createObjectInstance();
            } catch (FieldError e) {
                object = super.createObjectInstance();
            }
            T o = object;
            if (object != null) {
                fixedValues.forEach((m, v) -> {
                    try {
                        m.invoke(o, v);
                    } catch (Exception ignored) {
                    }
                });
            }
            return object;
        }

        @Override
        protected void customizeField(String fieldName, HasValue<?, ?> field) {
            AbstractDataEditor.this.customizeField(fieldName, field);
            super.customizeField(fieldName, field);
        }

        @Override
        protected boolean handleValueSetError(String fieldName, HasValue<?, ?> field, Object fieldValue, Object objectValue, Throwable error) {
            return AbstractDataEditor.this.handleValueSetError(fieldName, field, fieldValue, objectValue, error);
        }
    }
}
