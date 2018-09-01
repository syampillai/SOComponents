package com.storedobject.vaadin;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Enhancements to Vaadin's Icon to handle all Iron icon collections
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

    private static NameResolver nameResolver = new NameResolver() {
        @Override
        public String resolve(String name) {
            switch (name.toLowerCase()) {
                case "save":
                case "ok":
                case "yes":
                    return "check";
                case "cancel":
                case "no":
                    return "close";
            }
            return name;
        }
    };

    /**
     * Create from a Vaadin icon
     * @param icon Vaadin icon
     */
    public Icon(VaadinIcon icon) {
        super(icon);
    }

    /**
     * Create from an Iron icon
     * @param icon Name of the Iron icon
     */
    public Icon(String icon) {
        this("vaadin", icon);
    }

    /**
     * Create from an Iron icon available in a specific collection
     * @param collection Name of the icon collection
     * @param icon Name of the Iron icon
     */
    public Icon(String collection, String icon) {
        super(collection, icon);
        setIcon(collection, icon);
    }

    public static String resolveName(String name) {
        return nameResolver.resolve(name);
    }

    public static void setNameResolver(NameResolver resolver) {
        if(resolver == null) {
            resolver = name -> name;
        }
        nameResolver = resolver;
    }

    @FunctionalInterface
    public interface NameResolver {
        String resolve(String name);
    }
}
