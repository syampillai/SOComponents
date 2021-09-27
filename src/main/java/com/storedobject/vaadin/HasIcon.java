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
        if(getIconName(icon) == null) {
            getElement().removeAttribute("icon");
            return;
        }
        getElement().setAttribute("icon", getIconName(icon));
    }

    /**
     * Set a Vaadin icon as the icon.
     *
     * @param icon Vaadin icon
     */
    default void setIcon(VaadinIcon icon) {
        setIcon(getIconName(icon));
    }

    /**
     * Get the current icon name
     * @return Current icon name
     */
    default String getIcon() {
        return getElement().getAttribute("icon");
    }

    /**
     * Find the real name of the icon to be used from the {@link ApplicationEnvironment}.
     *
     * @param icon Name of the icon.
     * @return Name of the icon derived from the {@link ApplicationEnvironment}.
     */
    static String getIconName(String icon) {
        if(icon == null || icon.trim().isEmpty()) {
            return null;
        }
        if(!icon.contains(":")) {
            ApplicationEnvironment ae = ApplicationEnvironment.get();
            if(ae != null) {
                icon = ae.getIconName(icon);
            }
            if(!icon.contains(":")) {
                icon = "vaadin:" + icon;
            }
        }
        return icon.trim().toLowerCase().replace('_', '-');
    }

    /**
     * Get the name of the Vaadin icon.
     *
     * @param icon Icon
     * @return Name of the icon.
     */
    static String getIconName(VaadinIcon icon) {
        return "vaadin:" + icon.name().toLowerCase().replace('_', '-');
    }
}