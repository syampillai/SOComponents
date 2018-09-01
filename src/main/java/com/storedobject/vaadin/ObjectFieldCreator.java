package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public interface ObjectFieldCreator {

    default ObjectFieldCreator create(ObjectForm<?> objectForm) {
        return this;
    }

    default Stream<Method> getFieldGetMethods() {
        return null;
    }

    HasValue<?, ?> createField(String fieldName, Class<?> fieldType, String label);

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
        return Form.createLabel(fieldName);
    }

    default void close() {
    }
}