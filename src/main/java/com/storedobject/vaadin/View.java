package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.GeneratedVaadinDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class View implements ExecutableView {

    private final Application application;
    private Component component;
    private String caption;
    private boolean aborted = false;
    private boolean detachParent = false;
    private View parent;
    private Registration windowMonitor;
    private boolean internalWindowAction = false;
    private boolean doFocus = true;
    private Component postFocus;

    public View(Application a) {
        this(a, null);
    }

    public View(Application a, String caption) {
        this(a, null, caption);
    }

    public View(Application a, Component component, String caption) {
        this.application = a;
        setCaption(caption);
        if(component != null) {
            setComponent(component);
        }
    }

    public void setScrollable(boolean scrollable) {
        new Scrollable(getContent(), scrollable);
    }

    public boolean isScrollable() {
        return Scrollable.isScrollable(getContent());
    }

    public Component getContent() {
        Component c = getComponent();
        if(!(c instanceof Dialog)) {
            return c;
        }
        Optional<Component> oc = c.getChildren().findFirst();
        if(!oc.isPresent()) {
            return c;
        }
        if(c.getChildren().anyMatch(e -> e != oc.get())) {
            return c;
        }
        return oc.get();
    }


    protected void initUI() {
    }

    public Application getApplication() {
        return application;
    }

    public final void setComponent(Component component) {
        if(component == this.component || component == null) {
            return;
        }
        Element parent  = parent();
        if(parent != null) {
            this.component.getElement().removeFromParent();
            if(this.component instanceof Window) {
                ((Window) this.component).close();
            }
            component.getElement().removeFromParent();
            parent.appendChild(component.getElement());
        }
        this.component = component;
    }

    private Element parent() {
        return component == null ? null : component.getElement().getParent();
    }

    public final Component getComponent() {
        if(component == null) {
            initUI();
        }
        if(component == null) {
            throw new RuntimeException("Component not set in View: " + getCaption());
        }
        if(component instanceof Dialog) {
            Dialog d = (Dialog)component;
            if(windowMonitor == null) {
                windowMonitor = d.addOpenedChangeListener(new WindowMonitor(this));
            }
        }
        return component;
    }

    private static class WindowMonitor implements ComponentEventListener<Dialog.OpenedChangeEvent<Dialog>> {

        private final View view;

        private WindowMonitor(View view) {
            this.view = view;
        }

        @Override
        public void onComponentEvent(GeneratedVaadinDialog.OpenedChangeEvent openedChangeEvent) {
            if(view.internalWindowAction || openedChangeEvent.isOpened()) {
                view.internalWindowAction = false;
                return;
            }
            if(openedChangeEvent.isFromClient()) {
                ((Dialog)openedChangeEvent.getSource()).open();
                view.abort();
            } else {
                view.closeInt();
            }
        }
    }

    void setVisible(boolean visible) {
        if(component instanceof Dialog) {
            if(visible) {
                ((Dialog) component).open();
            } else {
                internalWindowAction = true;
                ((Dialog) component).close();
            }
        } else {
            component.setVisible(visible);
        }
        if(visible) {
            if(doFocus) {
                focus();
                doFocus = false;
            } else if(postFocus != null) {
                focus(postFocus);
                postFocus = null;
            } else {
                focus();
            }
        }
    }

    void setPostFocus(Component component) {
        this.postFocus = component;
    }

    public void focus() {
        if(!focus(getContent())) {
            focusAny(getContent());
        }
    }

    public static boolean focus(Component component) {
        if(component instanceof HasComponents) {
            return component.getChildren().anyMatch(View::focus);
        } else {
            if(component instanceof HasValue && component instanceof Focusable && !((HasValue) component).isReadOnly() && component.isVisible()) {
                ((Focusable) component).focus();
                return true;
            }
        }
        return false;
    }

    public static boolean focusAny(Component component) {
        if(component instanceof HasComponents) {
            return component.getChildren().anyMatch(View::focusAny);
        } else {
            if(component instanceof Focusable && component.isVisible()) {
                ((Focusable) component).focus();
                return true;
            }
        }
        return false;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public boolean isWindowMode() {
        return component instanceof Dialog;
    }

    public void setWindowMode(boolean windowOn) {
        getComponent();
        if(windowMonitor != null) {
            windowMonitor.remove();
            windowMonitor = null;
        }
        if(windowOn) {
            if(component instanceof Dialog) {
                return;
            }
            boolean ex = executing();
            Component c = component;
            Window w = new Window();
            setComponent(w);
            w.add(c);
            if(ex) {
                w.setVisible(true);
            }
            return;
        }
        if(!(component instanceof Dialog)) {
            return;
        }
        List<Component> list = new ArrayList<>();
        component.getChildren().forEach(list::add);
        Component nc;
        if(list.size() != 1) {
            nc = new Div();
            list.forEach(((Div) nc)::add);
        } else {
            nc = list.get(0);
        }
        ((Dialog)component).removeAll();
        setComponent(nc);
    }

    @Override
    public View getView(boolean create) {
        return this;
    }

    public final void execute() {
        execute(null, true);
    }

    public final void execute(View lock) {
        execute(lock, false);
    }

    public final void invoke(View lock) {
        execute(lock, true);
    }

    /**
     * Override this if you want to do something before the View comes up on the screen.
     * Call super.execute(parent, doNotLock) to make the View appear on the screen.
     * @param parent Parent view to lock
     * @param doNotLock True if parent should not be locked
     */
    protected void execute(View parent, boolean doNotLock) {
        if(getCaption() == null || getCaption().trim().isEmpty()) {
            throw new RuntimeException("Caption not set!");
        }
        if(this.parent != null && parent != null && this.parent != parent) {
            return;
        }
        this.parent = parent;
        aborted = false;
        getApplication().execute(this, doNotLock, parent);
    }

    public final boolean executing() {
        return parent() != null;
    }

    public final boolean aborted() {
        return aborted;
    }

    public void returnedFrom(@SuppressWarnings("unused") View parent) {
    }

    public void close() {
        closeInt();
    }

    public void abort() {
        aborted = true;
        closeInt();
    }

    void detachParentOnClose() {
        detachParent = true;
    }

    private void closeInt() {
        if(windowMonitor != null) {
            windowMonitor.remove();
            windowMonitor = null;
        }
        getApplication().close(this);
        if(detachParent && parent != null) {
            detachParent = false;
            View p = parent;
            parent = null;
            getApplication().close(p);
        } else {
            parent = null;
        }
        doFocus = true;
    }

    @Override
    public void clicked(Component c) {
    }

    @Override
    public void valueChanged(ChangedValues changedValues) {
    }
}