package com.storedobject.vaadin;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Enhancements to Vaadin's Icon to handle all Iron icon collections.
 *
 * @author Syam
 */
@NpmPackage(value = "@polymer/iron-icons", version = "3.0.1")
@JsModule("@polymer/iron-icons/iron-icons.js")
@JsModule("@polymer/iron-icons/av-icons.js")
@JsModule("@polymer/iron-icons/communication-icons.js")
@JsModule("@polymer/iron-icons/device-icons.js")
@JsModule("@polymer/iron-icons/editor-icons.js")
@JsModule("@polymer/iron-icons/hardware-icons.js")
@JsModule("@polymer/iron-icons/image-icons.js")
@JsModule("@polymer/iron-icons/maps-icons.js")
@JsModule("@polymer/iron-icons/notification-icons.js")
@JsModule("@polymer/iron-icons/social-icons.js")
@JsModule("@polymer/iron-icons/places-icons.js")
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
