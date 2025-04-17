package com.storedobject.vaadin.util;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Specialized implementation of the {@link Listeners} class that manages and notifies
 * event listeners specifically for handling {@link ComponentEvent}s.
 *
 * This class dispatches events to listeners that implement the {@link ComponentEventListener} interface.
 * When an event of type {@link ComponentEvent} is fired, it invokes the
 * {@link ComponentEventListener#onComponentEvent(ComponentEvent)} method on each registered listener.
 *
 * @author Syam
 */
public class ClickListeners extends Listeners {

    @Override
    protected void fire(EventListener listener, EventObject event) {
        //noinspection unchecked,rawtypes
        ((ComponentEventListener) listener).onComponentEvent((ComponentEvent<?>)event);
    }
}
