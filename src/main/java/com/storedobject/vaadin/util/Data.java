package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Form;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Data extends HashMap<String, Object> {

    private static ValidationResult OK = ValidationResult.ok();
    private static HasText errorText = new HasText() {
        @Override
        public Element getElement() {
            return null;
        }

        @Override
        public void setText(String text) {
            if(text == null || text.isEmpty()) {
                return;
            }
            Notification.show(text);
        }
    };
    private static long id = 0L;
    private FieldValueHandler valueHandler;
    private final Map<String, HasValue<?, ?>> fields= new HashMap<>();
    private final Map<HasValue<?, ?>, String>fieldNames = new HashMap<>();
    private final Map<HasValue<?, ?>, DataValidators> validators = new HashMap<>();
    private Binder<Data> binder;
    private boolean readOnly;

    public Data() {
        binder = new Binder<>();
        binder.setStatusLabel(errorText);
    }

    public void setFieldValueHandler(FieldValueHandler valueHandler) {
        this.valueHandler = valueHandler;
    }

    public FieldValueHandler getFieldValueHandler() {
        return valueHandler;
    }

    public String addField(HasValue<?, ?> field) {
        return addField(null, field, null, null);
    }

    public String addField(String fieldName, HasValue<?, ?> field, Supplier<?> get, Consumer<?> set) {
        if(field == null) {
            return null;
        }
        if(fieldName == null || fieldName.isEmpty()) {
            if(++id == Long.MAX_VALUE) {
                id = 1L;
            }
            fieldName = "_" + id;
        }
        if(fields.containsKey(fieldName)) {
            throw new RuntimeException("There is already a field with Id = " + fieldName);
        }
        fields.put(fieldName, field);
        fieldNames.put(field, fieldName);
        DataValidators validator = new DataValidators(field);
        validators.put(field, validator);
        final String name = fieldName;
        final HasValue<?, Object> f = (HasValue<?, Object>)field;
        binder.withValidator(validator);
        if(get != null) {
            if(set != null) {
                binder.bind(f, x -> get.get(), (d, v) -> ((Consumer<Object>)set).accept(v));
            } else {
                if(!valueHandler.isBasic() && valueHandler.canHandle(name) && valueHandler.canSet(name)) {
                    binder.bind(f, x -> get.get(), (d, v) -> valueHandler.setValue(name, v));
                } else {
                    binder.bind(f, x -> get.get(), null);
                }
            }
        } else {
            if (valueHandler.isBasic() || !valueHandler.canHandle(name)) {
                setValue(fieldName, field.getValue());
                binder.bind(f, x -> getValue(name), (d, v) -> setValue(name, v));
            } else {
                if (valueHandler.canSet(name)) {
                    binder.bind(f, x -> valueHandler.getValue(name), (d, v) -> valueHandler.setValue(name, v));
                } else {
                    binder.bind(f, x -> valueHandler.getValue(name), null);
                }
            }
        }
        return fieldName;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        if(readOnly) {
            fields.entrySet().forEach(s -> {
                s.getValue().setReadOnly(readOnly);
                setVisible(s.getKey(), s.getValue());
            });
            return;
        }
        fields.entrySet().forEach(s -> {
            boolean ro = false;
            if(valueHandler.canHandle(s.getKey())) {
                ro = !valueHandler.canSet(s.getKey());
            }
            if(!ro) {
                ro = !valueHandler.isEditable(s.getValue());
            }
            if(!ro) {
                ro = !valueHandler.isEditable(s.getKey());
            }
            s.getValue().setReadOnly(ro);
        });
    }

    private void setVisible(String fieldName, HasValue<?, ?> field) {
        if(!(field instanceof Component)) {
            return;
        }
        Component c = (Component)field;
        boolean v = valueHandler.isVisible(field);
        if(v) {
            v = valueHandler.isVisible(fieldName);
        }
        c.setVisible(v);
    }

    private Object getValue(String fieldName) {
        return get(fieldName);
    }

    private void setValue(String fieldName, Object value) {
        put(fieldName, value);
    }

    public HasValue<?, ?> removeField(String fieldName) {
        if(fieldName == null || fieldName.isEmpty()) {
            return null;
        }
        HasValue<?, ?> field = fields.remove(fieldName);
        if(field == null) {
            return null;
        }
        binder.removeBinding(field);
        fieldNames.remove(field);
        validators.remove(field);
        remove(fieldName);
        return field;
    }

    public String getName(HasValue<?, ?> field) {
        return fieldNames.get(field);
    }

    public HasValue<?, ?> getField(String fieldName) {
        return fields.get(fieldName);
    }

    public void loadValues() {
        binder.readBean(this);
        setReadOnly(!readOnly);
        setReadOnly(!readOnly);
    }

    public boolean saveValues() {
        if(!binder.writeBeanIfValid(this)) {
            return false;
        }
        if(!valueHandler.isBasic()) {
            fields.entrySet().forEach(s -> {
                if (valueHandler.canHandle(s.getKey()) && !valueHandler.canSet(s.getKey())) {
                    HasValue<?, Object> field = (HasValue<?, Object>) s.getValue();
                    field.setValue(valueHandler.getValue(s.getKey()));
                }
            });
        }
        setReadOnly(!readOnly);
        setReadOnly(!readOnly);
        return true;
    }

    public void clearErrors() {
        fields.values().forEach(hv -> Form.clearError(hv));
    }

    public void clearFields() {
        fields.values().forEach(hv -> hv.clear());
    }

    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator, String errorMessage) {
        if(field == null) {
            return;
        }
        DataValidators dv = validators.get(field);
        if(dv == null) {
            return;
        }
        dv.add(new DataValidator<T>(validator, errorMessage));
    }

    public void setRequired(HasValue<?, ?> field, boolean required, String errorMessage) {
        if(field == null) {
            return;
        }
        field.setRequiredIndicatorVisible(required);
        DataValidators dv = validators.get(field);
        if(dv == null) {
            return;
        }
        if(!required) {
            if(dv.isEmpty()) {
                return;
            }
            if(dv.get(0) instanceof Required) {
                dv.remove(0);
            }
            return;
        }
        if(!dv.isEmpty()) {
            Validator<Data> v = dv.get(0);
            if(v instanceof Required) {
                if(errorMessage == null || errorMessage.isEmpty()) {
                    if(((Required) v).errorMessage == null) {
                        return;
                    }
                } else {
                    if(errorMessage.equals(((Required) v).errorMessage)) {
                        return;
                    }
                }
                dv.remove(0);
            }
        }
        dv.add(0, errorMessage == null || errorMessage.isEmpty() ? Required.REQUIRED : new Required(errorMessage));
    }

    private static class Required implements Validator<Data> {

        private static final String CAN_NOT_BE_EMPTY = "Can not be empty!";
        private static final Required REQUIRED = new Required();
        private String errorMessage;

        private Required() {
            this(null);
        }

        private Required(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public ValidationResult apply(Data data, ValueContext valueContext) {
            HasValue<?, ?> field = valueContext.getHasValue().get();
            if(!field.isEmpty()) {
                return OK;
            }
            String m = errorMessage;
            if(m == null) {
                m = CAN_NOT_BE_EMPTY;
                String id = data.getName(field);
                if(!id.startsWith("_")) {
                    m = id + ": " + m;
                }
            }
            return ValidationResult.error(m);
        }
    }

    private static class DataValidator<T> implements Validator<Data> {

        private static final String INVALID = "Not valid";
        private Function<T, Boolean> validator;
        private String errorMessage;

        private DataValidator(Function<T, Boolean> validator, String errorMessage) {
            this.validator = validator;
            if(errorMessage != null && !errorMessage.isEmpty()) {
                this.errorMessage = errorMessage;
            }
        }

        @Override
        public ValidationResult apply(Data data, ValueContext valueContext) {
            HasValue<?, T> field = (HasValue<?, T>)valueContext.getHasValue().get();
            if(validator.apply(field.getValue())) {
                return OK;
            }
            String m = errorMessage;
            if(m == null) {
                m = INVALID;
                String id = data.getName(field);
                if(!id.startsWith("_")) {
                    m = id + ": " + m;
                }
            }
            return ValidationResult.error(errorMessage == null ? INVALID : errorMessage);
        }
    }

    private static class DataValidators extends ArrayList<Validator<Data>> implements Validator<Data> {

        private ValueContext valueContext;

        private DataValidators(HasValue<?, ?> field) {
            valueContext = new ValueContext((Component)field, field);
        }

        @Override
        public ValidationResult apply(Data data, ValueContext valueContext) {
            ValidationResult vr = OK;
            for(Validator<Data> v: this) {
                vr = v.apply(data, this.valueContext);
                if(vr.getErrorLevel().isPresent()) {
                    break;
                }
            }
            HasValue<?, ?> field = this.valueContext.getHasValue().get();
            if(vr.getErrorLevel().isPresent()) {
                Form.markError(field);
            } else {
                Form.clearError(field);
            }
            return vr;
        }
    }
}