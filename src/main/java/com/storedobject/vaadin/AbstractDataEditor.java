package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
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
public abstract class AbstractDataEditor<T> extends AbstractDataForm {

    private HashMap<Method, Object> fixedValues = new HashMap<>();

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
        setCaption(caption == null || caption.isEmpty() ? Application.get().getEnvironment().createLabel(getObjectClass()) : caption);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObjectForm<T> getForm() {
        return (ObjectForm<T>) super.getForm();
    }

    /**
     * Get the class of the object being edtited. (Same as {@link #getDataClass()}.
     * @return Object's class.
     */
    @SuppressWarnings("unchecked")
    public Class<T> getObjectClass() {
        return ((ObjectForm<T>)form).getObjectClass();
    }

    /**
     * Get the class of the object being edtited. (Same as {@link #getObjectClass()}.
     * @return Object's class.
     */
    @SuppressWarnings("unchecked")
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
    protected Method getFieldGetMethod(@SuppressWarnings("unused") String fieldName) {
        throw FIELD_ERROR;
    }

    /**
     * Get the field's "set" method. The default implementation checks both availability of setXXX method.
     * @param fieldName Name of the field
     * @param getMethod "get" method of this field (determined through {@link #getFieldGetMethod(String)})
     * @return Field' "set" method (if method is found, it will returns <code>null</code>).
     */
    protected Method getFieldSetMethod(@SuppressWarnings("unused") String fieldName, @SuppressWarnings("unused") Method getMethod) {
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
     * @param valueSetter Function that determines how to commit value from the field to the obejct's instance
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
     * @param setMethod Method that determines how to commit the value from the field to the obejct's instance
     */
    protected void addField(String fieldName, Method getMethod, Method setMethod) {
        getForm().addField(fieldName, getMethod, setMethod);
    }

    /**
     * Check whether field should be included in the form or not.
     * @param fieldName Name of the field.
     * @return True or false. (Default return value is <code>true</code>).
     */
    protected boolean includeField(@SuppressWarnings("unused") String fieldName) {
        throw FIELD_ERROR;
    }

    /**
     * Customize a field. This method is invoked when a field is created. One may do some customization here.
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void customizeField(String fieldName, HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    /**
     * Create the field for the particular name.. Default implementation try to obatin the value from the "field creator"
     * ({@link ObjectFieldCreator#createField(String, Class, String)}).
     * @param fieldName Name of the field
     * @param fieldType Type of the field's value
     * @param label Label
     * @return Field
     */
    protected HasValue<?, ?> createField(String fieldName, @SuppressWarnings("unused") Class<?> fieldType, @SuppressWarnings("unused") String label) {
        HasValue<?, ?> field = createField(fieldName);
        if(field != null) {
            return field;
        }
        throw FIELD_ERROR;
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
    @SuppressWarnings("unchecked")
    protected ObjectFieldCreator<T> getFieldCreator() {
        return ((ObjectForm<T>)form).getFieldCreator();
    }

    /**
     * Get the order in which a field to appear in the form. Any integer value can be returned and the field is placed in the form in
     * asceniding order of the values returned by this method. Default implementation try to obatin the value from the "field creator"
     * ({@link ObjectFieldCreator#getFieldOrder(String)}).
     * @param fieldName Name of the field
     * @return Field order.
     */
    protected int getFieldOrder(@SuppressWarnings("unused") String fieldName) {
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
     * Get the instance of the currently editing object. (A new object will be created by invoking {@link #createObjectInstance()} if there
     * is no current object instance set).
     * @return Instance of the currently loaded object.
     */
    @SuppressWarnings("unchecked")
    public T getObject() {
        return ((ObjectForm<T>)form).getObject();
    }

    /**
     * Set the current object. Fields will be loaded.
     * @param object Object to set
     */
    @SuppressWarnings("unchecked")
    public void setObject(T object) {
        ((ObjectForm<T>)form).setObject(object);
    }

    /**
     * Set the current object. Fields will be loaded.
     * @param object Object to set
     * @param load Whether to load the fields or not
     */
    @SuppressWarnings("unchecked")
    public void setObject(T object, boolean load) {
        ((ObjectForm<T>)form).setObject(object, load);
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
     * Handle errors while setting (committing)  values. Default implementation returns <code>true</code> without doing anything.
     * @param fieldName Name of the field
     * @param field Field
     * @param fieldValue Field's value that's being committed
     * @param objectValue Value currently being in the object
     * @param error Error occurred while setting the value
     * @return Whether the error situation is handled or not.
     */
    protected boolean handleValueSetError(@SuppressWarnings("unused") String fieldName, @SuppressWarnings("unused") HasValue<?, ?> field,
                                          @SuppressWarnings("unused") Object fieldValue,
                                          @SuppressWarnings("unused") Object objectValue,
                                          @SuppressWarnings("unused") Throwable error) {
        return true;
    }

    protected class DForm extends ObjectForm<T> {

        public DForm(Class<T> objectClass) {
            super(objectClass);
            setView(AbstractDataEditor.this);
            setMethodHandlerHost(AbstractDataEditor.this);
        }

        @Override
        protected void constructed() {
            formConstructed();
            super.constructed();
        }

        @Override
        protected HasComponents createContainer() {
            return AbstractDataEditor.this.createContainer();
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
            try {
                return AbstractDataEditor.this.getFieldGetMethod(fieldName);
            } catch (FieldError ignored) {
            }
            try {
                return AbstractDataEditor.this.getClass().getMethod("get" + fieldName);
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
            try {
                return AbstractDataEditor.this.getFieldSetMethod(fieldName, getMethod);
            } catch (FieldError ignored) {
            }
            Class[] params = new Class[] { getMethod.getReturnType() };
            try {
                return AbstractDataEditor.this.getClass().getMethod("set" + fieldName, params);
            } catch (NoSuchMethodException ignored) {
            }
            return super.getFieldSetMethod(fieldName, getMethod);
        }

        @Override
        protected HasValue<?, ?> createField(String fieldName, Class<?> fieldType, String label) {
            try {
                return AbstractDataEditor.this.createField(fieldName, fieldType, label);
            } catch(FieldError e) {
                return super.createField(fieldName, fieldType, label);
            }
        }

        @Override
        protected boolean includeField(String fieldName) {
            try {
                return AbstractDataEditor.this.includeField(fieldName);
            } catch (FieldError e) {
                return super.includeField(fieldName);
            }
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
        protected void attachField(String fieldName, HasValue<?, ?> field) {
            try {
                AbstractDataEditor.this.attachField(fieldName, field);
            } catch (FieldError e) {
                super.attachField(fieldName, field);
            }
        }

        @Override
        protected void detachField(String fieldName, HasValue<?, ?> field) {
            try {
                AbstractDataEditor.this.detachField(fieldName, field);
            } catch (FieldError e) {
                super.detachField(fieldName, field);
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
            if(object != null) {
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
        public String getLabel(String fieldName) {
            try {
                return AbstractDataEditor.this.getLabel(fieldName);
            } catch (FieldError e) {
                return super.getLabel(fieldName);
            }
        }

        @Override
        protected void customizeField(String fieldName, HasValue<?, ?> field) {
            try {
                AbstractDataEditor.this.customizeField(fieldName, field);
            } catch (FieldError e) {
                super.customizeField(fieldName, field);
            }
        }

        @Override
        protected boolean handleValueSetError(String fieldName, HasValue<?, ?> field, Object fieldValue, Object objectValue, Throwable error) {
            return AbstractDataEditor.this.handleValueSetError(fieldName, field, fieldValue, objectValue, error);
        }

        @Override
        public boolean isFieldVisible(String fieldName) {
            return AbstractDataEditor.this.isFieldVisible(fieldName);
        }

        @Override
        public boolean isFieldVisible(HasValue<?, ?> field) {
            return AbstractDataEditor.this.isFieldVisible(field);
        }

        @Override
        public boolean isFieldEditable(String fieldName) {
            if(fixedValues.keySet().stream().anyMatch(m -> m.getName().equals("set" + fieldName))) {
                return false;
            }
            return AbstractDataEditor.this.isFieldEditable(fieldName);
        }

        @Override
        public boolean isFieldEditable(HasValue<?, ?> field) {
            return AbstractDataEditor.this.isFieldEditable(field);
        }
    }
}
