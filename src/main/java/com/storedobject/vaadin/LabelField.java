package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicLabelField;
import com.storedobject.vaadin.util.CompositeField;
import com.vaadin.flow.component.AbstractField;

import java.util.List;

public class LabelField<T> extends
        CompositeField.SingleField<T, LabelField<T>, BasicLabelField<T>, AbstractField.ComponentValueChangeEvent<BasicLabelField<T>, T>> {

    public LabelField(List<T> items) {
        this(null, items);
    }

    public LabelField(String label, List<T> items) {
        super(new BasicLabelField<>(items), getDefault(items));
        setLabel(label);
    }

    private static <O> O getDefault(List<O> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public int getIndex(T value) {
        return getField().getField().getIndex(value);
    }

    public T getValue(int index) {
        return getField().getField().getValue(index);
    }

    public void setItems(List<T> items) {
        getField().getField().setItems(items);
    }
}