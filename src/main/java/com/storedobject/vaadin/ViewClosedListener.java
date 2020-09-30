package com.storedobject.vaadin;

/**
 * This listener can be added to {@link View} so that {@link ViewClosedListener#viewClosed(View)} will be invoked
 * whenever the view is closed/aborted.
 *
 * @author Syam
 */
@FunctionalInterface
public interface ViewClosedListener {

    /**
     * Invoked when view is closed.
     *
     * @param view The view that was closed.
     */
    void viewClosed(View view);
}
