package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.RequestHandler;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.io.IOException;
import java.util.*;

@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public abstract class Application extends UI implements RequestHandler {

    private String link;
    private ViewManager viewManager;
    private boolean error;

    @Override
    protected void init(VaadinRequest request) {
        addDetachListener(e -> close());
        VaadinSession vs = getSession();
        vs.addRequestHandler(this);
        String link = request.getContextPath();
        if (link != null && link.length() > 1 && link.startsWith("/")) {
            link = link.substring(1);
        } else {
            link = null;
        }
        this.link = link;
        link = request.getPathInfo();
        if(link != null && !link.equals("/")) {
            showNotification("Unknown application '" + (this.link == null ? "" : this.link) + link +
                    "'.\nPlease use the correct link.");
            error = true;
            unableToCreate();
        }
    }

    public final boolean isError() {
        return error;
    }

    protected void unableToCreate() {
        error = true;
    }

    protected abstract ApplicationLayout createLayout();

    @Override
    public boolean handleRequest(VaadinSession vaadinSession, VaadinRequest vaadinRequest, VaadinResponse vaadinResponse) throws IOException {
        return false;
    }

    @Override
    public void close() {
        super.close();
        VaadinSession vs = getSession();
        if(vs != null) {
            vs.close();
        }
    }

    public static Application get() {
        return (Application)UI.getCurrent();
    }

    public String toText(Object message) {
        if(message == null) {
            return "";
        }
        if(message.getClass() == Exception.class) {
            return ((Exception)message).getMessage();
        }
        String m = message.toString();
        return m == null ? "" : m;
    }

    public static void warning(Object message) {
        notification(message, 1);
    }

    public static void tray(Object message) {
        notification(message, 1, Notification.Position.BOTTOM_END);
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

    private static void notification(String caption, Object message, int messageType) {
        notification(caption, message, messageType, null, true);
    }

    private static void notification(Object message, int messageType, Notification.Position position) {
        notification(message, messageType, position, false);
    }

    private static void notification(Object message, int messageType, boolean log) {
        notification(null, message, messageType, null, log);
    }

    private static void notification(String caption, Object message, int messageType, boolean log) {
        notification(caption, message, messageType, null, log);
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
                    m = a.toText(message);
                    a.log(m);
                }
            }
        }
        if(m == null) {
            m = a == null ? message.toString() : a.toText(message);
        }
        Alert n = new Alert(m);
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
        return link;
    }

    public int getDeviceHeight() {
        return getSession().getBrowser().getScreenHeight();
    }

    public int getDeviceWidth() {
        return getSession().getBrowser().getScreenWidth();
    }

    public void showNotification(String text) {
        showNotification(null, text);
    }

    public void showNotification(String caption, String text) {
        notification(caption, text, 2, false);
    }

    public void showNotification(Throwable error) {
        showNotification(null, error);
    }

    public void showNotification(String caption, Throwable error) {
        notification(caption, "<BR/>Error: " + toText(error), 2, false);
    }

    final void setMainView(ApplicationLayout applicationLayout) {
        if(this.viewManager == null) {
            viewManager = new ViewManager(applicationLayout);
            if(!error) {
                login();
            }
        }
    }

    protected void login() {
        loggedin();
    }

    public String getIPAddress() {
        return getSession().getBrowser().getAddress();
    }

    public String getIdentifier() {
        return getSession().getBrowser().getBrowserApplication();
    }

    public int getMajorVersion() {
        return getSession().getBrowser().getBrowserMajorVersion();
    }

    public int getMinorVersion() {
        return getSession().getBrowser().getBrowserMinorVersion();
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
        private final ApplicationLayout layout;
        private List<View> stack = new ArrayList<>();
        private Map<View, MenuItem> contentMenu = new HashMap<>();
        private Map<View, View> parents = new HashMap<>();

        public ViewManager(ApplicationLayout layout) {
            this.layout = layout;
            this.menu = layout.getMenu();
        }

        public void loggedin(Application application) {
            layout.drawMenu(application);
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
            layout.addView(view);
            view.setVisible(true);
            MenuItem m = MenuItem.create(view.getCaption(), () -> select(view));
            menu.insert(0, m);
            contentMenu.put(view, m);
            hilite(m);
        }

        public void detach(View view) {
            MenuItem m = contentMenu.get(view);
            if(m == null) {
                return;
            }
            View child;
            while (true) {
                child = child(view);
                if(child == null) {
                    break;
                }
                if(!child.executing()) {
                    child.detachParentOnClose();
                    select(child);
                    child.abort();
                    return;
                }

            }
            menu.remove(m);
            contentMenu.remove(view);
            view.getComponent().getElement().removeFromParent();
            stack.remove(view);
            View parent = parents.remove(view);
            if(parent != null) {
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
            Application.get().setMainView(layout);
        }

        @Override
        protected Component initContent() {
            if(layout == null) {
                layout = Application.get().createLayout();
            }
            return layout.getComponent();
        }
    }
}
