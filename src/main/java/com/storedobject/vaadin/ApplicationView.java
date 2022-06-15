package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.server.VaadinRequest;
import org.atmosphere.cpr.ApplicationConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The class that defines the content view of the {@link Application}. An implementation of this class with all the
 * necessary annotations (minimally, root {@link com.vaadin.flow.router.Route} must be there. Others such as
 * {@link com.vaadin.flow.component.page.Push}, {@link com.vaadin.flow.server.PWA},
 * {@link com.vaadin.flow.component.page.BodySize} etc. could be added.) is required for using as part of the
 * {@link Application} class.
 *
 * @see Application
 * @author Syam
 */
public abstract class ApplicationView extends Composite<Component>
        implements BeforeEnterObserver, HasDynamicTitle, ApplicationConfig {

    /**
     * The application layout.
     */
    ApplicationLayout layout;
    private final Application application;
    private Locale locale;
    private boolean firstTime = true;
    /**
     * Query parameters. (Used by {@link Application}.
     */
    Map<String, String> queryParams;
    private int deviceWidth = -1, deviceHeight = -1;
    private String url;
    private final Span root = new Span();

    /**
     * Default constructor.
     */
    public ApplicationView() {
        application = createApplication();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI ui = attachEvent.getUI();
        if(locale != null) {
            ui.setLocale(locale);
        }
        Consumer<UI> configurator = application.getUIConfigurator();
        if(configurator != null) {
            configurator.accept(ui);
        }
        application.setUI(ui);
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
        application.detached();
    }

    final void setLocale(Locale locale) {
        if(locale == null) {
            return;
        }
        this.locale = locale;
        getUI().ifPresent(ui -> ui.setLocale(locale));
    }

    @Override
    protected final Component initContent() {
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
        add(layout.getComponent());
        return root;
    }

    public void add(Component screen) {
        root.add(screen);
    }

    public void setLayoutVisible(boolean visible) {
        Component c = layout.getComponent();
        if(c.isVisible() != visible) {
            c.setVisible(visible);
        }
    }

    /**
     * Get the device (browser) height.
     *
     * @return Device height.
     */
    public int getDeviceHeight() {
        if(deviceHeight < 0) {
            receiveSize();
        }
        return deviceHeight;
    }

    /**
     * Get the device (browser) width.
     *
     * @return Device width.
     */
    public int getDeviceWidth() {
        if(deviceWidth < 0) {
            receiveSize();
        }
        return deviceWidth;
    }

    /**
     * get tje URL of application.
     *
     * @return URL of the application as a string.
     */
    public String getURL() {
        if(url == null) {
            receiveSize();
        }
        return url;
    }

    @ClientCallable
    private void info(String url, int width, int height) {
        this.url = url;
        if(width == this.deviceWidth && height == this.deviceHeight) {
            return;
        }
        this.deviceWidth = width;
        this.deviceHeight = height;
        application.fireResized(width, height);
    }

    private void receiveSize() {
        getElement().executeJs("$0.$server.info(window.location.href,window.innerWidth,window.innerHeight);",
                getElement());
    }

    /**
     * Create an instance of the Application class. This is invoked only once.
     *
     * @return Newly created Application. (By default it tries to construct an instance for the
     * class name returned by {@link #getApplicationClassName()}).
     */
    protected Application createApplication() {
        try {
            return (Application)Class.forName(getApplicationClassName()).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                NoSuchMethodException |
                InvocationTargetException ignored) {
        }
        return null;
    }

    /**
     * Get the current application. (For internal use only).
     *
     * @return Current application.
     */
    Application getApplication() {
        return application;
    }

    /**
     * Get the name of the Application class.
     *
     * @return Fully qualified name of the Application class. (Default value returned is <code>null</code>).
     */
    protected String getApplicationClassName() {
        return null;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        queryParams = new HashMap<>();
        Location location = beforeEnterEvent.getLocation();
        location.getQueryParameters().getParameters().forEach((key, v) -> queryParams.put(key, v.get(0)));
        if(queryParams.isEmpty()) {
            queryParams = null;
        }
    }

    /**
     * Get the page title to be displayed. By default, the {@link Application#getCaption()} is used as the page title.
     *
     * @return Page title.
     */
    @Override
    public String getPageTitle() {
        return application.getCaption();
    }
}