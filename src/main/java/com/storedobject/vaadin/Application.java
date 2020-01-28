package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.GeneratedVaadinNotification;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * Application is the base class for creating a 'single page' web applications. The 'single page' should be defined
 * by extending {@link com.storedobject.vaadin.ApplicationView} class.
 * <pre>
 * <code>
 * public class Demo extends Application {
 *
 *    {@literal @}Override
 *     protected ApplicationLayout createLayout() {
 *         return new MyLayout();
 *     }
 *
 *    {@literal @}Route("")
 *    {@literal @}Push(PushMode.MANUAL)
 *    {@literal @}BodySize(height = "100vh", width = "100vw")
 *    {@literal @}Theme(value = Lumo.class, variant = Lumo.LIGHT)
 *     public static class DemoView extends ApplicationView {
 *
 *        {@literal @}Override
 *         protected Application createApplication() {
 *             return new Demo();
 *         }
 *     }
 * }
 *
 * public class MyLayout extends SplitLayout implements ApplicationLayout, ApplicationMenu {
 *
 *     public MyLayout() {
 *         super(new Div(), new Div());
 *         setSplitterPosition(15);
 *         setHeight("100vh");
 *         setWidth("100vw");
 *         getPrimaryComponent().getElement().getStyle().set("background-color", "lightblue");
 *     }
 *
 *    {@literal @}Override
 *     public Component getComponent() {
 *         return this;
 *     }
 *
 *    {@literal @}Override
 *     public ApplicationMenu getMenu() {
 *         return this;
 *     }
 *
 *    {@literal @}Override
 *     public HasComponents getMenuPane() {
 *         return (Div)getPrimaryComponent();
 *     }
 *
 *    {@literal @}Override
 *     public Component getContent() {
 *         return getSecondaryComponent();
 *     }
 *
 *    {@literal @}Override
 *     public void drawMenu(Application application) {
 *         getMenuPane().add(new HtmlComponent("hr"));
 *         add(application.createMenuItem(...));
 *         add(application.createMenuItem(...));
 *         add(application.createMenuItem(...));
 *     }
 * }
 * </code>
 * </pre>
 * The 'single page' can be any layout component that implements the interface {@link com.storedobject.vaadin.ApplicationLayout}. The layout
 * typically contains a 'menu' area and 'content' area. One can use Vaadin's <code>AppLayout</code> or similar components as the base for this.
 * The 'menu' area will contain {@link com.storedobject.vaadin.ApplicationMenuItem} instances and when a 'menu item' is clicked, the <code>Runnable</code>
 * action associated with it will be executed. One may associate any <code>Runnable</code> action with a 'menu item' such as generating a report or
 * invkoing a {@link com.storedobject.vaadin.View}. If a {@link com.storedobject.vaadin.View} is invoked, its associated 'view component' is
 * displayed in the 'content' area and its 'caption' is inserted as a new 'menu item' in the 'menu' area. The 'content' area displays only the 'active
 * view' (currently selected or executed view) and hides all previously displayed 'views' but any those 'views' can be made active again
 * by licking on its respective 'menu item' created from its 'caption'.
 *
 * @author Syam
 */
public abstract class Application {

    private static final String APP_KEY = "$so-app";
    private AlertCloser alertCloser = new AlertCloser();
    private ApplicationView applicationView;
    private ApplicationLayout applicationLayout;
    private UI ui;
    private ApplicationEnvironment environment;
    private Map<Object, AlertList> alerts = new HashMap<>();
    private Map<Object, Integer> pollIntervals = new HashMap<>();
    private ViewManager viewManager;
    private ArrayList<WeakReference<Closeable>> resources = new ArrayList<>();
    private String link;
    private int deviceWidth = -1, deviceHeight = -1;
    private final ArrayList<Command> commands = new ArrayList<>();
    private transient boolean closed = false;
    String error;
    private boolean speaker = false;
    interface SpeakerToggledListner {
        void speaker(boolean on);
    }
    private Set<SpeakerToggledListner> speakerToggledListeners;
    private ArrayList<WeakReference<BrowserResizedListener>> resizeListeners;

