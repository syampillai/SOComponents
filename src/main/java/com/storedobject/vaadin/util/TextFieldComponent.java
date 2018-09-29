package com.storedobject.vaadin.util;

import com.storedobject.vaadin.HasElement;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractSinglePropertyField;

/**
 * Generic Text Field component
 * @author Syam
 * @param <C> Type of component
 */
public abstract class TextFieldComponent<C extends AbstractField<C, String>> extends AbstractSinglePropertyField<C, String> implements HasElement {

    /**
     * Constructor
     * @param initialValue Intial value
     * @param placeholder Placeholder
     */
    public TextFieldComponent(String initialValue, String placeholder) {
        super("value", "", false);
        setPlaceholder(placeholder);
        setValue(initialValue);
    }

    /**
     * Set value
     * @param value Value to set. For null values, empty string will be set
     */
    @Override
    public void setValue(String value) {
        super.setValue(value == null ? "" : value);
    }

    /**
     * Get the maximum character length allowed
     * @return Maximum length
     */
    public int getMaxLength() {
        return this.getElement().getProperty("minlength", 0);
    }

    /**
     * Set the maximum character length allowed
     * @param maxLength Maximum charaters allowed
     */
    public void setMaxLength(int maxLength) {
        this.getElement().setProperty("maxlength", maxLength);
    }

    /**
     * Get the minimum character length required
     * @return Minimum length required
     */
    public int getMinLength() {
        return this.getElement().getProperty("minlength", 0);
    }

    /**
     * Set the minimum character length required
     * @param minLength Minimum charaters required
     */
    public void setMinLength(int minLength) {
        this.getElement().setProperty("minlength", minLength);
    }

    /**
     * Get placeholder
     * @return Placeholder
     */
    public String getPlaceholder() {
        return this.getElement().getProperty("placeholder");
    }

    /**
     * Set placeholder
     * @param placeholder Placeholder to set
     */
    public void setPlaceholder(String placeholder) {
        this.getElement().setProperty("placeholder", placeholder == null ? "" : placeholder);
    }

    /**
     * Is this field read only?
     * @return True or false
     */
    public boolean isReadonly() {
        return this.getElement().getProperty("readonly", false);
    }

    /**
     * Set 'read only' flag
     * @param readonly True or false
     */
    public void setReadonly(boolean readonly) {
        this.getElement().setProperty("readonly", readonly);
    }

    /**
     * Is input required on this field?
     * @return True or false
     */
    public boolean isRequired() {
        return this.getElement().getProperty("required", false);
    }

    /**
     * 'Input required' flag
     * @param required True or false
     */
    public void setRequired(boolean required) {
        this.getElement().setProperty("required", required);
    }
}