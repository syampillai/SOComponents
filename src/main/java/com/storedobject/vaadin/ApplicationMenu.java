package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;

public interface ApplicationMenu extends HasComponents {

    default HasComponents getMenuPane() {
        return this;
    }

    default void add(MenuItem menuItem) {
        getMenuPane().getElement().appendChild(menuItem.getComponent().getElement());
    }

    default void insert(int position, MenuItem menuItem) {
        getMenuPane().getElement().insertChild(position, menuItem.getComponent().getElement());
    }

    default void remove(MenuItem menuItem) {
        getMenuPane().getElement().removeChild(menuItem.getComponent().getElement());
    }
}