package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicListField;
import com.storedobject.vaadin.util.CompositeField;
import com.vaadin.flow.component.AbstractField;

import java.util.Collection;
import java.util.List;

public class ListField<T> extends
        CompositeField.SingleField<T, ListField<T>, BasicListField<T>, AbstractField.ComponentValueChangeEvent<BasicListField<T>, T>> {

    public ListField(Collection<T> items) {
        this(null, items);
    }

    public ListField(String label, Collection<T> items) {
        super(new BasicListField<>(items), getDefault(items));
        setLabel(label);
    }

    private static <O> O getDefault(Collection<O> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        if(items instanceof List) {
            return ((List<O>) items).get(0);
        }
        return items.stream().findFirst().get();
    }

    public int getIndex(T value) {
        return getField().getField().getIndex(value);
    }

    public T getValue(int index) {
        return getField().getField().getValue(index);
    }

    public void setItems(Collection<T> items) {
        getField().getField().setItems(items);
    }

    public void setScrollHeight(String height, int minimumItemCount) {
        getField().getField().setScrollHeight(height, minimumItemCount);
    }
}