package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Field;
import com.vaadin.flow.component.*;
import com.vaadin.flow.shared.Registration;

public abstract class CompositeSingleField<P, F extends Field<P>, S extends CompositeSingleField<P, F, S, T>, T>
        extends AbstractCompositeField<Component, S, T> implements Field<T>, Focusable<S> {

    protected Field<P> field;
    private boolean trackers = false;

    protected CompositeSingleField(T defaultValue) {
        super(defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onAttach(AttachEvent attachEvent) {
        if(field == null) {
            createField();
        }
        if(!trackers) {
            trackers = true;
            for(HasValue hv: fieldList()) {
                hv.addValueChangeListener(e -> {
                    if (!e.isFromClient()) {
                        return;
                    }
                    T value;
                    try {
                        value = getModelValue(hv);
                    } catch (Throwable error) {
                        value = getEmptyValue();
                    }
                    setPresentationValue(value);
                    setModelValue(value, true);
                });
            }
            createTrackers();
        }
        super.onAttach(attachEvent);
    }

    protected HasValue[] fieldList() {
        return new HasValue[]{ (HasValue)field };
    }

    protected T getModelValue(HasValue field) {
        return getModelValue(this.field.getValue());
    }

    protected void createTrackers() {
    }

    protected abstract void createField();

    protected Field<P> getField() {
        if(field == null) {
            createField();
        }
        return field;
    }

    @Override
    protected Component initContent() {
        if(field == null) {
            createField();
        }
        return (Component)field;
    }

    @Override
    public void setValue(T value) {
        if(field == null) {
            createField();
        }
        super.setValue(sanitizeValue(value));
    }

    protected T sanitizeValue(T value) {
        return value == null ? getEmptyValue() : value;
    }

    @Override
    protected void setPresentationValue(T value) {
        field.setValue(getPresentationValue(value));
        setExtraPresentationValue(value);
    }

    protected void setExtraPresentationValue(@SuppressWarnings("unused") T value) {
    }

    protected abstract P getPresentationValue(T value);

    protected abstract T getModelValue(P presentationValue);

    public void setLabel(String label) {
        field.setLabel(label);
    }

    public String getLabel() {
        return field.getLabel();
    }

    public void setTabIndex(int tabIndex) {
        ((Focusable)field).setTabIndex(tabIndex);
    }

    public int getTabIndex() {
        return ((Focusable)field).getTabIndex();
    }

    public void focus() {
        field.focus();
    }

    public void blur() {
        field.blur();
    }

    @Override
    public void setEnabled(boolean enabled) {
        field.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return field.isEnabled();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        ((HasValueAndElement)field).setRequiredIndicatorVisible(requiredIndicatorVisible);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return ((HasValueAndElement)field).isRequiredIndicatorVisible();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        ((HasValueAndElement)field).setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return ((HasValueAndElement)field).isReadOnly();
    }

    @Override
    public void setAutofocus(boolean autofocus) {
        field.setAutofocus(autofocus);
    }

    @Override
    public boolean isAutofocus() {
        return field.isAutofocus();
    }

    @Override
    protected boolean valueEquals(T value1, T value2) {
        if(value1 == null || value2 == null) {
            return false;
        }
        return super.valueEquals(value1, value2);
    }

    public void setWidth(String width) {
        field.setWidth(width);
    }

    public void setHeight(String height) {
        field.setHeight(height);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<S, T>> listener) {
        return super.addValueChangeListener(listener);
    }

    protected boolean isValid() {
        return true;
    }
}