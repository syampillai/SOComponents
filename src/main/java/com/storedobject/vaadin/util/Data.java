package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Alert;
import com.storedobject.vaadin.Form;
import com.storedobject.vaadin.ValueRequired;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.shared.Registration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Data extends HashMap<String, Object> {

    private static ValidationResult OK = ValidationResult.ok();
    private static Alert errorText = new Alert(null, NotificationVariant.LUMO_PRIMARY);
    private static long id = 0L;
    private FieldValueHandler valueHandler;
    private final Map<String, HasValue<?, ?>> fields= new HashMap<>();
    private final Map<HasValue<?, ?>, String> fieldNames = new HashMap<>();
    private final Map<HasValue<?, ?>, DataValidators> validators = new HashMap<>();
    private final Map<HasValue<?, ?>, Binder.Binding<Data, ?>> bindings = new HashMap<>();
    private Map<HasValue<?, ?>, List<Registration>> connections;
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

    public HasText getErrorDisplay() {
        return binder.getStatusLabel().orElse(errorText);
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
        Binder.Binding<Data, ?> binding;
        if (valueHandler.isBasic() || !valueHandler.canHandle(name)) {
            setValue(fieldName, field.getValue());
            binding = binder.bind(f, x -> getValue(name), (d, v) -> setValue(name, v));
        } else {
            if (valueHandler.canSet(name)) {
                binding = binder.bind(f, x -> valueHandler.getValue(name), (d, v) -> valueHandler.setValue(name, v));
            } else {
                binding = binder.bind(f, x -> valueHandler.getValue(name), null);
            }
        }
        bindings.put(field, binding);
        if(field instanceof ValueRequired && ((ValueRequired)field).isRequired() && valueHandler.canSet(fieldName)) {
            setRequired(field, true, null);
        }
        return fieldName;
    }

    public boolean connect(Collection<HasValue<?, ?>> fields) {
        List<HasValue<?, ?>> fieldList = new ArrayList<>(fields);
        if(fieldList.size() <= 1) {
            return false;
        }
        if(connections == null) {
            connections = new HashMap<>();
            binder.setBean(this);
        }
        List<Registration> registrations;
        for(HasValue<?, ?> field: fieldList) {
            registrations = connections.computeIfAbsent(field, k -> new ArrayList<>());
            registrations.add(field.addValueChangeListener(e -> {
                if(e.isFromClient()) {
                    updateConnections(e.getHasValue(), fieldList);
                }
            }));
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void updateConnections(HasValue<?, ?> source, List<HasValue<?, ?>> connections) {
        connections.forEach(f -> {
            if(f != source) {
                Binder.Binding<Data, Object> binding = (Binder.Binding<Data, Object>) bindings.get(f);
                ((HasValue<?, Object>)f).setValue(binding.getGetter().apply(this));
            }
        });
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
        this.readOnly = readOnly;
        if(readOnly) {
            fields.forEach((key, value) -> {
                value.setReadOnly(true);
                setVisible(key, value);
            });
            return;
        }
        fields.forEach((key, value) -> {
            boolean ro = false;
            if (valueHandler.canHandle(key)) {
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
        bindings.remove(field);
        fieldNames.remove(field);
        validators.remove(field);
        if(connections != null) {
            List<Registration> registrations = connections.get(field);
            if(registrations != null) {
                registrations.forEach(Registration::remove);
                connections.remove(field);
            }
        }
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
        binder.getStatusLabel().ifPresent(errDisp -> errDisp.setText(""));
        for(HasValue<?, ?> field: fields.values()) {
            if(field instanceof HasValidation && ((HasValidation) field).isInvalid()) {
                showErr(field);
                return false;
            }
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

    private void showErr(HasValue<?, ?> field) {
        HasText hasText = binder.getStatusLabel().orElse(null);
        if(hasText == null) {
            return;
        }
        HasValidation hv = (HasValidation)field;
        String m = hv.getErrorMessage();
        m = errMessage(field, m == null ? DataValidator.INVALID : m);
        hasText.setText(m);
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
        validator(field).add(new DataValidator<>(validator, errorMessage));
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
        dv.add(0, errorMessage == null || errorMessage.isEmpty() ? requiredCache() : new Required(errorMessage));
    }

    private Required requiredCache() {
        if(requiredCache == null) {
            requiredCache = new Required(null);
        }
        return requiredCache;
    }

    public static String getLabel(HasValue<?, ?> field) {
        try {
            return (String) (field.getClass().getMethod("getLabel")).invoke(field);
        } catch (Throwable ignored) {
        }
        return null;
    }

    private String errMessage(HasValue<?, ?> field, String message) {
        String m, id = getName(field);
        if(!id.startsWith("_")) {
            m = form.getLabel(id);
        } else {
            m = getLabel(field);
        }
        if(m == null) {
            return message;
        }
        return m + ": " + message;
    }

    private static class Required implements Validator<Data> {

        private static final String CAN_NOT_BE_EMPTY = "Can not be empty";
        private String errorMessage;

        private Required(String errorMessage) {
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
                m = data.errMessage(field, CAN_NOT_BE_EMPTY);
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
        @SuppressWarnings("unchecked")
        public ValidationResult apply(Data data, ValueContext valueContext) {
            HasValue<?, T> field = (HasValue<?, T>)valueContext.getHasValue().orElse(null);
            if(field != null && validator.apply(field.getValue())) {
                return OK;
            }
            String m = errorMessage;
            if(m == null) {
                m = data.errMessage(field, INVALID);
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