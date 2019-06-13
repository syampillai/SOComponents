package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinRequest;

import java.util.Locale;

/**
 * The class that defines the content view of the {@link Application}. An implementation of this class (with all the necessary
 * annotations) is required for using {@link Application} class.
 *
 * @see Application
 * @author Syam
 */
public abstract class ApplicationView extends Composite<Component> {

    /**
     * The application layout.
     */
    ApplicationLayout layout;
    private final Application application;
    private Locale locale;
    private boolean firstTime = true;

    /**
     * Default constructor.
     */
    public ApplicationView() {
        application = createApplication();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(locale != null) {
            attachEvent.getUI().setLocale(locale);
        }
        application.setUI(attachEvent.getUI());
        if(firstTime) {
            if(application.error != null) {
                application.close();
                return;
            }
            firstTime = false;
            if (layout != null) {
                application.setMainView(this);
            }
            receiveSize();
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        application.deatached();
    }

    final void setLocale(Locale locale) {
        if(locale == null) {
            return;
        }
        this.locale = locale;
        getUI().ifPresent(ui -> ui.setLocale(locale));
    }

    @Override
    protected Component initContent() {
        if(layout == null) {
            if(application == null) {
                Notification.show("Logged out");
            } else {
                application.init(VaadinRequest.getCurrent());
                if (application.error != null) {
                    Notification.show(application.error);
                }
            }
            if(application == null) {
                return new Div();
            }
            if(application.error != null) {
                return new Span(application.error);
            }
            layout = application.createLayout();
        }
        return layout.getComponent();
    }

    @ClientCallable
    final void deviceSize(int width, int height) {
        application.deviceSize(width, height);
    }

    final void receiveSize() {
        getElement().executeJs("$0.$server.deviceSize(window.innerWidth,window.innerHeight);", getElement());
    }

    /**
     * Create an instance of the Application class. This is invoked only once.
     *
     * @return Newly created Application. (By default it tries to construct an instance for the
     * class name returned by {@link #getApplicationClassName()}).
     */
    protected Application createApplication() {
        try {
            return (Application)Class.forName(getApplicationClassName()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignored) {
        }
        return null;
    }

    /**
     * Get the name of the Application class.
     *
     * @return Fully qualified name of the Application class. (Default value returned is <code>null</code>).
     */
    protected String getApplicationClassName() {
        return null;
    }
}