package com.storedobject.vaadin;

/**
 * An interface to monitor resizing of the browser page of the {@link Application} or its content.
 * See {@link Application#addBrowserResizedListener(ResizedListener)}.
 * See {@link Application#addContentResizedListener(ResizedListener)}
 *
 * @author Syam
 */
@FunctionalInterface
public interface ResizedListener {

    /**
     * Invoked whenever there is a change in the size.
     *
     * @param width Width
     * @param height Height
     */
    void resized(int width, int height);
}
