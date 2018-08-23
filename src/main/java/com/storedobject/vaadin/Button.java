package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

public class Button extends com.vaadin.flow.component.button.Button {

    /**
     * Constructor
     * @param text Text label to display and the respective icon will be used
     * @param clickHandler Click handler
     */
    public Button(String text, ClickHandler clickHandler) {
        this(text, text, clickHandler);
    }

    /**
     * Constructor
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public Button(Component icon, ClickHandler clickHandler) {
        super(icon, ClickHandler.convert(clickHandler));
    }

    /**
     * Constructor
     * @param text Text label to display
     * @param icon Name of the icon to use
     * @param clickHandler Click handler
     */
    public Button(String text, String icon, ClickHandler clickHandler) {
        this(text, new Icon(icon), clickHandler);
    }

    /**
     * Constructor
     * @param text Text label to display
     * @param icon Icon to use
     * @param clickHandler Click handler
     */
    public Button(String text, Component icon, ClickHandler clickHandler) {
        super(text, icon, ClickHandler.convert(clickHandler));
    }

    /**
     * Constructor
     * @param icon Icon to use
     * @param clickHandler Click handler
     */
    public Button(VaadinIcon icon, ClickHandler clickHandler) {
        super(new Icon(icon), ClickHandler.convert(clickHandler));
    }

    /**
     * Constructor
     * @param text Text label to display
     * @param icon Icon to use
     * @param clickHandler Click handler
     */
    public Button(String text, VaadinIcon icon, ClickHandler clickHandler) {
        super(text, new Icon(icon), ClickHandler.convert(clickHandler));
    }

    public Button asPrimary() {
        getElement().setAttribute("theme", "primary");
        return this;
    }
}
