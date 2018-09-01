package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dialog.Dialog;

public interface ApplicationMenu {

    HasComponents getMenuPane();

    HasComponents getContentPane();

    default void add(MenuItem menuItem) {
        getMenuPane().getElement().appendChild(menuItem.getComponent().getElement());
    }

    default void insert(int position, MenuItem menuItem) {
        getMenuPane().getElement().insertChild(position, menuItem.getComponent().getElement());
    }

    default void remove(MenuItem menuItem) {
        getMenuPane().getElement().removeChild(menuItem.getComponent().getElement());
    }

    default void addView(View view) {
        Component c = view.getComponent();
        if(!(c instanceof Dialog) && c instanceof HasSize) {
            ((HasSize) c).setWidth("98%");
        }
        getContentPane().getElement().appendChild(c.getElement());
    }
}
