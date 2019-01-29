package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

/**
 * Make a component look like an "elevated card" on the screen. In order to use this class, just wrap the component with this
 * class.
 * <p>Example: Component c; new Card(c);</p>
 */
public class Card extends Composite {

    private Component component;

    /**
     * Constructor.
     * @param component Component to wrap.
     */
    public Card(Component component) {
        this(component, 4);
    }

    /**
     * Constructor.
     * @param component Component to wrap.
     * @param padding Padding required in pixels
     */
    public Card(Component component, int padding) {
        this(component, padding + "px");
    }

    /**
     * Constructor.
     * @param component Component to wrap.
     * @param paddings Padding
     */
    public Card(Component component, String paddings) {
        this.component = component;
        component.getElement().getStyle().set("box-shadow", "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)");
        component.getElement().getStyle().set("padding", paddings);
    }

    @Override
    protected Component initContent() {
        return component;
    }
}
