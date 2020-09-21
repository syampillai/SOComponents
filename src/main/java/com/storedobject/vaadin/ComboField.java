package com.storedobject.vaadin;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.*;
import java.util.stream.Stream;

/**
 * A thin wrapper around Vaadin's {@link ComboBox}.
 *
 * @param <T> Type of the field's value.
 * @author Syam
 */
public class ComboField<T> extends ComboBox<T> {

    private final List<T> list = new ArrayList<>();
    private final ListDataProvider<T> dataProvider;

    /**
     * Constructor.
     *
     * @param list Items.
     */
    @SafeVarargs
    public ComboField(T... list) {
        this(null, list);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param list Items.
     */
    @SafeVarargs
    public ComboField(String label, T... list) {
        this(label, Arrays.asList(list));
    }

    /**
     * Constructor.
     *
     * @param list Items.
     */
    public ComboField(Collection<T> list) {
        this(null, list);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param list Items.
     */
    public ComboField(String label, Collection<T> list) {
        super(label);
        this.list.addAll(list);
        this.dataProvider = new ListDataProvider<>(this.list);
        setDataProvider(dataProvider);
    }

    /**
     * Get the index of qn item.
     *
     * @param item Item.
     * @return Index of the item or -1 if not found.
     */
    public int getIndex(T item) {
        if(item == null) {
            return -1;
        }
        return list.indexOf(item);
    }

    /**
     * Get item for a given index.
     *
     * @param index Index.
     * @return Item or null if not found.
     */
    public T getValue(int index) {
        if(index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    /**
     * Set value via index. If the index is out of range, no value will be set.
     *
     * @param index Index.
     */
    public void setIndex(int index) {
        if(index < 0 || index >= list.size()) {
            return;
        }
        setValue(list.get(index));
    }

    /**
     * Get the index of the current value.
     *
     * @return Index of the item or -1 if not found.
     */
    public int getIndex() {
        return getIndex(getValue());
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

    /**
     * Set items.
     *
     * @param items Items.
     */
    @Override
    public void setItems(Collection<T> items) {
        list.clear();
        addItems(items);
    }

    /**
     * Set items.
     *
     * @param items Items.
     */
    @SafeVarargs
    @Override
    public final void setItems(T... items) {
        list.clear();
        addItems(items);
    }

    /**
     * Set items.
     *
     * @param items Items.
     */
    @Override
    public void setItems(Stream<T> items) {
        list.clear();
        addItems(items);
    }

    /**
     * Remove items.
     *
     * @param items Items.
     */
    public void removeItems(Collection<T> items) {
        removeItems(items.stream());
    }

    /**
     * Remove items.
     *
     * @param items Items.
     */
    @SafeVarargs
    public final void removeItems(T... items) {
        removeItems(Arrays.asList(items));
    }

    /**
     * Remove items.
     *
     * @param items Items.
     */
    public void removeItems(Stream<T> items) {
        items.forEach(list::remove);
        refresh();
    }

    /**
     * Add items.
     *
     * @param items Items.
     */
    public void addItems(Collection<T> items) {
        items.stream().filter(Objects::nonNull).forEach(list::add);
        refresh();
    }

    /**
     * Add items.
     *
     * @param items Items.
     */
    @SafeVarargs
    public final void addItems(T... items) {
        if(items != null) {
            for(T item: items) {
                if(item != null) {
                    list.add(item);
                }
            }
        }
        refresh();
    }

    /**
     * Add items.
     *
     * @param items Items.
     */
    public void addItems(Stream<T> items) {
        items.filter(Objects::nonNull).forEach(list::add);
        refresh();
    }

    private void refresh() {
        if(getDataProvider() == dataProvider) {
            dataProvider.refreshAll();
        } else {
            setDataProvider(dataProvider);
        }
    }
}
