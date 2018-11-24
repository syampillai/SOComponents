package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class ObjectForm<T> extends Form {

    private final Class<T> objectClass;
    private final Map<String, Method> getM = new HashMap<>();
    private final Map<String, Method> setM = new HashMap<>();
    private final Map<String, Function<T, ?>> getF = new HashMap<>();
    private final Map<String, BiConsumer<T, ?>> setF = new HashMap<>();
    private T objectData;
    private ObjectFieldCreator<T> fCreator;

    public ObjectForm(Class<T> objectClass) {
        this(objectClass, null);
    }

    public ObjectForm(Class<T> objectClass, HasComponents container) {
        super(container);
        this.objectClass = objectClass;
        data.setFieldValueHandler(new FieldHandler());
    }

    @Override
    protected void constructed() {
        if(fCreator != null) {
            fCreator.close();
            fCreator = null;
        }
    }

    @SuppressWarnings("unchecked")
    ObjectFieldCreator<T> getFieldCreator() {
        if(fCreator == null) {
            fCreator = ((ObjectFieldCreator<T>) Objects.requireNonNull(ApplicationEnvironment.get()).getObjectFieldCreator()).create(this);
        }
        return fCreator;
    }

    @SuppressWarnings("unchecked")
    public void setMethodHandlerHost(Object host) {
        ((FieldHandler)data.getFieldValueHandler()).setHost(host);
    }

    public Class<T> getObjectClass() {
        return objectClass;
    }

    protected void addField(Iterable<String> fieldNames) {
        fieldNames.forEach(this::addField);
    }

    protected void addField(String... fieldNames) {
        Function<T, ?> valueGetter;
        BiConsumer<T, ?> valueSetter;
        for(String fieldName: fieldNames) {
            valueGetter = getFieldCreator().getValueGetter(fieldName);
            if(valueGetter != null) {
                getF.put(fieldName, valueGetter);
            }
            valueSetter = getFieldCreator().getValueSetter(fieldName);
            if(valueSetter != null) {
                setF.put(fieldName, valueSetter);
            }
            if(valueGetter == null && valueSetter == null) {
                addField(fieldName, null, (Method) null);
            }
        }
    }

    protected void addField(String fieldName, Function<T, ?> valueGetter) {
        addField(fieldName, valueGetter, null);
    }

    protected void addField(String fieldName, Function<T, ?> valueGetter, BiConsumer<T, ?> valueSetter) {
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

    protected void addField(String fieldName, Method getMethod) {
        addField(fieldName, getMethod, null);
    }

    protected void addField(String fieldName, Method getMethod, Method setMethod) {
        if(getM.containsKey(fieldName) || getF.containsKey(fieldName)) {
            return;
        }
        if(getMethod == null) {
            getMethod = getFieldGetMethod(fieldName);
        }
        if(getMethod == null) {
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

    protected Method getFieldSetMethod(String fieldName, Method getMethod) {
        Class[] params = new Class[] { getMethod.getReturnType() };
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

    @Override
    protected void generateFieldNames() {
        getFieldGetMethods().forEach(m -> {
            String name = getFieldCreator().getFieldName(m);
            if(name != null && includeField(name)) {
                addField(name, m, null);
            }
        });
        Stream<String> additionalNames = getFieldCreator().getFieldNames();
        if(additionalNames != null) {
            additionalNames.filter(Objects::nonNull).filter(this::includeField).forEach(this::addField);
        }
    }

    @SuppressWarnings("unchecked")
    private Stream<Method> getFieldGetMethods() {
        Stream<Method> stream = getFieldCreator().getFieldGetMethods();
        return stream == null ? Arrays.stream(objectClass.getMethods()) : stream;
    }

    protected final Stream<String> getFieldNames() {
        return Stream.concat(getM.keySet().stream(), getF.keySet().stream()).filter(n -> n != null && !n.isEmpty()).
                filter(this::includeField).sorted(Comparator.comparingInt(this::getFieldOrder));
    }

    protected int getFieldOrder(String fieldName) {
        return getFieldCreator().getFieldOrder(fieldName);
    }

    protected boolean includeField(String fieldName) {
        return true;
    }

    @Override
    protected final HasValue<?, ?> createField(String fieldName) {
        Class<?> returnType = null;
        Method m = getM.get(fieldName);
        if(m != null) {
            returnType = m.getReturnType();
        }
        HasValue<?, ?> field = createField(fieldName, returnType, getLabel(fieldName));
        if(field != null) {
            customizeField(fieldName, field);
        }
        return field;
    }

    @Override
    public String getLabel(String fieldName) {
        return getFieldCreator().getLabel(fieldName);
    }

    @SuppressWarnings("unchecked")
    protected HasValue<?, ?> createField(String fieldName, Class<?> fieldType, String label) {
        return getFieldCreator().createField(fieldName, fieldType, label);
    }

    protected void customizeField(String fieldName, HasValue<?, ?> field) {
    }

    protected T createObjectInstance() {
        try {
            return objectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        return null;
    }

    public T getObject() {
        return getObject(true);
    }

    public T getObject(boolean create) {
        if(objectData == null && create) {
            objectData = createObjectInstance();
        }
        return objectData;
    }

    public void setObject(T object) {
        setObject(object, true);
    }

    public void setObject(T object, boolean load) {
        objectData = object;
        if(load) {
            if(object == null) {
                clearFields();
            } else {
                load();
            }
        }
    }

    protected boolean handleValueSetError(String fieldName, HasValue<?, ?> field, Object fieldValue, Object objectValue, Throwable error) {
        return true;
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
            Function<T, ?> get = getF.get(fieldName);
            if(get != null) {
                return get.apply(getObject());
            }
            try {
                Method m = getM.get(fieldName);
                return m.invoke(actOn(m));
            } catch (NullPointerException | IllegalAccessException | InvocationTargetException ignored) {
            }
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void setValue(String fieldName, Object value) {
            BiConsumer<T, ?> set = setF.get(fieldName);
            if(set != null) {
                try {
                    ((BiConsumer<T, Object>)set).accept(getObject(), value);
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