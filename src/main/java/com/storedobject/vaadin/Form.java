package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Data;
import com.storedobject.vaadin.util.FieldValueHandler;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.dom.Element;

import java.util.function.Function;
import java.util.stream.Stream;

public class Form {

    protected HasComponents container;
    protected final Data data;
    private boolean loadPending = false;

    public Form() {
        this(null);
    }

    public Form(HasComponents container) {
        this(container, null);
        data.setFieldValueHandler(new ValueHandler());
    }

    protected Form(HasComponents container, @SuppressWarnings("unused") Object dummy) {
        this.data = new Data(this);
        this.container = container;
    }

    private static HasComponents createDefaultContainer() {
        return new FormLayout();
    }

    protected HasComponents createContainer() {
        return createDefaultContainer();
    }

    protected void generateFieldNames() {}

    public final HasComponents getContainer() {
        if(container == null) {
            container = createContainer();
            if (container == null) {
                container = createDefaultContainer();
            }
            generateFieldNames();
            Stream<String> fieldNames = getFieldNames();
            if (fieldNames != null) {
                fieldNames.forEach(n -> {
                    HasValue<?, ?> field = createField(n);
                    if(n != null) {
                        addField(n, field);
                    }
                });
            }
            constructed();
            if(loadPending) {
                load();
            }
        }
        return container;
    }

    protected void constructed() {
    }

    private void addFieldInt(String fieldName, HasValue<?, ?> field) {
        data.addField(fieldName, field, null, null);
        attachF(fieldName, field);
    }

    public final Component getComponent() {
        getContainer();
        return container instanceof Component ? (Component)container : null;
    }

    protected Stream<String> getFieldNames() {
        return null;
    }

    protected HasValue<?, ?> createField(String fieldName) {
        return null;
    }

    public void addField(HasValue<?, ?> field) {
        attachF(data.addField(field), field);
    }

    public void addField(String fieldName, HasValue<?, ?> field) {
        addFieldInt(fieldName, field);
    }

    public void removeField(String fieldName) {
        detachF(fieldName, data.removeField(fieldName));
    }

    public void removeField(String... fieldNames) {
        for(String id: fieldNames) {
            removeField(id);
        }
    }

    public void removeField(HasValue<?, ?> field) {
        String fieldName = data.getName(field);
        if(fieldName != null) {
            data.removeField(fieldName);
            detachF(fieldName, field);
        }
    }

    private void attachF(String fieldName, HasValue<?, ?> field) {
        if(fieldName != null && field != null) {
            attachField(fieldName, field);
        }
    }

    private void detachF(String fieldName, HasValue<?, ?> field) {
        if(fieldName != null && field != null) {
            detachField(fieldName, field);
        }
    }

    protected void attachField(String fieldName, HasValue<?, ?> field) {
        if(field instanceof Component) {
            getContainer().add((Component)field);
        }
    }

    protected void detachField(String fieldName, HasValue<?, ?> field) {
        if(field instanceof Component) {
            getContainer().remove((Component)field);
        }
    }

    public void add(Component... components) {
        getContainer().add(components);
    }

    public void remove(Component... components) {
        getContainer().remove(components);
    }

    public void removeAll() {
        getContainer().removeAll();
    }

    public void load() {
        if(container == null) {
            loadPending = true;
            return;
        }
        data.loadValues();
    }

    public boolean commit() {
        return data.saveValues();
    }

    public HasValue<?, ?> getField(String fieldName) {
        return data.getField(fieldName);
    }

    public void setRequired(HasValue<?, ?> field) {
        setRequired(field, true, null);
    }

    public void setRequired(String fieldName) {
        setRequired(fieldName, true, null);
    }

    public void setRequired(HasValue<?, ?> field, String errorMessage) {
        setRequired(field, true, errorMessage);
    }

    public void setRequired(String fieldName, String errorMessage) {
        setRequired(fieldName, true, errorMessage);
    }

    public void setRequired(HasValue<?, ?> field, boolean required) {
        setRequired(field, required, null);
    }

    public void setRequired(String fieldName, boolean required) {
        setRequired(fieldName, required, null);
    }

    public void setRequired(HasValue<?, ?> field, boolean required, String errorMessage) {
        data.setRequired(field, required, errorMessage);
    }

    public void setRequired(String fieldName, boolean required, String errorMessage) {
        setRequired(getField(fieldName), required, errorMessage);
    }

    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator) {
        addValidator(field, validator, null);
    }

    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator, String errorMessage) {
        data.addValidator(field, validator, errorMessage);
    }

    public static void markError(HasValue<?, ?> field) {
        setInvalid(field, true);
    }

    public static void clearError(HasValue<?, ?> field) {
        setInvalid(field, false);
    }

    private static void setInvalid(HasValue<?, ?> field, boolean invalid) {
        if(field instanceof Element) {
            ((Element) field).setAttribute("invalid", invalid);
        }
    }

    public void clearFields() {
        data.clearFields();
    }

    public void clearErrors() {
        data.clearErrors();
    }

    public void setErrorDisplay(HasText display) {
        data.setErrorDisplay(display);
    }

    public void setReadOnly(boolean readOnly) {
        data.setReadOnly(readOnly);
    }

    public boolean isFieldVisible(String fieldName) {
        return true;
    }

    public boolean isFieldVisible(HasValue<?, ?> field) {
        return true;
    }

    public boolean isFieldEditable(String fieldName) {
        return true;
    }

    public boolean isFieldEditable(HasValue<?, ?> field) {
        return true;
    }

    public String getLabel(String fieldName) {
        return ApplicationEnvironment.get().createLabel(fieldName);
    }

    protected class ValueHandler implements FieldValueHandler {

        @Override
        public boolean isBasic() {
            return true;
        }

        @Override
        public boolean canHandle(String fieldName) {
            return false;
        }

        @Override
        public boolean canSet(String fieldName) {
            return false;
        }

        @Override
        public Object getValue(String fieldName) {
            return null;
        }

        @Override
        public void setValue(String fieldName, Object value) {
        }

        @Override
        public boolean isVisible(String fieldName) {
            return isFieldVisible(fieldName);
        }

        @Override
        public boolean isVisible(HasValue<?, ?> field) {
            return isFieldVisible(field);
        }

        @Override
        public boolean isEditable(String fieldName) {
            return isFieldEditable(fieldName);
        }

        @Override
        public boolean isEditable(HasValue<?, ?> field) {
            return isFieldEditable(field);
        }
    }
}