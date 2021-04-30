package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Data;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A view that is used for creating "data entry forms". An {@link AbstractForm} is embedded in the view.
 *
 * @param <D> Type of data in the embedded form
 * @author Syam
 */
public abstract class AbstractDataForm<D> extends View implements HasContainer {

    /**
     * For internal use only.
     */
    static final FieldError FIELD_ERROR = new FieldError();
    /**
     * Form embedded in this view.
     */
    protected AbstractForm<D> form;
    private List<ConstructedListener> constructedListeners;
    private boolean formCreated = false;
    private HasContainer fieldContainerProvider;
    private final Set<String> readOnly = new HashSet<>();
    private final Set<String> hidden = new HashSet<>();
    private final Set<HasValue<?, ?>> readOnlyFields = new HashSet<>();
    private final Set<HasValue<?, ?>> hiddenFields = new HashSet<>();

    /**
     * Get the form embedded in this view.
     *
     * @return Form
     */
    public AbstractForm<D> getForm() {
        return form;
    }

    /**
     * This method is invoked when the form is constructed.
     */
    protected void formConstructed() {
    }

    /**
     * This method is invoked to inform all {@link ConstructedListener}s when the form is constructed.
     */
    protected final void fireFormConstructed() {
        formCreated = true;
        formConstructed();
        if(constructedListeners != null) {
            constructedListeners.forEach(cl -> cl.constructed(this));
            constructedListeners = null;
        }
    }

    /**
     * Add a {@link ConstructedListener} so that we will be informed about when the UI part is constructed.
     *
     * @param constructedListener Listener.
     * @return Registration.
     */
    public Registration addConstructedListener(ConstructedListener constructedListener) {
        if(formCreated) {
            constructedListener.constructed(this);
            return () -> {};
        }
        if(constructedListeners == null) {
            constructedListeners = new ArrayList<>();
        }
        constructedListeners.add(constructedListener);
        return () -> constructedListeners.remove(constructedListener);
    }

    /**
     * Create the window for embedding the form. Default implementation returns null.
     *
     * @param component Form component to embed
     * @return A window with the form component added to it.
     */
    protected Window createWindow(Component component) {
        return null;
    }

    /**
     * Get the "data" associated with the form.
     *
     * @return Data structure that holds the data.
     */
    protected final Data<D> getData() {
        return form.data;
    }

    /**
     * Get the field container of the form as a component.
     *
     * @return Field container as a component. <code>null</code> is returned if the field container is not a component.
     */
    @Override
    public Component getContent() {
        return form.getComponent();
    }

    /**
     * Set the field container provider for this form so that {@link #createFieldContainer()}} can provide the component
     * container from this.
     *
     * @param fieldContainerProvider Field container to set
     */
    public void setFieldContainerProvider(HasContainer fieldContainerProvider) {
        this.fieldContainerProvider = fieldContainerProvider;
    }

    /**
     * Create the field container of this form. Default implementation creates a container from the field container
     * provider that was set, otherwise, it creates {@link com.vaadin.flow.component.formlayout.FormLayout}.
     *
     * @return Field container created.
     */
    protected HasComponents createFieldContainer() {
        return fieldContainerProvider == null ? null : fieldContainerProvider.getContainer();
    }

    /**
     * This method is invoked to create the layout of the view.
     *
     * @return Layout
     */
    protected HasComponents createLayout() {
        return null;
    }

    /**
     * This method is invoked to create a "button panel" to be displayed typically at the top of the view's layout.
     *
     * @return A component to display other components, typically, buttons.
     */
    protected HasComponents createButtonLayout() {
        return null;
    }

    /**
     * Get the field container of the form.
     *
     * @return Field container.
     */
    @Override
    public HasComponents getContainer() {
        return form.getContainer();
    }

    /**
     * Get the field names. This is invoked to determine the fields to be created for constructing the form.
     *
     * @return Stream of fields.
     */
    public Stream<String> getFieldNames() {
        return null;
    }

    /**
     * Set "include field checker". It will determine if a field can be added or not.
     *
     * @param includeField The "include field checker"
     */
    public void setIncludeFieldChecker(IncludeField includeField) {
        getForm().setIncludeFieldChecker(includeField);
    }

    /**
     * Add an "include field checker" to the existing chain. If any one of the {@link IncludeField} in the chain
     * returns <code>false</code> for a specific field, the field will not be included in the form.
     *
     * @param includeField The "include field checker" to add.
     * @return Registration.
     */
    public Registration addIncludeFieldChecker(IncludeField includeField) {
        return getForm().addIncludeFieldChecker(includeField);
    }

