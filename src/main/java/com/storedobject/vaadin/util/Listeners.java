package com.storedobject.vaadin.util;

import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

/**
 * Abstract base class for managing and notifying a collection of listeners.
 * Subclasses should provide an implementation for the {@code fire} method to define
 * how events are dispatched to individual listeners.
 *
 * This class is designed to manage multiple listeners that can be added or removed
 * dynamically and supports the firing of events to all registered listeners.
 *
 * @author Syam
 */
public abstract class Listeners {

    private final ArrayList<EventListener> listeners = new ArrayList<>();

    /**
     * Notifies all registered listeners of the provided event by invoking the
     * {@code fire} method for each listener.
     *
     * @param event the event to be dispatched to each registered listener
     */
    public void fire(EventObject event) {
        listeners.forEach(l -> fire(l, event));
    }

    /**
     * Dispatches an event to a specified listener. This method defines how a particular
     * event is delivered to an individual listener.
     *
     * Subclasses must implement this method to define the specific behavior of event delivery.
     *
     * @param listener the event listener to which the event is dispatched
     * @param event the event object to be delivered to the listener
     */
    protected abstract void fire(EventListener listener, EventObject event);

    /**
     * Adds a listener to the internal collection of event listeners.
     *
     * @param listener the event listener to be added to the collection
     * @return a {@code Registration} object that can be used to remove the listener
     */
    public Registration add(EventListener listener) {
        listeners.add(listener);
        return new ListenerHolder(listener);
    }

    /**
     * Removes the specified event listener from the collection of registered listeners.
     * If the listener is not currently registered, no action is taken.
     *
     * @param listener the event listener to be removed from the collection
     */
    public void remove(EventListener listener) {
        listeners.remove(listener);
    }

    private class ListenerHolder implements Registration {

        private final EventListener listener;

        private ListenerHolder(EventListener listener) {
            this.listener = listener;
        }

        @Override
        public void remove() {
            listeners.remove(listener);
        }
    }
}