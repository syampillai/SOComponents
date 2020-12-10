package com.storedobject.vaadin.util;

import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.dataview.ListBoxListDataView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BasicComboList<T> extends ListBox<T> {

    protected List<T> list;

    @SafeVarargs
    public BasicComboList(T... list) {
        this(Arrays.asList(list));
    }

    public BasicComboList(Collection<T> list) {
        setItems(list);
    }

    @Override
    public ListBoxListDataView<T> setItems(Collection<T> items) {
        if(items == null) {
            return setItems(new ArrayList<>());
        }
        this.list = new ArrayList<>(items);
        ListBoxListDataView<T> r = super.setItems(this.list);
        setFirstValue();
        return r;
    }

    protected void setFirstValue() {
        setValue(list.size() > 0 ? list.get(0) : null);
    }

    public int getIndex() {
        return getIndex(getValue());
    }

    public int getIndex(T item) {
        return list.indexOf(item);
    }

    public T getValue(int index) {
        return index < 0 || index >= list.size() ? null : list.get(index);
    }

    @Override
    public T getEmptyValue() {
        return null;
    }
}
