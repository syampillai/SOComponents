package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Data;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A view that is used for creating "data entry forms". A {@link Form} is embedded in the view.
 * @author Syam
 */
public abstract class AbstractDataForm extends View {

    /**
     * For internal use only.
     */
    protected static final FieldError FIELD_ERROR = new FieldError();
    /**
     * Form embedded in this view.
     */
    protected Form form;
    private List<String> readOnly = new ArrayList<>(), hidden = new ArrayList<>();
    private List<HasValue<?, ?>> readOnlyFields = new ArrayList<>(), hiddenFields = new ArrayList<>();

    /**
     * Get the form embedded in this view.
     * @return Form
     */
    public Form getForm() {
        return form;
    }

    /**
     * This method is invoked when the form is constructed
     */
    protected void formConstructed() {
    }

    /**
     * Get the "data" associated with the form.
     * @return Data structure that holds the data.
     */
    protected final Data getData() {
        return form.data;
    }

    /**
     * Get the field container of the form as a component.
     * @return Field container as a component. <code>null</code> is returned if the field container is not a component.
     */
    @Override
    public Component getContent() {
        return form.getComponent();
    }

    /**
     * Create the field container of this form. This will be invoked only if no container was already set.
     * Default implementation creates a {@link com.vaadin.flow.component.formlayout.FormLayout}.
     * @return Field container created.
     */
    protected HasComponents createFieldContainer() {
        return null;
    }

    /**
     * This method is invoked to create the layout of the view.
     * @return Layout
     */
    protected HasComponents createLayout() {
        return null;
    }

    /**
     * This method is invoked to create a "button panel" to be displayed typically at the top of the view's layout.
     * @return A component to display other components, typically, buttons.
     */
    protected HasComponents createButtonLayout() {
        return null;
    }

    /**
     * Get the field container of the form.
     * @return Field container.
     */
    public HasComponents getContainer() {
        return form.getContainer();
    }

    /**
     * Get the field names. This is invoked to determine the fields to be created for constructing the form.
     * @return Stream of fields.
     */
    public Stream<String> getFieldNames() {
        return null;
    }

    /**
     * Create the field for a particular "field name".
     * @param fieldName Field name
     * @return Field created.
     */
    protected HasValue<?, ?> createField(@SuppressWarnings("unused") String fieldName) {
        return null;
    }

    /**
     * Add a field to the form. The field will not be having a "field name".
     * @param field Field to be added
     */
    public void addField(HasValue<?, ?> field) {
        form.addField(field);
    }

    /**
     * Add a field to the form.
     * @param fieldName Name associated with the field
     * @param field Field
     */
    public void addField(String fieldName, HasValue<?, ?> field) {
        form.addField(fieldName, field);
    }

    public void removeField(String fieldName) {
        form.removeField(fieldName);
    }

    /**
     * Remove fields from the form.
     * @param fieldNames Names of the field to be removed
     */
    public void removeField(String... fieldNames) {
        form.removeField(fieldNames);
    }

    /**
     * Remove a field from the form.
     * @param field Field to be removed.
     */
    public void removeField(HasValue<?, ?> field) {
        form.removeField(field);
    }

    /**
     * Add components to the form's field container.
     * @param components Compoents to add
     */
    public void add(Component... components) {
        form.add(components);
    }

    /**
     * Remove components from the form's field container.
     * @param components Compoents to remove
     */
    public void remove(Component... components) {
        form.remove(components);
    }

    /**
     * Remove all components from the form's field container.
     */
    public void removeAll() {
        form.removeAll();
    }

    /**
     * Load data to the fields from the internal data structure.
     */
    public void load() {
        form.load();
    }

    /**
     * Save data from the fields to the internal data structure.
     * @return True if data is saved successfully.
     */
    public boolean commit() {
        return form.commit();
    }

    /**
     * Get a field associated with a field name.
     * @param fieldName Name of the field
     * @return Field.
     */
    public HasValue<?, ?> getField(String fieldName) {
        return form.getField(fieldName);
    }

