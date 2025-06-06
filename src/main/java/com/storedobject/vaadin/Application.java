package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.storedobject.helper.LitComponent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.*;
import com.vaadin.flow.shared.Registration;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Application is the base class for creating a 'single page' web applications. The 'single page' should be defined
 * by extending {@link com.storedobject.vaadin.ApplicationView} class.
 * <pre>
 * <code>
 * public class Demo extends Application {
 *
 *   {@literal @}Override
 *   protected ApplicationLayout createLayout() {
 *     return new AppFrame();
 *   }
 *
 *   private static class AppFrame extends ApplicationFrame {
 *
 *     {@literal @}Override
 *     public void drawMenu(Application application) {
 *       setCaption("Sample Application  Ver 1.0.4");
 *       addToNavbar(new SpeakerButton());
 *       ApplicationMenu menu = getMenu();
 *       ApplicationMenuItem ami;
 *       ami = application.createMenuItem("One", () -> Notification.show("Hello World!"));
 *       menu.add(ami);
 *       ami = application.createMenuItem("Two", () -> Notification.show("Hello World 2!"));
 *       menu.add(ami);
 *       ami = application.createMenuItem("Greeting", () -> {
 *         Application a = Application.get();
 *         a.speak("Hello, how are you?");
 *         if(!a.isSpeakerOn()) {
 *           Notification.show("Speaker is off! Click on the speaker button to turn it on.");
 *         }
 *       });
 *       menu.add(ami);
 *     }
 *   }
 *
 *   {@literal @}Route("")
 *   public static class AppView extends ApplicationView {
 *
 *     {@literal @}Override
 *     protected Application createApplication() {
 *       return new Demo();
 *     }
 *   }
 * }
 * </code>
 * </pre>
 * The 'single page' can be any layout component that implements the interface {@link com.storedobject.vaadin.ApplicationLayout}. The layout
 * typically contains a 'menu' area and 'content' area. One can use Vaadin's <code>AppLayout</code> or similar components as the base for this.
 * The 'menu' area will contain {@link com.storedobject.vaadin.ApplicationMenuItem} instances and when a 'menu item' is clicked, the <code>Runnable</code>
 * action associated with it will be executed. One may associate any <code>Runnable</code> action with a 'menu item' such as generating a report or
 * invoking a {@link com.storedobject.vaadin.View}. If a {@link com.storedobject.vaadin.View} is invoked, its associated 'view component' is
 * displayed in the 'content' area and its 'caption' is inserted as a new 'menu item' in the 'menu' area. The 'content' area displays only the 'active
 * view' (currently selected or executed view) and hides all previously displayed 'views' but any of those 'views' can be made active again
 * by licking on its respective 'menu item' created from its 'caption'.
 *
 * @author Syam
 */
public abstract class Application {

    private final static String APP_KEY = "so$app";
    private ApplicationView applicationView;
    private ApplicationLayout applicationLayout;
    private UI ui;
    private ApplicationEnvironment environment;
    private final Map<Object, AlertList> alerts = new HashMap<>();
    private final Map<Object, Integer> pollIntervals = new HashMap<>();
    private final Map<Class<?>, Object> data = new HashMap<>();
    private ViewManager viewManager;
    private final ArrayList<WeakReference<Closeable>> resources = new ArrayList<>();
    private String link;
    private final ArrayList<Command> commands = new ArrayList<>();
    private transient boolean closing = false;
    String error;
    private boolean speaker = false;
    interface SpeakerToggledListener {
        void speaker(boolean on);
    }
    private Set<SpeakerToggledListener> speakerToggledListeners;
    private ArrayList<WeakReference<ResizedListener>> resizeListeners;
    private WebBrowser webBrowser;
    private final Alert errorText = new Alert(null, NotificationVariant.LUMO_PRIMARY);
    private int deviceWidth = -1, deviceHeight = -1;

