package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

import java.util.Set;

/**
 * Interface for listening on selected items (especially in {@link com.vaadin.flow.component.grid.Grid}.
 * @param <T> Type of item selected.
 * @author Syam
 */
public interface ItemsSelectedListener<T> extends ItemSelectedListener<T> {

    /**
     * Method called when set of items are selected.
     * @param component Compoenent from which the items are selected
     * @param items Selected items
     */
    default void itemsSelected(Component component, Set<T> items) {
        items.forEach(item -> itemSelected(component, item));
    }
}
