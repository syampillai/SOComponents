package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

import java.util.Set;

/**
 * Interface for listening on selected items (especially in {@link com.vaadin.flow.component.grid.Grid}.
 * @param <T> Type of item selected.
 *
 * @author Syam
 */
@FunctionalInterface
public interface ItemsSelectedListener<T> extends ItemSelectedListener<T> {

    default void itemSelected(Component component, T item) {
    }

    /**
     * Method called when set of items are selected.
     *
     * @param component Component from which the items are selected
     * @param items Selected items
     */
    void itemsSelected(Component component, Set<T> items);
}