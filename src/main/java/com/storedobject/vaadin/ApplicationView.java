package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

import java.util.Objects;

/**
 * The class that defines the content view of the {@link Application}. An implementation of this class (with all the necessary
 * annotations) is required for using {@link Application} class. There is no need to override or implement any methods.
 * @see Application
 */
public abstract class ApplicationView extends Composite<Component> {

    /**
     * The application layout.
     */
    ApplicationLayout layout;

    /**
     * Default constructor.
     */
    public ApplicationView() {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(layout != null) {
            Objects.requireNonNull(Application.get()).setMainView(this);
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
