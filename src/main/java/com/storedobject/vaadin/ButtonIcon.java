package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Vaadin Flow wrapper around the Web Component: Paper Icon Button.
 * 
 * @author Syam
 */
@Tag("paper-icon-button")
@NpmPackage(value = "@polymer/paper-icon-button", version = "3.0.2")
@JsModule("@polymer/paper-icon-button/paper-icon-button.js")
public class ButtonIcon extends Component implements HasSquareElement, HasIcon, ClickNotifier<ButtonIcon> {

    /**
     * Constructor
     * @param icon Text label to display and the respective icon will be used
     * @param clickHandler Click handler
     */
    public ButtonIcon(String icon, ClickHandler clickHandler) {
        this(icon, ClickHandler.convert(clickHandler));
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
        this(icon, (ComponentEventListener<ClickEvent<ButtonIcon>>)null);
    }

    private ButtonIcon(VaadinIcon icon, ComponentEventListener<ClickEvent<ButtonIcon>> listener) {
        this(icon.name().toLowerCase().replace('_', '-'), listener);
    }

    private ButtonIcon(String icon, ComponentEventListener<ClickEvent<ButtonIcon>> listener) {
        setIcon(icon);
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

    /**
     * Show tooltip/placeholder
     * @param placeholder Tooltip text to show
     */
    public void setPlaceholder(String placeholder) {
        getElement().setAttribute("title", placeholder == null ? "" : placeholder);
    }
}
