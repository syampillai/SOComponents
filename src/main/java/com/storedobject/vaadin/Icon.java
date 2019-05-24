package com.storedobject.vaadin;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Enhancements to Vaadin's Icon to handle all Iron icon collections.
 *
 * @author Syam
 */
@HtmlImport("frontend://bower_components/iron-icons/iron-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/av-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/communication-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/device-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/editor-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/hardware-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/image-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/maps-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/notification-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/social-icons.html")
@HtmlImport("frontend://bower_components/iron-icons/places-icons.html")
public class Icon extends com.vaadin.flow.component.icon.Icon implements HasIcon, HasSquareElement {

    /**
     * Create from a Vaadin icon
     * @param icon Vaadin icon
     */
    public Icon(VaadinIcon icon) {
        super(icon);
    }

    /**
     * Create from an Iron icon.
     * @param icon Name of the Iron icon
     */
    public Icon(String icon) {
        setIcon(icon);
    }
}
