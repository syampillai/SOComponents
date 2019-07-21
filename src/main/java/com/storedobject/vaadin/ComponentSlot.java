package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

/**
 * A component container that support slotted components.
 *
 * @author Syam
 */
public interface ComponentSlot extends HasComponents {

    /**
     * Add a component at a given slot.
     *
     * @param component Component to add
     * @param slot Slot name
     */
    default void add(Component component, String slot) {
        add(component);
        component.getElement().setAttribute("slot", slot);
    }
}