    /**
     * This method is invoked by {@link ApplicationView} class.
     * If you want to override this method, make sure that <code>super</code> is called.
     *
     * @param request Vaadin Request
     */
    protected void init(VaadinRequest request) {
        webBrowser = VaadinSession.getCurrent().getBrowser();
        String link = request.getContextPath();
        if (link != null && link.length() > 1 && link.startsWith("/")) {
            link = link.substring(1);
        } else {
            link = null;
        }
        this.link = link;
        link = request.getPathInfo();
        if (link != null && !link.equals("/")) {
            error = "Unknown application '" + (this.link == null ? "" : this.link) + link + "'.\nPlease use the correct link.";
        } else {
            applicationLayout = createLayout();
            if(applicationLayout == null) {
                error = "Layout missing";
            }
        }
        if(error == null) {
            if(!init(this.link)) {
                error = "Initialization failed";
            }
        }
        if(error == null) {
            new Thread(() -> {
                while (!closing) {
                    synchronized (commands) {
                        if(!commands.isEmpty()) {
                            ui.access(() -> {
                                commands.remove(0);
                                ui.push();
                            });
                            if(!commands.isEmpty()) {
                                continue;
                            }
                        }
                        try {
                            commands.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * Get the query parameter value passed to this application. This is typically used by some logic that
     * brings up the opening view of the application. It is advisable to remove the parameter after its usage
     * by invoking {@link #removeQueryParameter(String)} or {@link #removeQueryParameters()}
     * due to security reasons. Otherwise, some other logic may access it.
     * <p>Note: Only one parameter is allowed per key. If multiple values are passed for the same key,
     * only one will be made available.</p>
     *
     * @param key Key for which value needs to be obtained.
     * @return Value corresponding to the key or <code>null</code> if no such key exists.
     */
    public String getQueryParameter(String key) {
        return applicationView.queryParams == null ? null : applicationView.queryParams.get(key);
    }

    /**
     * Remove the query parameter value passed to this application.
     *
     * @param key Key for which value needs to be removed.
     */
    public void removeQueryParameter(String key) {
        if(applicationView.queryParams != null) {
            applicationView.queryParams.remove(key);
            if(applicationView.queryParams.isEmpty()) {
                applicationView.queryParams = null;
            }
        }
    }

    /**
     * Remove all query parameter values passed to this application.
     */
    public void removeQueryParameters() {
        if(applicationView.queryParams != null) {
            applicationView.queryParams.clear();
            applicationView.queryParams = null;
        }
    }

    /**
     * Get the representation of current the "web browser".
     *
     * @return Current web browser.
     */
    public WebBrowser getWebBrowser() {
        return webBrowser;
    }

    /**
     * Get the browser Page.
     *
     * @return Current page.
     */
    public final Page getPage() {
        return ui.getPage();
    }

    /**
     * Add a browser resized listener to this application so that the listener will be alerted whenever the browser is resized.
     *
     * @param listener Listener
     * @return Registration.
     */
    public Registration addBrowserResizedListener(ResizedListener listener) {
        if(listener == null) {
            return null;
        }
        if(resizeListeners == null) {
            resizeListeners = new ArrayList<>();
        }
        resizeListeners.add(new WeakReference<>(listener));
        return () -> {
            for(WeakReference<ResizedListener> w: resizeListeners) {
                if(w.get() == listener) {
                    resizeListeners.remove(w);
                    return;
                }
            }
        };
    }

    /**
     * Invoked internally when the browser is resized or initialized.
     *
     * @param width Width.
     * @param height Height.
     */
    void fireResized(int width, int height) {
        if(width == deviceWidth && height == deviceHeight) {
            return;
        }
        deviceWidth = width;
        deviceHeight = height;
        if(resizeListeners != null) {
            resizeListeners.removeIf(w -> w.get() == null);
            resizeListeners.forEach(w -> {
                ResizedListener l = w.get();
                if(l != null) {
                    l.resized(width, height);
                }
            });
        }
    }

    /**
     * Get the {@link Alert} that can be used for showing common messages. Please note that others may clear your
     * messages if you use this to display messages.
     *
     * @return Alert instance.
     */
    public Alert getCommonAlert() {
        return errorText;
    }

    /**
     * Get UI configurator if any so that UI will be passed to it for handling any special configuration. This will be
     * invoked before {@link #setUI(UI)} is called.
     *
     * @return Default implementation returns <code>null</code>.
     */
    protected Consumer<UI> getUIConfigurator() {
        return null;
    }

    /**
     * Get the UI associated with this Application.
     *
     * @return The UI.
     */
    public final UI getUI() {
        if(ui == null) {
            setUI(UI.getCurrent());
        }
        return ui;
    }

    /**
     * Inform Application about the UI change (This can happen when the user refreshes the page).
     *
     * @param ui UI to set
     */
    final void setUI(UI ui) {
        if(ui == null || closing) {
            if(ui != null) {
                ui.removeAll();
            }
            return;
        }
        @SuppressWarnings("unchecked") ArrayList<Application> aList = (ArrayList<Application>) VaadinSession.getCurrent().getAttribute(APP_KEY);
        if(aList == null) {
            aList = new ArrayList<>();
            VaadinSession.getCurrent().setAttribute(APP_KEY, aList);
        }
        if(!aList.contains(this)) {
            aList.add(this);
        }
        if(this.ui == null) { // Default styles for the first time only
            ui.getElement().getStyle().
                    set("--so-header-color", "var(--lumo-primary-contrast-color)").
                    set("--so-header-background", "var(--lumo-primary-color)").
                    set("--so-header-height", "9vh").
                    set("--so-menu-drawer-color", "var(--so-header-color)").
                    set("--so-header-background-50pct", "var(--lumo-primary-color-50pct)").
                    set("--so-hover-background", "var(--lumo-primary-color-50pct)").
                    set("--so-grid-selected-background", "var(--lumo-primary-color-50pct)").
                    set("--so-grid-stripes-background", "var(--lumo-primary-color-10pct)");
        }
        this.ui = ui;
        attached();
    }

    /**
     * Invoked whenever this application is attached to a UI. Default implementation does nothing.
     */
    public void attached() {
    }

    private void removeUI() {
        removeUI(false);
    }

    private void removeUI(boolean removeContent) {
        if(ui != null) {
            if(removeContent) {
                try {
                    ui.removeAll();
                } catch(Throwable ignored) {
                }
            }
            try {
                ui.close();
            } catch(Throwable ignored) {
            }
            ui = null;
        }
    }

    /**
     * Invoked whenever this application is detached from its UI (it may get attached again to another UI if the user
     * just refreshed the browser). The default implementation closes the application ({@link #close()}) after
     * 20 seconds.
     */
    public void detached() {
        removeUI();
        new Timer().schedule(new AppCloser(this), 20000L);
    }

    private static class AppCloser extends TimerTask {

        private final Application a;

        private AppCloser(Application a) {
            this.a = a;
        }

        @Override
        public void run() {
            if(a.ui == null) {
                a.close();
            }
        }
    }

    /**
     * Set the locale for this Application.
     *
     * @param locale Locate to set
     */
    public void setLocale(Locale locale) {
       applicationView.setLocale(locale);
    }

    /**
     * Retrieves the current locale used in the application view.
     *
     * @return the Locale object representing the current locale.
     */
    public Locale getLocale() {
        return applicationView.locale;
    }

    /**
     * Lock the UI and execute a command. After executing the command, UI changes, if any, will be sent to the browser.
     * <p>Note: All errors are silently ignored. If you want any control over errors/exceptions, please handle it
     * in the commands itself.</p>
     *
     * @param command Command to execute.
     * @return A future that can be used to check for task completion and to cancel the command.
     */
    public Future<Void> access(Command command) {
        UI ui = getUI();
        Future<Void> result = null;
        try {
            result = ui.access(() -> {
                try {
                    command.execute();
                } catch(Throwable ignored) {
                }
                if(ui.getPushConfiguration().getPushMode().isEnabled()) {
                    try {
                        ui.push();
                    } catch(Throwable ignored) {
                    }
                }
            });
        } catch(Throwable ignored) {
        }
        return result;
    }

    /**
     * This method is invoked when the application comes up.
     *
     * @param link The context path of the application.
     * @return True if application can go ahead. Otherwise, an "Initialization failed" message is displayed.
     * Default return value is <code>true</code>.
     */
    protected boolean init(String link) {
        return true;
    }

    /**
     * This method is invoked only once to determine the layout of the application.
     *
     * @return Application layout.
     */
    protected abstract ApplicationLayout createLayout();

    /**
     * An "application environment" may be created to specify certain behaviours of the application. If this method returns <code>null</code>, a default
     * "environment" will be created.
     *
     * @return Returns null by default.
     */
    protected ApplicationEnvironment createEnvironment() {
        return null;
    }

    /**
     * Get the current "application environment".
     *
     * @return Application environment.
     */
    public final ApplicationEnvironment getEnvironment() {
        if(environment == null) {
            environment = createEnvironment();
        }
        if(environment == null) {
            environment = new ApplicationEnvironment() {};
        }
        return environment;
    }

    /**
     * Create a menu item.
     *
     * @param label Label of the menu item
     * @param menuAction Action to execute when menu item is clicked
     * @return Menu item. By default, the menu item will be created from the "application environment"
     * {@link ApplicationEnvironment#createMenuItem(String, String, Runnable)}
     */
    public ApplicationMenuItem createMenuItem(String label, Runnable menuAction) {
        return createMenuItem(label, null, menuAction);
    }

    /**
     * Create a menu item.
     *
     * @param label Label of the menu item
     * @param icon Icon to be used
     * @param menuAction Action to execute when menu item is clicked
     * @return Menu item. By default, the menu item will be created from the "application environment"
     * {@link ApplicationEnvironment#createMenuItem(String, String, Runnable)}
     */
    public ApplicationMenuItem createMenuItem(String label, String icon, Runnable menuAction) {
        return getEnvironment().createMenuItem(label, icon, menuAction);
    }

    /**
     * Create a menu item.
     *
     * @param label Label of the menu item
     * @return Menu item. By default, the menu item will be created from the "application environment"
     * {@link ApplicationEnvironment#createMenuItemGroup(String)}
     */
    public ApplicationMenuItemGroup createMenuItemGroup(String label) {
        return getEnvironment().createMenuItemGroup(label);
    }

    /**
     * Close the application by closing all registered "resources". If the associated session is not closed and no other
     * Application instance exists, it will also be closed.
     */
    public synchronized void close() {
        synchronized(alerts) {
            for(Object owner: alerts.keySet()) {
                alerts.get(owner).forEach(Alert::remove);
            }
            alerts.clear();
        }
        closing = true;
        VaadinSession vs = VaadinSession.getCurrent();
        if(vs != null) {
            @SuppressWarnings("unchecked") ArrayList<Application> aList = (ArrayList<Application>) vs.getAttribute(APP_KEY);
            if (aList != null) {
                aList.remove(this);
            }
        }
        while(resources.size() > 0) {
            Closeable resource = resources.remove(0).get();
            if(resource != null) {
                try {
                    resource.close();
                } catch (IOException ignored) {
                }
            }
        }
        if(vs != null) {
            ArrayList<UI> uis = new ArrayList<>(vs.getUIs());
            if(ui != null) {
                uis.remove(ui);
            }
            uis.removeIf(u -> u.isClosing() || get(u) == null);
            if(uis.isEmpty()) {
                removeUI(true);
                try {
                    VaadinService.getCurrentRequest().getWrappedSession().invalidate();
                } catch(Throwable ignored) {
                }
                try {
                    vs.getSession().invalidate();
                } catch(Throwable ignored) {
                }
                try {
                    vs.close();
                } catch(Throwable ignored) {
                }
            } else {
                removeUI(true);
            }
        }
        synchronized (commands) {
            commands.notifyAll();
        }
    }

    /**
     * Register a "resource" that will be closed when the application is shutdown.
     *
     * @param resource Resource to close.
     */
    public void registerResource(Closeable resource) {
        resources.add(new WeakReference<>(resource));
    }

    /**
     * Get the current application.
     *
     * @return Current application.
     */
    public static Application get() {
        return get(null);
    }

    /**
     * Get the application for the given UI.
     *
     * @param ui UI for which application needs to be obtained
     * @return Application for the given UI.
     */
    public static Application get(UI ui) {
        if(ui == null) {
            ui = UI.getCurrent();
        }
        if(ui == null) {
            return null;
        }
        UI u = ui;
        //noinspection unchecked
        return ((ArrayList<Application>)VaadinSession.getCurrent().getAttribute(APP_KEY)).stream()
                .filter(a -> a.ui == u).findAny().orElse(null);
    }

    /**
     * Register an alert with the application.
     *
     * @param alert Alert to be registered.
     * @param owner Owner of the alert.
     */
    public static void registerAlert(Alert alert, Object owner) {
        Application a = Application.get();
        if(a == null) {
            return;
        }
        a.regAlert(alert, owner);
    }

    /**
     * Get alert list for a specified owner.
     *
     * @param owner Owner of the alert
     * @return List of alerts belonging to this owner. (<code>null</code> is returned if application is not reachable
     * or if the owner doesn't own any alert).
     */
    public static List<Alert> getAlerts(Object owner) {
        Application a = get();
        if(a == null) {
            return null;
        }
        return a.alerts.get(owner);
    }

    /**
     * Get the number of alerts for a specified owner.
     *
     * @param owner Owner of the alert
     * @return Number of alert. (-1 is returned if application is not reachable)
     */
    public static int getAlertCount(Object owner) {
        Application a = get();
        if(a == null) {
            return -1;
        }
        List<Alert> list = a.alerts.get(owner);
        return list == null ? 0 : list.size();
    }

    /**
     * This method is invoked whenever alter count changes for a particular owner.
     *
     * @param owner Alert owner
     */
    public void alertCountChanged(Object owner) {
    }

    /**
     * Show all alerts.
     */
    public static void showAlerts() {
        Application a = get();
        if(a == null) {
            return;
        }
        for(AlertList alerts: a.alerts.values()) {
            alerts.forEach(Alert::show);
        }
    }

    /**
     * Show all alerts of the specified owner.
     *
     * @param owner Owner
     */
    public static void showAlerts(Object owner) {
        Application a = get();
        if(a == null) {
            return;
        }
        List<Alert> list = a.alerts.get(owner);
        if(list == null) {
            return;
        }
        list.forEach(Alert::show);
    }

    /**
     * Clear all alerts. (Alerts with any associated "click action" will not be removed).
     */
    public static void clearAlerts() {
        Application a = get();
        if(a == null) {
            return;
        }
        synchronized(a.alerts) {
            new ArrayList<>(a.alerts.keySet()).forEach(a::clrAlerts);
        }
    }

    /**
     * Clear all alerts of the specified owner. (Alerts associated with any "click action" will not be removed).
     *
     * @param owner Owner
     */
    public static void clearAlerts(Object owner) {
        Application a = get();
        if(a == null) {
            return;
        }
        synchronized(a.alerts) {
            a.clrAlerts(owner);
        }
    }

    private void clrAlerts(Object owner) {
        List<Alert> list = alerts.get(owner);
        if(list == null) {
            return;
        }
        List<Alert> listToRemove = new ArrayList<>(list);
        listToRemove.removeIf(a -> !a.deleteOnClose());
        listToRemove.forEach(this::removeAlert);
        if(list.isEmpty()) {
            alerts.remove(owner);
        }
    }

    /**
     * Clear an alert.
     *
     * @param alert Alert to be cleared.
     */
    public static void clearAlert(Alert alert) {
        Application a = get();
        if(a == null) {
            return;
        }
        a.removeAlert(alert);
    }

    /**
     * Remove an alert unconditionally.
     *
     * @param alert Alert to be removed
     */
    final void removeAlert(Alert alert) {
        synchronized(alerts) {
            for(AlertList list : alerts.values()) {
                if(list.remove(alert)) {
                    if(list.isEmpty()) {
                        alerts.remove(list.owner);
                        break;
                    }
                }
            }
        }
        if(alert.isOpened()) {
            alert.close();
        }
    }

    /**
     * Show a warning message from the parameter passed. The parameter will be converted to <code>String</code> by
     * invoking the method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param message Message.
     */
    public static void warning(Object message) {
        notification(null, message,1);
    }

    /**
     * Show a message on the tray from the parameter passed. The parameter will be converted to <code>String</code> by
     * invoking the method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param message Message.
     */
    public static void tray(Object message) {
        notification(null, null, message,1, Notification.Position.BOTTOM_END, false);
    }

    /**
     * Show a message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param message Message.
     */

    public static void message(Object message) {
        if(message instanceof Throwable) {
            error(message);
        } else {
            notification(null, message,0);
        }
    }

    /**
     * Show an error message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param message Message.
     */
    public static void error(Object message) {
        notification(null, message,2);
    }


    /**
     * Show a warning message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param owner Owner of the message.
     * @param message Message.
     */
    public static void warning(Object owner, Object message) {
        notification(owner, message,1);
    }

    /**
     * Show a message on the tray from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param owner Owner of the message.
     * @param message Message.
     */
    public static void tray(Object owner, Object message) {
        notification(null, owner, message,1, Notification.Position.BOTTOM_END, false);
    }

    /**
     * Show a message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param owner Owner of the message.
     * @param message Message.
     */

    public static void message(Object owner, Object message) {
        if(message instanceof Throwable) {
            error(owner, message);
        } else {
            notification(owner, message,0);
        }
    }

    /**
     * Show an error message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param owner Owner of the message.
     * @param message Message.
     */
    public static void error(Object owner, Object message) {
        notification(owner, message, 2);
    }

    private static void notification(Object owner, Object message, int messageType) {
        notification(null, owner, message, messageType, null, true);
    }

    /**
     * Show a notification. (For internal use only).
     * @param a Application.
     * @param owner Owner of the message.
     * @param message Message object.
     * @param messageType Message type - 0: Normal, 1:Warning, 2: Error
     * @param position Position of the message on the screen.
     */
    static void notification(Application a, Object owner, Object message, int messageType,
                             Notification.Position position) {
        notification(a, owner, message, messageType, position, messageType == 2);
    }

    private static void notification(Application a, Object owner, Object message, int messageType,
                                     Notification.Position position, boolean log) {
        if(a == null) {
            a = get();
        }
        if(message instanceof Notification) {
            if(a != null) {
                a.access(((Notification) message)::open);
            } else {
                ((Notification) message).open();
            }
            return;
        }
        String m = null;
        if(log && (a == null || messageType == 2)) {
            if(message instanceof Throwable) {
                if(a == null) {
                    ((Throwable)message).printStackTrace();
                } else {
                    a.log(message);
                }
            } else {
                if(a != null) {
                    m = a.getEnvironment().toDisplay(message);
                    a.log(a.getEnvironment().toString(m));
                }
            }
        }
        if(m == null) {
            m = a == null ? message.toString() : a.getEnvironment().toDisplay(message);
        }
        Alert n = new Alert(m.replace("\n", "<BR>"));
        if(position != null) {
            n.setPosition(position);
        }
        switch(messageType) {
            case 1 -> { // Warning
                messageType = 20000;
                n.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            }
            case 2 -> { // Error
                messageType = Integer.MAX_VALUE;
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            default -> {
                messageType = 10000;
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        }
        n.setDuration(messageType);
        if(a == null) {
            System.err.println(m);
            n.open();
        } else {
            a.access(n::open);
            a.regAlert(n, owner);
        }
    }

    private void regAlert(Alert alert, Object owner) {
        alert.addOpenedChangeListener(e -> {
            if(!alert.isOpened() && alert.deleteOnClose()) {
                removeAlert(alert);
            }
        });
        if(owner != null) {
            synchronized(alerts) {
                alerts.computeIfAbsent(owner, k -> new AlertList(owner)).add(alert);
            }
        }
    }

    /**
     * Log something (goes to the System error stream).
     *
     * @param anything Message to log, it could be a {@link Throwable}
     */
    public void log(Object anything) {
        if(anything instanceof Throwable) {
            ((Throwable)anything).printStackTrace();
        } else {
            System.err.println(anything);
        }
    }

    /**
     * Log something along with an exception (goes to the System error stream).
     *
     * @param anything Message to log, it could be a {@link Throwable}
     * @param error Error to be printed
     */
    public void log(Object anything, Throwable error) {
        if(anything instanceof Throwable) {
            ((Throwable)anything).printStackTrace();
        } else {
            System.err.println(anything);
        }
        if(error != null) {
            error.printStackTrace();
        }
    }

    /**
     * Get the "context path" of the application.
     *
     * @return Context path
     */
    public String getLinkName() {
        return link;
    }

    /**
     * Get the device (browser) height.
     *
     * @return Device height.
     */
    public int getDeviceHeight() {
        if(deviceHeight > 0) {
            return deviceHeight;
        }
        getDeviceWidth();
        return deviceHeight;
    }

    /**
     * Get the device (browser) width.
     *
     * @return Device width.
     */
    public int getDeviceWidth() {
        if(deviceWidth > 0) {
            return deviceWidth;
        }
        if(viewManager != null) {
            viewManager.content.checkSize();
        }
        return deviceWidth;
    }

    /**
     * get tje URL of application.
     *
     * @return URL of the application as a string.
     */
    public String getURL() {
        return applicationView == null ? null : applicationView.getURL();
    }

    /**
     * Get the height of the "content area" of the application.
     *
     * @return Pixel height of the content area.
     */
    public int getContentHeight() {
        return viewManager.content.h();
    }

    /**
     * Get the width of the "content area" of the application.
     *
     * @return Pixel width of the content area.
     */
    public int getContentWidth() {
        return viewManager.content.w();
    }

    /**
     * Add a content resized listener so that the listener will be alerted whenever the "content area" of the
     * application is resized.
     *
     * @param listener Listener
     * @return Registration.
     */
    public Registration addContentResizedListener(ResizedListener listener) {
        return viewManager.content.addContentResizedListener(listener);
    }

    /**
     * Show a notification on the screen.
     *
     * @param text Text of the notification
     */
    public void showNotification(String text) {
        showNotification(null, text);
    }

    /**
     * Show a notification on the screen.
     *
     * @param caption Caption of the notification
     * @param text Text of the notification
     */
    public void showNotification(String caption, String text) {
        notification(this, caption, text, 2, Notification.Position.BOTTOM_END,false);
    }

    /**
     * Show error notification,
     *
     * @param error Error
     */
    public void showNotification(Throwable error) {
        showNotification(null, error);
    }

    /**
     * Show error notification.
     *
     * @param caption Caption to be displayed
     * @param error Error
     */
    public void showNotification(String caption, Throwable error) {
        String e = getEnvironment().toDisplay(error);
        if(!e.toLowerCase().contains("error")) {
            e = "Error: " + e;
        }
        notification(this, caption, e,2, Notification.Position.BOTTOM_END,false);
    }

    /**
     * For internal use only.
     *
     * @param applicationView Application view.
     */
    final void setMainView(ApplicationView applicationView) {
        this.applicationView = applicationView;
        if(this.viewManager == null) {
            viewManager = new ViewManager(this);
            applicationLayout.closeMenu();
            login();
        }
    }

    /**
     * Set caption for the application (It will set the caption for the {@link ApplicationLayout} and the {@link Page}).
     *
     * @param caption Caption to be set
     */
    public void setCaption(String caption) {
        if(applicationLayout != null) {
            applicationLayout.setCaption(caption);
        }
        getPage().setTitle(caption);
    }

    /**
     * Speak out the given sentence if the {@link ApplicationLayout} is supporting it (The default layout
     * {@link ApplicationFrame} supports it). Please note that the speaker
     * must have been switched on before calling this (See {@link SpeakerButton}).
     *
     * @param sentence Sentence to speak out.
     */
    public void speak(String sentence) {
        if(sentence != null && speaker && viewManager != null) {
            sentence = sentence.trim();
            if(sentence.isEmpty()) {
                return;
            }
            sentence = sentence.replaceAll("\\s+", " ");
            while (sentence.contains("  ")) {
                sentence = sentence.replace("  ", " ");
            }
            viewManager.content.speak(sentence);
        }
    }

    /**
     * Check whether still speaking the previously set sentences.
     *
     * @return True/false.
     */
    public boolean isSpeaking() {
        return speaker && viewManager != null && viewManager.content.speaking;
    }

    /**
     * Internally used by {@link SpeakerButton} to switch the speaker on or off.
     *
     * @param on True to switch on the speaker
     */
    synchronized void setSpeaker(boolean on) {
        this.speaker = on;
        if(speakerToggledListeners != null) {
            speakerToggledListeners.forEach(listener -> listener.speaker(this.speaker));
        }
    }

    /**
     * Internally used by {@link SpeakerButton} to check the status of the speaker.
     *
     * @return True if the speaker is on.
     */
    public boolean isSpeakerOn() {
        return speaker;
    }

    Registration addSpeakerToggedListener(SpeakerToggledListener listener) {
        if(listener == null) {
            return null;
        }
        if(speakerToggledListeners == null) {
            speakerToggledListeners = new HashSet<>();
        }
        speakerToggledListeners.add(listener);
        return () -> speakerToggledListeners.remove(listener);
    }

    /**
     * This method can be overridden to accept login credentials and {@link #loggedin()} must be called if successfully logged in.
     */
    protected void login() {
        loggedin();
    }

    /**
     * Get the IP address of the application.
     *
     * @return IP address.
     */
    public String getIPAddress() {
        return webBrowser.getAddress();
    }

    /**
     * Get an identifier of the application (can be used for logging etc.).
     *
     * @return Ab identifier derived from the browser information.
     */
    public String getIdentifier() {
        return webBrowser.getBrowserApplication();
    }

    /**
     * Get the major version number (from browser information).
     *
     * @return Major version number.
     */
    public int getMajorVersion() {
        return webBrowser.getBrowserMajorVersion();
    }

    /**
     * Get the minor version number (from browser information).
     *
     * @return Minor version number.
     */
    public int getMinorVersion() {
        return webBrowser.getBrowserMinorVersion();
    }

    /**
     * For internal use only. (Execute a "view").
     *
     * @param view View to execute
     * @param doNotLock Whether to lock the parent view or not
     * @param parent Parent view if any, otherwise <code>null</code>
     */
    void execute(View view, boolean doNotLock, View parent) {
        viewManager.attach(view, doNotLock, parent);
    }

    /**
     * For internal use only. (Close a "view").
     *
     * @param view View to close
     */
    void close(View view) {
        viewManager.detach(view);
        viewDetached(view);
    }

    /**
     * For internal use only. (Select a view)
     *
     * @param view View to be selected
     * @return Whether the view was selected or not.
     */
    boolean select(View view) {
        return viewManager.select(view);
    }

    /**
     * This method must be called from {@link #login()} when login credentials are verified.
     */
    protected final void loggedin() {
        viewManager.loggedIn(this);
        applicationLayout.toggleMenu();
        Component menuSearcher = applicationLayout.getMenuSearcher();
        if(menuSearcher instanceof Focusable) {
            ((Focusable<?>) menuSearcher).focus();
        }
        getContentWidth(); // Sometimes, content-width is not updated. This will trigger an update.
    }

    /**
     * Set a component to be focused later when the current "view" is selected again. This is useful in situations like bringing back to
     * the current "data entry" screen to a particular field.
     *
     * @param component Component to be focused later.
     */
    public void setPostFocus(Component component) {
        View v = viewManager.getActiveView();
        if(v != null) {
            v.setPostFocus(component);
        }
    }

    /**
     * Get the currently active {@link View}.
     *
     * @return Currently active View or null if no View is active.
     */
    public View getActiveView() {
        return viewManager.getActiveView();
    }

    /**
     * Set the polling interval with this application as the owner.
     * (Please see {@link #setPollInterval(Object, int)} ).
     *
     * @param intervalInMillis Interval in milliseconds
     */
    public void setPollInterval(int intervalInMillis) {
        setPollInterval(this, intervalInMillis);
    }

    /**
     * Set polling interval. Several owners may be requesting it and the value actually set will the lowest value. It is
     * the owner's responsibility to release polling by invoking stopPolling when polling is
     * no more required.
     *
     * @param owner Object that is invoking the request
     * @param intervalInMillis Interval in milliseconds
     * @see #stopPolling(Object)
     */
    public void setPollInterval(Object owner, int intervalInMillis) {
        if(owner == null) {
            return;
        }
        if(intervalInMillis <= 0) {
            intervalInMillis = -1;
        }
        Integer pi = pollIntervals.get(owner);
        if(pi == null) {
            if(intervalInMillis == -1) {
                return;
            }
            pollIntervals.put(owner, intervalInMillis);
            setPoll();
            return;
        }
        if(pi == intervalInMillis) {
            return;
        }
        if(intervalInMillis == -1) {
            pollIntervals.remove(owner);
            setPoll();
            return;
        }
        pollIntervals.put(owner, intervalInMillis);
        if(intervalInMillis < pi) {
            if (intervalInMillis <= ui.getPollInterval()) {
                ui.setPollInterval(intervalInMillis);
            }
            return;
        }
        setPoll();
    }

    private void setPoll() {
        int pi = pollIntervals.values().stream().min(Comparator.comparing(Integer::valueOf)).orElse(-1);
        UI u = ui;
        if(u != null) {
            u.access(() -> {
                if(ui != null) {
                    u.setPollInterval(pi);
                }
            });
        }
    }

    /**
     * Start polling by setting the interval to 1000 milliseconds.
     *
     * @param owner Object that is invoking the request
     */
    public void startPolling(Object owner) {
        setPollInterval(owner, 1000);
    }

    /**
     * Stop polling. Polling will not be stopped if requests are there from other owners.
     *
     * @param owner Owner who requested for polling earlier
     */
    public void stopPolling(Object owner) {
        setPollInterval(owner, -1);
    }

    /**
     * Get the view in which a particular component is currently appearing.
     *
     * @param component Component to be checked
     * @return View in which the component is appearing or <code>null</code> if it's not displayed in any of the views.
     */
    public View getViewFor(Component component) {
        return viewManager == null ? null : viewManager.getViewFor(component);
    }

    /**
     * This method is invoked whenever a running {@link View} is detached. (Sometimes, the
     * application wants to take some action when a view is detached).
     *
     * @param view View that is detached now.
     */
    protected void viewDetached(View view) {
    }

    /**
     * Get the number of active {@link View}s ({@link HomeView}s are not counted).
     *
     * @return Number of active views.
     */
    public int getActiveViewCount() {
        return (int) viewManager.contentMenu.keySet().stream().filter(v -> !v.isHomeView()).count();
    }

    /**
     * Get active {@link View}s ({@link HomeView}s are not included).
     *
     * @return Stream of active views.
     */
    public Stream<View> getActiveViews() {
        return viewManager.contentMenu.keySet().stream().filter(v -> !v.isHomeView());
    }

    /**
     * Execute a command. (The command will be executed only if the current UI is associated with this application).
     *
     * @param command Command to execute by invoking its {@link Runnable#run()} method.
     * @param clearViews If <code>true</code> is passed, all {@link View}s will be closed (by invoking
     *                   {@link View#abort()} calls) before the command is executed.
     */
    public void execute(Runnable command, boolean clearViews) {
        if(viewManager != null) {
            UI ui = getUI();
            if(ui != null && UI.getCurrent() == ui) {
                viewManager.execute(command, clearViews);
            }
        }
    }

    /**
     * Execute a command. (The command will be executed only if the current UI is associated with this application).
     *
     * @param command Command to execute by invoking its {@link Runnable#run()} method.
     */
    public void execute(Runnable command) {
        if(viewManager != null) {
            UI ui = getUI();
            if(ui != null && UI.getCurrent() == ui) {
                command.run();
            }
        }
    }

    /**
     * Set some data in this application so that it can be retrieved later.
     *
     * @param anything Data (An instance of anyClass).
     */
    public void setData(Object anything) {
        if(anything != null) {
            data.put(anything.getClass(), anything);
        }
    }

    /**
     * Set some data in this application so that it can be retrieved later.
     *
     * @param anyClass Kind of data.
     * @param anything Data (An instance of anyClass).
     * @param <T> Type of data.
     */
    public <T> void setData(Class<T> anyClass, T anything) {
        if(anything != null) {
            data.put(anyClass, anything);
        }
    }

    /**
     * Remove the data previously set in this application.
     *
     * @param anything Data to remove.
     * @param <T> Type of data.
     */
    public <T> void removeData(T anything) {
        if(anything != null) {
            removeData(anything.getClass());
        }
    }

    /**
     * Remove the data previously set in this application.
     *
     * @param anyClass Class of the data to remove.
     * @param <T> Type of data.
     */
    public <T> void removeData(Class<T> anyClass) {
        if(anyClass != null) {
            data.remove(anyClass);
        }
    }

    /**
     * Get the data that was previously set in this application.
     *
     * @param anyClass Class of the data to get.
     * @param <T> Type of data.
     * @return Data if exists, otherwise <code>null</code>.
     */
    public <T> T getData(Class<T> anyClass) {
        //noinspection unchecked
        return anyClass == null ? null : (T)data.get(anyClass);
    }

    /**
     * Get ISO 2-character the country-code of the user in capital letters. (Examples: "US" for US, "IN" for India).
     * This information is used for formatting the numbers in {@link com.storedobject.vaadin.util.NumericField}.
     *
     * @return ISO country code. (Default is "US").
     */
    public String getCountry() {
        return "US";
    }

    /**
     * Get the caption for this application. By default, {@link ApplicationLayout#getCaption()} is returned.
     *
     * @return Caption.
     */
    public String getCaption() {
        return applicationLayout == null ? null : applicationLayout.getCaption();
    }

    /**
     * Check whether the application is currently in full-screen mode or not.
     *
     * @return True/false.
     */
    public boolean isFullScreenMode() {
        return viewManager.isFS();
    }

    private class AlertList extends ArrayList<Alert> {

        private final Object owner;

        private AlertList(Object owner) {
            this.owner = owner;
        }

        @Override
        public Alert remove(int index) {
            Alert a = super.remove(index);
            if(a != null) {
                alertCountChanged(owner);
            }
            return a;
        }

        @Override
        public boolean remove(Object o) {
            if(super.remove(o)) {
                alertCountChanged(owner);
                return true;
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if(super.removeAll(c)) {
                alertCountChanged(owner);
                return true;
            }
            return false;
        }

        @Override
        public boolean removeIf(Predicate<? super Alert> filter) {
            if(super.removeIf(filter)) {
                alertCountChanged(owner);
                return true;
            }
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            if(super.retainAll(c)) {
                alertCountChanged(owner);
                return true;
            }
            return false;
        }

        @Override
        public boolean add(Alert alert) {
            super.add(alert);
            alertCountChanged(owner);
            return true;
        }

        @Override
        public void add(int index, Alert element) {
            super.add(index, element);
            alertCountChanged(owner);
        }

        @Override
        public boolean addAll(Collection<? extends Alert> c) {
            super.addAll(c);
            alertCountChanged(owner);
            return true;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Alert> c) {
            super.addAll(index, c);
            alertCountChanged(owner);
            return true;
        }
    }

    boolean executing(View view) {
        return viewManager.executing(view);
    }

    private static class ViewManager {

        private final ApplicationMenu menu;
        private final ApplicationView applicationView;
        private final List<View> stack = new ArrayList<>();
        private final List<View> homeStack = new ArrayList<>();
        private final Map<View, ApplicationMenuItem> contentMenu = new HashMap<>();
        private final Map<View, View> parents = new HashMap<>();
        private View homeView;
        private Runnable pendingToRun;
        private final Content content;

        public ViewManager(Application application) {
            this.applicationView = application.applicationView;
            this.menu = applicationView.layout.getMenu();
            this.content = new Content(application);
            applicationView.layout.setContent(this.content);
        }

        public void loggedIn(Application application) {
            applicationView.layout.loggedin(application);
            applicationView.layout.drawMenu(application);
        }

        private void runPending() {
            if(pendingToRun != null) {
                Runnable r = pendingToRun;
                pendingToRun = null;
                r.run();
            }
        }
        
        public void execute(Runnable runnable, boolean clearViews) {
            if(runnable == null) {
                runnable = () -> { };
            }
            if(pendingToRun != null) {
                pendingToRun = runnable;
                return;
            }
            if(!clearViews || clearViewStack(runnable)) {
                runnable.run();
            }
        }

        private boolean clearViewStack(Runnable pending) {
            View view = contentMenu.keySet().stream().filter(v -> !v.isHomeView()).findAny().orElse(null);
            if(view == null) {
                return true;
            }
            pendingToRun = pending;
            view.abort();
            return false;
        }

        private View getViewFor(Component c) {
            for(View v: stack) {
                if(isViewFor(c, v)) {
                    return v;
                }
            }
            return null;
        }

        private static boolean isViewFor(Component c, View v) {
            if(c == null) {
                return false;
            }
            if(v.getComponent() == c) {
                return true;
            }
            return isViewFor(c.getParent().orElse(null), v);
        }

        public void attach(View view, boolean doNotLock, View parent) {
            synchronized(stack) {
                attachInt(view, doNotLock, parent);
            }
        }

        private void attachInt(View view, boolean doNotLock, View parent) {
            if(select(view)) {
                return;
            }
            View current = getActiveView();
            Component c = view.getComponent();
            boolean window = c instanceof Dialog;
            if(view.isHomeView() && homeView != null && !window) {
                homeStack.add(homeView);
                homeView.setVisibility(false);
                homeView = null;
            }
            if(window && parent == null && stack.size() > 0) {
                parent = stack.get(stack.size() - 1);
                if(parent.getComponent() instanceof Dialog) {
                    parent = null;
                }
            }
            if(parent != null) {
                if(!doNotLock) {
                    if(abortChild(parent, false)) {
                        if(abortChild(parent, false)) {
                            warning("Unable to run: " + view.getCaption());
                            return;
                        }
                    }
                    ApplicationMenuItem m = contentMenu.get(parent);
                    if(m != null) {
                        m.setEnabled(false);
                    }
                }
                parents.put(view, parent);
            }
            if(!window) {
                hideAllContent(null);
            }
            stack.add(view);
            view.decorateComponent();
            boolean fs;
            if(c instanceof Dialog && isFS()) {
                fs = true;
            } else {
                fs = isFS(view, parent);
            }
            applicationView.setLayoutVisible(!fs);
            if(c instanceof Dialog) {
                UI.getCurrent().add(c);
            } else {
                if(fs) {
                    applicationView.add(c);
                } else {
                    content.getElement().appendChild(c.getElement());
                }
            }
            view.setVisibility(true);
            ApplicationMenuItem m = view.getMenuItem(() -> select(view));
            if(view.isHomeView()) {
                if(!(view.getComponent() instanceof Dialog)) {
                    homeView = view;
                }
            } else {
                menu.insert(0, m);
            }
            contentMenu.put(view, m);
            if(current != null && current.getComponent() instanceof Dialog && !window) {
                select(current);
                return;
            }
            hilite(m);
            applicationView.layout.viewSelected(view);
        }

        private boolean abortChild(View view, boolean suicide) {
            View child = child(view);
            if(child != null && executing(child)) {
                if(suicide) {
                    child.detachParentOnClose();
                }
                select(child);
                child.abort();
                return true;
            }
            return false;
        }

        public void detach(View view) {
            synchronized(stack) {
                detachInt(view);
            }
        }

        private void detachInt(View view) {
            if(abortChild(view, true)) {
                return;
            }
            ApplicationMenuItem m = contentMenu.get(view);
            if(m == null) {
                runPending();
                return;
            }
            if(!(view.isHomeView())) {
                menu.remove(m);
            }
            if(view == homeView && !(view.getComponent() instanceof Dialog)) {
                homeView = null;
            }
            contentMenu.remove(view);
            view.getComponent().getElement().removeFromParent();
            applicationView.setLayoutVisible(true);
            stack.remove(view);
            homeStack.remove(view);
            View parent = parents.remove(view);
            if(parent != null) {
                if(pendingToRun != null) {
                    parent.abort();
                    return;
                }
                m = contentMenu.get(parent);
                if(m != null) {
                    m.setEnabled(true);
                }
                boolean selected = select(parent);
                parent.returnedFrom(view);
                if(!selected) {
                    parent = getActiveView();
                    if(parent != null) {
                        select(parent);
                    }
                }
            } else {
                if(stack.isEmpty()) {
                    if(homeView == null && !homeStack.isEmpty()) {
                        homeView = homeStack.remove(homeStack.size() - 1);
                        homeView.setVisibility(true);
                    }
                    runPending();
                } else {
                    view = stack.get(stack.size() - 1);
                    if(pendingToRun != null) {
                        if(view instanceof HomeView) {
                            runPending();
                            return;
                        }
                        view.abort();
                        return;
                    }
                    select(view);
                }
            }
        }

        public View getActiveView() {
            return stack.size() > 0 ? stack.get(stack.size() - 1) : null;
        }

        private View child(View view) {
            return parents.keySet().stream().filter(k -> parents.get(k) == view).findAny().orElse(null);
        }

        private boolean isFS(View view, View parent) {
            return view.isFullScreen() || isFS(parent);
        }

        private boolean isFS(View view) {
            if(view == null) {
                return false;
            }
            if(view.isFullScreen()) {
                return true;
            }
            return isFS(parents.get(view));
        }

        private boolean isFS() {
            return !applicationView.layout.getComponent().isVisible();
        }

        private void hideAllContent(View except) {
            stack.forEach(v -> v.setVisibility(v == except));
            if(except != null) {
                applicationView.setLayoutVisible(!isFS(except));
            }
        }

        private boolean select(View view) {
            ApplicationMenuItem m = contentMenu.get(view);
            if(m == null) {
                return false;
            }
            if(!m.isEnabled()) {
                return true; // Not selected because menu is deactivated
            }
            View current = getActiveView();
            if(view == current) {
                current = null;
            }
            if(current != null && current.getComponent() instanceof Dialog
                    && !(view.getComponent() instanceof Dialog)) {
                return true; // Not selected because another dialog is active
            }
            hideAllContent(view);
            hilite(m);
            stack.remove(view);
            stack.add(view);
            applicationView.layout.viewSelected(view);
            return true;
        }

        private boolean executing(View view) {
            return contentMenu.get(view) != null;
        }

        private void hilite(ApplicationMenuItem menuItem) {
            menuItem.setEnabled(true);
            contentMenu.values().forEach(mi -> {
                if(mi == menuItem) {
                    mi.hilite();
                } else {
                    mi.dehilite();
                }
            });
        }

        @Tag("so-app-content")
        @JsModule("./so/appcontent/content.js")
        private static class Content extends LitComponent {

            private final ArrayList<WeakReference<ResizedListener>> resizeListeners = new ArrayList<>();
            private int width = -1, height = -1;
            private boolean speaking = false;
            private StringBuilder sentence;
            private final Application application;

            public Content(Application application) {
                this.application = application;
                getElement().setProperty("idContent", "so" + ID.newID());
                application.applicationLayout.resizeContent();
            }

            @ClientCallable
            private void resized(int w, int h, int ww, int wh) {
                if(w != width || h != height) {
                    this.width = w;
                    this.height = h;
                    resizeListeners.removeIf(wr -> wr.get() == null);
                    resizeListeners.forEach(wr -> {
                        ResizedListener l = wr.get();
                        if (l != null) {
                            l.resized(width, height);
                        }
                    });
                }
                application.fireResized(ww, wh);
            }

            int w() {
                if(width > 0) {
                    return width;
                }
                checkSize();
                return width;
            }

            int h() {
                if(height > 0) {
                    return height;
                }
                w();
                return height;
            }

            private void checkSize() {
                executeJS("sendSize");
            }

            Registration addContentResizedListener(ResizedListener listener) {
                resizeListeners.add(new WeakReference<>(listener));
                if(height > -1) {
                    listener.resized(width, height);
                }
                return () -> {
                    for(WeakReference<ResizedListener> w: resizeListeners) {
                        if(w.get() == listener) {
                            resizeListeners.remove(w);
                            return;
                        }
                    }
                };
            }

            synchronized void speak(String sentence) {
                if(speaking) {
                    if(this.sentence == null) {
                        this.sentence = new StringBuilder(sentence);
                    } else {
                        this.sentence.append(' ').append(sentence);
                    }
                    return;
                }
                if(this.sentence != null) {
                    this.sentence.append(' ').append(sentence);
                }
                toSpeak(this.sentence == null ? sentence : this.sentence.toString());
            }

            @ClientCallable
            private synchronized void spoken() {
                if(sentence != null) {
                    toSpeak(sentence.toString());
                    return;
                }
                speaking = false;
            }

            private void toSpeak(String sentence) {
                speaking = true;
                getElement().setProperty("speak", sentence);
                this.sentence = null;
            }
        }
    }
}
