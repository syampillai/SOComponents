package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Data;
import com.storedobject.vaadin.util.FieldValueHandler;
import com.vaadin.flow.component.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Class to represent a "data entry form". A "field" in the form is a {@link HasValue}. Form internally keeps
 * {@link Data} that is a structure
 * that keeps data for all the "fields" of the form.
 * <p>A form is not used directly in most cases. Instead, a {@link View} derived from {@link AbstractDataForm}
 * is used where a form is
 * already embedded. All overridable methods in the form can be defined in {@link AbstractDataForm} too.</p>
 * <p>If at all required to use it directly, one should use {@link Form} instead of {@link AbstractForm}.</p>
 *
 * @param <D> Type of data object in the form
 * @author Syam
 */
public abstract class AbstractForm<D> extends Composite<Component> {

    /**
     * Class of the data object in the form.
     */
    protected final Class<D> objectClass;
    /**
     * Current instance of the data object in the form.
     */
    protected D objectData;
    /**
     * The field container of the form.
     */
    protected HasComponents container;
    /**
     * The "data" associated with this form.
     */
    protected final Data<D> data;
    private boolean loadPending = false;
    private View view;
    private int columns = 2;
    /**
     * This will be used to check whether a field needs to be included or not.
     */
    IncludeField includeField = new IncludeFieldArray();

    /**
     * Construct a form.
     *
     * @param objectClass Type of the data object
     */
    public AbstractForm(Class<D> objectClass) {
        this(objectClass, null);
    }

    /**
     * Construct a form with a specified field container.
     *
     * @param objectClass Type of the data object
     * @param container Field container of the form.
     */
    public AbstractForm(Class<D> objectClass, HasComponents container) {
        this(objectClass, container, null);
        data.setFieldValueHandler(new ValueHandler());
    }

    /**
     * For internal use only.
     *
     * @param objectClass Type of the data object
     * @param container Field container of the form.
     * @param dummy Dummy value, not used
     */
    protected AbstractForm(Class<D> objectClass, HasComponents container, Object dummy) {
        this.objectClass = objectClass;
        this.data = new Data<>(this);
        this.container = container;
    }

    @Override
    protected final Component initContent() {
        return (Component) container;
    }

    @Override
    public final Component getContent() {
        return (Component) getContainer();
    }

    /**
     * Get the data binder. (For internal use only).
     *
     * @return Data binder associated with this form.
     */
    public Binder<D> getBinder() {
        return data.getBinder();
    }

    /**
     * Set "include field checker". It will determine if a field can be added or not.
     *
     * @param includeField The "include field checker"
     */
    public void setIncludeFieldChecker(IncludeField includeField) {
        ((IncludeFieldArray)this.includeField).clear();
        if(includeField != null) {
            ((IncludeFieldArray)this.includeField).add(includeField);
        }
    }

    /**
     * Add an "include field checker" to the existing chain. If any one of the {@link IncludeField} in the chain
     * returns <code>false</code> for a specific field, the field will not be included in the form.
     *
     * @param includeField The "include field checker" to add.
     * @return Registration.
     */
    public Registration addIncludeFieldChecker(IncludeField includeField) {
        if(includeField == null) {
            return () -> {};
        }
        ((IncludeFieldArray)this.includeField).add(includeField);
        return () -> ((IncludeFieldArray)this.includeField).remove(includeField);
    }

    private static HasComponents createDefaultContainer() {
        return new FormLayout();
    }

    /**
     * Create the field container of this form. This will be invoked only if no container was already set.
     * Default implementation creates a {@link FormLayout}.
     *
     * @return Field container created.
     */
    protected HasComponents createContainer() {
        HasComponents c = view instanceof AbstractDataForm ? ((AbstractDataForm<?>)view).createFieldContainer() : null;
        return c == null ? createDefaultContainer() : c;
    }

    /**
     * For internal use only.
     */
    protected void generateFieldNames() {
    }

