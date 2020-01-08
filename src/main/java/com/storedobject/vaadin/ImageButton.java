package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

/**
 * A button that shows only an icon. Since no text is displayed, it saves screen real estate.
 *
 * @author Syam
 */
public class ImageButton extends Icon implements HasBadgeStyle {

    private ElementClick click;

    /**
     * Constructor.
     *
     * @param iconName Icon
     * @param clickHandler Click handler
     */
    public ImageButton(String iconName, ClickHandler clickHandler) {
        super(iconName);
        init(clickHandler);
        if(iconName != null) {
            setAttribute("title", iconName);
        }
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ImageButton(VaadinIcon icon, ClickHandler clickHandler) {
        this(null, icon, clickHandler);
    }

    /**
     * Constructor.
     *
     * @param title Title (to be shown as tooltip)
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ImageButton(String title, VaadinIcon icon, ClickHandler clickHandler) {
        super(icon);
        init(clickHandler);
        if(title != null) {
            setAttribute("title", title);
        }
    }

    private void init(ClickHandler clickHandler) {
        getElement().getStyle().set("cursor", "pointer");
        click = new ElementClick(this);
        addClickHandler(clickHandler);
    }

    /**
     * Add click handler.
     *
     * @param clickHandler Click handler
     * @return Registration.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Registration addClickHandler(ClickHandler clickHandler) {
        return click.addClickListener(ClickHandler.convert(clickHandler));
    }

    /**
     * Set size of the image.
     *
     * @param sizeInPixels Size in pixels
     * @return Self reference.
     */
    public ImageButton withSize(int sizeInPixels) {
        setSize(sizeInPixels <= 0 ? null : (sizeInPixels + "px"));
        return this;
    }

    /**
     * Set size to small.
     *
     * @return Self reference.
     */
    public ImageButton asSmall() {
        return (ImageButton) HasBadgeStyle.super.asSmall();
    }
}
