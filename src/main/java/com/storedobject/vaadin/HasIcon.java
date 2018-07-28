package com.storedobject.vaadin;

/**
 * Element that has an Icon
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
     * @param icon nameof the Iron icon to set
     */
    default void setIcon(String iconCollection, String icon) {
        if(icon == null || icon.trim().isEmpty()) {
            icon = "vaadin:vaadin-h";
        }
        if(!icon.contains(":")) {
            icon = Icon.resolveName(icon);
        }
        if(iconCollection != null && !iconCollection.trim().isEmpty() && !icon.contains(":")) {
            icon = iconCollection.trim() + ":" + icon;
        }
        getElement().setAttribute("icon", icon.trim().toLowerCase());
    }

    /**
     * Get the current icon name
     * @return Current icon name
     */
    default String getIcon() {
        return getElement().getAttribute("icon");
    }
}
