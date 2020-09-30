package com.storedobject.vaadin.util;

import com.storedobject.vaadin.AbstractForm;
import com.storedobject.vaadin.Alert;
import com.storedobject.vaadin.ID;
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

/**
 * Class that is internally handling Vaadin's {@link Binder}.
 *
 * @param <T> Type of the bean.
 * @author Syam
 */
@SuppressWarnings("rawtypes")
public class Data<T> extends HashMap<String, Object> {

    private static final String FIELD_CANT_BE_EMPTY = "Field can't be empty!";
    private static final ValidationResult OK = ValidationResult.ok();
    private static final Alert errorText = new Alert(null, NotificationVariant.LUMO_PRIMARY);
    private FieldValueHandler valueHandler;
    private final Map<String, HasValue<?, ?>> fields= new HashMap<>();
    private final Map<HasValue<?, ?>, String> fieldNames = new HashMap<>();
    private final Map<HasValue<?, ?>, DataValidators<T, ?>> validators = new HashMap<>();
    private final Map<HasValue<?, ?>, Binder.Binding<T, ?>> bindings = new HashMap<>();
    private Map<HasValue<?, ?>, List<Registration>> connections;
    private final Binder<T> binder;
    private boolean readOnly;
    private final AbstractForm<T> form;
    private Required requiredCache;
    private boolean extraErrors = false;

    /**
     * Constructor.
     *
     * @param form Form that contains the fields.
     */
    public Data(AbstractForm<T> form) {
        this.form = form;
        binder = new Binder<>();
        binder.setStatusLabel(errorText);
    }

    /**
     * Get the embedded binder.
     *
     * @return The binder.
     */
    public Binder<T> getBinder() {
        return binder;
    }

    /**
     * Set the error display (If not set, a default mechanism is used to display errors and warnings).
     *
     * @param display Display to set.
     */
    public void setErrorDisplay(HasText display) {
        binder.setStatusLabel(display == null ? errorText : display);
    }

    /**
     * Get the current error display.
     *
     * @return Get the current error display.
     */
    public HasText getErrorDisplay() {
        return binder.getStatusLabel().orElse(errorText);
    }

    /**
     * Set extra errors flag.
     */
    public void setExtraErrors() {
        this.extraErrors = true;
    }

    /**
     * Set the field value handler.
     *
     * @param valueHandler Field value handler.
     */
    public void setFieldValueHandler(FieldValueHandler valueHandler) {
        this.valueHandler = valueHandler;
    }

    /**
     * Get the current field value handler.
     *
     * @return The field value handler.
     */
    public FieldValueHandler getFieldValueHandler() {
        return valueHandler;
    }

    /**
     * Add a field with auto-generated filed name.
     *
     * @param field The field to add.
     * @return Name of the field.
     */
    public String addField(HasValue<?, ?> field) {
        return addField(null, field);
    }

