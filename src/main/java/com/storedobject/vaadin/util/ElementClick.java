package com.storedobject.vaadin.util;

import com.storedobject.vaadin.EnhancedClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;

/**
 * A class that enables handling and notifying click events for a specified component.
 * The class adds an event listener to the component's associated {@code Element} to capture
 * click events with detailed event data, and notifies registered listeners when a click event occurs.
 * This provides an enhanced mechanism to manage and process click events on components.
 *
 * The class uses {@code EnhancedClickEvent} to encapsulate detailed click event information,
 * such as screen coordinates, client coordinates, component-relative coordinates, button state,
 * and additional keyboard modifier states (e.g., Ctrl, Shift, Alt, Meta).
 *
 * This class implements the {@code ClickNotifier} interface, allowing the addition and removal
 * of click listeners dynamically.
 *
 * @author Syam
 */
public class ElementClick implements ClickNotifier {

    private final Component component;
    private final Listeners listeners = new ClickListeners();

    /**
     * Constructor to initialize an ElementClick instance with a specified component.
     *
     * @param component the component to which the click event listener will be attached.
     */
    public ElementClick(Component component) {
        this(component, null);
    }

    /**
     * Constructs an instance of the ElementClick class. Sets up a click event listener for the specified element,
     * or resolves the element from the given component if the element is null. The event listener captures various
     * click data such as position, button, and modifier keys.
     *
     * @param component the component associated with the click event. If the element is null, the component's element
     *                  or internal element (if the component is an instance of InternalElement) is used.
     * @param element the specific element to which the click event listener is attached. If null, the element
     *                is resolved from the provided component.
     */
    public ElementClick(Component component, Element element) {
        this.component = component;
        if(element == null) {
            if(component instanceof InternalElement) {
                element = ((InternalElement)component).getInternalElement();
            } else {
                element = component.getElement();
            }
        }
        element.addEventListener("click", this::handleClick)
                .addEventData("event.screenX")
                .addEventData("event.screenY")
                .addEventData("event.clientX")
                .addEventData("event.clientY")
                .addEventData("event.offsetX")
                .addEventData("event.offsetY")
                .addEventData("event.detail")
                .addEventData("event.button")
                .addEventData("event.ctrlKey")
                .addEventData("event.shiftKey")
                .addEventData("event.altKey")
                .addEventData("event.metaKey");
    }

    private void handleClick(DomEvent event) {
        JsonObject e = event.getEventData();
        EnhancedClickEvent<?> ce = new EnhancedClickEvent<>(component, true, (int)e.getNumber("event.screenX"),
                (int)e.getNumber("event.screenY"), (int)e.getNumber("event.clientX"), (int)e.getNumber("event.clientY"),
                (int)e.getNumber("event.offsetX"), (int)e.getNumber("event.offsetY"),
                (int)e.getNumber("event.detail"), (int)e.getNumber("event.button"), e.getBoolean("event.ctrlKey"),
                e.getBoolean("event.shiftKey"), e.getBoolean("event.altKey"), e.getBoolean("event.metaKey"));
        listeners.fire(ce);
    }

    @Override
    public Registration addClickListener(ComponentEventListener<com.vaadin.flow.component.ClickEvent<Component>> listener) {
        return listeners.add(listener);
    }

    @Override
    public void removeClickListener(ComponentEventListener<com.vaadin.flow.component.ClickEvent<Component>> listener) {
        listeners.remove(listener);
    }
}