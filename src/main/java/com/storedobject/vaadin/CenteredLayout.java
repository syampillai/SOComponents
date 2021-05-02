package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * A simple layout that can be used as a component of the {@link com.storedobject.vaadin.View}. Typically, it is
 * used to contain only one component that will be displayed at the center of the viewing area in its original size.
 *
 * @author Syam
 */
public class CenteredLayout extends ButtonLayout {

    /**
     * Constructor.
     */
    public CenteredLayout() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param component Component to add.
     */
    public CenteredLayout(Component component) {
        setSizeFull();
        getStyle().
                set("justify-content", "space-evenly").
                set("box-sizing", "border-box");
        if(component != null) {
            add(component);
        }
    }
}
