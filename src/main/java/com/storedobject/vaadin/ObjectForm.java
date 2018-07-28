package com.storedobject.vaadin;

import com.storedobject.vaadin.util.FieldValueHandler;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ObjectForm<T> extends Form {

    private final Class<T> objectClass;
    private final Map<String, Method> gets = new HashMap<>();
    private final Map<String, Method> sets = new HashMap<>();
    private T objectData;

    public ObjectForm(Class<T> objectClass) {
        this(objectClass, null);
    }

    public ObjectForm(Class<T> objectClass, HasComponents container) {
        super(container);
        this.objectClass = objectClass;
        data.setFieldValueHandler(new FieldHandler());
    }

    public Class<T> getObjectClass() {
        return objectClass;
    }

    public Stream<String> getFieldNames() {
        String name;
        for(Method m: objectClass.getMethods()) {
            name = m.getName();
            if(name.equals("getClass")) {
                continue;
            }
            if(name.startsWith("get")) {
                name = name.substring(3);
            } else if(name.startsWith("is")) {
                name = name.substring(2);
            } else {
                continue;
            }
            if(includeField(name)) {
                gets.put(name, m);
            }
        }
        for(String fieldName: gets.keySet()) {
            try {
                sets.put(fieldName, objectClass.getMethod("set" + fieldName, new Class[] { gets.get(fieldName).getReturnType() }));
            } catch (NoSuchMethodException e) {
            }
        }
        return gets.keySet().stream();
    }

    protected boolean includeField(String fieldName) {
        return true;
    }

    @Override
    protected HasValue<?, ?> createField(String fieldName) {
        Method m = gets.get(fieldName);
        if(m == null) {
            return null;
        }
        String label = getLabel(fieldName);
        Class<?> type = m.getReturnType();
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

    protected T createObjectInstance() {
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
        this.objectData = object;
    }

    private class FieldHandler extends ValueHandler {

        @Override
        public boolean isBasic() {
            return false;
        }

        @Override
        public boolean canHandle(String fieldName) {
            return gets.containsKey(fieldName);
        }

        @Override
        public boolean canSet(String fieldName) {
            return sets.containsKey(fieldName);
        }

        @Override
        public Object getValue(String fieldName) {
            try {
                return gets.get(fieldName).invoke(getObject());
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            return null;
        }

        @Override
        public void setValue(String fieldName, Object value) {
            Method m = sets.get(fieldName);
            if(m != null) {
                try {
                    m.invoke(getObject(), new Object[] { value });
                } catch (Throwable e) {
                    if(value != null) {
                        getField(fieldName).setReadOnly(true);
                    }
                }
            }
        }
    }
}