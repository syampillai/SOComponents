package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

/**
 * A responsive layout that can be used to display components (typically Buttons horizontally.
 * Unlike HorizontalLayout, this wraps its components to more than one row if required.
 *
 * @author Syam
 */
public class ButtonLayout extends FlexLayout {

    private int margin = 5;

    public ButtonLayout() {
        this((Component[])null);
    }

    public ButtonLayout(Component... components) {
        getStyle().set("flex-wrap", "wrap");
        getStyle().set("align-items", "center");
        getStyle().set("align-content", "space-around");
        if(components != null) {
            add(components);
        }
    }

    public void setGap(int gap) {
        String m = this.margin + "px";
        getChildren().filter(c -> m.equals(c.getElement().getStyle().get("margin-right"))).forEach(c -> m(c, gap));
        this.margin = gap;
    }

    public int getGap() {
        return margin;
    }

    @Override
    public void add(Component... components) {
        if(components == null) {
            return;
        }
        for(Component c: components) {
            add(c, margin);
        }
    }

    public void add(Component c, int margin) {
        m(c, margin);
        super.add(c);
    }

    private void m(Component c, int margin) {
        c.getElement().getStyle().set("margin-right", margin + "px");
    }
}