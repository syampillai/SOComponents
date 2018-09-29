package com.storedobject.vaadin;

public interface ValueRequired {

    default void setRequired(@SuppressWarnings("unused") boolean required) {
    }

    default boolean isRequired() {
        return true;
    }
}
