package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

import java.util.Set;

public interface ItemsSelectedListener<T> extends ItemSelectedListener<T> {

    default void itemsSelected(Component component, Set<T> items) {
        items.forEach(item -> itemSelected(component, item));
    }
}
