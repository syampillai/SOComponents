package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;

public abstract class Application implements RequestHandler {

    private ApplicationEnvironment environment;
    private ViewManager viewManager;
    private ApplicationUI ui;
    private ArrayList<WeakReference<Closeable>> resources = new ArrayList<>();

    protected boolean init(@SuppressWarnings("unused") String link) {
        return true;
    }

    protected abstract ApplicationLayout createLayout();

    protected ApplicationEnvironment createEnvironment() {
        return null;
    }

    public final ApplicationEnvironment getEnvironment() {
        if(environment == null) {
            environment = createEnvironment();
        }
        if(environment == null) {
            environment = new ApplicationEnvironment() {};
        }
        return environment;
    }

    @Override
    public boolean handleRequest(VaadinSession vaadinSession, VaadinRequest vaadinRequest, VaadinResponse vaadinResponse) {
        return false;
    }

    public void close() {
        if(ui != null && !ui.isClosing()) {
            ui.close();
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
        VaadinSession vs = VaadinSession.getCurrent();
        if(vs != null) {
            vs.close();
        }
    }

    public static Application get() {
        return get(VaadinSession.getCurrent());
    }

    static Application get(VaadinSession vs) {
        return vs == null ? null : (Application)vs.getAttribute("_SOApp_");
    }

    static Application create() {
        VaadinSession vs = VaadinSession.getCurrent();
        Application a = get(vs);
        if(a == null) {
            try {
                SOServlet sos = (SOServlet) VaadinServlet.getCurrent();
                a = sos.createApplication();
                if(a != null) {
                    a.ui = (ApplicationUI) UI.getCurrent();
                    if(a.ui.getError() == null) {
                        if(!a.init(a.ui.getLink())) {
                            return null;
                        }
                        vs.addRequestHandler(a);
                    }
                    vs.setAttribute("_SOApp_", a);
                }
            } catch(Throwable ignored) {
            }
        }
        return a;
    }

    public static void warning(Object message) {
        notification(message, 1);
    }

    public static void tray(Object message) {
        notification(message, 1, Notification.Position.BOTTOM_END, false);
    }

    public static void message(Object message) {
        if(message instanceof Throwable) {
            error(message);
        } else {
            notification(message, 0);
        }
    }

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
                    logError((Throwable)message);
                } else {
                    a.log((Throwable) message);
                }
            } else {
                if(a == null) {
                    m = message.toString();
                    logMessage(m);
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
                messageType = 150000;
                break;
            case 2: // Error
                messageType = Integer.MAX_VALUE;
                break;
            default:
                messageType = 50000;
                break;
        }
        n.setDuration(messageType);
        n.open();
    }

    public static void logError(Throwable error) {
        error.printStackTrace();
    }

    public static void logMessage(String message) {
        System.err.println(message);
    }

    public void log(Throwable error) {
        logError(error);
    }

    public void log(String message) {
        logMessage(message);
    }

    public String getLinkName() {
        return ((ApplicationUI)UI.getCurrent()).getLink();
    }

    public int getDeviceHeight() {
        return VaadinSession.getCurrent().getBrowser().getScreenHeight();
    }

    public int getDeviceWidth() {
        return VaadinSession.getCurrent().getBrowser().getScreenWidth();
    }

    public void showNotification(String text) {
        showNotification(null, text);
    }

    public void showNotification(String caption, String text) {
        notification(caption, text, 2, Notification.Position.BOTTOM_END,false);
    }

    public void showNotification(Throwable error) {
        showNotification(null, error);
    }

    public void showNotification(String caption, Throwable error) {
        notification(caption, "<BR/>Error: " + getEnvironment().toDisplay(error),
                2, Notification.Position.BOTTOM_END,false);
    }

    final void setMainView(ApplicationView applicationView) {
        if(this.viewManager == null) {
            viewManager = new ViewManager(applicationView);
            login();
        }
    }

    protected void login() {
        loggedin();
    }

    public String getIPAddress() {
        return VaadinSession.getCurrent().getBrowser().getAddress();
    }

    public String getIdentifier() {
        return VaadinSession.getCurrent().getBrowser().getBrowserApplication();
    }

    public int getMajorVersion() {
        return VaadinSession.getCurrent().getBrowser().getBrowserMajorVersion();
    }

    public int getMinorVersion() {
        return VaadinSession.getCurrent().getBrowser().getBrowserMinorVersion();
    }

    void execute(View view, boolean doNotLock, View parentBox) {
        viewManager.attach(view, doNotLock, parentBox);
    }

    void close(View view) {
        viewManager.detach(view);
    }

    protected final void loggedin() {
        viewManager.loggedin(this);
    }

    public void setPostFocus(Component component) {
        View db = viewManager.getActiveView();
        if(db != null) {
            db.setPostFocus(component);
        }
    }

    private static class ViewManager {

        private final ApplicationMenu menu;
        private final ApplicationView applicationView;
        private List<View> stack = new ArrayList<>();
        private Map<View, MenuItem> contentMenu = new HashMap<>();
        private Map<View, View> parents = new HashMap<>();

        public ViewManager(ApplicationView applicationView) {
            this.applicationView = applicationView;
            this.menu = applicationView.layout.getMenu();
        }

        public void loggedin(Application application) {
            applicationView.layout.loggedin(application);
            applicationView.layout.drawMenu(application);
        }

        public void attach(View view, boolean doNotLock, View parent) {
            if(select(view)) {
                return;
            }
            Component c = view.getComponent();
            boolean window = c instanceof Dialog;
            if(window && parent == null && stack.size() > 0) {
                parent = stack.get(stack.size() - 1);
            }
            if(parent != null) {
                parents.put(view, parent);
                if(!doNotLock) {
                    MenuItem m = contentMenu.get(parent);
                    if(m != null) {
                        m.getComponent().getElement().setEnabled(false);
                    }
                }
            }
            if(!window) {
                hideAllContent(null);
            }
            stack.add(view);
            applicationView.layout.addView(view);
            view.setVisible(true);
            MenuItem m = MenuItem.create(view.getCaption(), () -> select(view));
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
            MenuItem m = contentMenu.get(view);
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
            MenuItem m = contentMenu.get(view);
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

        private void hilite(MenuItem menuItem) {
            menuItem.getComponent().getElement().setEnabled(true);
            contentMenu.values().forEach(mi -> {
                if(mi == menuItem) {
                    mi.hilite();
                } else {
                    mi.getComponent().getElement().getStyle().set("background-color", "transparent");
                }
            });
        }
    }

    @Route("")
    @BodySize(height = "100vh", width = "100vw")
    @Theme(value = Lumo.class, variant = Lumo.LIGHT)
    public static class ApplicationView extends Composite<Component> {

        private ApplicationLayout layout;

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
                        String error = a.ui.getError();
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
}