    /**
     * This method is invoked as documented in Vaadin Flow's documentation for UI's init(VaadinRequest) method.
     * If you want to override this method, make sure that <code>super</code> is called.
     *
     * @param request Vaadin Request
     */
    protected void init(VaadinRequest request) {
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
                while (!closed) {
                    synchronized (commands) {
                        if(!commands.isEmpty()) {
                            ui.access(commands.remove(0));
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
    public Registration addBrowserResizedListener(BrowserResizedListener listener) {
        if(listener == null) {
            return null;
        }
        if(resizeListeners == null) {
            resizeListeners = new ArrayList<>();
            getPage().addBrowserWindowResizeListener(e -> {
                fireResized(e.getWidth(), e.getHeight());
                applicationView.receiveSize();
            });
        }
        resizeListeners.add(new WeakReference<>(listener));
        return () -> {
            for(WeakReference<BrowserResizedListener> w: resizeListeners) {
                if(w.get() == listener) {
                    resizeListeners.remove(w);
                    return;
                }
            }
        };
    }

    private void fireResized(int width, int height) {
        if(width == deviceWidth && height == deviceHeight) {
            return;
        }
        if(resizeListeners != null) {
            resizeListeners.removeIf(w -> w.get() == null);
            resizeListeners.forEach(w -> {
                BrowserResizedListener l = w.get();
                if(l != null) {
                    l.browserResized(width, height);
                }
            });
        }
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
     * Inform Application about the UI change (This can happen when users refreshes the page).
     *
     * @param ui UI to set
     */
    final void setUI(UI ui) {
        if(ui == null) {
            return;
        }
        this.ui = ui;
        ui.getSession().setAttribute(APP_KEY, this);
        attached();
    }

    /**
     * Invoked whenever this application is attached to a UI. Default implementation does nothing.
     */
    public void attached() {
    }

    /**
     * Invoked whenever this application is detached from its UI (it may get attached again to another UI if the user just refreshed
     * the browser). The default implementation closes the application ({@link #close()}) after 20 seconds.
     */
    public void deatached() {
        this.ui = null;
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
     * Lock the UI and execute a command.
     *
     * @param command Command to execute.
     * @return A future that can be used to check for task completion and to cancel the command.
     */
    public Future<Void> access(Command command) {
        return getUI().access(command);
    }

    /**
     * This method is invoked when the applicationn comes up.
     *
     * @param link The context path of the application.
     * @return True if application can go ahead. Otherwise, an "Initialization failed" message is displayed. Default return value is <code>true</code>.
     */
    protected boolean init(@SuppressWarnings("unused") String link) {
        return true;
    }

    /**
     * This method is invoked only once to determine the layout of the application.
     *
     * @return Application layout.
     */
    protected abstract ApplicationLayout createLayout();

    /**
     * An "application environment" may be created to specifiy certain behaviours of the applicaion. If this method returns <code>null</code>, a default
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
     * @return Menu item. By default, the menu item will be created from the "application environment" {@link ApplicationEnvironment#createMenuItem(String, String, Runnable)}
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
     * @return Menu item. By default, the menu item will be created from the "application environment" {@link ApplicationEnvironment#createMenuItem(String, String, Runnable)}
     */
    public ApplicationMenuItem createMenuItem(String label, String icon, Runnable menuAction) {
        return getEnvironment().createMenuItem(label, icon, menuAction);
    }

    /**
     * Create a menu item.
     *
     * @param label Label of the menu item
     * @return Menu item. By default, the menu item will be created from the "application environment" {@link ApplicationEnvironment#createMenuItemGroup(String)}
     */
    public ApplicationMenuItemGroup createMenuItemGroup(String label) {
        return getEnvironment().createMenuItemGroup(label);
    }

    /**
     * Close the application by closing all registered "resources". If the associated session is not closed, it will also be closed.
     */
    public synchronized void close() {
        for(Object owner: alerts.keySet()) {
            alerts.get(owner).forEach(Alert::remove);
        }
        alerts.clear();
        closed = true;
        while(resources.size() > 0) {
            Closeable resource = resources.remove(0).get();
            if(resource != null) {
                try {
                    resource.close();
                } catch (IOException ignored) {
                }
            }
        }
        VaadinSession vs = VaadinSession.getCurrent();
        if(vs != null) {
            vs.setAttribute(APP_KEY, null);
            vs.close();
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
     * @param ui UI
     * @return Current application.
     */
    public static Application get(UI ui) {
        if(ui == null) {
            ui = UI.getCurrent();
        }
        return ui == null ? null : (Application)ui.getSession().getAttribute(APP_KEY);
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
    public void alertCountChanged(@SuppressWarnings("unused") Object owner) {
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
        new ArrayList<>(a.alerts.keySet()).forEach(a::clrAlerts);
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
        a.clrAlerts(owner);
    }

    private void clrAlerts(Object owner) {
        List<Alert> list = alerts.get(owner);
        if(list == null) {
            return;
        }
        List<Alert> listToremove = new ArrayList<>(list);
        listToremove.removeIf(Alert::deleteOnClose);
        listToremove.forEach(this::removeAlert);
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
        for(AlertList list: alerts.values()) {
            if(list.remove(alert)) {
                if(list.isEmpty()) {
                    alerts.remove(list.owner);
                    break;
                }
            }
        }
        alert.close();
    }

    /**
     * Show a warning message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param message Message.
     */
    public static void warning(Object message) {
        notification(null, message,1);
    }

    /**
     * Show a message on the tray from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     *
     * @param message Message.
     */
    public static void tray(Object message) {
        notification(null, message,1, Notification.Position.BOTTOM_END, false);
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
        notification(owner, message,1, Notification.Position.BOTTOM_END, false);
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
        notification(owner, message, messageType, null, true);
    }

    private static void notification(Object owner, Object message, int messageType, Notification.Position position, boolean log) {
        if(message instanceof Notification) {
            ((Notification)message).open();
            return;
        }
        Application a = get();
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
        Alert n = new Alert(m);
        if(position != null) {
            n.setPosition(position);
        }
        switch (messageType) {
            case 1: // Warning
                messageType = 20000;
                n.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                break;
            case 2: // Error
                messageType = Integer.MAX_VALUE;
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                break;
            default:
                messageType = 10000;
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                break;
        }
        n.setDuration(messageType);
        n.open();
        if(a == null) {
            return;
        }
        a.regAlert(n, owner);
    }

    private void regAlert(Alert alert, Object owner) {
        alert.addOpenedChangeListener(alertCloser);
        if(owner != null) {
            alerts.computeIfAbsent(owner, k -> new AlertList(owner)).add(alert);
        }
    }

    private class AlertCloser implements ComponentEventListener<GeneratedVaadinNotification.OpenedChangeEvent<Notification>> {
        @Override
        public void onComponentEvent(GeneratedVaadinNotification.OpenedChangeEvent<Notification> e) {
            Alert alert = (Alert)e.getSource();
            if(!alert.isOpened() && alert.deleteOnClose()) {
                removeAlert(alert);
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

    final void deviceSize(int width, int height) {
        this.deviceWidth = width;
        this.deviceHeight = height;
        fireResized(width, height);
    }

    /**
     * Get the device (browser) height.
     *
     * @return Device height.
     */
    public int getDeviceHeight() {
        if(deviceHeight < 0) {
            if(applicationView != null) {
                applicationView.receiveSize();
            }
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
            if(applicationView != null) {
                applicationView.receiveSize();
            }
        }
        return deviceWidth;
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
        notification(caption, text, 2, Notification.Position.BOTTOM_END,false);
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
        notification(caption, e,2, Notification.Position.BOTTOM_END,false);
    }

    /**
     * For internal use only.
     *
     * @param applicationView Application view.
     */
    final void setMainView(ApplicationView applicationView) {
        this.applicationView = applicationView;
        if(this.viewManager == null) {
            viewManager = new ViewManager(applicationView);
            applicationLayout.toggleMenu();
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
        if(sentence != null && speaker && applicationLayout != null) {
            sentence = sentence.trim();
            if(sentence.isEmpty()) {
                return;
            }
            sentence = sentence.replaceAll("\\s+", " ");
            while (sentence.contains("  ")) {
                sentence = sentence.replace("  ", " ");
            }
            applicationLayout.speak(sentence);
        }
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

    Registration addSpeakerToggedListener(SpeakerToggledListner listener) {
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
        return VaadinSession.getCurrent().getBrowser().getAddress();
    }

    /**
     * Get an identifier of the application (can be used for logging etc.).
     *
     * @return Ab identifier derived from the browser information.
     */
    public String getIdentifier() {
        return VaadinSession.getCurrent().getBrowser().getBrowserApplication();
    }

    /**
     * Get the major version number (from browser information).
     *
     * @return Major version number.
     */
    public int getMajorVersion() {
        return VaadinSession.getCurrent().getBrowser().getBrowserMajorVersion();
    }

    /**
     * Get the minor version number (from browser information).
     *
     * @return Minor version number.
     */
    public int getMinorVersion() {
        return VaadinSession.getCurrent().getBrowser().getBrowserMinorVersion();
    }

    /**
     * For internal use only. (Execute a "view").
     *
     * @param view View to execute
     * @param doNotLock Whether to lock the parent or not
     * @param parentBox Parent view if any, otherwise <code>null</code>
     */
    void execute(View view, boolean doNotLock, View parentBox) {
        viewManager.attach(view, doNotLock, parentBox);
    }

    /**
     * For internal use only. (Close a "view").
     *
     * @param view View to close
     */
    void close(View view) {
        viewManager.detach(view);
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
        viewManager.loggedin(this);
        applicationLayout.toggleMenu();
        Component menuSearcher = applicationLayout.getMenuSearcher();
        if(menuSearcher instanceof Focusable) {
            ((Focusable<?>) menuSearcher).focus();
        }
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

    private static class ViewManager {

        private final ApplicationMenu menu;
        private final ApplicationView applicationView;
        private List<View> stack = new ArrayList<>();
        private Map<View, ApplicationMenuItem> contentMenu = new HashMap<>();
        private Map<View, View> parents = new HashMap<>();
        private View homeView;

        public ViewManager(ApplicationView applicationView) {
            this.applicationView = applicationView;
            this.menu = applicationView.layout.getMenu();
        }

        public void loggedin(Application application) {
            applicationView.layout.loggedin(application);
            applicationView.layout.drawMenu(application);
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
            if(select(view)) {
                return;
            }
            if(view instanceof HomeView && homeView != null && !(view.getComponent() instanceof Dialog)) {
                homeView.abort();
            }
            Component c = view.getComponent();
            boolean window = c instanceof Dialog;
            if(window && parent == null && stack.size() > 0) {
                parent = stack.get(stack.size() - 1);
                if(parent.getComponent() instanceof Dialog) {
                    parent = null;
                }
            }
            if(parent != null) {
                parents.put(view, parent);
                if(!doNotLock) {
                    ApplicationMenuItem m = contentMenu.get(parent);
                    if(m != null) {
                        m.getElement().setEnabled(false);
                    }
                }
            }
            if(!window) {
                hideAllContent(null);
            }
            stack.add(view);
            applicationView.layout.addView(view);
            view.setVisible(true);
            ApplicationMenuItem m = view.getMenuItem(() -> select(view));
            if(view instanceof HomeView) {
                if(!(view.getComponent() instanceof Dialog)) {
                    homeView = view;
                }
            } else {
                menu.insert(0, m);
            }
            contentMenu.put(view, m);
            hilite(m);
        }

        public void detach(View view) {
            View child = child(view);
            if(child != null) {
                child.detachParentOnClose();
                select(child);
                child.abort();
                return;
            }
            ApplicationMenuItem m = contentMenu.get(view);
            if(m == null) {
                return;
            }
            if(!(view instanceof HomeView)) {
                menu.remove(m);
            }
            if(view == homeView && !(view.getComponent() instanceof Dialog)) {
                homeView = null;
            }
            contentMenu.remove(view);
            Element element = view.getComponent().getElement();
            element.removeFromParent();
            stack.remove(view);
            View parent = parents.remove(view);
            if(parent != null) {
                m = contentMenu.get(parent);
                if(m != null) {
                    m.getElement().setEnabled(true);
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
                if(!stack.isEmpty()) {
                    select(stack.get(stack.size() - 1));
                }
            }
        }

        public View getActiveView() {
            return stack.size() > 0 ? stack.get(stack.size() - 1) : null;
        }

        private View child(View view) {
            Optional<View> c = parents.keySet().stream().filter(k -> parents.get(k) == view).findAny();
            return c.orElse(null);
        }

        private void hideAllContent(View except) {
            stack.forEach(db -> db.setVisible(db == except));
        }

        private boolean select(View view) {
            ApplicationMenuItem m = contentMenu.get(view);
            if(m == null) {
                return false;
            }
            if(!m.getElement().isEnabled()) {
                return true;
            }
            hideAllContent(view);
            hilite(m);
            stack.remove(view);
            stack.add(view);
            return true;
        }

        private void hilite(ApplicationMenuItem menuItem) {
            menuItem.getElement().setEnabled(true);
            contentMenu.values().forEach(mi -> {
                if(mi == menuItem) {
                    mi.hilite();
                } else {
                    mi.dehilite();
                }
            });
        }
    }
}
