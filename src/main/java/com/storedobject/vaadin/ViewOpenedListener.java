package com.storedobject.vaadin;

/**
 * This listener can be added to {@link View} so that {@link ViewOpenedListener#viewOpened(View)} will be invoked
 * whenever the view is executed.
 *
 * @author Syam
 */
@FunctionalInterface
public interface ViewOpenedListener {

    /**
     * Invoked just before executing the view.
     *
     * @param view View that is being executed.
     */
    default void viewOpening(View view) {
    }

    /**
     * Invoked when the view is executed.
     *
     * @param view View that is being executed.
     */
    void viewOpened(View view);
}
