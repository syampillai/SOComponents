package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

/**
 * A button that shows only an icon. Since no text is displayed, it saves screen real estate.
 *
 * @author Syam
 */
public class ImageButton extends Icon implements HasBadgeStyle, HasEnabled {

    private ElementClick click;
    private boolean enabled = true;
    private String color = null, disabledColor = "var(--lumo-disabled-text-color)";

    /**
     * Constructor.
     *
     * @param iconName Icon
     * @param clickHandler Click handler
     */
    public ImageButton(String iconName, ClickHandler clickHandler) {
        super(iconName);
        init(clickHandler, iconName);
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
        init(clickHandler, title);
    }

    /**
     * Constructor.
     *
     * @param title Title (to be shown as tooltip)
     * @param iconName Icon
     * @param clickHandler Click handler
     */
    public ImageButton(String title, String iconName, ClickHandler clickHandler) {
        super(iconName);
        init(clickHandler, title);
    }

    private void init(ClickHandler clickHandler, String title) {
        getElement().getStyle().set("cursor", "pointer");
        click = new ElementClick(this);
        addClickHandler(clickHandler);
        if(title != null) {
            setAttribute("title", title);
        }
        setColor(color);
    }

    /**
     * Add click handler.
     *
     * @param clickHandler Click handler
     * @return Registration.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Registration addClickHandler(ClickHandler clickHandler) {
        return click.addClickListener(e -> {
            if(enabled && clickHandler != null) {
                clickHandler.onComponentEvent(e);
            }
        });
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

    /**
     * Draw a box around ans set the overall size to 25x25 pixels.
     *
     * @return Self
     */
    public ImageButton withBox() {
        return withBox(25);
    }

    /**
     * Draw a box around after sizing it.
     *
     * @param sizeInPixels Size
     * @return Self
     */
    public ImageButton withBox(int sizeInPixels) {
        if(sizeInPixels < 5) {
            sizeInPixels = 25;
        }
        Box box = new Box(this);
        setSize(sizeInPixels + "px");
        box.alignSizing();
        box.grey();
        return this;
    }

    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(enabled) {
            super.setColor(color);
        } else {
            super.setColor(disabledColor);
        }
    }

    @Override
    public void setColor(String color) {
        this.color = color;
        if(enabled) {
            super.setColor(color);
        }
    }

    /**
     * Set the color when disabled.
     *
     * @param color Color
     */
    public void setDisabledColor(String color) {
        this.disabledColor = color;
        if(!enabled) {
            super.setColor(color);
        }
    }
}
