package com.storedobject.vaadin;

import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Element that has an Icon.
 *
 * @author Syam
 */
public interface HasIcon extends HasElement {

    /**
     * Set the icon
     * @param icon Name of the Iron icon to set
     */
    default void setIcon(String icon) {
        if(icon == null || icon.trim().isEmpty()) {
            getElement().removeAttribute("icon");
            return;
        }
        if(!icon.contains(":")) {
            ApplicationEnvironment ae = ApplicationEnvironment.get();
            if(ae != null) {
                icon = ae.getIconName(icon);
            }
            icon = "vaadin:" + icon;
        }
        getElement().setAttribute("icon", icon.trim().toLowerCase().replace('_', '-'));
    }

    /**
     * Set a Vaadin icon as the icon.
     *
     * @param icon Vaadin icon
     */
    default void setIcon(VaadinIcon icon) {
        setIcon(icon.name().toLowerCase().replace('_', '-'));
    }

    /**
     * Get the current icon name
     * @return Current icon name
     */
    default String getIcon() {
        return getElement().getAttribute("icon");
    }
}