    /**
     * Get the field container of the form.
     *
     * @return Field container.
     */
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
                    if(includeField.includeField(n)) {
                        HasValue<?, ?> field = createFieldInt(n, getLabel(n));
                        if (n != null) {
                            addField(n, field);
                        }
                    }
                });
            }
            setColumns(columns);
            constructed();
            if(loadPending) {
                load();
            }
        }
        return container;
    }

    private HasValue<?, ?> createFieldInt(String fieldName, String label) {
        HasValue<?, ?> field = createField(fieldName, label);
        if(field == null) {
            field = createField(fieldName);
            if(field != null && label != null) {
                setFieldLabel(fieldName, label);
            }
        }
        return field;
    }

    /**
     * This method acts as a hook to the field customizer.
     *
     * @param fieldName Field name.
     * @param field Field.
     */
    void customize(String fieldName, HasValue<?, ?> field) {
    }

    /**
     * This method is invoked when the "fields" are created and the form is ready to display.
     */
    protected void constructed() {
        if(view instanceof AbstractDataForm) {
            ((AbstractDataForm<?>) view).fireFormConstructed();
        }
    }

    private void addFieldInt(String fieldName, HasValue<?, ?> field) {
        if(!includeField.includeField(fieldName)) {
            return;
        }
        data.addField(fieldName, field);
        customize(fieldName, field);
        attachF(fieldName, field);
    }

    /**
     * Get the field container of the form as a component.
     *
     * @return Field container as a component. <code>null</code> is returned if the field container is not a component.
     */
    public final Component getComponent() {
        getContainer();
        return container instanceof Component ? (Component)container : null;
    }

    /**
     * Get the field names. This is invoked to determine the fields to be created for constructing the form.
     *
     * @return Stream of fields.
     */
    protected Stream<String> getFieldNames() {
        //noinspection unchecked
        return view instanceof AbstractDataForm ? ((AbstractDataForm<D>) view).getFieldNames() : null;
    }

    /**
     * Create the field for a particular "field name".
     *
     * @param fieldName Field name
     * @return Field created.
     */
    protected HasValue<?, ?> createField(String fieldName) {
        //noinspection unchecked
        return view instanceof AbstractDataForm ? ((AbstractDataForm<D>) view).createField(fieldName) : null;
    }

    /**
     * Create the field for a particular "field name".
     *
     * @param fieldName Field name
     * @param label Label for the field
     * @return Field created.
     */
    protected HasValue<?, ?> createField(String fieldName, String label) {
        //noinspection unchecked
        return view instanceof AbstractDataForm ? ((AbstractDataForm<D>) view).createField(fieldName, label) : null;
    }

    /**
     * Add a field to the form. The field will not be having a "field name".
     *
     * @param field Field to be added
     */
    public void addField(HasValue<?, ?> field) {
        attachF(data.addField(field), field);
    }

    /**
     * Add multiple fields to the form. These fields will not be having "field names".
     *
     * @param fields Fields to be added
     */
    public void addFields(HasValue<?, ?>... fields) {
        if(fields != null) {
            for(HasValue<?, ?> f: fields) {
                if(f != null) {
                    addField(f);
                }
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
        addFieldInt(fieldName, field);
    }

    /**
     * Remove a field from the form.
     *
     * @param fieldName Name of the field to be removed
     */
    public void removeField(String fieldName) {
        detachF(fieldName, data.removeField(fieldName));
    }

    /**
     * Remove fields from the form.
     *
     * @param fieldNames Names of the field to be removed
     */
    public void removeField(String... fieldNames) {
        for(String id: fieldNames) {
            removeField(id);
        }
    }

    /**
     * Remove a field from the form.
     *
     * @param field Field to be removed.
     */
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
            if(field instanceof ViewDependent && view != null) {
                ((ViewDependent) field).setDependentView(view);
            }
            if(view instanceof AbstractDataForm) {
                ((AbstractDataForm<?>) view).fieldAttached(fieldName, field);
            }
        }
    }

    private void detachF(String fieldName, HasValue<?, ?> field) {
        if(fieldName != null && field != null) {
            detachField(fieldName, field);
            if(view instanceof AbstractDataForm) {
                ((AbstractDataForm<?>) view).fieldDetached(fieldName, field);
            }
        }
    }

    /**
     * Attach a field to the form. This is invoked whenever a field is added to the form. The default implementation
     * adds the field component (if it is a component) to the field container.
     *
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void attachField(String fieldName, HasValue<?, ?> field) {
        if(view instanceof AbstractDataForm) {
            try {
                //noinspection unchecked
                ((AbstractDataForm<D>) view).attachField(fieldName, field);
                return;
            } catch (AbstractDataForm.FieldError ignored) {
            }
        }
        if(field instanceof Component) {
            getContainer().add((Component)field);
        }
    }

    /**
     * Detach a field from the form. This is invoked whenever a field is removed to the form. The default
     * implementation removes the field component (if it is a component) from the field container.
     *
     * @param fieldName Name of the field
     * @param field Field
     */
    protected void detachField(String fieldName, HasValue<?, ?> field) {
        if(view instanceof AbstractDataForm) {
            try {
                //noinspection unchecked
                ((AbstractDataForm<D>) view).detachField(fieldName, field);
                return;
            } catch (AbstractDataForm.FieldError ignored) {
            }
        }
        if(field instanceof Component) {
            getContainer().remove((Component)field);
        }
    }

    /**
     * Add components to the form's field container.
     *
     * @param components Components to add
     */
    public void add(Component... components) {
        getContainer().add(components);
    }

    /**
     * Remove components from the form's field container.
     *
     * @param components Components to remove
     */
    public void remove(Component... components) {
        getContainer().remove(components);
    }

    /**
     * Remove all components from the form's field container.
     */
    public void removeAll() {
        getContainer().removeAll();
    }

    /**
     * Set number of responsive columns for the form. (This will work only when the container is of type
     * {@link FormLayout}).
     *
     * @param columns Number of responsive columns required
     */
    public void setColumns(int columns) {
        if(container instanceof FormLayout) {
            ((FormLayout) container).setColumns(columns);
        }
        this.columns = columns;
    }

    /**
     * Get number of responsive columns for the form. (This will work only when the container is of type
     * {@link FormLayout}).
     *
     * @return Number of responsive columns of the form.
     */
    public final int getColumns() {
        if(container instanceof FormLayout) {
            return ((FormLayout) container).getColumns();
        }
        return columns;
    }

    /**
     * Add a new line to the form. (This will work only when the container is of type {@link FormLayout}).
     */
    public void newLine() {
        if(container instanceof FormLayout) {
            ((FormLayout) container).newLine();
        }
    }

    /**
     * Set number of columns to span for a particular component. (This will work only when the container is of type
     * {@link FormLayout}).
     *
     * @param component Component for which column span to be set
     * @param columnSpan Number of columns to span
     */
    public void setColumnSpan(Component component, int columnSpan) {
        component.getElement().setAttribute("colspan", "" + Math.min(Math.max(1, columnSpan),
                getColumns()));
    }

    /**
     * Get the column span of a component. (This will work only when the container is of type {@link FormLayout}).
     *
     * @param component Component for which column span to be retrieved
     * @return Column span for the component.
     */
    public int getColumnSpan(Component component) {
        try {
            return Integer.parseInt(component.getElement().getAttribute("colspan"));
        } catch (Throwable error) {
            return 1;
        }
    }

    /**
     * Load data to the fields from the internal data structure.
     */
    public void load() {
        if(container == null) {
            loadPending = true;
            return;
        }
        data.loadValues();
    }

    /**
     * Save data from the fields to the internal data structure.
     *
     * @return True if data is saved successfully.
     */
    public boolean commit() {
        return data.saveValues();
    }

    /**
     * Print values from the internal data structure to {@link System#err} (Used for debugging purposes).
     */
    public void dumpValues() {
        data.forEach((k, v) -> System.err.println(k + " = " + v));
    }

    /**
     * Get all fields.
     *
     * @return Stream of fields.
     */
    public final Stream<HasValue<?, ?>> streamFieldsCreated() {
        return data.getFields();
    }

    /**
     * Get all fields names.
     *
     * @return Stream of field names.
     */
    public final Stream<String> streamFieldNamesCreated() {
        return data.getFieldNames();
    }

    /**
     * Get a field associated with a field name.
     *
     * @param fieldName Name of the field
     * @return Field.
     */
    public final HasValue<?, ?> getField(String fieldName) {
        return data.getField(fieldName);
    }

    /**
     * Get a field name associated with a field.
     *
     * @param field Field
     * @return Name of the field.
     */
    public final String getFieldName(HasValue<?, ?> field) {
        return data.getName(field);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data
     * (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param field Field
     */
    public void setRequired(HasValue<?, ?> field) {
        setRequired(field, true, null);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data
     * (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param fieldName Field name
     */
    public void setRequired(String fieldName) {
        setRequired(fieldName, true, null);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data
     * (checked by invoking {@link HasValue#isEmpty()}),
     * {@link #commit()} will fail.
     *
     * @param field Field
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(HasValue<?, ?> field, String errorMessage) {
        setRequired(field, true, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking
     * {@link HasValue#isEmpty()}), {@link #commit()} will fail.
     *
     * @param fieldName Field name
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(String fieldName, String errorMessage) {
        setRequired(fieldName, true, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking
     * {@link HasValue#isEmpty()}), {@link #commit()} will fail.
     *
     * @param field Field
     * @param required True or false
     */
    public void setRequired(HasValue<?, ?> field, boolean required) {
        setRequired(field, required, null);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking
     * {@link HasValue#isEmpty()}), {@link #commit()} will fail.
     *
     * @param fieldName Field name
     * @param required True or false
     */
    public void setRequired(String fieldName, boolean required) {
        setRequired(fieldName, required, null);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking
     * {@link HasValue#isEmpty()}), {@link #commit()} will fail.
     *
     * @param field Field
     * @param required True or false
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(HasValue<?, ?> field, boolean required, String errorMessage) {
        data.setRequired(field, required, errorMessage);
    }

    /**
     * Set the "required" attribute of a field. If set to true, with empty data (checked by invoking
     * {@link HasValue#isEmpty()}), {@link #commit()} will fail.
     *
     * @param fieldName Field name
     * @param required True or false
     * @param errorMessage Error message to show when the field is empty
     */
    public void setRequired(String fieldName, boolean required, String errorMessage) {
        setRequired(getField(fieldName), required, errorMessage);
    }

    /**
     * Add a validator for the field. Validator should return <code>true</code> if the field is valid.
     *
     * @param field Field
     * @param validator Validator
     * @param <F> Type of the field value.
     */
    public <F> void addValidator(HasValue<?, F> field, Function<F, Boolean> validator) {
        addValidator(field, validator, null);
    }

    /**
     * Add a validator for the field. Validator should return <code>true</code> if the field is valid.
     *
     * @param field Field
     * @param validator Validator
     * @param errorMessage Error message to be displayed if the field is not valid
     * @param <F> Type of the field value.
     */
    public <F> void addValidator(HasValue<?, F> field, Function<F, Boolean> validator, String errorMessage) {
        data.addValidator(field, validator, errorMessage);
    }

    /**
     * Mark a field as not valid.
     *
     * @param field Field
     */
    public static void markError(HasValue<?, ?> field) {
        setInvalid(field, true);
    }

    /**
     * Clear the error status associated with a field.
     *
     * @param field Field
     */
    public static void clearError(HasValue<?, ?> field) {
        setInvalid(field, false);
    }

    private static void setInvalid(HasValue<?, ?> field, boolean invalid) {
        if(field instanceof Element) {
            ((Element) field).setAttribute("invalid", invalid);
        }
    }

    /**
     * Clear values of all the fields.
     */
    public void clearFields() {
        data.clearFields();
    }

    /**
     * Clear error status of all the fields.
     */
    public void clearErrors() {
        data.clearErrors();
        data.getErrorDisplay().setText(null);
    }

    /**
     * Error messages of the form are typically displayed using
     * {@link com.vaadin.flow.component.notification.Notification}. However,
     * one can set any {@link HasText} for that.
     *
     * @param display Error messages will be displayed on this
     */
    public void setErrorDisplay(HasText display) {
        data.setErrorDisplay(display);
    }


    /**
     * Get the current error display of the form. (See {@link #setErrorDisplay(HasText)}).
     *
     * @return Current error display.
     */
    public HasText getErrorDisplay() {
        return data.getErrorDisplay();
    }

    /**
     * Set the form read only.
     *
     * @param readOnly True or false
     */
    public void setReadOnly(boolean readOnly) {
        data.setReadOnly(readOnly);
    }

    /**
     * Check whether the form is "read only" or not.
     *
     * @return True or false.
     */
    public boolean isReadOnly() {
        return data.isReadOnly();
    }

    /**
     * This method is invoked to determine if a field needs to be made visible or not.
     *
     * @param fieldName Name of the field
     * @return True or false.
     */
    public boolean isFieldVisible(String fieldName) {
        if(view instanceof AbstractDataForm) {
            return ((AbstractDataForm<?>) view).isFieldVisible(fieldName);
        }
        return true;
    }

    /**
     * This method is invoked to determine if a field needs to be made visible or not.
     *
     * @param field Field
     * @return True or false.
     */
    public boolean isFieldVisible(HasValue<?, ?> field) {
        if(view instanceof AbstractDataForm) {
            //noinspection unchecked
            return ((AbstractDataForm<D>) view).isFieldVisible(field);
        }
        return true;
    }

    /**
     * This method is invoked to determine if a field needs to be made editable or not.
     *
     * @param fieldName Name of the field
     * @return True or false.
     */
    public boolean isFieldEditable(String fieldName) {
        if(view instanceof AbstractDataForm) {
            return ((AbstractDataForm<?>) view).isFieldEditable(fieldName);
        }
        return true;
    }

    /**
     * This method is invoked to determine if a field needs to be made editable or not.
     *
     * @param field Field
     * @return True or false.
     */
    public boolean isFieldEditable(HasValue<?, ?> field) {
        if(view instanceof AbstractDataForm) {
            //noinspection unchecked
            return ((AbstractDataForm<D>) view).isFieldEditable(field);
        }
        return true;
    }

    /**
     * This method is invoked to determine the label used for displaying the field. The default implementation
     * determine it by invoking {@link ApplicationEnvironment#createLabel(String)}.
     *
     * @param fieldName Name of the field
     * @return Label
     */
    public String getLabel(String fieldName) {
        if(view instanceof AbstractDataForm) {
            try {
                return ((AbstractDataForm<?>) view).getLabel(fieldName);
            } catch (AbstractDataForm.FieldError ignored) {
            }
        }
        return Objects.requireNonNull(ApplicationEnvironment.get()).createLabel(fieldName);
    }

    /**
     * Set label for a specific field. Label will be set only if the field has a setLabel(String) method.
     *
     * @param fieldName Name of the field for which label needs to be set
     * @param label Label to set
     */
    public final void setFieldLabel(String fieldName, String label) {
        setFieldLabel(getField(fieldName), label);
    }

    /**
     * Set label for a specific field. Label will be set only if the field has a setLabel(String) method.
     *
     * @param field Field for which label needs to be set
     * @param label Label to set
     */
    public final void setFieldLabel(HasValue<?, ?> field, String label) {
        if(field == null) {
            return;
        }
        try {
            field.getClass().getMethod("setLabel", String.class).invoke(field, label);
        } catch (Throwable ignored) {
        }
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
        return field == null ? null : Data.getLabel(field);
    }

    /**
     * Connect fields so that a change in any of it is propagated to others within that connected group.
     *
     * @param fieldNames Fields to connect.
     * @return True if connection is established successfully.
     */
    public boolean connect(String... fieldNames) {
        List<HasValue<?, ?>> fieldList = new ArrayList<>();
        HasValue<?, ?> field;
        for(String fieldName: fieldNames) {
            field = getField(fieldName);
            if(field != null) {
                fieldList.add(field);
            }
        }
        return connect(fieldList);
    }

    /**
     * Connect fields so that a change in any of it is propagated to others within that connected group.
     *
     * @param fields Fields to connect.
     * @return True if connection is established successfully.
     */
    public boolean connect(HasValue<?, ?>... fields) {
        List<HasValue<?, ?>> fieldList = new ArrayList<>();
        Collections.addAll(fieldList, fields);
        return connect(fieldList);
    }

    /**
     * Connect fields so that a change in any of it is propagated to others within that connected group.
     *
     * @param fields Fields to connect.
     * @return True if connection is established successfully.
     */
    public boolean connect(Collection<HasValue<?, ?>> fields) {
        return data.connect(fields);
    }

    /**
     * Set an associated view for this form.
     *
     * @param view View
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Get the associated view of this form.
     *
     * @return View.
     */
    public View getView() {
        return view;
    }

    /**
     * Create an instance of the data object. Default implementation tries to invoke the default constructor
     * to create an instance.
     * @return Newly created data object.
     */
    protected D createObjectInstance() {
        try {
            return objectClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException ignored) {
        }
        return null;
    }

    /**
     * Get the instance of the currently editing data object. (A new data object will be created by invoking
     * {@link #createObjectInstance()} if there is no current object instance set).
     * @return Instance of the currently loaded object.
     */
    public D getObject() {
        return getObject(true);
    }

    /**
     * Get the instance of the currently editing data object. A new data object will be created by invoking
     * {@link #createObjectInstance()} if there is no current object instance set and the parameter passed
     * is <code>true</code>.
     * @param create Whether to create a new instance or not
     * @return Instance of the currently loaded object.
     */
    public D getObject(boolean create) {
        if(objectData == null && create) {
            objectData = createObjectInstance();
        }
        return objectData;
    }

    /**
     * Set the current object. Fields will be loaded.
     * @param object Object to set
     */
    public void setObject(D object) {
        setObject(object, true);
    }

    /**
     * Set the current object. Fields will be loaded.
     * @param object Object to set
     * @param load Whether to load the fields or not
     */
    public void setObject(D object, boolean load) {
        objectData = object;
        if(load) {
            if(object == null) {
                clearFields();
            } else {
                load();
            }
        }
    }

    /**
     * Basic value-handler implementation.
     *
     * @author Syam
     */
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
            return true;
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

    private static class IncludeFieldArray extends ArrayList<IncludeField> implements IncludeField {

        @Override
        public boolean includeField(String fieldName) {
            return stream().allMatch(i -> i.includeField(fieldName));
        }
    }
}