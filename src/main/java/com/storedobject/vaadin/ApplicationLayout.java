package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dialog.Dialog;

public interface ApplicationLayout {

    Component getComponent();

    Component getContent();

    ApplicationMenu getMenu();

    void drawMenu(Application application);

    default void addView(View view) {
        Component c = view.getComponent();
        if(!(c instanceof Dialog) && c instanceof HasSize) {
            ((HasSize) c).setWidth("98%");
        }
        getContent().getElement().appendChild(c.getElement());
    }
}