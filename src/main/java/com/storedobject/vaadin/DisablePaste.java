package com.storedobject.vaadin;

import com.vaadin.flow.component.HasElement;

/**
 * Method implementation for disabling paste feature in fields.
 *
 * @author Syam
 */
@FunctionalInterface
public interface DisablePaste extends HasElement {

    /**
     * Disable paste feature so that Ctrl-V will not paste anything from the copy buffer.
     */
    default void disablePaste() {
        getElement().setAttribute("onpaste", "return false;");
    }
}
