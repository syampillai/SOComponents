package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.dom.Style;

/**
 * A responsive layout that can be used to display components (typically Buttons horizontally).
 * Unlike {@link com.vaadin.flow.component.orderedlayout.HorizontalLayout}, this wraps its components to more than
 * one row if required.
 *
 * @author Syam
 */
public class ButtonLayout extends FlexLayout implements StickyElement {

    private int margin = 5;

    /**
     * Constructor.
     */
    public ButtonLayout() {
        this((Component[])null);
    }

    /**
     * Constructor.
     * @param components Components to add
     */
    public ButtonLayout(Component... components) {
        getStyle().set("flex-wrap", "wrap").set("align-items", "center").set("align-content", "space-around");
        if(components != null) {
            add(components);
        }
    }

    /**
     * Set the gap between components. (Default gap is 5 pixels).
     * @param gap Gap in pixels.
     */
    public void setGap(int gap) {
        String m = this.margin + "px";
        getChildren().filter(c -> m.equals(c.getElement().getStyle().get("margin-right"))).forEach(c -> m(c, gap));
        this.margin = gap;
    }

    /**
     * Get the gap between components.
     * @return Gap in pixels.
     */
    public int getGap() {
        return margin;
    }

    /**
     * Add components.
     * @param components Components to add
     */
    @Override
    public void add(Component... components) {
        if(components == null) {
            return;
        }
        for(Component c: components) {
            add(c, margin);
        }
    }

    /**
     * Add a component.
     * @param c Component
     * @param margin Margin (gap) in pixels
     */
    public void add(Component c, int margin) {
        if(c == null) {
            return;
        }
        m(c, margin);
        super.add(c);
    }

    /**
     * Create and add a filler component. A filler component is a special component that stretches to the whole
     * horizontal length of the button layout. Any components added after this will be pushed to the
     * right most end.
     *
     * @return The filler component that is created and added now.
     */
    public Component addFiller() {
        Filler filler = new Filler();
        add(filler);
        return filler;
    }

    private void m(Component c, int margin) {
        if(!(c instanceof InvisibleComponent)) {
            c.getElement().getStyle().set("margin-right", margin + "px");
        }
    }

    private static class Filler extends Span implements InvisibleComponent {

        private Filler() {
            Style s = getStyle();
            s.set("display", "flex");
            s.set("flex-grow", "100");
        }
    }
}