package com.storedobject.vaadin;

/**
 * Interface for "Value Required" feature of a field ({@link com.vaadin.flow.component.HasValue}).
 * @author Syam
 */
public interface ValueRequired {

    /**
     * Set value required flag.
     * @param required Required or not
     */
    default void setRequired(@SuppressWarnings("unused") boolean required) {
    }

    /**
     * Get the value required flag.
     * @return Whether required or not.
     */
    default boolean isRequired() {
        return true;
    }
}
