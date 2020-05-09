package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.dom.Element;

/**
 * Interface with default implementation to make an {@link com.vaadin.flow.dom.Element} stick to the top of
 * its container.
 *
 * @author Syam
 */
public interface StickyElement extends HasElement {

    /**
     * Make this stick to the top when the rest of the content scrolls.
     */
    default void sticky() {
        sticky(getElement());
    }

    /**
     * Make a {@link Component} stick to the top when the rest of the content scrolls.
     *
     * @param component Component to make it sticky
     */
    static void sticky(Component component) {
        sticky(component.getElement());
    }

    /**
     * Make an {@link Element} stick to the top when the rest of the content scrolls.
     *
     * @param element Element to make it sticky
     */
    static void sticky(Element element) {
        element.getStyle().set("position", "sticky").set("top", "0px").set("z-index", "1").
                set("background", "var(--lumo-base-color)");
    }
}