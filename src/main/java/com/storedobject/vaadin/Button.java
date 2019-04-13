package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

/**
 * Enhancements to Vaadin {@link com.vaadin.flow.component.button.Button}.
 * Main features are: (1) A {@link ClickHandler} can e defined as the action when button is clicked. (2) From the text on the button,
 * it will try to create an {@link Icon} (unless an icon is already specified).
 * @author Syam
 */
public class Button extends com.vaadin.flow.component.button.Button {

    /**
     * Constructor.
     *
     * @param text Text label to display and the respective icon will be used
     * @param clickHandler Click handler
     */
    public Button(String text, ClickHandler clickHandler) {
        this(text, text, clickHandler);
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public Button(Component icon, ClickHandler clickHandler) {
        this(null, icon, clickHandler);
    }

    /**
     * Constructor.
     *
     * @param text Text label to display
     * @param icon Name of the icon to use
     * @param clickHandler Click handler
     */
    public Button(String text, String icon, ClickHandler clickHandler) {
        this(text, new Icon(icon), clickHandler);
    }

    /**
     * Constructor.
     *
     * @param text Text label to display
     * @param icon Icon to use
     * @param clickHandler Click handler
     */
    public Button(String text, Component icon, ClickHandler clickHandler) {
        super();
        if(icon != null) {
            setIcon(icon);
        }
        if(text != null) {
            this.setText(text);
        }
        addClickHanlder(clickHandler);
        getElement().getStyle().set("cursor", "pointer");
        if(text == null) {
            addTheme("icon");
        }
    }

    /**
     * Constructor.
     *
     * @param icon Icon to use
     * @param clickHandler Click handler
     */
    public Button(VaadinIcon icon, ClickHandler clickHandler) {
        this(null, new Icon(icon), clickHandler);
    }

    /**
     * Constructor.
     *
     * @param text Text label to display
     * @param icon Icon to use
     * @param clickHandler Click handler
     */
    public Button(String text, VaadinIcon icon, ClickHandler clickHandler) {
        this(text, new Icon(icon), clickHandler);
    }

    /**
     * Add a click handler.
     *
     * @param clickHandler Click handler to add
     * @return Registration.
     */
    public Registration addClickHanlder(ClickHandler clickHandler) {
        if(clickHandler == null) {
            return null;
        }
        return addClickListener(ClickHandler.convert(clickHandler));
    }

    /**
     * Set a Vaadin icon as the icon for this button.
     *
     * @param icon Vaadin icon
     */
    public void setIcon(VaadinIcon icon) {
        super.setIcon(new Icon(icon));
    }

    /**
     * Mark this Button as Primary.
     *
     * @return Self reference.
     */
    public Button asPrimary() {
        addTheme("primary");
        return this;
    }

    /**
     * Make the button Small.
     *
     * @return Self reference.
     */
    public Button asSmall() {
        addTheme("small");
        return this;
    }

    private void addTheme(String theme) {
        String current = getElement().getAttribute("theme");
        if(current == null) {
            getElement().setAttribute("theme", theme);
            return;
        }
        current = " " + current + " ";
        if(current.contains(theme)) {
            return;
        }
        getElement().setAttribute("theme", current.substring(1) + theme);
    }
}
