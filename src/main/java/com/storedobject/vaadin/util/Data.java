package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Alert;
import com.storedobject.vaadin.Form;
import com.storedobject.vaadin.ValueRequired;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Data extends HashMap<String, Object> {

    private static ValidationResult OK = ValidationResult.ok();
    private static Alert errorText = new Alert(null);
    private static long id = 0L;
    private FieldValueHandler valueHandler;
    private final Map<String, HasValue<?, ?>> fields= new HashMap<>();
    private final Map<HasValue<?, ?>, String> fieldNames = new HashMap<>();
    private final Map<HasValue<?, ?>, DataValidators> validators = new HashMap<>();
    private final List<HasValue<?, ?>> readOnlyFields = new ArrayList<>();
    private Binder<Data> binder;
    private boolean readOnly;
    private final Form form;
    private Required requiredCache;
    private boolean extraErrors = false;

    public Data(Form form) {
        this.form = form;
        binder = new Binder<>();
        binder.setStatusLabel(errorText);
    }

    public void setErrorDisplay(HasText display) {
        binder.setStatusLabel(display == null ? errorText : display);
    }

    public void setExtraErrors() {
        this.extraErrors = true;
    }

    public void setFieldValueHandler(FieldValueHandler valueHandler) {
        this.valueHandler = valueHandler;
    }

    public FieldValueHandler getFieldValueHandler() {
        return valueHandler;
    }

    public String addField(HasValue<?, ?> field) {
        return addField(null, field);
    }

    @SuppressWarnings("unchecked")
    public String addField(String fieldName, HasValue<?, ?> field) {
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
        DataValidators validator = validator(field);
        final String name = fieldName;
        final HasValue<?, Object> f = (HasValue<?, Object>)field;
        binder.withValidator(validator);
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
        if(field instanceof ValueRequired && ((ValueRequired)field).isRequired() && valueHandler.canSet(fieldName)) {
            setRequired(field, true, null);
        }
        return fieldName;
    }

    private DataValidators validator(HasValue<?, ?> field) {
        DataValidators dv = validators.get(field);
        if(dv == null) {
            dv = new DataValidators(field);
            validators.put(field, dv);
        }
        return dv;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        boolean collectROs = readOnly && (this.readOnly != readOnly);
        if(collectROs) {
            readOnlyFields.clear();
        }
        this.readOnly = readOnly;
        if(readOnly) {
            fields.forEach((key, value) -> {
                if(collectROs && value.isReadOnly()) {
                    readOnlyFields.add(value);
                } else {
                    value.setReadOnly(true);
                }
                setVisible(key, value);
            });
            return;
        }
        fields.forEach((key, value) -> {
            boolean ro = readOnlyFields.contains(value);
            if (!ro && valueHandler.canHandle(key)) {
                ro = !valueHandler.canSet(key);
            }
            if (!ro) {
                ro = !valueHandler.isEditable(value);
            }
            if (!ro) {
                ro = !valueHandler.isEditable(key);
            }
            value.setReadOnly(ro);
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

    public int getFieldCount() {
        return fields.size();
    }

    public Stream<String> getFieldNames() {
        return fields.keySet().stream();
    }

    public Stream<HasValue<?, ?>> getFields() {
        return fieldNames.keySet().stream();
    }

    public void loadValues() {
        binder.readBean(this);
        setReadOnly(!readOnly);
        setReadOnly(!readOnly);
    }

    @SuppressWarnings("unchecked")
    public boolean saveValues() {
        if(errorText.equals(binder.getStatusLabel().orElse(null))) {
            errorText.close();
        }
        extraErrors = false;
        if(!binder.writeBeanIfValid(this)) {
            return false;
        }
        if(!valueHandler.isBasic()) {
            fields.forEach((key, value) -> {
                if (valueHandler.canHandle(key) && !valueHandler.canSet(key)) {
                    HasValue<?, Object> field = (HasValue<?, Object>) value;
                    field.setValue(valueHandler.getValue(key));
                }
            });
        }
        if(extraErrors) {
            return false;
        }
        setReadOnly(!readOnly);
        setReadOnly(!readOnly);
        return true;
    }

    public void clearErrors() {
        fields.values().forEach(Form::clearError);
        if(errorText.equals(binder.getStatusLabel().orElse(null))) {
            errorText.close();
        }
    }

    public void clearFields() {
        fields.values().forEach(HasValue::clear);
    }

    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator, String errorMessage) {
        if(field == null) {
            return;
        }
        validator(field).add(new DataValidator<>(form, validator, errorMessage));
    }

    public void setRequired(HasValue<?, ?> field, boolean required, String errorMessage) {
        if(field == null) {
            return;
        }
        Object value = field.getValue();
        if(value != null) {
            Class<?> valueClass = value.getClass();
            if(valueClass == boolean.class || valueClass == Boolean.class) {
                return;
            }
        }
        field.setRequiredIndicatorVisible(required);
        DataValidators dv = validator(field);
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
        dv.add(0, errorMessage == null || errorMessage.isEmpty() ? requiredCache() : new Required(form, this, errorMessage));
    }

    private Required requiredCache() {
        if(requiredCache == null) {
            requiredCache = new Required(form, this,null);
        }
        return requiredCache;
    }

    private static class Required implements Validator<Data> {

        private static final String CAN_NOT_BE_EMPTY = "Can not be empty";
        private String errorMessage;
        private Form form;
        private Data data;

        private Required(Form form, Data data, String errorMessage) {
            this.form = form;
            this.data = data;
            if(errorMessage != null && !errorMessage.isEmpty()) {
                this.errorMessage = errorMessage;
            }
        }

        @Override
        public ValidationResult apply(Data data, ValueContext valueContext) {
            HasValue<?, ?> field = valueContext.getHasValue().orElse(null);
            if(field == null || !field.isEmpty()) {
                return OK;
            }
            String fieldName = data.getName(field);
            if(fieldName != null && data.valueHandler.canHandle(fieldName) && !data.valueHandler.canSet(fieldName)) {
                return OK;
            }
            String m = errorMessage;
            if(m == null) {
                m = CAN_NOT_BE_EMPTY;
                String id = data.getName(field);
                if(!id.startsWith("_")) {
                    m = form.getLabel(id) + ": " + m;
                }
            }
            return ValidationResult.error(m);
        }
    }

    private static class DataValidator<T> implements Validator<Data> {

        private static final String INVALID = "Not valid";
        private Function<T, Boolean> validator;
        private String errorMessage;
        private Form form;

        private DataValidator(Form form, Function<T, Boolean> validator, String errorMessage) {
            this.form = form;
            this.validator = validator;
            if(errorMessage != null && !errorMessage.isEmpty()) {
                this.errorMessage = errorMessage;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public ValidationResult apply(Data data, ValueContext valueContext) {
            HasValue<?, T> field = (HasValue<?, T>)valueContext.getHasValue().orElse(null);
            if(field != null && validator.apply(field.getValue())) {
                return OK;
            }
            String m = errorMessage;
            if(m == null) {
                m = INVALID;
                String id = data.getName(field);
                if(!id.startsWith("_")) {
                    m = form.getLabel(id) + ": " + m;
                }
            }
            return ValidationResult.error(m);
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
            HasValue<?, ?> field = this.valueContext.getHasValue().orElse(null);
            if(vr.getErrorLevel().isPresent()) {
                Form.markError(field);
            } else {
                Form.clearError(field);
            }
            return vr;
        }
    }
}