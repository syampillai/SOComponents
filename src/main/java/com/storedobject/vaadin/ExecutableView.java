package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

/**
 * An interface used by "dispylable" classes such as {@link View}.
 * @author Syam
 */
public interface ExecutableView extends Runnable, ClickHandler, ValueChangeHandler {

    /**
     * Show a warning message. Default implementation is to invoke {@link Application#warning(Object)}.
     *
     * @param message Warning
     */
    default void warning(Object message) {
        Application.warning(message);
    }

    /**
     * Show a warning message n the "tray area" of the application. Default implementation is to invoke {@link Application#tray(Object)}.
     *
     * @param message Message to show in the tray.
     */
    default void tray(Object message) {
        Application.tray(message);
    }

    /**
     * Show a message. Default implementation is to invoke {@link Application#message(Object)}.
     *
     * @param message Message
     */
    default void message(Object message) {
        Application.message(message);
    }

    /**
     * Show an error message. Default implementation is to invoke {@link Application#error(Object)}.
     * @param message Error
     */
    default void error(Object message) {
        Application.error(message);
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
     * Default implmenentation is to invoke {@link #execute()}.
     */
    default void run() {
        execute();
    }

    /**
     * Execute this by displaying it in a View.
     */
    default void execute() {
        getView(true).execute();
    }

    /**
     * Close this.
     */
    void close();

    /**
     * Abort this.
     */
    void abort();

    /**
     * Set the caption
     *
     * @param caption Caption
     */
    void setCaption(String caption);

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
}
