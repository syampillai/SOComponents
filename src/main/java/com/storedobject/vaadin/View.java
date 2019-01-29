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

/**
 * View represents an independent piece of information (typically a "data entry form" or some information dashboard) to be displayed
 * in the content area of the {@link Application}. View implements {@link Runnable} interface. So, it can be associated with a {@link MenuItem}.
 * When a {@link MenuItem} is clicked, the {@link Runnable#run()} method of the view is invoked and we say the "view is executed". When a
 * view is executed, its component (specified using the {@link View#setComponent(Component)}) is displayed in the "content" area of the application.
 * @author Syam
 */
public class View implements ExecutableView {

    private Component component;
    private String caption;
    private boolean aborted = false;
    private boolean detachParent = false;
    private View parent;
    private Registration windowMonitor;
    private boolean internalWindowAction = false;
    private boolean doFocus = true;
    private Component postFocus;

    /**
     * Create a View with no caption.
     */
    public View() {
        this(null);
    }

    /**
     * Create a view with a caption.
     * @param caption Caption
     */
    public View(String caption) {
        this(null, caption);
    }

    /**
     * Create a view of the specific component with a caption
     * @param component Component to display
     * @param caption Caption
     */
    public View(Component component, String caption) {
        setCaption(caption);
        if(component != null) {
            setComponent(component);
        }
    }

    /**
     * Specifiy the scrollable attribute of the view. Scrollable views will scroll within the "content" area of the application.
     * @param scrollable Scrollable
     */
    public void setScrollable(boolean scrollable) {
        new Scrollable(getContent(), scrollable);
    }

    /**
     * See if this view is scrollable or not.
     * @return True or false.
     */
    public boolean isScrollable() {
        return Scrollable.isScrollable(getContent());
    }

    /**
     * Get the component that represents the "content" of the view. It may be different from the actual component displayed by the
     * application. For example, if a view may be displayed as a {@link Dialog} and in such cases, the "content" of the {@link Dialog} is
     * the "content" of the view.
     * @return The "content" as a component.
     */
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

    /**
     * This method is invoked when the view wants to determine its "content" to be displayed and nothing exists at that moment.
     * {@link #setComponent(Component)} may be called from within this method.
     */
    protected void initUI() {
    }

    /**
     * Set the "content" of the view.
     * @param component Component to be displayed.
     */
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

    /**
     * Get the "content" component of the view.
     * @return Component
     */
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

    /**
     * For internal use only.
     * @param visible Visibility of the view
     */
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

    /**
     * For internal use only.
     * @param component Component to set the focus to.
     */
    void setPostFocus(Component component) {
        this.postFocus = component;
    }

    /**
     * Focus this view by finding the first focusable component.
     */
    public void focus() {
        if(!focus(getContent())) {
            focusAny(getContent());
        }
    }

    /**
     * Focus a component (or its focusable child component).
     * @param component Component to focus
     * @return False if no focusable component exists (must be a field ({@link HasValue}), must be "visible" and should not be "read only").
     */
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

    /**
     * Focus a component (or its focusable child component).
     * @param component Component to focus
     * @return False if no focusable component exists (must be "visible").
     */
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

    /**
     * Set caption for this view. Caption is displayed as a {@link MenuItem} by the {@link Application} so that the view can be selected
     * (only one view is displayed at a time) by clicking it.
     * @param caption Caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Get the caption.
     * @return Caption.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * See if this view is in a "window mode" (means its content is displayed as a {@link Dialog}).
     * @return Window mode.
     */
    public boolean isWindowMode() {
        return component instanceof Dialog;
    }

    /**
     * Set the "window mode". Content of the view will be wrapped in a {@link Dialog} for "window mode".
     * @param windowOn Window mode
     */
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

    /**
     * Implementation of {@link ExecutableView#getView(boolean)}.
     * @param create Whether to create or not.
     * @return Always return this view.
     */
    @Override
    public View getView(boolean create) {
        return this;
    }

    /**
     * Execute this view.
     */
    public final void execute() {
        execute(null, true);
    }

    /**
     * Execute this view by locking another view (the locked view will not be selectable until this view is closed).
     * The locked view acts as its "parent" and it will automatically get selected when this view closes.
     * @param lock View to be locked.
     */
    public final void execute(View lock) {
        execute(lock, false);
    }

    /**
     * Execute this view and set its parent too. (In this case, parent view is not locked). The paent view is automatically selected
     * when this view closes.
     * @param parent Parent view to be set
     */
    public final void invoke(View parent) {
        execute(parent, true);
    }

    /**
     * Override this if you want to do something before the View comes up on the screen.
     * Call super.execute(parent, doNotLock) to make the View appear on the screen.
     * Parent view is automatically selected when this view closes.
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
        getApp().execute(this, doNotLock, parent);
    }

    private Application getApp() {
        return Application.get();
    }

    /**
     * Is if this view is currently being displayed.
     * @return True or false.
     */
    public final boolean executing() {
        return parent() != null;
    }

    /**
     * See if this view was aborted.
     * @see #abort()
     * @return True or false.
     */
    public final boolean aborted() {
        return aborted;
    }

    /**
     * This method is invoked whenever a view is automatically selected becasue its parent was closed.
     * @param parent Parent view that was closed.
     */
    public void returnedFrom(@SuppressWarnings("unused") View parent) {
    }

    /**
     * Close this view.
     */
    public void close() {
        closeInt();
    }

    /**
     * Abort this view. Default implentation sets an "abort flag" and closes the view.
     */
    public void abort() {
        aborted = true;
        closeInt();
    }

    /**
     * For internal use only.
     */
    void detachParentOnClose() {
        detachParent = true;
    }

    private void closeInt() {
        if(windowMonitor != null) {
            windowMonitor.remove();
            windowMonitor = null;
        }
        getApp().close(this);
        if(detachParent && parent != null) {
            detachParent = false;
            View p = parent;
            parent = null;
            getApp().close(p);
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