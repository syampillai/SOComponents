package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Vaadin Flow wrapper around the Web Component: <a href="https://github.com/PolymerElements/paper-icon-button" target="_blank">Paper Icon Button</a>
 * @author Syam
 */
@Tag("paper-icon-button")
@HtmlImport("bower_components/iron-icons/iron-icons.html")
@HtmlImport("bower_components/iron-icons/av-icons.html")
@HtmlImport("bower_components/iron-icons/communication-icons.html")
@HtmlImport("bower_components/iron-icons/device-icons.html")
@HtmlImport("bower_components/iron-icons/editor-icons.html")
@HtmlImport("bower_components/iron-icons/hardware-icons.html")
@HtmlImport("bower_components/iron-icons/image-icons.html")
@HtmlImport("bower_components/iron-icons/maps-icons.html")
@HtmlImport("bower_components/iron-icons/notification-icons.html")
@HtmlImport("bower_components/iron-icons/social-icons.html")
@HtmlImport("bower_components/iron-icons/places-icons.html")
@HtmlImport("bower_components/vaadin-icons/vaadin-icons.html")
@HtmlImport("bower_components/paper-icon-button/paper-icon-button.html")
public class PaperIconButton extends Component implements HasSquareElement, HasIcon, ClickNotifier<PaperIconButton> {

    /**
     * Create with a Vaadin icon
     * @param icon Vaadin icon
     */
    public PaperIconButton(VaadinIcon icon) {
        this(icon, null);
    }

    /**
     * Create with a Vaadin icon and a click event listener
     * @param icon Vaadin icon
     * @param listener Click listener
     */
    public PaperIconButton(VaadinIcon icon, ComponentEventListener<ClickEvent<PaperIconButton>> listener) {
        this("vaadin", icon.name().toLowerCase().replace('_', '-'), listener);
    }

    /**
     * Create with an Iron icon name
     * @param icon Name of the Iron icon
     */
    public PaperIconButton(String icon) {
        this(null, icon);
    }

    /**
     * Create with an Iron icon from a specific collection
     * @param iconCollection Icon collection name
     * @param icon Name of the Iron icon
     */
    public PaperIconButton(String iconCollection, String icon) {
        this(iconCollection, icon, null);
    }

    /**
     * Create with an Iron icon and a click event listener
     * @param icon Name of the Iron icon
     * @param listener Click listener
     */
    public PaperIconButton(String icon, ComponentEventListener<ClickEvent<PaperIconButton>> listener) {
        this(null, icon, listener);
    }

    /**
     * Create with an Iron icon from a specific collection and a click event listener
     * @param iconCollection Iron icon collection name
     * @param icon Name of the Iron icon
     * @param listener Click listener
     */
    public PaperIconButton(String iconCollection, String icon, ComponentEventListener<ClickEvent<PaperIconButton>> listener) {
        setIcon(iconCollection, icon);
        if(listener != null) {
            addClickListener(listener);
        }
    }

    /**
     * Set the ink color
     * @param color Color value to set. Passing null value will remove the current value.
     */
    public void setInkColor(String color) {
        setStyle("--paper-icon-button-ink-color", color);
    }

    /**
     * Get the ink color
     * @return Current value of the ink color
     */
    public String getInkColor() {
        return getStyle("--paper-icon-button-ink-color");
    }
}
