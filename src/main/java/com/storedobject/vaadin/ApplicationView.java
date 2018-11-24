package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;

@JavaScript("so-body.js")
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
            Application a = Application.get(VaadinSession.getCurrent());
            if(a != null) {
                Notification.show("Logged out");
                a.close();
                a = null;
            } else {
                a = Application.create();
                if(a != null) {
                    String error = a.getUI().getError();
                    if (error != null) {
                        Notification.show(error);
                        a = null;
                    }
                } else {
                    Notification.show("Unable to initialize application");
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
