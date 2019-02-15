package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * A button that shows only an icon. Since no text is displayed, it saves screen real estate.
 *
 * @author Syam
 */
public class ImageButton extends Icon {

    private ElementClick click;
    private ComponentEventListener<ClickEvent<Component>> eventListener;

    /**
     * Constructor.
     * @param iconName Icon
     * @param clickHandler Click handler
     */
    public ImageButton(String iconName, ClickHandler clickHandler) {
        super(iconName);
        init(clickHandler);
    }

    /**
     * Constructor.
     * @param iconName Icon
     * @param iconCollection Icon collection name
     * @param clickHandler Click handler
     */
    public ImageButton(String iconName, String iconCollection, ClickHandler clickHandler) {
        super(iconName, iconCollection);
        init(clickHandler);
        setAttribute("title", iconName);
    }

    /**
     * Constructor.
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ImageButton(VaadinIcon icon, ClickHandler clickHandler) {
        this(null, icon, clickHandler);
    }

    /**
     * Constructor.
     * @param title Title (to be shown as tooltip)
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ImageButton(String title, VaadinIcon icon, ClickHandler clickHandler) {
        super(icon);
        init(clickHandler);
        setAttribute("title", title);
    }

    private void init(ClickHandler clickHandler) {
        getElement().getStyle().set("cursor", "pointer");
        click = new ElementClick(this);
        setClickHandler(clickHandler);
    }

    /**
     * Set click handler.
     * @param clickHandler Click handler
     */
    public void setClickHandler(ClickHandler clickHandler) {
        if(eventListener != null) {
            click.removeClickListener(eventListener);
        }
        eventListener = ClickHandler.convert(clickHandler);
        click.addClickListener(eventListener);
    }

    /**
     * Add click handler.
     * @param clickHandler Click handler
     */
    public void addClickHandler(ClickHandler clickHandler) {
        click.addClickListener(ClickHandler.convert(clickHandler));
    }

    /**
     * Set size of the image.
     * @param sizeInPixels Size in pixels
     * @return Self reference.
     */
    public ImageButton withSize(int sizeInPixels) {
        setSize(sizeInPixels <= 0 ? null : (sizeInPixels + "px"));
        return this;
    }

    /**
     * Set size to small (20 pixels).
     * @return Self reference.
     */
    public ImageButton asSmall() {
        return withSize(20);
    }
}
