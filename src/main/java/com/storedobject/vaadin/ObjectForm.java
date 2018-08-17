package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ObjectForm<T> extends Form {

    private final Class<T> objectClass;
    private final Map<String, Method> getM = new HashMap<>();
    private final Map<String, Method> setM = new HashMap<>();
    private final Map<String, Supplier<?>> getF = new HashMap<>();
    private final Map<String, Consumer<?>> setF = new HashMap<>();
    private T objectData;

    public ObjectForm(Class<T> objectClass) {
        this(objectClass, null);
    }

    public ObjectForm(Class<T> objectClass, HasComponents container) {
        super(container);
        this.objectClass = objectClass;
        data.setFieldValueHandler(new FieldHandler());
    }

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
        for(String fieldName: fieldNames) {
            addField(fieldName, (Method)null, (Method)null);
        }
    }

    protected void addField(String fieldName, Supplier<?> valueGetter) {
        addField(fieldName, valueGetter, null);
    }

    protected void addField(String fieldName, Supplier<?> valueGetter, Consumer<?> valueSetter) {
        if(getM.containsKey(fieldName) || getF.containsKey(fieldName)) {
            return;
        }
        if(valueGetter == null && valueSetter == null) {
            addField(fieldName, (Method)null, (Method)null);
            return;
        }
        Method m = null;
        if(valueGetter == null) {
            m = getFieldGetMethod(fieldName);
            if(m != null) {
                getM.put(fieldName, m);
            } else {
                return;
            }
        } else {
            getF.put(fieldName, valueGetter);
        }
        if(valueSetter != null) {
            setF.put(fieldName, valueSetter);
        } else if(m != null) {
            m = getFieldSetMethod(fieldName, m);
            if(m != null) {
                setM.put(fieldName, m);
            }
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
        } catch (NoSuchMethodException e) {
        }
        try {
            return this.getClass().getMethod("is" + fieldName);
        } catch (NoSuchMethodException e) {
        }
        try {
            return objectClass.getMethod("get" + fieldName);
        } catch (NoSuchMethodException e) {
        }
        try {
            return objectClass.getMethod("is" + fieldName);
        } catch (NoSuchMethodException e) {
        }
        return null;
    }

    protected Method getFieldSetMethod(String fieldName, Method getMethod) {
        Class[] params = new Class[] { getMethod.getReturnType() };
        try {
            return this.getClass().getMethod("set" + fieldName, params);
        } catch (NoSuchMethodException e) {
        }
        try {
            return objectClass.getMethod("set" + fieldName, params);
        } catch (NoSuchMethodException e) {
        }
        return null;
    }

    @Override
    protected void generateFieldNames() {
        getFieldGetMethods().forEach(m -> {
            String name = getFieldName(m);
            if(name != null && includeField(name)) {
                addField(name, m, null);
            }
        });
    }

    protected String getFieldName(Method getMethod) {
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

    protected Stream<Method> getFieldGetMethods() {
        return Arrays.stream(objectClass.getMethods());
    }

    protected final Stream<String> getFieldNames() {
        return Stream.concat(getM.keySet().stream(), getF.keySet().stream()).filter(n -> n != null && !n.isEmpty()).
                filter(this::includeField).sorted(Comparator.comparingInt(this::getFieldOrder));
    }

    public int getFieldOrder(String fieldName) {
        return Integer.MAX_VALUE;
    }

    public boolean includeField(String fieldName) {
        return true;
    }

    @Override
    protected final HasValue<?, ?> createField(String fieldName) {
        Method m = getM.get(fieldName);
        if(m == null) {
            return null;
        }
        return createField(fieldName, m.getReturnType(), getLabel(fieldName));
    }

    protected HasValue<?, ?> createField(String fieldName, Class<?> type, String label) {
        if(type == boolean.class || type == Boolean.class) {
            return new BooleanField(label);
        }
        if(type == String.class) {
            return new TextField(label);
        }
        if(type == int.class || type == Integer.class) {
            return new IntegerField(label);
        }
        return null;
    }

    public String getLabel(String fieldName) {
        StringBuilder label = new StringBuilder();
        label.append(fieldName.charAt(0));
        char c;
        for(int i = 1; i < fieldName.length(); i++) {
            c = fieldName.charAt(i);
            if(Character.isUpperCase(c)) {
                label.append(' ');
            }
            label.append(c);
        }
        return label.toString();
    }

    public T createObjectInstance() {
        try {
            return objectClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public T getObject() {
        if(objectData == null) {
            objectData = createObjectInstance();
        }
        return objectData;
    }

    public void setObject(T object) {
        setObject(object, true);
    }

    public void setObject(T object, boolean load) {
        if (object != objectData) {
            objectData = object;
        }
        if(load) {
            load();
        }
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
            return getM.containsKey(fieldName);
        }

        @Override
        public boolean canSet(String fieldName) {
            return setM.containsKey(fieldName);
        }

        @Override
        public Object getValue(String fieldName) {
            Supplier<?> get = getF.get(fieldName);
            if(get != null) {
                return get.get();
            }
            try {
                Method m = getM.get(fieldName);
                return m.invoke(actOn(m));
            } catch (NullPointerException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            return null;
        }

        @Override
        public void setValue(String fieldName, Object value) {
            Consumer<?> set = setF.get(fieldName);
            if(set != null) {
                try {
                    ((Consumer<Object>)set).accept(value);
                } catch (Throwable e) {
                    if(value != null) {
                        getField(fieldName).setReadOnly(true);
                    }
                }
                return;
            }
            Method m = setM.get(fieldName);
            if(m != null) {
                try {
                    m.invoke(actOn(m), new Object[] { value });
                } catch (Throwable e) {
                    if(value != null) {
                        getField(fieldName).setReadOnly(true);
                    }
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
            return host;
        }
    }
}