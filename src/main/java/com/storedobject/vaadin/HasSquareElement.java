package com.storedobject.vaadin;

/**
 * Element that has a square share (equal width and height)
 * @author Syam
 */
public interface HasSquareElement extends HasElement {

    /**
     * Set the size (Width and height)
     * @param size Width/height of the square. Null value will reset the size to the default.
     */
    default void setSize(String size) {
        setStyle("width", size);
        setStyle("height", size);
    }

    /**
     * Get the current size
     * @return Current size
     */
    default String getSize() {
        return getStyle("width");
    }
}