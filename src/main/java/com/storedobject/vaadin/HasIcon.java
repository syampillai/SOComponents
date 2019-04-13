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
        setIcon(null, icon);
    }

    /**
     * Set the icon from a specific collection
     * @param iconCollection Name of the icon collection
     * @param icon Name of the Iron icon to set
     */
    default void setIcon(String iconCollection, String icon) {
        if(icon == null || icon.trim().isEmpty()) {
            icon = "vaadin:vaadin-h";
        }
        if(!icon.contains(":")) {
            ApplicationEnvironment ae = ApplicationEnvironment.get();
            if(ae != null) {
                icon = ae.getIconName(icon);
            }
        }
        if(iconCollection != null && !iconCollection.trim().isEmpty() && !icon.contains(":")) {
            icon = iconCollection.trim() + ":" + icon;
        }
        getElement().setAttribute("icon", icon.trim().toLowerCase().replace('_', '-'));
    }

    /**
     * Set a Vaadin icon as the icon.
     *
     * @param icon Vaadin icon
     */
    default void setIcon(VaadinIcon icon) {
        setIcon("vaadin", icon.name().toLowerCase().replace('_', '-'));
    }

    /**
     * Get the current icon name
     * @return Current icon name
     */
    default String getIcon() {
        return getElement().getAttribute("icon");
    }
}
