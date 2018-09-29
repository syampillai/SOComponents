package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

public interface ItemSelectedListener<T> {

    void itemSelected(Component component, T item);
}
