package com.storedobject.vaadin;

import com.storedobject.vaadin.util.HasTextValue;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.HasPrefixAndSuffix;
import com.vaadin.flow.dom.Element;

import java.util.Objects;

/**
 * A "custom field" where the value is represented as a "text" using an internal text field.
 *
 * @param <T> Value type
 * @author Syam
 */
public abstract class CustomTextField<T> extends CustomField<T> implements HasPrefixAndSuffix, HasStyle {

    private HasTextValue field;
    private String emptyDisplay;

    /**
     * Constructor.
     *
     * @param defaultValue Default value
     */
    protected CustomTextField(T defaultValue) {
        super(defaultValue);
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        field.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        field.setHeight(height);
    }

    /**
     * Create the internal text field. By default, it creates a {@link com.vaadin.flow.component.textfield.TextField}.
     *
     * @return Text field to be used as internal field;
     */
    protected HasTextValue createField() {
        if(field != null) {
            return field;
        }
        return createFieldInt();
    }

    private HasTextValue createFieldInt() {
        field = new TF();
        field.setWidthFull();
        add((TF)field);
        ((TF)field).setPreventInvalidInput(true);
        customizeTextField(field);
        return field;
    }

    /**
     * Get the internal text field that represents the value of the field.
     *
     * @return Internal text field.
     */
    public HasTextValue getField() {
        if(field == null) {
            field = createField();
            if(field == null) {
                field = createFieldInt();
            }
            if(!(field instanceof TF)) {
                if(field instanceof Component) {
                    add((Component) field);
                }
                customizeTextField(field);
            }
        }
        return field;
    }

    /**
     * Customize the internal text field. This is invoked only when the text field is created.
     *
     * @param textField Internal text field
     */
    protected void customizeTextField(@SuppressWarnings("unused") HasTextValue textField) {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getField();
        super.onAttach(attachEvent);
    }

    protected abstract T getModelValue(String string);

    @Override
    protected T generateModelValue() {
        String fv1 = getField().getValue();
        T v = getModelValue(getField().getValue());
        String fv2 = (emptyDisplay == null || !Objects.equals(getEmptyValue(), v)) ? format(v) : emptyDisplay;
        if(!Objects.equals(fv1, fv2)) {
            getField().setValue(fv2);
        }
        return v;
    }

    @Override
    protected void setPresentationValue(T value) {
        getField().setValue(format(value));
    }

    protected String format(T value) {
        return value == null ? "" : value.toString();
    }

    /**
     * Set text to be displayed when the value of the field is empty.
     * @param placeholder Text to be displayed
     */
    public void setPlaceholder(String placeholder) {
        this.emptyDisplay = placeholder;
        if(placeholder == null) {
            return;
        }
        if(Objects.equals(getValue(), getEmptyValue()) && placeholder.equals(getField().getValue())) {
            return;
        }
        getField().setValue(placeholder);
    }

    @Override
    public void setPrefixComponent(Component component) {
        if(field instanceof HasPrefixAndSuffix) {
            ((HasPrefixAndSuffix) field).setPrefixComponent(component);
        }
    }

    @Override
    public Component getPrefixComponent() {
        return field instanceof HasPrefixAndSuffix ? ((HasPrefixAndSuffix)field).getPrefixComponent() : null;
    }

    @Override
    public void setSuffixComponent(Component component) {
        if(field instanceof HasPrefixAndSuffix) {
            ((HasPrefixAndSuffix) field).setSuffixComponent(component);
        }
    }

    @Override
    public Component getSuffixComponent() {
        return field instanceof HasPrefixAndSuffix ? ((HasPrefixAndSuffix)field).getSuffixComponent() : null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if(field instanceof HasValue) {
            ((HasValue<?, ?>)field).setReadOnly(readOnly);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(field instanceof HasEnabled) {
            ((HasEnabled)field).setEnabled(enabled);
        }
    }

    @Override
    public Element getInternalElement() {
        return field.getElement();
    }

    private static class TF extends com.vaadin.flow.component.textfield.TextField implements HasSize, HasTextValue {
    }
}