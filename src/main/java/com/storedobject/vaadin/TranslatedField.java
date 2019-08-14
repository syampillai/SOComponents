package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Validation;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasValue;
import java.util.function.BiFunction;

/**
 * A field that translates value from the value of an internal field.
 *
 * @param <T> Value type of the field
 * @param <I> Value type of the internal field
 * @author Syam
 */
public class TranslatedField<T, I> extends CustomField<T> {

    private final HasValue<?, I> field;
    private final BiFunction<HasValue<?, I>, T, I> convertT2I;
    private final BiFunction<HasValue<?, I>, I, T> convertI2T;

    /**
     * Constructor.
     *
     * @param field Internal field
     * @param convertToValue Function to convert internal value to value of the field
     * @param convertToInternalValue Function to convert value of the field to the internal value
     */
    public TranslatedField(HasValue<?, I> field, BiFunction<HasValue<?, I>, I, T> convertToValue,
                           BiFunction<HasValue<?, I>, T, I> convertToInternalValue) {
        this(field, convertToValue, convertToInternalValue, convertToValue.apply(field, field.getEmptyValue()));
    }

    /**
     * Constructor.
     *
     * @param field Internal field
     * @param convertToValue Function to convert internal value to value of the field
     * @param convertToInternalValue Function to convert value of the field to the internal value
     * @param emptyValue Empty value for the field
     */
    public TranslatedField(HasValue<?, I> field, BiFunction<HasValue<?, I>, I, T> convertToValue,
                           BiFunction<HasValue<?, I>, T, I> convertToInternalValue, T emptyValue) {
        super(emptyValue);
        this.field = field;
        this.convertT2I = convertToInternalValue;
        this.convertI2T = convertToValue;
        if (field instanceof Component) {
            add((Component)field);
        }
        setPresentationValue(getEmptyValue());
        field.addValueChangeListener(e -> setModelValue(this.generateModelValue(), e.isFromClient()));
    }

    @Override
    protected T generateModelValue() {
        T v = null;
        I i = field.getValue();
        if(i != null) {
            v = convertI2T.apply(field, i);
        }
        return v == null ? getEmptyValue() : v;
    }

    @Override
    protected void setPresentationValue(T value) {
        field.setValue(convertT2I.apply(field, value));
    }

    /**
     * Get the embedded field.
     *
     * @return The internal field.
     */
    public HasValue<?, I> getField() {
        return field;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        field.setReadOnly(readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(field instanceof HasEnabled) {
            ((HasEnabled) field).setEnabled(enabled);
        }
    }

    @Override
    public boolean isInvalid() {
        if(super.isInvalid()) {
            return true;
        }
        if(field instanceof Validation) {
            return ((Validation) field).isInvalid();
        }
        return false;
    }
}