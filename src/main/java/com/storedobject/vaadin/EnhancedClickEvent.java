package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * Enhanced version of Vaadin's {@link com.vaadin.flow.component.ClickEvent} class. This class has additionally 2 attributes -
 * componentX and componentY to provide (X, Y) position of the point on the component being clicked (relative to component's
 * top-left corner). This event class instance is available when you use {@link com.storedobject.vaadin.Clickable}.
 *
 * @author Syam
 */
public class EnhancedClickEvent extends com.vaadin.flow.component.ClickEvent {

    private final int componentX, componentY;

    public EnhancedClickEvent(Component source, boolean fromClient, int screenX, int screenY, int clientX, int clientY, int componentX, int componentY,
                              int clickCount, int button, boolean ctrlKey, boolean shiftKey, boolean altKey, boolean metaKey) {
        super(source, fromClient, screenX, screenY, clientX, clientY, clickCount, button, ctrlKey, shiftKey, altKey, metaKey);
        this.componentX = componentX;
        this.componentY = componentY;
    }

    public int getComponentX() {
        return componentX;
    }

    public int getComponentY() {
        return componentY;
    }
}