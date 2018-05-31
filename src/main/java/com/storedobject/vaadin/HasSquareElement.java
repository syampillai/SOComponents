package com.storedobject.vaadin;

import com.vaadin.flow.component.HasSize;

/**
 * Element that has a square shape (equal width and height)
 * @author Syam
 */
public interface HasSquareElement extends HasElement, HasSize {

    /**
     * Set the size (Width and height)
     * @param size Width/height of the square. Null value will reset the size to the default.
     */
    default void setSize(String size) {
        setWidth(size);
        setHeight(size);
    }

    /**
     * Get the current size
     * @return Current size
     */
    default String getSize() {
        return getStyle("width");
    }
}