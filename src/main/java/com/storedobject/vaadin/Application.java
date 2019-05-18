package com.storedobject.vaadin;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Application is an extension to Vaadin's UI class for creating 'single page' web applications. The 'single page' should be defined
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
 *    {@literal @}WebServlet(urlPatterns = "/*", name = "DemoServlet", asyncSupported = true, loadOnStartup = 0)
 *    {@literal @}VaadinServletConfiguration(ui = Demo.class, productionMode = false, closeIdleSessions = true)
 *     public static class DemoServlet extends VaadinServlet {
 *     }
 *
 *    {@literal @}Route("")
 *    {@literal @}Push(PushMode.MANUAL)
 *    {@literal @}BodySize(height = "100vh", width = "100vw")
 *    {@literal @}Theme(value = Lumo.class, variant = Lumo.LIGHT)
 *     public static class DemoView extends ApplicationView {
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
 *         add(MenuItem.create(...));
 *         add(MenuItem.create(...));
 *         add(MenuItem.create(...));
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
 * @author Syam
 */
public abstract class Application extends UI {

    private ApplicationEnvironment environment;
    private Map<Object, Integer> pollIntervals = new HashMap<>();
    private ViewManager viewManager;
    private ArrayList<WeakReference<Closeable>> resources = new ArrayList<>();
    private String link;
    private int deviceWidth = -1, deviceHeight = -1;
    private final ArrayList<Command> commands = new ArrayList<>();
    private transient boolean closed = false;
    String error;

    /**
     * This method is invoked as documented in Vaadin Flow's documentation. If you want to override this method, make sure that <code>super</code>
     * is called.
     * @param request Vaadin Request
     */
    @Override
    protected void init(VaadinRequest request) {
        addDetachListener(e -> close());
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
            if(createLayout() == null) {
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
                            super.access(commands.remove(0));
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
     * This method is invoked when the applicationn comes up.
     * @param link The context path of the application.
     * @return True if application can go ahead. Otherwise, an "Initialization failed" message is displayed. Default return value is <code>true</code>.
     */
    protected boolean init(@SuppressWarnings("unused") String link) {
        return true;
    }

    /**
     * This method is invoked only once to determine the layout of the application.
     * @return Application layout.
     */
    protected abstract ApplicationLayout createLayout();

    /**
     * An "application environment" may be created to specifiy certain behaviours of the applicaion. If this method returns <code>null</code>, a default
     * "environment" will be created.
     * @return Returns null by default.
     */
    protected ApplicationEnvironment createEnvironment() {
        return null;
    }

    /**
     * Get the current "application environment".
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
     * Close the application by closing all registered "resources". If the associated session is not closed, it will also be closed.
     */
    @Override
    public synchronized void close() {
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
            vs.close();
        }
        super.close();
        synchronized (commands) {
            commands.notifyAll();
        }
    }

    /**
     * Register a "resource" that will be closed when the application is shutdown.
     * @param resource Resource to close.
     */
    public void registerResource(Closeable resource) {
        resources.add(new WeakReference<>(resource));
    }

    /**
     * Get the current application.
     * @return Current application.
     */
    public static Application get() {
        UI ui = UI.getCurrent();
        return ui instanceof Application ? (Application)ui : null;
    }

    /**
     * Show a warning message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     * @param message Message.
     */
    public static void warning(Object message) {
        notification(message, 1);
    }

    /**
     * Show a message on the tray from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     * @param message Message.
     */
    public static void tray(Object message) {
        notification(message, 1, Notification.Position.BOTTOM_END, false);
    }

    /**
     * Show a message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     * @param message Message.
     */

    public static void message(Object message) {
        if(message instanceof Throwable) {
            error(message);
        } else {
            notification(message, 0);
        }
    }

    /**
     * Show an error message from the parameter passed. The parameter will be converted to <code>String</code> by invoking the
     * method {@link com.storedobject.vaadin.ApplicationEnvironment#toDisplay(Object)}.
     * @param message Message.
     */

    public static void error(Object message) {
        notification(message, 2);
    }


    private static void notification(Object message, int messageType) {
        notification(message, messageType, null, true);
    }

    private static void notification(Object message, int messageType, Notification.Position position, boolean log) {
        notification(null, message, messageType, position, log);
    }

    private static void notification(String caption, Object message, int messageType, Notification.Position position, boolean log) {
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
                if(a == null) {
                    System.err.println(m);
                } else {
                    m = a.getEnvironment().toDisplay(message);
                    a.log(a.getEnvironment().toString(m));
                }
            }
        }
        if(m == null) {
            m = a == null ? message.toString() : a.getEnvironment().toDisplay(message);
        }
        Alert n = new Alert(caption, m);
        if(position != null) {
            n.setPosition(position);
        }
        switch (messageType) {
            case 1: // Warning
                messageType = 20000;
                break;
            case 2: // Error
                messageType = Integer.MAX_VALUE;
                break;
            default:
                messageType = 10000;
                break;
        }
        n.setDuration(messageType);
        n.open();
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
     * @return Context path
     */
    public String getLinkName() {
        return link;
    }

    @ClientCallable
    private void deviceSize(int width, int height) {
        this.deviceWidth = width;
        this.deviceHeight = height;
    }

    private void receiveSize() {
        getPage().executeJavaScript("document.body.$server.deviceSize(document.body.clientWidth, document.body.clientHeight);");
    }

    /**
     * Get the device (broswer) height.
     * @return Device height.
     */
    public int getDeviceHeight() {
        if(deviceHeight < 0) {
            receiveSize();
        }
        return deviceHeight;
    }

    /**
     * Get the device (broswer) width.
     * @return Device width.
     */
    public int getDeviceWidth() {
        if(deviceWidth < 0) {
            receiveSize();
        }
        return deviceWidth;
    }

    /**
     * Show a notification on the screen.
     * @param text Text of the notification
     */
    public void showNotification(String text) {
        showNotification(null, text);
    }

    /**
     * Show a notification on the screen.
     * @param caption Caption of the notification
     * @param text Text of the notification
     */
    public void showNotification(String caption, String text) {
        notification(caption, text, 2, Notification.Position.BOTTOM_END,false);
    }

    /**
     * Show error notification,
     * @param error Error
     */
    public void showNotification(Throwable error) {
        showNotification(null, error);
    }

    /**
     * Show error notification.
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
     * @param applicationView Application view.
     */
    final void setMainView(ApplicationView applicationView) {
        if(this.viewManager == null) {
            viewManager = new ViewManager(applicationView);
            receiveSize();
            login();
        }
    }

    /**
     * This method can be overriden to accept login credentials and {@link #loggedin()} must be called if successfully logged in.
     */
    protected void login() {
        loggedin();
    }

    /**
     * Get the IP address of the application.
     * @return IP address.
     */
    public String getIPAddress() {
        return VaadinSession.getCurrent().getBrowser().getAddress();
    }

    /**
     * Get an identifier of the application (can be used for logging etc.).
     * @return Ab identifier derived from the browser information.
     */
    public String getIdentifier() {
        return VaadinSession.getCurrent().getBrowser().getBrowserApplication();
    }

    /**
     * Get the major version number (from browser information).
     * @return Major version number.
     */
    public int getMajorVersion() {
        return VaadinSession.getCurrent().getBrowser().getBrowserMajorVersion();
    }

    /**
     * Get the minor version number (from browser information).
     * @return Minor version number.
     */
    public int getMinorVersion() {
        return VaadinSession.getCurrent().getBrowser().getBrowserMinorVersion();
    }

    /**
     * For internal use only. (Execute a "view").
     * @param view View to execute
     * @param doNotLock Whether to lock the parent or not
     * @param parentBox Parent view if any, otherwise <code>null</code>
     */
    void execute(View view, boolean doNotLock, View parentBox) {
        viewManager.attach(view, doNotLock, parentBox);
    }

    /**
     * For internal use only. (Close a "view").
     * @param view View to close
     */
    void close(View view) {
        viewManager.detach(view);
    }

    /**
     * For internal use only. (Select a view)
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
    }

    /**
     * Set a component to be focused later when the current "view" is selected again. This is useful in situations like bringing back to
     * the current "data entry" screen to a particular field.
     * @param component Component to be focused later.
     */
    public void setPostFocus(Component component) {
        View v = viewManager.getActiveView();
        if(v != null) {
            v.setPostFocus(component);
        }
    }

    @Override
    public void setPollInterval(int intervalInMillis) {
        setPollInterval(this, intervalInMillis);
    }

    /**
     * Set polling interval. Several owners may be requesting it and the value actually set will the lowest value. It is
     * the owner's responsibility to release polling by invoking stopPolling when polling is
     * no more required.
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
            if (intervalInMillis <= getPollInterval()) {
                super.setPollInterval(intervalInMillis);
            }
            return;
        }
        setPoll();
    }

    private void setPoll() {
        int pi = pollIntervals.values().stream().min(Comparator.comparing(Integer::valueOf)).orElse(-1);
        access(() -> super.setPollInterval(pi));
    }

    /**
     * Start polling by setting the interval to 1000 milliseconds.
     * @param owner Object that is invoking the request
     */
    public void startPolling(Object owner) {
        setPollInterval(owner, 1000);
    }

    /**
     * Stop polling. Polling will not be stopped if requests are there from other owners.
     * @param owner Owner who requested for polling earlier
     */
    public void stopPolling(Object owner) {
        setPollInterval(owner, -1);
    }

    /**
     * Get the view in which a particular component is currently appearing.
     * @param component Component to be checked
     * @return View in which the component is appearing or <code>null</code> if it's not displayed in any of the views.
     */
    public View getViewFor(Component component) {
        return viewManager == null ? null : viewManager.getViewFor(component);
    }

    private static class ViewManager {

        private final ApplicationMenu menu;
        private final ApplicationView applicationView;
        private List<View> stack = new ArrayList<>();
        private Map<View, ApplicationMenuItem> contentMenu = new HashMap<>();
        private Map<View, View> parents = new HashMap<>();

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
            menu.insert(0, m);
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
            if(m != null) {
                menu.remove(m);
            }
            contentMenu.remove(view);
            view.getComponent().getElement().removeFromParent();
            stack.remove(view);
            View parent = parents.remove(view);
            if(parent != null) {
                m = contentMenu.get(parent);
                if(m != null) {
                    m.setEnabled(true);
                }
                select(parent);
                parent.returnedFrom(view);
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
            if(!m.isEnabled()) {
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
                    mi.getElement().getStyle().set("background-color", "transparent");
                }
            });
        }
    }
}
