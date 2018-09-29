package com.storedobject.vaadin.util;

import com.vaadin.flow.component.listbox.ListBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BasicComboList<T> extends ListBox<T> {

    protected Collection<T> list;

    @SafeVarargs
    public BasicComboList(T... list) {
        this(Arrays.asList(list));
    }

    public BasicComboList(Collection<T> list) {
        this.list = list;
        setItems(list);
    }

    @Override
    public void setItems(Collection<T> items) {
        if(items == null) {
            return;
        }
        this.list = items;
        super.setItems(items);
        setFirstValue();
    }

    void setFirstValue() {
        setValue((list instanceof List) ? ((List<T>) list).get(0) : list.stream().findFirst().orElse(null));
    }

    public int getIndex() {
        return getIndex(getValue());
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
