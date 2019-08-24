package com.storedobject.vaadin;

import com.vaadin.flow.component.combobox.ComboBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * A thin wrapper around Vaadin's {@link ComboBox}.
 * @param <T> Type of the field's value.
 *
 * @author Syam
 */
public class ComboField<T> extends ComboBox<T> {

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

    /**
     * Get the number of items in the combo.
     *
     * @return Number of items.
     */
    public int size() {
        return list.size();
    }

    /**
     * Get items from the list as a stream.
     *
     * @return Stream of items from the combo.
     */
    public Stream<T> items() {
        return list.stream();
    }
}
