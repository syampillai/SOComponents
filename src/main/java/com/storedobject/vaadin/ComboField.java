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
     * Sets the items for the combo box using the provided items.
     *
     * @param items The items to be set in the combo box.
     * @return The updated ComboBoxListDataView representing the current state of the combo box data.
     */
    @SafeVarargs
    @Override
    public final ComboBoxListDataView<T> setItems(T... items) {
        return setItems(Arrays.asList(items));
    }

    /**
     * Removes the specified collection of items from the ComboBox's data view.
     *
     * @param items A collection of items to be removed.
     * @return The updated ComboBoxListDataView after removing the specified items.
     */
    public ComboBoxListDataView<T> removeItems(Collection<T> items) {
        return (ComboBoxListDataView<T>) getListDataView().removeItems(items);
    }

    /**
     * Removes the specified items from the data view of the combo box.
     *
     * @param items An array of items to be removed.
     * @return The updated ComboBoxListDataView after removing the specified items.
     */
    @SafeVarargs
    public final ComboBoxListDataView<T> removeItems(T... items) {
        return removeItems(Arrays.asList(items));
    }

    /**
     * Removes items from the ComboBoxListDataView that match the specified stream of items.
     *
     * @param items A stream of items to be removed from the ComboBoxListDataView.
     * @return The updated ComboBoxListDataView instance after items have been removed.
     */
    public ComboBoxListDataView<T> removeItems(Stream<T> items) {
        return addItems(items.collect(Collectors.toList()));
    }

    /**
     * Adds a collection of items to the ComboBox's data view.
     *
     * @param items The collection of items to be added.
     * @return An instance of ComboBoxListDataView representing the updated data view of the ComboBox.
     */
    public ComboBoxListDataView<T> addItems(Collection<T> items) {
        return (ComboBoxListDataView<T>) getListDataView().addItems(items);
    }

    /**
     * Adds the specified items to the ComboBox list data view.
     *
     * @param items Items to be added to the ComboBox.
     * @return A ComboBoxListDataView instance for further operations.
     */
    @SafeVarargs
    public final ComboBoxListDataView<T> addItems(T... items) {
        return addItems(Arrays.asList(items));
    }

    /**
     * Adds the given items represented as a Stream to the combo box.
     *
     * @param items Stream of items to be added to the combo box.
     * @return The updated data view of the combo box after adding the items.
     */
    public ComboBoxListDataView<T> addItems(Stream<T> items) {
        return addItems(items.collect(Collectors.toList()));
    }
}
