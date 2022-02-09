package com.storedobject.vaadin;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.dataview.ComboBoxListDataView;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A thin wrapper around Vaadin's {@link ComboBox}.
 *
 * @param <T> Type of the field's value.
 * @author Syam
 */
public class ComboField<T> extends ComboBox<T> implements SpellCheck {

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
        setItems(list);
    }

    /**
     * Get the index of qn item.
     *
     * @param item Item.
     * @return Index of the item or -1 if not found.
     */
    public int getIndex(T item) {
        ComboBoxListDataView<T> view = getListDataView();
        for(int i = 0; i < view.getItemCount(); i++) {
            if(view.getItem(i).equals(item)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get item for a given index.
     *
     * @param index Index.
     * @return Item or null if not found.
     */
    public T getValue(int index) {
        ComboBoxListDataView<T> view = getListDataView();
        if(index < 0 || index >= view.getItemCount()) {
            return null;
        }
        return view.getItem(index);
    }

    /**
     * Set value via index. If the index is out of range, no value will be set.
     *
     * @param index Index.
     */
    public void setIndex(int index) {
        ComboBoxListDataView<T> view = getListDataView();
        if(index < 0 || index >= view.getItemCount()) {
            return;
        }
        setValue(view.getItem(index));
        getListDataView();
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
        return getListDataView().getItemCount();
    }

    /**
     * Get items from the list as a stream.
     *
     * @return Stream of items from the combo.
     */
    public Stream<T> items() {
        return getListDataView().getItems();
    }

    /**
     * Set items.
     *
     * @param items Items.
     */
    @SafeVarargs
    @Override
    public final ComboBoxListDataView<T> setItems(T... items) {
        return setItems(Arrays.asList(items));
    }

    /**
     * Remove items.
     *
     * @param items Items.
     */
    public ComboBoxListDataView<T> removeItems(Collection<T> items) {
        return (ComboBoxListDataView<T>) getListDataView().removeItems(items);
    }

    /**
     * Remove items.
     *
     * @param items Items.
     */
    @SafeVarargs
    public final ComboBoxListDataView<T> removeItems(T... items) {
        return removeItems(Arrays.asList(items));
    }

    /**
     * Remove items.
     *
     * @param items Items.
     */
    public ComboBoxListDataView<T> removeItems(Stream<T> items) {
        return addItems(items.collect(Collectors.toList()));
    }

    /**
     * Add items.
     *
     * @param items Items.
     */
    public ComboBoxListDataView<T> addItems(Collection<T> items) {
        return (ComboBoxListDataView<T>) getListDataView().addItems(items);
    }

    /**
     * Add items.
     *
     * @param items Items.
     */
    @SafeVarargs
    public final ComboBoxListDataView<T> addItems(T... items) {
        return addItems(Arrays.asList(items));
    }

    /**
     * Add items.
     *
     * @param items Items.
     */
    public ComboBoxListDataView<T> addItems(Stream<T> items) {
        return addItems(items.collect(Collectors.toList()));
    }
}
