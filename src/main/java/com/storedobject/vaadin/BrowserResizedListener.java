package com.storedobject.vaadin;

/**
 * An interface to monitor resizing of the browser page of the {@link Application}.
 * See {@link Application#addBrowserResizedListener(BrowserResizedListener)}.
 *
 * @author Syam
 */
@FunctionalInterface
public interface BrowserResizedListener {
    void browserResized(int width, int height);
}
