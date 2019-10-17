package com.storedobject.vaadin;

/**
 * This listener can be added to {@link View} so that {@link ViewOpenedListener#viewOpened(View)} will be invoked
 * whenever the view is executed.
 *
 * @author Syam
 */
@FunctionalInterface
public interface ViewOpenedListener {
    void viewOpened(View view);
}
