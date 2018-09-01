package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public abstract class AbstractDataEditor<T> extends AbstractDataForm {

    public AbstractDataEditor(Application a, Class<T> objectClass) {
        this(a, objectClass, null);
    }

    public AbstractDataEditor(Application a, Class<T> objectClass, String caption) {
        super(a);
        this.form = new DForm(objectClass);
        setCaption(caption == null || caption.isEmpty() ? label(getObjectClass()) : caption);
    }

    private static String label(Class<?> className) {
        String s = className.getName();
        if(s.contains(".")) {
            s = s.substring(s.lastIndexOf('.') + 1);
        }
        if(s.contains("$")) {
            s = s.substring(s.lastIndexOf('$') + 1);
        }
        return Form.createLabel(s);
    }

    @Override
    public ObjectForm<T> getForm() {
        return (ObjectForm<T>) super.getForm();
    }

    public Class<T> getObjectClass() {
        return ((ObjectForm<T>)form).getObjectClass();
    }

    public Stream<String> getFieldNames() {
        throw FIELD_ERROR;
    }

    protected Method getFieldGetMethod(String fieldName) {
        throw FIELD_ERROR;
    }

    protected Method getFieldSetMethod(String fieldName, Method getMethod) {
        throw FIELD_ERROR;
    }

    protected boolean includeField(String fieldName) {
        throw FIELD_ERROR;
    }

    protected void customizeField(String filedName, HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    protected HasValue<?, ?> createField(String fieldName, Class<?> type, String label) {
        throw FIELD_ERROR;
    }

    protected String getLabel(String fieldName) {
        throw FIELD_ERROR;
    }

    protected int getFieldOrder(String fieldName) {
        throw FIELD_ERROR;
    }

    protected T createObjectInstance() {
        throw FIELD_ERROR;
    }

    public T getObject() {
        return ((ObjectForm<T>)form).getObject();
    }

    public void setObject(T object) {
        ((ObjectForm<T>)form).setObject(object);
    }

    public void setObject(T object, boolean load) {
        ((ObjectForm<T>)form).setObject(object, load);
    }

    protected class DForm extends ObjectForm<T> {

        public DForm(Class<T> objectClass) {
            super(objectClass);
            setMethodHandlerHost(AbstractDataEditor.this);
        }

        @Override
        protected HasComponents createContainer() {
            return AbstractDataEditor.this.createContainer();
        }

        @Override
        protected void generateFieldNames() {
            try {
                AbstractDataEditor.this.getFieldNames().filter(n -> includeField(n)).forEach(n -> addField(n, getFieldGetMethod(n), null));
            } catch (FieldError e) {
                super.generateFieldNames();
            }
        }

        @Override
        protected Method getFieldGetMethod(String fieldName) {
            try {
                return AbstractDataEditor.this.getFieldGetMethod(fieldName);
            } catch (FieldError e) {
            }
            try {
                return AbstractDataEditor.this.getClass().getMethod("get" + fieldName);
            } catch (NoSuchMethodException e) {
            }
            try {
                return AbstractDataEditor.this.getClass().getMethod("is" + fieldName);
            } catch (NoSuchMethodException e) {
            }
            return super.getFieldGetMethod(fieldName);
        }

        @Override
        protected Method getFieldSetMethod(String fieldName, Method getMethod) {
            try {
                return AbstractDataEditor.this.getFieldSetMethod(fieldName, getMethod);
            } catch (FieldError e) {
            }
            Class[] params = new Class[] { getMethod.getReturnType() };
            try {
                return AbstractDataEditor.this.getClass().getMethod("set" + fieldName, params);
            } catch (NoSuchMethodException e) {
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
            try {
                return AbstractDataEditor.this.createObjectInstance();
            } catch (FieldError e) {
                return super.createObjectInstance();
            }
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
    }
}
