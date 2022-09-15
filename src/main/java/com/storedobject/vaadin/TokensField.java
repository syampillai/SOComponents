package com.storedobject.vaadin;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.*;
import java.util.stream.Stream;

public class TokensField<T> extends MultiSelectComboBox<T> implements RequiredField {
    
    /**
     * Constructor.
     */
    public TokensField() {
        this((String)null);
    }

    /**
     * Constructor.
     * @param label Label
     */
    public TokensField(String label) {
        this(label, (Collection<T>)null);
    }

    /**
     * Constructor.
     * @param items Items
     */
    public TokensField(Collection<T> items) {
        this(null, items);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items Items
     */
    public TokensField(String label, Collection<T> items) {
        this(label, items, null);
    }

    /**
     * Constructor.
     * @param itemLabelGenerator Label generator
     */
    public TokensField(ItemLabelGenerator<T> itemLabelGenerator) {
        this(null, null, itemLabelGenerator);
    }

    /**
     * Constructor.
     * @param label Label
     * @param itemLabelGenerator Label generator
     */
    public TokensField(String label, ItemLabelGenerator<T> itemLabelGenerator) {
        this(label, null, itemLabelGenerator);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items Items
     * @param itemLabelGenerator Label generator
     */
    public TokensField(String label, Collection<T> items, ItemLabelGenerator<T> itemLabelGenerator) {
        super(label, items == null ? new ArrayList<>() : items);
        if(itemLabelGenerator != null) {
            setItemLabelGenerator(itemLabelGenerator);
        }
    }

    @Override
    protected boolean valueEquals(Set<T> value1, Set<T> value2) {
        if(Objects.equals(value1, value2)) {
            return true;
        }
        if(value1 == null || value2 == null || value1.size() != value2.size()) {
            return false;
        }
        for(T v: value1) {
            if(!value2.contains(v)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add tokens to the existing set.
     *
     * @param value Tokens to be added
     */
    public void addValue(Set<T> value) {
        if(value == null || value.isEmpty()) {
            return;
        }
        Set<T> v = new HashSet<>();
        v.addAll(getValue());
        v.addAll(value);
        setValue(v);
    }

    /**
     * Add more items to the selection list.
     *
     * @param items Items to be added
     */
    public void addItems(Collection<T> items) {
        if(items == null || items.isEmpty()) {
            return;
        }
        addItems(items.stream());
    }

    /**
     * Add more items to the selection list.
     *
     * @param items Items to be added
     */
    @SafeVarargs
    public final void addItems(T... items) {
        if(items == null || items.length == 0) {
            return;
        }
        addItems(Stream.of(items));
    }

    /**
     * Add more items to the selection list.
     *
     * @param streamOfItems Items to be added
     */
    public void addItems(Stream<T> streamOfItems) {
        if(streamOfItems == null) {
            return;
        }
        List<T> items = new ArrayList<>(items());
        streamOfItems.forEach(items::add);
        setItems(items);
    }

    @Override
    public void setValue(Set<T> value) {
        if(value == null) {
            value = new HashSet<>();
        }
        super.setValue(value);
    }

    public void setValue(Collection<T> value) {
        setValue(new HashSet<>(value));
    }

    /**
     * Select all items. The {@link #getValue()} will return all the items after invoking this method.
     */
    public void selectAll() {
        setValue(new HashSet<>(items()));
    }

    /**
     * Deselect all items. This is equivalent to setValue(null). The {@link #getValue()} will return an empty
     * set after invoking this method.
     */
    public void deselectAll() {
        setValue(Set.of());
    }

    private Collection<T> items() {
        //noinspection unchecked
        return ((ListDataProvider<T>)getDataProvider()).getItems();
    }
}
