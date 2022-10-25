package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.notification.Notification;

/**
 * An interface used by "displayable" classes such as {@link View}.
 *
 * @author Syam
 */
public interface ExecutableView extends Runnable, ClickHandler, ValueChangeHandler {

    /**
     * Show a warning message. Default implementation is to invoke {@link Application#warning(Object)}.
     *
     * @param message Warning
     */
    default void warning(Object message) {
        Application.notification(getApplication(), this, message, 1, null);
    }

    /**
     * Show a warning message n the "tray area" of the application. Default implementation is to invoke {@link Application#tray(Object)}.
     *
     * @param message Message to show in the tray.
     */
    default void tray(Object message) {
        Application.notification(getApplication(), this, message, 1, Notification.Position.BOTTOM_END);
    }

    /**
     * Show a message. Default implementation is to invoke {@link Application#message(Object)}.
     *
     * @param message Message
     */
    default void message(Object message) {
        Application.notification(getApplication(), this, message, 0, null);
    }

    /**
     * Show an error message. Default implementation is to invoke {@link Application#error(Object)}.
     * @param message Error
     */
    default void error(Object message) {
        Application.notification(getApplication(), this, message, 2, null);
    }

    /**
     * Clear all alters owned by this view.
     */
    default void clearAlerts() {
        Application.clearAlerts(this);
    }

    /**
     * Get the currently active View. It could be null.
     *
     * @return Currently active view.
     */
    default View getView() {
        return getView(false);
    }

    /**
     * Get the View. Create it if it doesn't exist.
     *
     * @param create Whether to create or not.
     * @return View.
     */
    View getView(boolean create);

    /**
     * Default implementation is to invoke {@link #execute()}.
     */
    default void run() {
        execute();
    }

    /**
     * Execute this by displaying it in a View.
     */
    default void execute() {
        execute(null);
    }

    /**
     * Execute the associated view by locking another view (the locked view will not be selectable until this view is closed).
     * The locked view acts as its "parent" and it will automatically get selected when this view closes.
     *
     * @param lock View to be locked.
     */
    default void execute(View lock) {
        View v = getView(true);
        if(v != null) {
            v.execute(lock);
        }
    }

    /**
     * Execute the associated view and set its parent too. (In this case, parent view is not locked). The parent view is automatically selected
     * when this view closes.
     *
     * @param parent Parent view to be set
     */
    default void invoke(View parent) {
        View v = getView(true);
        if(v != null) {
            v.invoke(parent);
        }
    }

    /**
     * Close this.
     */
    default void close() {
        View v = getView(false);
        if(v != null && v != this) {
            v.close();
        }
    }

    /**
     * Abort this.
     */
    default void abort() {
        View v = getView(false);
        if(v != null && v != this) {
            v.abort();
        }
    }

    /**
     * Clean this up by closing resources if any.
     */
    default void clean() {
        View v = getView(false);
        if(v != null && v != this) {
            v.clean();
        }
    }

    /**
     * Get the caption for this view.
     *
     * @return Caption.
     */
    default String getCaption() {
        View v = getView(false);
        if(v != null && v != this) {
            return v.getCaption();
        }
        return null;
    }

    /**
     * Set the caption
     *
     * @param caption Caption
     */
    default void setCaption(String caption) {
        View v = getView(false);
        if(v != null && v != this) {
            v.setCaption(caption);
        }
    }

    /**
     * Track value changes of a field {@link HasValue}. Whenever a value is changed, {@link #valueChanged(HasValue.ValueChangeEvent)}
     * method is invoked (and that in turn, will invoke {@link #valueChanged(ChangedValues)} because that is the default implementation).
     *
     * @param field Field to be tracked.
     */
    @SuppressWarnings("unchecked")
    default void trackValueChange(HasValue<?, ?> field) {
        field.addValueChangeListener(this);
    }

    /**
     * This method has the same effect of {@link ExecutableView#trackValueChange(HasValue)}. If a field is made "clickable",
     * its value will be tracked and {@link ExecutableView#valueChanged(ChangedValues)} whenever its value is changed.
     *
     * @see ExecutableView#trackValueChange(HasValue)
     * @param field Field to be tracked.
     */
    default void setClickable(HasValue<?, ?> field) {
        trackValueChange(field);
    }

    /**
     * Get the current Application.
     *
     * @param <A> Application type
     * @return Current Application.
     */
    @SuppressWarnings("unchecked")
    default <A extends Application> A getApplication() {
        return (A)Application.get();
    }

    /**
     * This method is invoked whenever this view is automatically selected because its parent was closed.
     *
     * @param parent Parent view that was closed
     */
    default void returnedFrom(View parent) {
    }

