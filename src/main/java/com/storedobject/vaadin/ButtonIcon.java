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
public class ButtonIcon extends Component implements HasSquareElement, HasIcon, ClickNotifier<ButtonIcon> {

    /**
     * Constructor
     * @param icon Text label to display and the respective icon will be used
     * @param clickHandler Click handler
     */
    public ButtonIcon(String icon, ClickHandler clickHandler) {
        this(null, icon, ClickHandler.convert(clickHandler));
    }

    /**
     * Constructor
     * @param icon Icon to use
     * @param clickHandler Click handler
     */
    public ButtonIcon(VaadinIcon icon, ClickHandler clickHandler) {
        this(icon, ClickHandler.convert(clickHandler));
    }

    /**
     * Create with an Iron icon from a specific collection
     * @param iconCollection Icon collection name
     * @param icon Name of the Iron icon
     * @param clickHandler Click handler
     */
    public ButtonIcon(String iconCollection, String icon, ClickHandler clickHandler) {
        this(iconCollection, icon, ClickHandler.convert(clickHandler));
    }

    /**
     * Create with a Vaadin icon
     * @param icon Vaadin icon
     */
    public ButtonIcon(VaadinIcon icon) {
        this(icon, (ComponentEventListener<ClickEvent<ButtonIcon>>)null);
    }

    /**
     * Create with an Iron icon name
     * @param icon Name of the Iron icon
     */
    public ButtonIcon(String icon) {
        this(null, icon);
    }

    /**
     * Create with an Iron icon from a specific collection
     * @param iconCollection Icon collection name
     * @param icon Name of the Iron icon
     */
    public ButtonIcon(String iconCollection, String icon) {
        this(iconCollection, icon, (ComponentEventListener<ClickEvent<ButtonIcon>>)null);
    }

    private ButtonIcon(VaadinIcon icon, ComponentEventListener<ClickEvent<ButtonIcon>> listener) {
        this("vaadin", icon.name().toLowerCase().replace('_', '-'), listener);
    }

    private ButtonIcon(String iconCollection, String icon, ComponentEventListener<ClickEvent<ButtonIcon>> listener) {
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
