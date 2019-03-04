package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicComboList;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;

import java.util.ArrayList;
import java.util.Collection;
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
    }

    /**
     * Get index of an item.
     *
     * @param value Item for which index needs to determined
     * @return Index of the item.
     */
    public int getIndex(T value) {
        return items.indexOf(value);
    }

    /**
     * Get the item for the index passed.
     *
     * @param index Index
     * @return Item at the index.
     */
    public T getValue(int index) {
        return index < 0 || index >= items.size() ? null : items.get(index);
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
}