    /**
     * This default implementation invokes the {@link ExecutableView#clicked(Component)} with the changed field
     * as the parameter. However, nothing happens if the field is not a component.
     *
     * @param changedValues Change information (field and it value changes) wrapped into {@link ChangedValues}.
     */
    @Override
    default void valueChanged(ChangedValues changedValues) {
        HasValue<?, ?> field = changedValues.getChanged();
        if(field instanceof Component) {
            clicked((Component)field);
        }
    }

    /**
     * Default implementation, does nothing.
     *
     * @param c Component
     */
    @Override
    default void clicked(Component c) {
    }

    /**
     * Log something. Default implementation invokes {@link Application#log(Object)}.
     *
     * @param anything Message to log, it could be a {@link Throwable}
     */
    default void log(Object anything) {
        Application application = getApplication();
        if(application != null) {
            application.log(anything);
        } else {
            System.err.println(anything);
        }
    }

    /**
     * Log something along with an exception. Default implementation invokes {@link Application#log(Object, Throwable)}.
     *
     * @param anything Message to log, it could be a {@link Throwable}
     * @param error Error to be printed
     */
    default void log(Object anything, Throwable error) {
        Application application = getApplication();
        if(application != null) {
            application.log(anything, error);
        } else {
            System.err.println(anything);
            error.printStackTrace();
        }
    }

    /**
     * Get the menu item for this view. This is the menu item displayed by the {@link Application} when the view is activated.
     * By default, this invokes {@link #createMenuItem(Runnable)} to create the menu item.
     *
     * @param menuAction Action for the menu item to be created
     * @return Menu item.
     */
    default ApplicationMenuItem getMenuItem(Runnable menuAction) {
        ApplicationMenuItem m = createMenuItem(menuAction);
        if(m == null) {
            m = getApplication().getEnvironment().createMenuItem(this, getCaption(), menuAction);
        }
        return m;
    }

    /**
     * Create the menu item for this view. This will be invoked by {@link #getMenuItem(Runnable)}.
     * By default, this invokes {@link ApplicationEnvironment#createMenuItem(ExecutableView, String, Runnable)} to
     * create the menu item.
     *
     * @param menuAction Action for the menu item to be created
     * @return Menu item.
     */
    default ApplicationMenuItem createMenuItem(Runnable menuAction) {
        return getApplication().getEnvironment().createMenuItem(this, getCaption(), menuAction);
    }

    /**
     * Get the name of the icon to be displayed in the menu when this view is active (running).
     *
     * @return Default implementation takes this value from {@link ApplicationEnvironment#getActiveMenuIconName()}.
     */
    default String getMenuIconName() {
        return getApplication().getEnvironment().getActiveMenuIconName();
    }

    /**
     * Check if this view is closeable or not. If closeable, a "closeable" menu item will be created by
     * {@link #getMenuItem(Runnable)}.
     * This is checked only once when the "menu item" is created.
     *
     * @return True if closeable. By default, a view is closeable if it implements {@link CloseableView} or its
     * internal component ({@link View#getComponent()}) implements {@link CloseableView}.
     */
    default boolean isCloseable() {
        if(this instanceof CloseableView) {
            return true;
        }
        if(this instanceof View v) {
            return v.getComponent() instanceof CloseableView;
        }
        return false;
    }

    /**
     * Check if this view is a "Home View" or not. A "Home View" will not show any menu when it gets executed.
     * Typically, views that show background images or some sort of dashboards can be defined as "Home Views" so
     * that they will always appear on the content portion of the {@link com.storedobject.vaadin.Application}.
     *
     * @return True if this view is a "Home View". By default, if a view implements {@link HomeView} or its
     * internal component ({@link View#getComponent()}) implements {@link HomeView}, this method
     * return <code>true</code>.
     */
    default boolean isHomeView() {
        if(this instanceof HomeView) {
            return true;
        }
        if(this instanceof View v) {
            return v.getComponent() instanceof HomeView;
        }
        return false;
    }

    /**
     * Check if this view is a full-screen view or not. A full-screen view takes up the whole screen.
     *
     * @return True if closeable. By default, a view takes up full-screen if it implements {@link FullScreen} or its
     * internal component ({@link View#getComponent()}) implements {@link FullScreen}.
     */
    default boolean isFullScreen() {
        if(this instanceof FullScreen) {
            return true;
        }
        if(this instanceof View v) {
            return v.getComponent() instanceof FullScreen;
        }
        return false;
    }

    /**
     * Speak out the given sentence if the {@link ApplicationLayout} is supporting it (The default layout
     * {@link ApplicationFrame} supports it). Please note that the speaker
     * must have been switched on before calling this (See {@link SpeakerButton}).
     *
     * @param sentence Sentence to speak out.
     */
    default void speak(String sentence) {
        Application a = getApplication();
        if(a != null) {
            a.speak(sentence);
        }
    }
}
