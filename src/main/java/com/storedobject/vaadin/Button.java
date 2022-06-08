package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

/**
 * Enhancements to Vaadin {@link com.vaadin.flow.component.button.Button}.
 * Main features are: (1) A {@link ClickHandler} can e defined as the action when button is clicked.
 * (2) From the text on the button, it will try to create an {@link Icon} (unless an icon is already specified),
 * (3) Enable/disable display of icons on any newly created buttons via method {@link #setNoIcons(boolean)}.
 * @author Syam
 */
public class Button extends com.vaadin.flow.component.button.Button implements HasThemeStyle {

    private static boolean noIcons = false;

    /**
     * Constructor.
     *
     * @param text Text label to display and the respective icon will be used (unless {@link #setNoIcons(boolean)} was
     *             used to disable the display of icons.
     * @param clickHandler Click handler
     */
    public Button(String text, ClickHandler clickHandler) {
        this(text, noIcons ? null : text, clickHandler);
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
        this(text, icon == null || icon.isEmpty() ? null : new Icon(icon), clickHandler);
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
        addClickHandler(clickHandler);
        getElement().getStyle().set("cursor", "pointer");
        if(text == null) {
            ThemeStyle.add(this, ThemeStyle.ICON);
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
    public Registration addClickHandler(ClickHandler clickHandler) {
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
        setIcon(new Icon(icon));
    }

    /**
     * Set icon for this button.
     *
     * @param icon Name of the icon
     */
    public void setIcon(String icon) {
        setIcon(new Icon(icon));
    }

    @Override
    public void setIcon(Component icon) {
        if(noIcons) {
            return;
        }
        super.setIcon(icon);
    }

    /**
     * Mark this Button as Primary.
     *
     * @return Self reference.
     */
    public Button asPrimary() {
        return (Button) HasThemeStyle.super.asPrimary();
    }

    /**
     * Make the button Small.
     *
     * @return Self reference.
     */
    public Button asSmall() {
        return (Button) HasThemeStyle.super.asSmall();
    }

    /**
     * Set "no icons" mode for buttons. If this is "on", no icon will be displayed on buttons.
     * @param noIcons True/false.
     */
    public static void setNoIcons(boolean noIcons) {
        Button.noIcons = noIcons;
    }
}
