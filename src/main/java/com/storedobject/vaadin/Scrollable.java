package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

/**
 * Make any Vaadin component scrollable.
 * <p>Example: Component c; new Scrollable(c);</p>
 *
 * @author Syam
 */
public class Scrollable extends Composite<Component> {

    private Component component;

    /**
     * Constructor.
     * @param component Component to make it scroll
     */
    public Scrollable(Component component) {
        this(component, true);
    }

    /**
     * Constructor.
     * @param component Component to make it scroll
     * @param scrollable Scrollable or not
     */
    public Scrollable(Component component, boolean scrollable) {
        //noinspection unchecked
        this.component = component instanceof Composite ? ((Composite<Component>) component).getContent() : component;
        if(scrollable) {
            component.getElement().getStyle().set("overflow", "auto");
        } else if(isScrollable()) {
            component.getElement().getStyle().remove("overflow");
        }
    }

    @Override
    protected Component initContent() {
        return component;
    }

    /**
     * Return curent state.
     * @return Scrollable or not.
     */
    public boolean isScrollable() {
        return isScrollable(component);
    }

    /**
     * Check current state of scrollability of a particular component.
     * @param component Component to check
     * @return Scrollable or not.
     */
    public static boolean isScrollable(Component component) {
        if(component instanceof Composite) {
            //noinspection unchecked
            return isScrollable(((Composite<Component>) component).getContent());
        }
        return "auto".equals(component.getElement().getStyle().get("overflow"));
    }
}