    /**
     * Set an external field customizer. If set, the method {@link FieldCustomizer#customizeField(String, HasValue)}
     * will be invoked when each field gets created.
     *
     * @param fieldCustomizer Field customizer.
     */
    public void setFieldCustomizer(FieldCustomizer fieldCustomizer) {
        ((ObjectForm<D>)this.form).setFieldCustomizer(fieldCustomizer);
    }

    /**
     * Create the field for a particular "field name".
     *
     * @param fieldName Field name
     * @return Field created.
     */
    protected HasValue<?, ?> createField(String fieldName) {
        return null;
    }

    /**
     * Create the field for a particular "field name".
     *
     * @param fieldName Field name
     * @param label Label for the field
     * @return Field created.
     */
    protected HasValue<?, ?> createField(String fieldName, String label) {
        return null;
    }

    /**
     * Add fields to the form. These fields will not be having "field names".
     *
     * @param fields Fields to be added
     */
    public void addField(HasValue<?, ?>... fields) {
        if(fields != null) {
            for(HasValue<?, ?> field: fields) {
                form.addField(field);
            }
        }
    }

    /**
     * Add a field to the form.
     *
     * @param fieldName Name associated with the field
     * @param field Field
     */
    public void addField(String fieldName, HasValue<?, ?> field) {
        form.addField(fieldName, field);
    }

    /**
     * Remove a field from the form.
     *
     * @param fieldName Name of the field to be removed.
     */
    public void removeField(String fieldName) {
        form.removeField(fieldName);
    }

    /**
     * Remove fields from the form.
     *
     * @param fieldNames Names of the field to be removed
     */
    public void removeField(String... fieldNames) {
        form.removeField(fieldNames);
    }

    /**
     * Remove a field from the form.
     *
     * @param field Field to be removed.
     */
    public void removeField(HasValue<?, ?> field) {
        form.removeField(field);
    }

    /**
     * Add components to the form's field container.
     *
     * @param components Components to add
     */
    public void add(Component... components) {
        form.add(components);
    }

    /**
     * Remove components from the form's field container.
     *
     * @param components Components to remove
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
     *
     * @return True if data is saved successfully.
     */
    public boolean commit() {
        return form.commit();
    }

    /**
     * Get a field associated with a field name.
     *
     * @param fieldName Name of the field
     * @return Field.
     */
    public HasValue<?, ?> getField(String fieldName) {
        return form.getField(fieldName);
    }

