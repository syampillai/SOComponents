package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicComboList;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Slightly enhanced version of Vaadin's {@link com.vaadin.flow.component.select.Select}.
 *
 * @param <T> Value type of the field
 * @author Syam
 */
public class ListField<T> extends Select<T> {

    private List<T> items;

    /**
     * Constructor.
     *
     * @param items List items
     */
    public ListField(Collection<T> items) {
        this(null, items);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param items List items
     */
    public ListField(String label, Collection<T> items) {
        setItems(items);
        setLabel(label);
        setRequired(true);
    }

    /**
     * Get index of an item.
     *
     * @param value Item for which index needs to determined
     * @return Index of the item.
     */
    public int getIndex(T value) {
        int i = value == null ? -1 : items.indexOf(value);
        return i < 0 && !items.isEmpty() && isRequiredBoolean() ? 0 : i;
    }

    /**
     * Get the item for the index passed.
     *
     * @param index Index
     * @return Item at the index.
     */
    public T getValue(int index) {
        return index < 0 || index >= items.size() ? (isRequiredBoolean() && !items.isEmpty() ? items.get(0) : null) : items.get(index);
    }

    /**
     * Set item list.
     *
     * @param items Item list
     */
    public void setItems(Collection<T> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        super.setItems(this.items);
    }

    /**
     * Set item list.
     *
     * @param items Item list
     */
    public void setItems(Iterable<T> items) {
        this.items = new ArrayList<>();
        for(T item: items) {
            this.items.add(item);
        }
        super.setItems(this.items);
    }

    /**
     * Set item list.
     *
     * @param items Item list
     */
    public void setItems(T[] items) {
        this.items = new ArrayList<>();
        Collections.addAll(this.items, items);
        super.setItems(this.items);
    }

    /**
     * Replace an item at a specified index. (If the index is outside the range, it will not be replaced).
     *
     * @param index Index
     * @param item New item
     */
    public void setItem(int index, T item) {
        if(index >= 0 && index < items.size()) {
            items.set(index, item);
            super.setItems(items);
        }
    }
}