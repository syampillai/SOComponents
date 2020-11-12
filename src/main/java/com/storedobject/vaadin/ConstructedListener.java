package com.storedobject.vaadin;

/**
 * A listener that can be added to classes such as {@link AbstractDataForm}, {@link HasColumns} etc. The
 * {@link #constructed(Object)} method will be invoked when the UI part of the class instance is constructed.
 *
 * @see AbstractDataForm#addConstructedListener(ConstructedListener)
 * @see HasColumns#addConstructedListener(ConstructedListener)
 * @author Syam
 */
@FunctionalInterface
public interface ConstructedListener {

    /**
     * This method will be invoked when the UI part of the class instance is constructed.
     *
     * @param object The instance to which this listener was added.
     */
    void constructed(Object object);
}
