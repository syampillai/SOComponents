package com.storedobject.vaadin.util;

import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.dataview.ListBoxListDataView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A generic combo box component that extends the ListBox class.
 * This class allows managing a collection of items and provides methods to access,
 * manipulate, and retrieve item values and their corresponding indices.
 *
 * @param <T> the type of items managed by this combo list
 *
 * @author Syam
 */
public class BasicComboList<T> extends ListBox<T> {

    /**
     * The list of items managed by the BasicComboList. This list represents the currently available
     * collection of items within the combo box, which can be manipulated or retrieved as needed.
     */
    protected List<T> list;

    /**
     * Constructs a BasicComboList instance with a variable number of elements.
     * Allows initializing the combo list with multiple items provided as arguments.
     *
     * @param list the elements to initialize the combo list with.
     *             These elements will be converted into a collection of items managed by this combo list.
     */
    @SafeVarargs
    public BasicComboList(T... list) {
        this(Arrays.asList(list));
    }

    /**
     * Constructs a BasicComboList instance using the provided collection of items.
     *
     * @param list the collection of items to initialize the combo list; if null, an empty list will be used
     */
    public BasicComboList(Collection<T> list) {
        setItems(list);
    }

    /**
     * Sets the items for the combo list with the provided collection.
     * If the input collection is null, an empty collection is used instead.
     * This method also updates the internal list and sets the first value
     * of the list as the selected value, if applicable.
     *
     * @param items the collection of items to be set in the combo list
     * @return the updated ListBoxListDataView instance reflecting the new items
     */
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

    /**
     * Sets the current value of the combo list to the first item in the list.
     * If the list is empty, the value is set to {@code null}.
     *
     * This method ensures that the combo list initializes its value to the first
     * available item in its data collection, or remains unset if no items exist.
     */
    protected void setFirstValue() {
        setValue(list.size() > 0 ? list.get(0) : null);
    }

    /**
     * Retrieves the index of the item currently selected or set as the value
     * of the combo list.
     *
     * @return the index of the current value in the list, or -1 if the value is not found
     */
    public int getIndex() {
        return getIndex(getValue());
    }

    /**
     * Returns the index of the specified item in the list.
     *
     * @param item the item whose index is to be determined; can be null
     * @return the zero-based index of the specified item in the list, or -1 if the item is not present
     */
    public int getIndex(T item) {
        return list.indexOf(item);
    }

    /**
     * Retrieves the value at the specified index from the combo list.
     *
     * @param index the position of the item in the list to retrieve.
     *              If the index is out of bounds (less than 0 or greater than or equal to the size of the list), null is returned.
     * @return the value at the specified index, or null if the index is invalid.
     */
    public T getValue(int index) {
        return index < 0 || index >= list.size() ? null : list.get(index);
    }

    /**
     * Returns the default empty value for the component.
     * This method is overridden to provide a null value as the representation
     * of an empty value for the generic combo box.
     *
     * @return the empty value of the component, which is null
     */
    @Override
    public T getEmptyValue() {
        return null;
    }
}
