package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A unique id generator that can be used to set a unique id for components.
 *
 * @author Syam
 */
public class ID {

    private static final AtomicLong ID = new AtomicLong(1L);

    private ID() {
    }

    /**
     * Get a new id.
     *
     * @return New unique id.
     */
    public static long newID() {
        if(ID.get() == Long.MAX_VALUE) {
            ID.set(1);
        }
        return ID.getAndIncrement();
    }

    /**
     * Set a unique id for the given component if it doesn't have an id now.
     * (Id set will be of the format "soN" where N will be a unique long number).
     *
     * @param component Component for which id needs to be set.
     */
    public static void set(Component component) {
        if(!component.getId().isPresent()) {
            component.setId("so" + newID());
        }
    }
}