    /**
     * Get a field name associated with a field.
     * @param field Field
     * @return Name of the field.
     */
    public String getFieldName(HasValue<?, ?> field) {
        return form.getFieldName(field);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param field Field
     */
    public void setRequired(HasValue<?, ?> field) {
        form.setRequired(field);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param fieldName Field name
     */
    public void setRequired(String fieldName) {
        form.setRequired(fieldName);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param field Field
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(HasValue<?, ?> field, String errorMessage) {
        form.setRequired(field, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param fieldName Field name
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(String fieldName, String errorMessage) {
        form.setRequired(fieldName, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param field Field
     * @param required True or false
     */
    public void setRequired(HasValue<?, ?> field, boolean required) {
        form.setRequired(field, required);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param fieldName Field name
     * @param required True or false
     */
    public void setRequired(String fieldName, boolean required) {
        form.setRequired(fieldName, required);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param field Field
     * @param required True or false
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(HasValue<?, ?> field, boolean required, String errorMessage) {
        form.setRequired(field, required, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     * @param fieldName Field name
     * @param required True or false
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(String fieldName, boolean required, String errorMessage) {
        form.setRequired(fieldName, required, errorMessage);
    }

    /**
     * Add a validator for the field. Validator should return <code>true</code> if the field is valid.
     * @param field Field
     * @param validator Validator
     * @param <T> Type of the field.
     */
    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator) {
        form.addValidator(field, validator);
    }

    /**
     * Add a validator for the field. Validator should return <code>true</code> if the field is valid.
     * @param field Field
     * @param validator Validator
     * @param errorMessage Error message to be displayed if the field is not valid
     * @param <T> Type of the field.
     */
    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator, String errorMessage) {
        form.addValidator(field, validator, errorMessage);
    }

    /**
     * Mark a field as not valid.
     * @param field Field
     */
    public static void markError(HasValue<?, ?> field) {
        Form.markError(field);
    }

    /**
     * Clear the error status associated with a field.
     * @param field Field
     */
    public static void clearError(HasValue<?, ?> field) {
        Form.clearError(field);
    }

    /**
     * Clear values of all the fields.
     */
    public void clearFields() {
        form.clearFields();
    }

    /**
     * Clear error status of all the fields.
     */
    public void clearErrors() {
        form.clearErrors();
    }

    /**
     * Set the view read only.
     * @param readOnly True or false
     */
    public void setReadOnly(boolean readOnly) {
        form.setReadOnly(readOnly);
    }

    /**
     * This method is invoked to determine if a field needs to be made visible or not.
     * @param fieldName Name of the field
     * @return True or false.
     */
    public boolean isFieldVisible(@SuppressWarnings("unused") String fieldName) {
        return !hidden.contains(fieldName);
    }

    /**
     * This method is invoked to determine if a field needs to be made visible or not.
     *
     * @param field Field
     * @return True or false.
     */
    public boolean isFieldVisible(@SuppressWarnings("unused") HasValue<?, ?> field) {
        return !hiddenFields.contains(field);
    }

    /**
     * Set fields visible. (By default fields are visible).
     *
     * @param fieldNames Field names
     */
    public void setFieldVisible(String... fieldNames) {
        if(fieldNames == null || fieldNames.length == 0) {
            return;
        }
        for(String fieldName: fieldNames) {
            hidden.remove(fieldName);
            hiddenFields.remove(form.getField(fieldName));
        }
    }

    /**
     * Set fields visible. (By default fields are visible).
     *
     * @param fields Fields
     */
    public void setFieldVisible(HasValue<?, ?>... fields) {
        if(fields == null || fields.length == 0) {
            return;
        }
        for(HasValue<?, ?> field: fields) {
            hidden.remove(form.getFieldName(field));
            hiddenFields.remove(field);
        }
    }

    /**
     * Set fields hidden. (By default fields are visible).
     *
     * @param fieldNames Field names
     */
    public void setFieldHidden(String... fieldNames) {
        if(fieldNames == null || fieldNames.length == 0) {
            return;
        }
        HasValue<?, ?> field;
        for(String fieldName: fieldNames) {
            if(fieldName == null || fieldName.isEmpty()) {
                continue;
            }
            hidden.add(fieldName);
            field = form.getField(fieldName);
            if(field != null) {
                hiddenFields.add(field);
            }
        }
    }

    /**
     * Set fields hidden. (By default fields are visible).
     *
     * @param fields Fields
     */
    public void setFieldHidden(HasValue<?, ?>... fields) {
        if(fields == null || fields.length == 0) {
            return;
        }
        String fieldName;
        for(HasValue<?, ?> field: fields) {
            if(field == null || field.isEmpty()) {
                continue;
            }
            fieldName = form.getFieldName(field);
            if(fieldName != null) {
                readOnly.add(fieldName);
            }
            readOnlyFields.add(field);
        }
    }

    /**
     * This method is invoked to determine if a field needs to be made editable or not.
     *
     * @param fieldName Name of the field
     * @return True or false.
     */
    public boolean isFieldEditable(@SuppressWarnings("unused") String fieldName) {
        return !readOnly.contains(fieldName);
    }

    /**
     * This method is invoked to determine if a field needs to be made editable or not.
     *
     * @param field Field
     * @return True or false.
     */
    public boolean isFieldEditable(@SuppressWarnings("unused") HasValue<?, ?> field) {
        return !readOnlyFields.contains(field);
    }

    /**
     * Set fields editable. (By default fields are editable).
     *
     * @param fieldNames Field names
     */
    public void setFieldEditable(String... fieldNames) {
        if(fieldNames == null || fieldNames.length == 0) {
            return;
        }
        for(String fieldName: fieldNames) {
            readOnly.remove(fieldName);
            readOnlyFields.remove(form.getField(fieldName));
        }
    }

    /**
     * Set fields editable. (By default fields are editable).
     *
     * @param fields Fields
     */
    public void setFieldEditable(HasValue<?, ?>... fields) {
        if(fields == null || fields.length == 0) {
            return;
        }
        for(HasValue<?, ?> field: fields) {
            readOnly.remove(form.getFieldName(field));
            readOnlyFields.remove(field);
        }
    }

    /**
     * Set fields read only. (By default fields are editable).
     *
     * @param fieldNames Field names
     */
    public void setFieldReadOnly(String... fieldNames) {
        if(fieldNames == null || fieldNames.length == 0) {
            return;
        }
        HasValue<?, ?> field;
        for(String fieldName: fieldNames) {
            if(fieldName == null || fieldName.isEmpty()) {
                continue;
            }
            readOnly.add(fieldName);
            field = form.getField(fieldName);
            if(field != null) {
                readOnlyFields.add(field);
            }
        }
    }

    /**
     * Set fields read only. (By default fields are editable).
     *
     * @param fields Fields
     */
    public void setFieldReadOnly(HasValue<?, ?>... fields) {
        if(fields == null || fields.length == 0) {
            return;
        }
        String fieldName;
        for(HasValue<?, ?> field: fields) {
            if(field == null || field.isEmpty()) {
                continue;
            }
            fieldName = form.getFieldName(field);
            if(fieldName != null) {
                readOnly.add(fieldName);
            }
            readOnlyFields.add(field);
        }
    }

    /**
     * This method is invoked to determine the label used for dislaying the field. The default implementation determine it by invoking
     * {@link ApplicationEnvironment#createLabel(String)}.
     *
     * @param fieldName Name of the field
     * @return Label
     */
    protected String getLabel(@SuppressWarnings("unused") String fieldName) {
        throw FIELD_ERROR;
    }


    /**
     * Set label for a specific field. Label will be set only if the field has a setLabel(String) method.
     *
     * @param fieldName Name of the field for which label needs to be set
     * @param label Label to set
     */
    public void setFieldLabel(String fieldName, String label) {
        form.setFieldLabel(fieldName, label);
    }

    /**
     * Set label for a specific field. Label will be set only if the field has a setLabel(String) method.
     *
     * @param field Field for which label needs to be set
     * @param label Label to set
     */
    public void setFieldLabel(HasValue<?, ?> field, String label) {
        form.setFieldLabel(field, label);
    }

    /**
     * Attach a field to the form. This is invoked whenever a field is added to the form. The default implementation adds the component of
     * the field (if it is a component) to the field container.
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void attachField(@SuppressWarnings("unused") String fieldName,
                               @SuppressWarnings("unused") HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    /**
     * Detach a field from the form. This is invoked whenever a field is removed to the form. The default implementation removes the component of
     * the field (if it is a component) from the field container.
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void detachField(@SuppressWarnings("unused") String fieldName,
                               @SuppressWarnings("unused") HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    /**
     * Error messages of the form are typcially displayed using {@link com.vaadin.flow.component.notification.Notification}. However,
     * one can set any {@link HasText} for that.
     * @param display Error messages will be displayed on this
     */
    public void setErrorDisplay(HasText display) {
        form.setErrorDisplay(display);
    }

    /**
     * For internal use only.
     */
    protected static class FieldError extends RuntimeException {
    }

    /**
     * Set number of responsive columns for the form.
     *
     * @param columns Number of responsive columns required
     */
    public void setColumns(int columns) {
        form.setColumns(columns);
    }

    /**
     * Get number of responsive columns for the form.
     *
     * @return Number of responsive columns of the form.
     */
    public final int getColumns() {
        return form.getColumns();
    }

    /**
     * Set number of columns to span for a particular component.
     *
     * @param component Component for which column span to be set
     * @param columnSpan Number of columns to span
     */
    public void setColumnSpan(Component component, int columnSpan) {
        form.setColumnSpan(component, columnSpan);
    }

    /**
     * Get the column span of a component.
     *
     * @param component omponent for which column span to be retrieved
     * @return Column span for the component.
     */
    public int getColumnSpan(Component component) {
        return form.getColumnSpan(component);
    }
}
