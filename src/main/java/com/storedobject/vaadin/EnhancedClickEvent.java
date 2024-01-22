package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * Enhanced version of Vaadin's {@link com.vaadin.flow.component.ClickEvent} class. This class has additionally 2 attributes -
 * componentX and componentY to provide (X, Y) position of the point on the component being clicked (relative to component's
 * top-left corner). This event class instance is available when you use {@link com.storedobject.vaadin.Clickable}.
 *
 * @param <C> Component type.
 * @author Syam
 */
public class EnhancedClickEvent<C extends Component> extends com.vaadin.flow.component.ClickEvent<C> {

    private final int componentX, componentY;

    /**
     * Constructor.
     *
     * @param source Source of the component.
     * @param fromClient Happened from the client side or not.
     * @param screenX X coordinate of the screen.
     * @param screenY Y coordinate of the screen.
     * @param clientX X coordinate of the client.
     * @param clientY Y coordinate of the client.
     * @param componentX X coordinate of the component.
     * @param componentY Y coordinate of the component.
     * @param clickCount Number of clicks.
     * @param button The button that was clicked.
     * @param ctrlKey Whether "Ctrl" key was pressed or not.
     * @param shiftKey Whether "Shift" key was pressed or not.
     * @param altKey Whether "Alt" key was pressed or not.
     * @param metaKey Whether "Meta" key was pressed or not.
     */
    public EnhancedClickEvent(Component source, boolean fromClient, int screenX, int screenY, int clientX, int clientY, int componentX, int componentY,
                              int clickCount, int button, boolean ctrlKey, boolean shiftKey, boolean altKey, boolean metaKey) {
        super(source, fromClient, screenX, screenY, clientX, clientY, clickCount, button, ctrlKey, shiftKey, altKey, metaKey);
        this.componentX = componentX;
        this.componentY = componentY;
    }

    /**
     * Get the X coordinate of the component.
     *
     * @return X coordinate.
     */
    public int getComponentX() {
        return componentX;
    }

    /**
     * Get the Y coordinate of the component.
     *
     * @return Y coordinate.
     */
    public int getComponentY() {
        return componentY;
    }
}