    /**
     * Get a field name associated with a field.
     *
     * @param field Field
     * @return Name of the field.
     */
    public String getFieldName(HasValue<?, ?> field) {
        return form.getFieldName(field);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param field Field
     */
    public void setRequired(HasValue<?, ?> field) {
        form.setRequired(field);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param fieldName Field name
     */
    public void setRequired(String fieldName) {
        form.setRequired(fieldName);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param field Field
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(HasValue<?, ?> field, String errorMessage) {
        form.setRequired(field, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param fieldName Field name
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(String fieldName, String errorMessage) {
        form.setRequired(fieldName, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param field Field
     * @param required True or false
     */
    public void setRequired(HasValue<?, ?> field, boolean required) {
        form.setRequired(field, required);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param fieldName Field name
     * @param required True or false
     */
    public void setRequired(String fieldName, boolean required) {
        form.setRequired(fieldName, required);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
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
     *
     * @param fieldName Field name
     * @param required True or false
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(String fieldName, boolean required, String errorMessage) {
        form.setRequired(fieldName, required, errorMessage);
    }

    /**
     * Add a validator for the field. Validator should return <code>true</code> if the field is valid.
     *
     * @param field Field
     * @param validator Validator
     * @param <T> Type of the field.
     */
    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator) {
        form.addValidator(field, validator);
    }

    /**
     * Add a validator for the field. Validator should return <code>true</code> if the field is valid.
     *
     * @param field Field
     * @param validator Validator
     * @param errorMessage Error message to be displayed if the field is not valid
     * @param <T> Type of the field value.
     */
    public <T> void addValidator(HasValue<?, T> field, Function<T, Boolean> validator, String errorMessage) {
        form.addValidator(field, validator, errorMessage);
    }

    /**
     * Mark a field as not valid.
     *
     * @param field Field
     */
    public static void markError(HasValue<?, ?> field) {
        AbstractForm.markError(field);
    }

    /**
     * Clear the error status associated with a field.
     *
     * @param field Field
     */
    public static void clearError(HasValue<?, ?> field) {
        AbstractForm.clearError(field);
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
     *
     * @param readOnly True or false
     */
    public void setReadOnly(boolean readOnly) {
        form.setReadOnly(readOnly);
    }

    /**
     * Check whether the view is "read only" or not.
     *
     * @return True or false.
     */
    public boolean isReadOnly() {
        return form.isReadOnly();
    }

    /**
     * This method is invoked to determine if a field needs to be made visible or not.
     *
     * @param fieldName Name of the field
     * @return True or false.
     */
    public boolean isFieldVisible(String fieldName) {
        return !hidden.contains(fieldName);
    }

    /**
     * This method is invoked to determine if a field needs to be made visible or not.
     *
     * @param field Field
     * @return True or false.
     */
    public boolean isFieldVisible(HasValue<?, ?> field) {
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
        HasValue<?, ?> field;
        for(String fieldName: fieldNames) {
            if(fieldName == null || fieldName.isEmpty()) {
                continue;
            }
            hidden.remove(fieldName);
            field = form.getField(fieldName);
            if(field != null) {
                hiddenFields.remove(field);
                if(field instanceof Component) {
                    ((Component) field).setVisible(true);
                }
            }
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
        String fieldName;
        for(HasValue<?, ?> field: fields) {
            if(field == null) {
                continue;
            }
            fieldName = form.getFieldName(field);
            if(fieldName != null) {
                hidden.remove(fieldName);
            }
            hiddenFields.remove(field);
            if(field instanceof Component) {
                ((Component) field).setVisible(true);
            }
        }
    }

    /**
     * Set fields visible. (By default fields are visible).
     *
     * @param visible To make it visible or not
     * @param fieldNames Field names
     */
    public void setFieldVisible(boolean visible, String... fieldNames) {
        if(visible) {
            setFieldVisible(fieldNames);
        } else {
            setFieldHidden(fieldNames);
        }
    }

    /**
     * Set fields visible. (By default fields are visible).
     *
     * @param visible To make it visible or not
     * @param fields Fields
     */
    public void setFieldVisible(boolean visible, HasValue<?, ?>... fields) {
        if(visible) {
            setFieldVisible(fields);
        } else {
            setFieldHidden(fields);
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
                if(field instanceof  Component) {
                    ((Component) field).setVisible(false);
                }
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
            if(field == null) {
                continue;
            }
            if(field instanceof  Component) {
                ((Component) field).setVisible(false);
            }
            fieldName = form.getFieldName(field);
            if(fieldName != null) {
                hidden.add(fieldName);
            }
            hiddenFields.add(field);
        }
    }

    /**
     * This method is invoked to determine if a field needs to be made editable or not.
     *
     * @param fieldName Name of the field
     * @return True or false.
     */
    public boolean isFieldEditable(String fieldName) {
        return !readOnly.contains(fieldName);
    }

    /**
     * This method is invoked to determine if a field needs to be made editable or not.
     *
     * @param field Field
     * @return True or false.
     */
    public boolean isFieldEditable(HasValue<?, ?> field) {
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
        HasValue<?, ?> field;
        for(String fieldName: fieldNames) {
            if(fieldName == null || fieldName.isEmpty()) {
                continue;
            }
            readOnly.remove(fieldName);
            field = form.getField(fieldName);
            if(field != null) {
                readOnlyFields.remove(field);
                if(!isReadOnly()) {
                    field.setReadOnly(false);
                }
            }
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
        String fieldName;
        for(HasValue<?, ?> field: fields) {
            if(field == null) {
                continue;
            }
            fieldName = form.getFieldName(field);
            if(fieldName != null) {
                readOnly.remove(fieldName);
            }
            readOnlyFields.remove(field);
            if(!isReadOnly()) {
                field.setReadOnly(false);
            }
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
                field.setReadOnly(true);
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
            if(field == null) {
                continue;
            }
            field.setReadOnly(true);
            fieldName = form.getFieldName(field);
            if(fieldName != null) {
                readOnly.add(fieldName);
            }
            readOnlyFields.add(field);
        }
    }

    /**
     * Set fields read only. (By default fields are editable).
     *
     * @param readOnly Read-only or not.
     * @param fieldNames Field names
     */
    public void setFieldReadOnly(boolean readOnly, String... fieldNames) {
        if(readOnly) {
            setFieldReadOnly(fieldNames);
        } else {
            setFieldEditable(fieldNames);
        }
    }

    /**
     * Set fields read only. (By default fields are editable).
     *
     * @param readOnly Read-only or not.
     * @param fields Fields
     */
    public void setFieldReadOnly(boolean readOnly, HasValue<?, ?>... fields) {
        if(readOnly) {
            setFieldReadOnly(fields);
        } else {
            setFieldEditable(fields);
        }
    }

    /**
     * This method is invoked to determine the label used for displaying the field. The default implementation determine it by invoking
     * {@link ApplicationEnvironment#createLabel(String)}.
     *
     * @param fieldName Name of the field
     * @return Label
     */
    protected String getLabel(String fieldName) {
        throw FIELD_ERROR;
    }


    /**
     * Set label for a specific field. Label will be set only if the field has a setLabel(String) method.
     *
     * @param fieldName Name of the field for which label needs to be set
     * @param label Label to set
     */
    public final void setFieldLabel(String fieldName, String label) {
        form.setFieldLabel(fieldName, label);
    }

    /**
     * Set label for a specific field. Label will be set only if the field has a setLabel(String) method.
     *
     * @param field Field for which label needs to be set
     * @param label Label to set
     */
    public final void setFieldLabel(HasValue<?, ?> field, String label) {
        form.setFieldLabel(field, label);
    }

    /**
     * Get label for the given field.
     *
     * @param fieldName Name of the field
     * @return Label string if "getLabel" method exists in the field, otherwise null.
     */
    public final String getFieldLabel(String fieldName) {
        return fieldName == null ? null : getFieldLabel(getField(fieldName));
    }

    /**
     * Get label for the given field.
     *
     * @param field Field
     * @return Label string if "getLabel" method exists in the field, otherwise null.
     */
    public final String getFieldLabel(HasValue<?, ?> field) {
        return field == null ? null : form.getFieldLabel(field);
    }

    /**
     * Attach a field to the form. This is invoked whenever a field is added to the form. The default implementation adds the component of
     * the field (if it is a component) to the field container.
     *
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void attachField(String fieldName, HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    /**
     * This method is invoked whenever a field is attached to the form.
     *
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void fieldAttached(String fieldName, HasValue<?, ?> field) {
    }

    /**
     * Detach a field from the form. This is invoked whenever a field is removed to the form. The default implementation removes the component of
     * the field (if it is a component) from the field container.
     *
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void detachField(String fieldName, HasValue<?, ?> field) {
        throw FIELD_ERROR;
    }

    /**
     * This method is invoked whenever a field is detached from the form.
     *
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void fieldDetached(String fieldName, HasValue<?, ?> field) {
    }

    /**
     * Error messages of the form are typically displayed using {@link com.vaadin.flow.component.notification.Notification}. However,
     * one can set any {@link HasText} for that.
     *
     * @param display Error messages will be displayed on this
     */
    public void setErrorDisplay(HasText display) {
        form.setErrorDisplay(display);
    }

    /**
     * Get the current error display of the form. (See {@link #setErrorDisplay(HasText)}).
     *
     * @return Current error display.
     */
    public HasText getErrorDisplay() {
        return form.getErrorDisplay();
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
     * Add a new line to the form.
     */
    public void newLine() {
        form.newLine();
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
     * @param component Component for which column span to be retrieved
     * @return Column span for the component.
     */
    public int getColumnSpan(Component component) {
        return form.getColumnSpan(component);
    }

    /**
     * Connect fields so that a change in any of it is propagated to others within that connected group.
     *
     * @param fieldNames Fields to connect.
     * @return True if connection is established successfully.
     */
    public boolean connect(String... fieldNames) {
        return form.connect(fieldNames);
    }

    /**
     * Connect fields so that a change in any of it is propagated to others within that connected group.
     *
     * @param fields Fields to connect.
     * @return True if connection is established successfully.
     */
    public boolean connect(HasValue<?, ?>... fields) {
        return form.connect(fields);
    }

    /**
     * Connect fields so that a change in any of it is propagated to others within that connected group.
     *
     * @param fields Fields to connect.
     * @return True if connection is established successfully.
     */
    public boolean connect(Collection<HasValue<?, ?>> fields) {
        return form.connect(fields);
    }

    @Override
    public void clean() {
        getForm().data.getErrorDisplay().setText(null);
        super.clean();
    }

    /**
     * Get all fields.
     *
     * @return Stream of fields.
     */
    public final Stream<HasValue<?, ?>> streamFieldsCreated() {
        return form.streamFieldsCreated();
    }

    /**
     * Get all fields names.
     *
     * @return Stream of field names.
     */
    public final Stream<String> streamFieldNamesCreated() {
        return form.streamFieldNamesCreated();
    }

    @Override
    public void clearAlerts() {
        super.clearAlerts();
        getForm().data.getErrorDisplay().setText(null);
    }
}