package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Class to represent a "data entry form" for a particular type of object.
 * <p>Form (see {@link AbstractForm}) contains "fields" ({@link HasValue}) and each field has a name. By default, "field names" are automatically determined
 * from the attributes of the object under editing via reflection. So, getXXX (for example, getFirstName()) methods are used to
 * determine the field names and its value types.
 * <p>Field values can be loaded from the object instance that is being edited and edited values can be committed to the object instance.
 * By default, <code>getXXX/setXXX</code> methods of the object class are used for this. However, a particular getXXX or setXXX method can be
 * overridden by defining a method with the same name in the form itself. Yet another facility is to define some of those methods in some
 * other class and set an instance of that class in the form using {@link #setMethodHandlerHost(Object)}. However, the signatures of the getXXX/setXXX
 * methods that are overridden like this take the object instance as an additional parameter as its first parameter. For example, let's assume that
 * our object class represents a "Person" and we want to override the "DateOfBirth" field's "set" method. In the Person class, we may be
 * having the getDateOfBirth() method that returns "date of birth" and setDateOfBirth(Date) to set the "date of birth". So, the overridden "set"
 * method in the form should have signatures as follows: setDateOfBirth(Person, Date). If we want to override the "get" method, it should look
 * like getDateOfBirth(Person).</p>
 * <p>The order of precedence when it has to determine get/set methods - First preference is given to the available methods in the form, then the
 * available methods in the "method handler host" (if ones is set) and finally, in the object class itself. If it can't determine the "set" method
 * for a field, the field will be set as "read only".</p>
 * <p>If a getXXX method of the "host" should not be treated like this, it can be annotated with {@link NoField}.</p>
 * <p>What is the purpose of overriding get/set methods? One can implement extra logic such as value conversion, validation etc.
 * before getting/setting the values from/to the object instance. (It is better to add validators if that is the only purpose - see
 * {@link #addValidator(HasValue, Function)}, {@link #addValidator(HasValue, Function, String)}). When extra fields (see next paragraph)
 * need to be added, this mechanism is anyway required.</p>
 * <p>Any number of extra fields can be added using {@link #addField(String...)}, {@link #addField(Iterable)},
 * {@link #addField(String, Method, Method)}, {@link #addField(String, Method)}, {@link #addField(String, Function, BiConsumer)},
 * {@link #addField(String, Function)}, {@link #addField(String, HasValue)}, {@link #addField(HasValue)}. When you add additional
 * fields, depending on the method you use, it can determine how to get/set values. If not enough information is provided as parameters
 * (for example, methods {@link #addField(String...)}), reflection is used to determine it as mentioned in the previous paragraphs.</p>
 * <p>An "object form" is not used directly in most cases. Instead, a {@link View} derived from {@link AbstractDataEditor} or
 * {@link DataEditor} is used where an "object form" is already embedded. All overridable methods in the form can be defined in these
 * "views" too.</p>
 *
 * @param <D> Type of object to be edited
 * @author Syam
 */
public class ObjectForm<D> extends AbstractForm<D> {

    private final Map<String, Method> getM = new HashMap<>();
    private final Map<String, Method> setM = new HashMap<>();
    private final Map<String, Function<D, ?>> getF = new HashMap<>();
    private final Map<String, BiConsumer<D, ?>> setF = new HashMap<>();
    private ObjectFieldCreator<D> fCreator;

    /**
     * Constructor
     * @param objectClass Class of the object
     */
    public ObjectForm(Class<D> objectClass) {
        this(objectClass, null);
    }

    /**
     * Constructor
     * @param objectClass Class of the object
     * @param container Field container
     */
    public ObjectForm(Class<D> objectClass, HasComponents container) {
        super(objectClass, container);
        data.setFieldValueHandler(new FieldHandler());
    }

    @Override
    protected void constructed() {
        if(fCreator != null) {
            fCreator.close();
            fCreator = null;
        }
        super.constructed();
    }

    /**
     * Get the "field creator" for this form. By default, it tries to obtain it from {@link ApplicationEnvironment#getObjectFieldCreator()}.
     * @return Field creator.
     */
    @SuppressWarnings("unchecked")
    ObjectFieldCreator<D> getFieldCreator() {
        if(fCreator == null) {
            fCreator = ((ObjectFieldCreator<D>) Objects.requireNonNull(ApplicationEnvironment.get()).getObjectFieldCreator()).create(this);
        }
        return fCreator;
    }

    /**
     * Set the method handler host. (See the documenation of the this class to get an idea).
     * @param host Method handler host
     */
    @SuppressWarnings("unchecked")
    public void setMethodHandlerHost(Object host) {
        ((FieldHandler)data.getFieldValueHandler()).setHost(host);
    }

    /**
     * Get the class of the object being edtited. (Same as {@link #getDataClass()}.
     * @return Object's class.
     */
    public Class<D> getObjectClass() {
        return objectClass;
    }

    /**
     * Get the class of the object being edtited. (Same as {@link #getObjectClass()}.
     * @return Object's class.
     */
    public Class<D> getDataClass() {
        return objectClass;
    }

    /**
     * Add extra fields.
     * @param fieldNames Names of the fields to be added
     */
    protected void addField(Iterable<String> fieldNames) {
        fieldNames.forEach(this::addField);
    }

    /**
     * Add extra fields.
     * @param fieldNames Names of the fields to be added
     */
    protected void addField(String... fieldNames) {
        Function<D, ?> valueGetter;
        BiConsumer<D, ?> valueSetter;
        Method valueGetterM, valueSetterM;
        for(String fieldName: fieldNames) {
            if(!includeF(fieldName)) {
                continue;
            }
            valueGetterM = getGetMethodFromHost(fieldName);
            valueSetterM = valueGetterM == null ? null : getSetMethodFromHost(fieldName, valueGetterM);
            if(valueGetterM == null) {
                valueGetter = getFieldCreator().getValueGetter(fieldName);
                if (valueGetter != null) {
                    getF.put(fieldName, valueGetter);
                }
            } else {
                valueGetter = null;
            }
            if(valueSetterM == null) {
                valueSetter = getFieldCreator().getValueSetter(fieldName);
                if (valueSetter != null) {
                    setF.put(fieldName, valueSetter);
                    if(valueGetterM != null) {
                        getM.put(fieldName, valueGetterM);
                        continue;
                    }
                }
            } else {
                valueSetter = null;
            }
            if(valueGetter == null && valueSetter == null) {
                addField(fieldName, valueGetterM, valueSetterM);
            }
        }
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param valueGetter Function that determines how to get the value to load the field.
     */
    protected void addField(String fieldName, Function<D, ?> valueGetter) {
        addField(fieldName, valueGetter, null);
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param valueGetter Function that determines how to get the value to load the field
     * @param valueSetter Function that determines how to commit value from the field to the obejct's instance
     */
    protected void addField(String fieldName, Function<D, ?> valueGetter, BiConsumer<D, ?> valueSetter) {
        if(!includeF(fieldName)) {
            return;
        }
        if(getM.containsKey(fieldName) || getF.containsKey(fieldName)) {
            return;
        }
        if(valueGetter == null && valueSetter == null) {
            addField(fieldName, null, (Method)null);
            return;
        }
        Method m;
        if(valueGetter == null) {
            m = getFieldGetMethod(fieldName);
            if(m == null) {
                return;
            }
            getM.put(fieldName, m);
            setF.put(fieldName, valueSetter);
            return;
        }
        getF.put(fieldName, valueGetter);
        if(valueSetter != null) {
            setF.put(fieldName, valueSetter);
            return;
        }
        m = getFieldGetMethod(fieldName);
        if(m == null) {
            return;
        }
        m = getFieldSetMethod(fieldName, m);
        if(m != null) {
            setM.put(fieldName, m);
        }
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param getMethod Method that determines how to get the value to load the field
     */
    protected void addField(String fieldName, Method getMethod) {
        addField(fieldName, getMethod, null);
    }

    /**
     * Add an extra field
     * @param fieldName Name of the field.
     * @param getMethod Method that determines how to get the value to load the field
     * @param setMethod Method that determines how to commit the value from the field to the obejct's instance
     */
    protected void addField(String fieldName, Method getMethod, Method setMethod) {
        if(!includeF(fieldName)) {
            return;
        }
        if(getM.containsKey(fieldName) || getF.containsKey(fieldName)) {
            return;
        }
        if(getMethod == null) {
            getMethod = getFieldGetMethod(fieldName);
        }
        if(getMethod == null) {
            getM.put(fieldName, dummyGET);
            return;
        }
        getM.put(fieldName, getMethod);
        if(setMethod == null) {
            setMethod = getFieldSetMethod(fieldName, getMethod);
        }
        if(setMethod == null) {
            return;
        }
        setM.put(fieldName, setMethod);
    }

    private boolean isField(Method m) {
        return m.getAnnotation(NoField.class) == null;
    }

    private Method getGetMethodFromHost(String fieldName) {
        Object host = getHost();
        if(host == null) {
            return null;
        }
        Method m;
        try {
            m = host.getClass().getMethod("get" + fieldName);
            if(m != null && m.getDeclaringClass() == host.getClass() && isField(m)) {
                return m;
            }
        } catch (NoSuchMethodException ignored) {
        }
        try {
            m = host.getClass().getMethod("is" + fieldName);
            if(m != null && m.getDeclaringClass() == host.getClass() && isField(m)) {
                return m;
            }
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    private Method getSetMethodFromHost(String fieldName, Method getMethod) {
        Object host = getHost();
        if(host == null) {
            return null;
        }
        Class<?>[] params = new Class[] { getMethod.getReturnType() };
        try {
            Method m = host.getClass().getMethod("set" + fieldName, params);
            if(m != null && m.getDeclaringClass() == host.getClass()) {
                return m;
            }
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    /**
     * Get the field's "get" method. The default implementation checks for both getXXX and isXXX methods.
     * @param fieldName Name of the field
     * @return Field' "get" method (if method is found, it will returns <code>null</code>).
     */
    protected Method getFieldGetMethod(String fieldName) {
        try {
            return this.getClass().getMethod("get" + fieldName);
        } catch (NoSuchMethodException ignored) {
        }
        try {
            return this.getClass().getMethod("is" + fieldName);
        } catch (NoSuchMethodException ignored) {
        }
        try {
            return objectClass.getMethod("get" + fieldName);
        } catch (NoSuchMethodException ignored) {
        }
        try {
            return objectClass.getMethod("is" + fieldName);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    /**
     * Get the field's "set" method. The default implementation checks both availability of setXXX method.
     * @param fieldName Name of the field
     * @param getMethod "get" method of this field (determined through {@link #getFieldGetMethod(String)})
     * @return Field' "set" method (if method is found, it will returns <code>null</code>).
     */
    protected Method getFieldSetMethod(String fieldName, Method getMethod) {
        Class<?>[] params = new Class[] { getMethod.getReturnType() };
        try {
            return this.getClass().getMethod("set" + fieldName, params);
        } catch (NoSuchMethodException ignored) {
        }
        try {
            return objectClass.getMethod("set" + fieldName, params);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    /**
     * Generate field names. This method is generally not overridden.
     */
    @Override
    protected void generateFieldNames() {
        getFieldGetMethods().forEach(m -> {
            String name = getFieldCreator().getFieldName(m);
            if(name != null && includeF(name)) {
                Method hm = getGetMethodFromHost(name);
                addField(name, hm == null ? m : hm, null);
            }
        });
        Stream<String> additionalNames = getFieldCreator().getFieldNames();
        if(additionalNames != null) {
            additionalNames.filter(Objects::nonNull).filter(this::includeF).forEach(this::addField);
        }
    }

    private Stream<Method> getFieldGetMethods() {
        Stream<Method> stream = getFieldCreator().getFieldGetMethods();
        return stream == null ? Arrays.stream(objectClass.getMethods()) : stream;
    }

    /**
     * Get the valid/allowed field names. (For internal use only).
     * @return Stream of field names.
     */
    protected final Stream<String> getFieldNames() {
        return Stream.concat(getM.keySet().stream(), getF.keySet().stream()).filter(n -> n != null && !n.isEmpty()).
                filter(this::includeF).sorted(Comparator.comparingInt(this::getFieldOrder));
    }

    /**
     * Get the order in which a field to appear in the form. Any integer value can be returned and the field is placed in the form in
     * asceniding order of the values returned by this method. Default implementation try to obatin the value from the "field creator"
     * ({@link ObjectFieldCreator#getFieldOrder(String)}).
     * @param fieldName Name of the field
     * @return Field order.
     */
    protected int getFieldOrder(String fieldName) {
        return getFieldCreator().getFieldOrder(fieldName);
    }

    /**
     * Check whether field should be included in the form or not.
     * @param fieldName Name of the field.
     * @return True or false. (Default return value is <code>true</code>).
     */
    protected boolean includeField(String fieldName) {
        return true;
    }

    private boolean includeF(String fieldName) {
        return includeField.includeField(fieldName) && includeField(fieldName);
    }

    private static final Method dummyGET = dummyGET();

    private static Method dummyGET() {
        try {
            return Object.class.getMethod("toString");
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    /**
     * Create the field for the particular name.
     * @param fieldName Name of the field
     * @return Field.
     */
    @Override
    protected final HasValue<?, ?> createField(String fieldName) {
        Method m = getM.get(fieldName);
        if(m == dummyGET) {
            getM.remove(fieldName);
            m = null;
        }
        HasValue<?, ?> field = super.createField(fieldName);
        if(field != null) {
            return field;
        }
        Class<?> returnType = m == null ? null : m.getReturnType();
        field = createField(fieldName, returnType, getLabel(fieldName));
        if(field != null) {
            customizeField(fieldName, field);
            getFieldCreator().customizeField(fieldName, field);
        }
        return field;
    }

    /**
     * Get the label for the field. Default implementation try to obtain the value from the "field creator"
     * ({@link ObjectFieldCreator#getLabel(String)}).
     * @param fieldName Name of the field
     * @return Label
     */
    @Override
    public String getLabel(String fieldName) {
        if(getView() instanceof AbstractDataForm) {
            try {
                return ((AbstractDataForm<?>) getView()).getLabel(fieldName);
            } catch (AbstractDataForm.FieldError ignored) {
            }
        }
        return getFieldCreator().getLabel(fieldName);
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
        return getFieldCreator().createField(fieldName, fieldType, label);
    }

    /**
     * Customize a field. This method is invoked when a field is created. One may do some customization here.
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void customizeField(String fieldName, HasValue<?, ?> field) {
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
    protected boolean handleValueSetError(String fieldName, HasValue<?, ?> field, Object fieldValue, Object objectValue, Throwable error) {
        return true;
    }

    @SuppressWarnings("unchecked")
    private Object getHost() {
        return ((FieldHandler)data.getFieldValueHandler()).host;
    }

    private class FieldHandler extends ValueHandler {

        private Object host;

        private void setHost(Object host) {
            this.host = host;
        }

        @Override
        public boolean isBasic() {
            return false;
        }

        @Override
        public boolean canHandle(String fieldName) {
            return getM.containsKey(fieldName) || getF.containsKey(fieldName);
        }

        @Override
        public boolean canSet(String fieldName) {
            return setM.containsKey(fieldName) || setF.containsKey(fieldName);
        }

        @Override
        public Object getValue(String fieldName) {
            Function<D, ?> get = getF.get(fieldName);
            if(get != null) {
                return get.apply(getObject());
            }
            try {
                Method m = getM.get(fieldName);
                if(m == null || m == dummyGET) {
                    return null;
                }
                return m.invoke(actOn(m));
            } catch (Throwable error) {
                Application a = Application.get();
                if(a != null) {
                    a.log("Field: " + fieldName, error);
                }
            }
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void setValue(String fieldName, Object value) {
            BiConsumer<D, ?> set = setF.get(fieldName);
            if(set != null) {
                try {
                    ((BiConsumer<D, Object>)set).accept(getObject(), value);
                } catch (Throwable e) {
                    handleValueSetError(fieldName, getField(fieldName), value, getValue(fieldName), e);
                }
                return;
            }
            Method m = setM.get(fieldName);
            if(m != null) {
                try {
                    m.invoke(actOn(m), value);
                } catch (Throwable e) {
                    handleValueSetError(fieldName, getField(fieldName), value, getValue(fieldName), e);
                }
            }
        }

        private Object actOn(Method m) {
            Class<?> dc = m.getDeclaringClass();
            if(dc == getObjectClass()) {
                return getObject();
            }
            if(dc == ObjectForm.this.getClass()) {
                return ObjectForm.this;
            }
            if(host != null && dc == host.getClass()) {
                return host;
            }
            if(dc.isAssignableFrom(getObjectClass())) {
                return getObject();
            }
            if(dc.isAssignableFrom(ObjectForm.this.getClass())) {
                return ObjectForm.this;
            }
            if(host != null && dc.isAssignableFrom(host.getClass())) {
                return host;
            }
            return null;
        }

        private void handleValueSetError(String fieldName, HasValue<?, ?> field, Object fieldValue, Object objectValue, Throwable error) {
            markError(field);
            if(ObjectForm.this.handleValueSetError(fieldName, field, fieldValue, objectValue, error)) {
                data.setExtraErrors();
            }
        }
    }
}