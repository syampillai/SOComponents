package com.storedobject.vaadin;

import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.data.SelectListDataView;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Slightly enhanced version of Vaadin's {@link com.vaadin.flow.component.select.Select}.
 *
 * @param <T> Value type of the field
 * @author Syam
 */
public class ListField<T> extends Select<T> implements SpellCheck, ValueRequired {

    private SelectListDataView<T> view;

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
        for(int i = 0; i < view.getItemCount(); i++) {
            if(view.getItem(i).equals(value)) {
                return i;
            }
        }
        return view.getItemCount() != 0 && isRequired() ? 0 : -1;
    }

    /**
     * Set value via index. If the index is out of range, no value will be set.
     *
     * @param index Index.
     */
    public void setIndex(int index) {
        if(index < 0 || index >= view.getItemCount()) {
            return;
        }
        setValue(view.getItem(index));
    }

    /**
     * Get the item for the index passed.
     *
     * @param index Index
     * @return Item at the index.
     */
    public T getValue(int index) {
        if(index >= 0 && index < view.getItemCount()) {
            T item = view.getItem(index);
            if(item != null) {
                return item;
            }
        }
        return isRequired() && view.getItemCount() != 0 ? view.getItem(0) : null;
    }

    /**
     * Set item list.
     *
     * @param items Item list
     * @return Data view
     */
    public SelectListDataView<T> setItems(Collection<T> items) {
        view = super.setItems(items);
        return view;
    }

    /**
     * Set item list.
     *
     * @param items Item list
     * @return Data view
     */
    public SelectListDataView<T> setItems(Iterable<T> items) {
        List<T> list = new ArrayList<>();
        for(T item: items) {
            list.add(item);
        }
        return setItems(list);
    }

    /**
     * Set item list.
     *
     * @param items Item list
     * @return Data view
     */
    @SafeVarargs
    public final SelectListDataView<T> setItems(T... items) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        return setItems(list);
    }

    /**
     * Replace an item at a specified index. (If the index is outside the range, it will not be replaced).
     *
     * @param index Index
     * @param item New item
     */
    public void setItem(int index, T item) {
        List<T> items = view.getItems().collect(Collectors.toList());
        if(index >= 0 && index < items.size()) {
            items.set(index, item);
            setItems(items);
        }
    }

    /**
     * Removes the specified items from the current view.
     *
     * @param items The collection of items to be removed from the view.
     * @return The updated data view after removing the specified items.
     */
    public SelectListDataView<T> removeItems(Collection<T> items) {
        view.removeItems(items);
        return view;
    }

    /**
     * Removes the specified items from the list and updates the data view.
     *
     * @param items Items to be removed.
     * @return The updated data view after removing the items.
     */
    @SafeVarargs
    public final SelectListDataView<T> removeItems(T... items) {
        return removeItems(Arrays.asList(items));
    }

    /**
     * Removes the specified items from the list and updates the view accordingly.
     *
     * @param items Stream of items to be removed from the list.
     * @return Updated data view after the removal of items.
     */
    public SelectListDataView<T> removeItems(Stream<T> items) {
        items.forEach(view::removeItem);
        return view;
    }

    /**
     * Adds a collection of items to the view.
     *
     * @param items The collection of items to be added.
     * @return The view representing the updated list of items.
     */
    public SelectListDataView<T> addItems(Collection<T> items) {
        view.addItems(items);
        return view;
    }

    /**
     * Adds the specified items to the view if they are not null.
     *
     * @param items Items to be added to the view. Items that are null will be ignored.
     * @return The updated data view after adding the items.
     */
    @SafeVarargs
    public final SelectListDataView<T> addItems(T... items) {
        if(items != null) {
            for(T item: items) {
                if(item != null) {
                    view.addItem(item);
                }
            }
        }
        return view;
    }

    /**
     * Adds a stream of non-null items to the data view.
     *
     * @param items A stream of items to be added. Null items in the stream will be filtered out.
     * @return The updated data view after adding the items.
     */
    public SelectListDataView<T> addItems(Stream<T> items) {
        items.filter(Objects::nonNull).forEach(view::addItem);
        return view;
    }

    @Override
    public void setRequired(boolean required) {
        this.getElement().setProperty("required", required);
    }

    @Override
    public boolean isRequired() {
        return this.getElement().getProperty("required", false);
    }
}