package com.storedobject.vaadin.util;

import com.vaadin.flow.dom.Element;

/**
 * This interface represents an internal element structure.
 * It provides a method to retrieve the internal element.
 *
 * @author Syam
 */
@FunctionalInterface
public interface InternalElement {
    /**
     * Retrieves the internal element.
     *
     * @return the internal element of type Element
     */
    Element getInternalElement();
}