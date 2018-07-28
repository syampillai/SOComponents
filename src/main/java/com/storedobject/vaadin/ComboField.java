package com.storedobject.vaadin;

import com.vaadin.flow.component.combobox.ComboBox;

import java.util.Arrays;
import java.util.Collection;

public class ComboField<T> extends ComboBox<T> implements Field<T> {

    private Collection<T> list;

    public ComboField(T... list) {
        this(null, list);
    }

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
        int i = 0;
        for(T loop: list) {
            if(i == index) {
                return loop;
            }
            ++i;
        }
        return null;
    }
}
