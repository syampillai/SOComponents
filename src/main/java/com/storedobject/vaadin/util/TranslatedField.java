package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Field;
import com.vaadin.flow.component.HasValue;

import java.util.function.BiFunction;

public class TranslatedField<T, P, F extends Field<P>>
        extends CompositeSingleField<P, F, TranslatedField<T, P, F>, T> {

    private BiFunction<F, P, T> get;
    private BiFunction<F, T, P> set;

    public TranslatedField(T defaultValue, F field, BiFunction<F, P, T> get, BiFunction<F, T, P> set) {
        super(defaultValue);
        this.field = field;
        this.get = get;
        this.set = set;
    }

    @Override
    protected void createField() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected F getField() {
        return (F)super.getField();
    }

    @Override
    protected P getPresentationValue(T value) {
        return set.apply(getField(), value);
    }

    @Override
    protected T getModelValue(P presentationValue) {
        return get.apply(getField(), presentationValue);
    }

    @Override
    public boolean isEmpty() {
        return ((HasValue)getField()).isEmpty();
    }
}