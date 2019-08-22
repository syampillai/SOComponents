package com.storedobject.vaadin;

/**
 * An interface that will be invoked when the "data form" construction is completed in {@link AbstractDataForm}.
 *
 * @see AbstractDataForm#setFormConstructedAction(FormConstructed)
 * @author Syam
 */
@FunctionalInterface
public interface FormConstructed {
    void formConstructed();
}
