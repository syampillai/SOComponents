package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

public interface ExecutableView extends Runnable, ClickHandler, ValueChangeHandler {

    default void warning(Object message) {
        Application.warning(message);
    }

    default void tray(Object message) {
        Application.tray(message);
    }

    default void message(Object message) {
        Application.message(message);
    }

    default void error(Object message) {
        Application.error(message);
    }

    /**
     * Get the currently active View. It could be null.
     * @return Currently active view.
     */
    default View getView() {
        return getView(false);
    }

    /**
     * Get the View. Create it if it doesn't exist.
     * @param create Whether to create or not.
     * @return View.
     */
    View getView(boolean create);

    default void run() {
        execute();
    }

    /**
     * Execute this by displaying it in a View.
     */
    default void execute() {
        getView(true).execute();
    }

    void close();

    void abort();

    void setCaption(String caption);

    @SuppressWarnings("unchecked")
    default void trackValueChange(HasValue<?, ?> field) {
        field.addValueChangeListener(this);
    }
}
