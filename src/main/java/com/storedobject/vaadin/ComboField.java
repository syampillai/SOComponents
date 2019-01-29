package com.storedobject.vaadin;

import com.vaadin.flow.component.combobox.ComboBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A thin wrapper around Vaadin's {@link ComboBox}.
 * @param <T> Type of the field's value.
 * @author Syam
 */
public class ComboField<T> extends ComboBox<T> implements Field<T> {

    private Collection<T> list;

    @SafeVarargs
    public ComboField(T... list) {
        this(null, list);
    }

    @SafeVarargs
    public ComboField(String label, T... list) {
        this(label, Arrays.asList(list));
    }

    public ComboField(Collection<T> list) {
        this(null, list);
    }

    public ComboField(String label, Collection<T> list) {
        super(label, list);
        this.list = list;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return super.isReadOnly();
    }

    @Override
    public void focus() {
        super.focus();
    }

    @Override
    public void blur() {
        super.blur();
    }

    public int getIndex(T item) {
        if(item == null) {
            return -1;
        }
        if(list instanceof List) {
            return ((List<T>) list).indexOf(item);
        }
        int i = 0;
        for(T loop: list) {
            if(loop.equals(item)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public T getValue(int index) {
        if(index < 0 || index >= list.size()) {
            return null;
        }
        if(list instanceof List) {
            return ((List<T>) list).get(index);
        }
        int i = 0;
        for(T loop: list) {
            if(i == index) {
                return loop;
            }
            ++i;
        }
        return null;
    }

    @Override
    public T getEmptyValue() {
        return null;
    }
}