    /**
     * Add a field.
     *
     * @param fieldName Field name.
     * @param field The field to add.
     * @return Name of the field.
     */
    @SuppressWarnings("unchecked")
    public String addField(String fieldName, HasValue<?, ?> field) {
        if(field == null) {
            return null;
        }
        if(fieldName == null || fieldName.isEmpty()) {
            fieldName = "_" + ID.newID();
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
        Binder.Binding<T, ?> binding;
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

    /**
     * Connect a collection of fields so that their values will be updated whenever any of the field value is changed.
     *
     * @param fields Collection of fields to connect together.
     * @return Returns true if the field list contains at least 2 fields.
     */
    public boolean connect(Collection<HasValue<?, ?>> fields) {
        List<HasValue<?, ?>> fieldList = new ArrayList<>(fields);
        if(fieldList.size() <= 1) {
            return false;
        }
        if(connections == null) {
            connections = new HashMap<>();
            binder.setBean(form.getObject());
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

    private <F> DataValidators<T, F> validator(HasValue<?, F> field) {
        @SuppressWarnings("unchecked") DataValidators<T, F> dv = (DataValidators<T, F>) validators.get(field);
        if(dv == null) {
            dv = new DataValidators<>(field);
            validators.put(field, dv);
        }
        return dv;
    }

    /**
     * Is this read-only?
     *
     * @return True or false.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Set this as read-only.
     *
     * @param readOnly True or false.
     */
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
            if(valueHandler.canHandle(key)) {
                ro = !valueHandler.canSet(key);
            }
            if(!ro) {
                ro = !valueHandler.isEditable(value);
            }
            if(!ro) {
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

    /**
     * Remove a field.
     *
     * @param fieldName Name of the field to remove.
     * @return The field that is removed.
     */
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

    /**
     * Get the name of the given field.
     *
     * @param field Filed.
     * @return Name of the field.
     */
    public String getName(HasValue<?, ?> field) {
        return fieldNames.get(field);
    }

    /**
     * Get the field for the given field name.
     *
     * @param fieldName Field name.
     * @return Field if found.
     */
    public HasValue<?, ?> getField(String fieldName) {
        return fields.get(fieldName);
    }

    /**
     * Get the number of fields.
     *
     * @return Number of fields.
     */
    public int getFieldCount() {
        return fields.size();
    }

    /**
     * Get the names of all the fields.
     *
     * @return Names of all the fields.
     */
    public Stream<String> getFieldNames() {
        return fields.keySet().stream();
    }

    /**
     * Get all the fields.
     *
     * @return All the fields.
     */
    public Stream<HasValue<?, ?>> getFields() {
        return fieldNames.keySet().stream();
    }

    /**
     * Load all the fields from the bean.
     */
    public void loadValues() {
        binder.readBean(form.getObject());
        setReadOnly(!readOnly);
        setReadOnly(!readOnly);
    }

    /**
     * Save all the field values to the bean.
     *
     * @return True if the save is successful.
     */
    @SuppressWarnings("unchecked")
    public boolean saveValues() {
        binder.getStatusLabel().ifPresent(errDisplay -> errDisplay.setText(""));
        for(HasValue<?, ?> field: fields.values()) {
            if(field instanceof HasValidation && ((HasValidation) field).isInvalid()) {
                showErr(field);
                return false;
            }
        }
        extraErrors = false;
        if(!binder.writeBeanIfValid(form.getObject())) {
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

    /**
     * Clear all errors.
     */
    public void clearErrors() {
        fields.values().forEach(AbstractForm::clearError);
        if(errorText.equals(binder.getStatusLabel().orElse(null))) {
            errorText.close();
        }
    }

    /**
     * Clear all field values.
     */
    public void clearFields() {
        fields.values().forEach(HasValue::clear);
    }

    /**
     * Add a validator for the given field.
     *
     * @param field Field.
     * @param validator Validator.
     * @param errorMessage Error message to show when validation fails.
     * @param <F> Field value type.
     */
    public <F> void addValidator(HasValue<?, F> field, Function<F, Boolean> validator, String errorMessage) {
        if(field == null) {
            throw new RuntimeException(FIELD_CANT_BE_EMPTY);
        }
        validator(field).add(new DataValidator<>(this, validator, errorMessage));
    }

    /**
     * Set the "required" attribute of the field.
     *
     * @param field Field.
     * @param required True/false.
     * @param errorMessage Error message to show when empty.
     * @param <F> Field value type.
     */
    public <F> void setRequired(HasValue<?, F> field, boolean required, String errorMessage) {
        if(field == null) {
            throw new RuntimeException(FIELD_CANT_BE_EMPTY);
        }
        Object value = field.getValue();
        if(value != null) {
            Class<?> valueClass = value.getClass();
            if(valueClass == boolean.class || valueClass == Boolean.class) {
                return;
            }
        }
        field.setRequiredIndicatorVisible(required);
        DataValidators<T, F> dv = validator(field);
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
            Validator<F> v = dv.get(0);
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
        //noinspection unchecked
        dv.add(0, errorMessage == null || errorMessage.isEmpty() ? requiredCache() : new Required<>(this, errorMessage));
    }

    private Required requiredCache() {
        if(requiredCache == null) {
            requiredCache = new Required<>(this, null);
        }
        return requiredCache;
    }

    /**
     * Get the label for the given field.
     *
     * @param field Field.
     * @return Label string if exists, otherwise null.
     */
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

    private static class Required<D, F> implements Validator<F> {

        private static final String CAN_NOT_BE_EMPTY = "Can not be empty";
        private String errorMessage;
        private final Data<D> data;

        private Required(Data<D> data, String errorMessage) {
            this.data = data;
            if(errorMessage != null && !errorMessage.isEmpty()) {
                this.errorMessage = errorMessage;
            }
        }

        @Override
        public ValidationResult apply(F value, ValueContext valueContext) {
            HasValue<?, ?> field = valueContext.getHasValue().orElse(null);
            if(field == null || !field.isEmpty()) {
                return OK;
            }
            String fieldName = this.data.getName(field);
            if(fieldName != null && this.data.valueHandler.canHandle(fieldName) && !this.data.valueHandler.canSet(fieldName)) {
                return OK;
            }
            String m = errorMessage;
            if(m == null) {
                m = this.data.errMessage(field, CAN_NOT_BE_EMPTY);
            }
            return ValidationResult.error(m);
        }
    }

    private static class DataValidator<D, F> implements Validator<F> {

        private static final String INVALID = "Not valid";
        private final Function<F, Boolean> validator;
        private String errorMessage;
        private final Data<D> data;

        private DataValidator(Data<D> data, Function<F, Boolean> validator, String errorMessage) {
            this.data = data;
            this.validator = validator;
            if(errorMessage != null && !errorMessage.isEmpty()) {
                this.errorMessage = errorMessage;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public ValidationResult apply(F value, ValueContext valueContext) {
            if(validator.apply(value)) {
                return OK;
            }
            HasValue<?, F> field = (HasValue<?, F>)valueContext.getHasValue().orElse(null);
            if(field == null) {
                return OK;
            }
            String m = errorMessage;
            if(m == null) {
                m = this.data.errMessage(field, INVALID);
            }
            return ValidationResult.error(m);
        }
    }

    private static class DataValidators<D, F> extends ArrayList<Validator<F>> implements Validator<D> {

        private final ValueContext valueContext;
        private final HasValue<?, F> field;

        private DataValidators(HasValue<?, F> field) {
            this.field = field;
            valueContext = new ValueContext(field instanceof Component ? (Component)field : errorText, field);
        }

        @Override
        public ValidationResult apply(D data, ValueContext valueContext) {
            ValidationResult vr = OK;
            for(Validator<F> v: this) {
                vr = v.apply(field.getValue(), this.valueContext);
                if(vr.getErrorLevel().isPresent()) {
                    break;
                }
            }
            if(field == null) {
                return OK;
            }
            if(vr.getErrorLevel().isPresent()) {
                AbstractForm.markError(field);
            } else {
                AbstractForm.clearError(field);
            }
            return vr;
        }
    }
}