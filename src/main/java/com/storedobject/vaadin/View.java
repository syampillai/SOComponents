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
 *
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
    private Focusable<?> firstFocus;
    private Application application;
    private ApplicationMenuItem menuItem;

    /**
     * Create a View with no caption.
     */
    public View() {
        this(null);
    }

    /**
     * Create a view with a caption.
     *
     * @param caption Caption
     */
    public View(String caption) {
        this(null, caption);
    }

    /**
     * Create a view of the specific component with a caption
     *
     * @param component Component to display
     * @param caption Caption
     */
    public View(Component component, String caption) {
        getApplication();
        setCaption(caption);
        if(component != null) {
            setComponent(component);
        }
    }

    /**
     * Create a {@link CloseableView}. A helper method to create a {@link View} that is closeable.
     *
     * @param component Compoment of the {@link View}
     * @param caption Caption
     * @return A closable view.
     */
    public static View createCloseableView(Component component, String caption) {
        class V extends View implements CloseableView {
            private V(Component component, String caption) {
                super(component, caption);
            }
        }
        return new V(component, caption);
    }

    /**
     * Specifiy the scrollable attribute of the view. Scrollable views will scroll within the "content" area of the application.
     *
     * @param scrollable Scrollable
     */
    public void setScrollable(boolean scrollable) {
        new Scrollable(getContent(), scrollable);
    }

    /**
     * See if this view is scrollable or not.
     *
     * @return True or false.
     */
    public boolean isScrollable() {
        return Scrollable.isScrollable(getContent());
    }

    /**
     * Get the component that represents the "content" of the view. It may be different from the actual component displayed by the
     * application. For example, if a view may be displayed as a {@link Dialog} and in such cases, the "content" of the {@link Dialog} is
     * the "content" of the view.
     *
     * @return The "content" as a component.
     */
    public Component getContent() {
        return getContentInt();
    }

    private Component getContentInt() {
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
     *
     * {@link #setComponent(Component)} may be called from within this method.
     */
    protected void initUI() {
    }

    /**
     * Set the "content" of the view.
     *
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
     *
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
     *
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
                focus(postFocus, false);
                postFocus = null;
            } else {
                focus();
            }
        }
    }

    /**
     * For internal use only.
     *
     * @param component Component to set the focus to.
     */
    void setPostFocus(Component component) {
        this.postFocus = component;
    }

    /**
     * Set the first focusable component. (If this is not set, it will try to find out the first focusable component by traversing the
     * component tree).
     *
     * @param firstFocus Component to be focused
     */
    public void setFirstFocus(Focusable<?> firstFocus) {
        this.firstFocus = firstFocus;
    }

    /**
     * Check if this component to be skipped while traversing the component tree to find the first focusable component or not.
     *
     * @param skipFocus The focusable component
     * @return False if it should not be skipped (Default implementation returns false).
     */
    public boolean skipFirstFocus(@SuppressWarnings("unused") Focusable<?> skipFocus) {
        return false;
    }

    /**
     * Focus this view by finding the first focusable component. The "first focus" is set, then it will be focused.
     */
    public void focus() {
        if(firstFocus != null) {
            firstFocus.focus();
        }
        if(focus(getContent())) {
            return;
        }
        if(focusAny(getContent(), true)) {
            return;
        }
        if(focusAny(getContentInt(), true)) {
            return;
        }
        if(focusAny(getContent(), false)) {
            return;
        }
        focusAny(getContentInt(), false);
    }

    /**
     * Focus a component (or its focusable child component).
     *
     * @param component Component to focus
     * @return False if no focusable component exists (must be a field ({@link HasValue}), must be "visible" and should not be "read only").
     */
    public boolean focus(Component component) {
        return focus(component, true);
    }

    private boolean focus(Component component, boolean checkSkip) {
        if(component instanceof HasComponents) {
            return component.getChildren().anyMatch(c -> focus(c, checkSkip));
        } else {
            if(component instanceof HasValue && component instanceof Focusable &&
                    !((HasValue) component).isReadOnly() && component.isVisible()) {
                Focusable focusable = (Focusable) component;
                if(!focusable.isEnabled() || (checkSkip && skipFirstFocus(focusable))) {
                    return false;
                }
                focusable.focus();
                return true;
            }
        }
        return false;
    }

    /**
     * Focus a component (or its focusable child component).
     *
     * @param component Component to focus
     * @return False if no focusable component exists (must be "visible").
     */
    public boolean focusAny(Component component) {
        return focusAny(component, false);
    }

    private boolean focusAny(Component component, boolean checkSkip) {
        if(component instanceof HasComponents) {
            return component.getChildren().anyMatch(c -> focusAny(c, checkSkip));
        } else {
            if(component instanceof Focusable && component.isVisible()) {
                Focusable focusable = (Focusable) component;
                if(!focusable.isEnabled() || (checkSkip && skipFirstFocus(focusable))) {
                    return false;
                }
                focusable.focus();
                return true;
            }
        }
        return false;
    }

    /**
     * Get the menu item for this view. This is the menu item displayed by the {@link Application} when the view is activated.
     * This method is final but {@link #createMenuItem(Runnable)} can be overridden for customizing it.
     *
     * @param menuAction Action for the menu item to be created
     * @return Menu item.
     */
    public final ApplicationMenuItem getMenuItem(Runnable menuAction) {
        if(menuItem == null) {
            menuItem = ExecutableView.super.getMenuItem(menuAction);
            if(menuItem == null) {
                menuItem = getApplication().getEnvironment().createMenuItem(this, caption, menuAction);
            }
        }
        return menuItem;
    }

    /**
     * Set caption for this view. Caption is displayed as a {@link MenuItem} by the {@link Application} so that the view can be selected
     * (only one view is displayed at a time) by clicking it.
     *
     * @param caption Caption
     */
    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        if(menuItem != null) {
            menuItem.setLabel(caption);
        }
    }

    /**
     * Get the caption.
     *
     * @return Caption.
     */
    @Override
    public String getCaption() {
        return caption;
    }

    /**
     * See if this view is in a "window mode" (means its content is displayed as a {@link Dialog}).
     *
     * @return Window mode.
     */
    public boolean isWindowMode() {
        return component instanceof Dialog;
    }

    /**
     * Set the "window mode". Content of the view will be wrapped in a {@link Dialog} for "window mode".
     *
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
     *
     * @param create Whether to create or not.
     * @return Always return this view.
     */
    @Override
    public View getView(boolean create) {
        return this;
    }

    /**
     * Select this as the active view of the application.
     *
     * @return True if the view is set as the active view of the application.
     */
    public boolean select() {
        return getApplication().select(this);
    }

    /**
     * Execute this view.
     */
    @Override
    public final void execute() {
        execute(null, true);
    }

    /**
     * Execute this view by locking another view (the locked view will not be selectable until this view is closed).
     * The locked view acts as its "parent" and it will automatically get selected when this view closes.
     *
     * @param lock View to be locked.
     */
    @Override
    public final void execute(View lock) {
        execute(lock, false);
    }

    /**
     * Execute this view and set its parent too. (In this case, parent view is not locked). The paent view is automatically selected
     * when this view closes.
     *
     * @param parent Parent view to be set
     */
    @Override
    public final void invoke(View parent) {
        execute(parent, true);
    }

    /**
     * Override this if you want to do something before the View comes up on the screen.
     * Call super.execute(parent, doNotLock) to make the View appear on the screen.
     * Parent view is automatically selected when this view closes.
     *
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

    /**
     * Get the application that's tied to this View.
     *
     * @return The Application.
     */
    @SuppressWarnings("unchecked")
    public <A extends Application> A getApplication() {
        if(application == null) {
            application = Application.get();
        }
        return (A)application;
    }

    /**
     * Is if this view is currently being displayed.
     *
     * @return True or false.
     */
    public final boolean executing() {
        return parent() != null;
    }

    /**
     * See if this view was aborted.
     *
     * @see #abort()
     * @return True or false.
     */
    public final boolean aborted() {
        return aborted;
    }

    /**
     * Close this view.
     */
    @Override
    public void close() {
        closeInt();
    }

    /**
     * Abort this view. Default implementation sets an "abort flag" and closes the view.
     */
    @Override
    public void abort() {
        aborted = true;
        closeInt();
    }

    /**
     * Close resources if any that are left opened.
     * This method is invoked when the view is removed from the {@link Application}.
     * The default implementation does nothing.
     */
    public void clean() {
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
        clean();
    }
}