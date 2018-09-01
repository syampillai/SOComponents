package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

public interface ApplicationLayout {

    Component getComponent();

    ApplicationMenu getMenu();

    void drawMenu(Application application);
}