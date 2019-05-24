package com.storedobject.vaadin;

import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Icons with badge style.
 *
 * @author Syam
 */
public class BadgeIcon extends Icon implements HasBadgeStyle {

    /**
     * Create from a Vaadin icon
     * @param icon Vaadin icon
     */
    public BadgeIcon(VaadinIcon icon) {
        super(icon);
    }

    /**
     * Create from an Iron icon.
     * @param icon Name of the Iron icon
     */
    public BadgeIcon(String icon) {
        super(icon);
    }
}