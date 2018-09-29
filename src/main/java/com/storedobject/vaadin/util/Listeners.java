package com.storedobject.vaadin.util;

import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

public abstract class Listeners {

    private ArrayList<EventListener> listeners = new ArrayList<>();

    public void fire(EventObject event) {
        listeners.forEach(l -> fire(l, event));
    }

    protected abstract void fire(EventListener listener, EventObject event);

    public Registration add(EventListener listener) {
        listeners.add(listener);
        return new ListenerHolder(listener);
    }

    public void remove(EventListener listener) {
        listeners.remove(listener);
    }

    private class ListenerHolder implements Registration {

        private EventListener listener;

        private ListenerHolder(EventListener listener) {
            this.listener = listener;
        }

        @Override
        public void remove() {
            listeners.remove(listener);
        }
    }
}