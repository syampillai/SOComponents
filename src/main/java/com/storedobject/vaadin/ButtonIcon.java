package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

/**
 * Vaadin Flow wrapper around the Web Component: Paper Icon Button.
 * <p>Note: Vaadin icons can't be used here because Vaadin icons are not "Iron" icons.</p>
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
     * Create with an Iron icon name
     * @param icon Name of the Iron icon
     */
    public ButtonIcon(String icon) {
        this(icon, (ComponentEventListener<ClickEvent<ButtonIcon>>)null);
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

    /**
     * Draw a box around ans set the overall size to 25x25 pixels.
     *
     * @return Self
     */
    public ButtonIcon withBox() {
        return withBox(25);
    }

    /**
     * Draw a box around after sizing it.
     *
     * @param sizeInPixels Size
     * @return Self
     */
    public ButtonIcon withBox(int sizeInPixels) {
        if(sizeInPixels < 5) {
            sizeInPixels = 25;
        }
        Box box = new Box(this);
        setSize(sizeInPixels + "px");
        box.alignSizing();
        box.grey();
        return this;
    }
}
