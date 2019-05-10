package com.storedobject.vaadin;

/**
 * An interface that generate HTML text.
 *
 * @author Syam
 */
@FunctionalInterface
public interface HTMLGenerator {

    /**
     * Get the HTML text generated. (The generated HTML may not contain outer tags. So, span or div tag may be used to
     * bracket it in order to make it a valid HTML text).
     *
     * @return HTML text generated.
     */
    String getHTML();

    /**
     * Get a printable object that can be used for creating a printable representation of the HTML text.
     *
     * @return The default implementation returns the same HTML text getHTML() returns.
     */
    default Object getPrintText() {
        return getHTML();
    }
}
