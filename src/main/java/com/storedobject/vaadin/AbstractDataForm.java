package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Data;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;

import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AbstractDataForm extends View {

    protected static final FieldError FIELD_ERROR = new FieldError();
    protected Form form;

    public Form getForm() {
        return form;
    }

    protected void formConstructed() {
    }

    protected final Data getData() {
        return form.data;
    }

    @Override
    public Component getContent() {
        return form.getComponent();
    }

    protected HasComponents createContainer() {
        return null;
    }

    protected HasComponents createLayout() {
        return null;
    }

    protected HasComponents createButtonLayout() {
        return null;
    }

    public HasComponents getContainer() {
        return form.getContainer();
    }

    public Stream<String> getFieldNames() {
        return null;
    }

    protected HasValue<?, ?> createField(@SuppressWarnings("unused") String fieldName) {
        return null;
    }

    public void addField(HasValue<?, ?> field) {
        form.addField(field);
    }

    public void addField(String fieldName, HasValue<?, ?> field) {
        form.addField(fieldName, field);
    }

    public void removeField(String fieldName) {
        form.removeField(fieldName);
    }

    public void removeField(String... fieldNames) {
        form.removeField(fieldNames);
    }

    public void removeField(HasValue<?, ?> field) {
        form.removeField(field);
    }

    public void add(Component... components) {
        form.add(components);
    }

    public void remove(Component... components) {
        form.remove(components);
    }

    public void removeAll() {
        form.removeAll();
    }

    public void load() {
        form.load();
    }

    public boolean commit() {
        return form.commit();
    }

    public HasValue<?, ?> getField(String fieldName) {
        return form.getField(fieldName);
    }

    public String getFieldName(HasValue<?, ?> field) {
        return form.getFieldName(field);
    }

    public void setRequired(HasValue<?, ?> field) {
        form.setRequired(field);
    }

    public void setRequired(String fieldName) {
        form.setRequired(fieldName);
    }

    public void setRequired(HasValue<?, ?> field, String errorMessage) {
        form.setRequired(field, errorMessage);
    }

    public void setRequired(String fieldName, String errorMessage) {
        form.setRequired(fieldName, errorMessage);
    }

    public void setRequired(HasValue<?, ?> field, boolean required) {
        form.setRequired(field, required);
    }

    public void setRequired(String fieldName, boolean required) {
        form.setRequired(fieldName, required);
    }

    public void setRequired(HasValue<?, ?> field, boolean required, String errorMessage) {
        form.setRequired(field, required, errorMessage);
    }

    public void setRequired(String fieldName, boolean required, String errorMessage) {
        form.setRequired(fieldName, required, errorMessage);
    }

    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator) {
        form.addValidator(field, validator);
    }

    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator, String errorMessage) {
        form.addValidator(field, validator, errorMessage);
    }

    public static void markError(HasValue<?, ?> field) {
        Form.markError(field);
    }

    public static void clearError(HasValue<?, ?> field) {
        Form.clearError(field);
    }

    public void clearFields() {
        form.clearFields();
    }

    public void clearErrors() {
        form.clearErrors();
    }

    public void setReadOnly(boolean readOnly) {
        form.setReadOnly(readOnly);
    }

    public boolean isFieldVisible(@SuppressWarnings("unused") String fieldName) {
        return true;
    }

    public boolean isFieldVisible(@SuppressWarnings("unused") HasValue<?, ?> field) {
        return true;
    }

    public boolean isFieldEditable(@SuppressWarnings("unused") String fieldName) {
        return true;
    }

    public boolean isFieldEditable(@SuppressWarnings("unused") HasValue<?, ?> field) {
        return true;
    }

    protected void attachField(@SuppressWarnings("unused") String fieldName,
                               @SuppressWarnings("unused") HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    protected void detachField(@SuppressWarnings("unused") String fieldName,
                               @SuppressWarnings("unused") HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    public void setErrorDisplay(HasText display) {
        form.setErrorDisplay(display);
    }

    protected static class FieldError extends RuntimeException {
    }
}
