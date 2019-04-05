package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * Interface for listening on selected items (especially in {@link com.vaadin.flow.component.grid.Grid}.
 * @param <T> Type of item selected.
 *
 * @author Syam
 */
@FunctionalInterface
public interface ItemSelectedListener<T> {

    /**
     * Method called when an item is selected.
     *
     * @param component Component from which the item is selected
     * @param item Selected item
     */
    void itemSelected(Component component, T item);
}
