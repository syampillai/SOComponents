package com.storedobject.vaadin.util;

import com.storedobject.vaadin.EnhancedClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;

public class ElementClick implements ClickNotifier {

    private final Component component;
    private final Listeners listeners = new ClickListeners();

    public ElementClick(Component component) {
        this(component, null);
    }

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