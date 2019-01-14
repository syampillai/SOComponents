package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;

public abstract class ApplicationView extends Composite<Component> {

    ApplicationLayout layout;

    public ApplicationView() {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(layout != null) {
            Application.get().setMainView(this);
        }
    }

    @Override
    protected Component initContent() {
        if(layout == null) {
            Application a = Application.get();
            if(a == null) {
                Notification.show("Logged out");
            } else {
                if (a.error != null) {
                    Notification.show(a.error);
                    a = null;
                }
            }
            if(a == null) {
                return new Div();
            }
            layout = a.createLayout();
        }
        return layout.getComponent();
    }
}
