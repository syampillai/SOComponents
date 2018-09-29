package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ImageButton extends Icon {

    private ElementClick click;
    private ComponentEventListener<ClickEvent<Component>> eventListener;

    public ImageButton(String iconName, ClickHandler clickHandler) {
        super(iconName);
        init(clickHandler);
    }

    public ImageButton(String iconName, String iconCollection, ClickHandler clickHandler) {
        super(iconName, iconCollection);
        init(clickHandler);
    }

    public ImageButton(VaadinIcon icon, ClickHandler clickHandler) {
        super(icon);
        init(clickHandler);
    }

    private void init(ClickHandler clickHandler) {
        getElement().getStyle().set("cursor", "pointer");
        click = new ElementClick(this);
        setClickHandler(clickHandler);
    }

    public void setClickHandler(ClickHandler clickHandler) {
        if(eventListener != null) {
            click.removeClickListener(eventListener);
        }
        eventListener = ClickHandler.convert(clickHandler);
        click.addClickListener(eventListener);
    }

    public void addClickHandler(ClickHandler clickHandler) {
        click.addClickListener(ClickHandler.convert(clickHandler));
    }

    public ImageButton withSize(int sizeInPixels) {
        setSize(sizeInPixels <= 0 ? null : (sizeInPixels + "px"));
        return this;
    }

    public ImageButton asSmall() {
        return withSize(20);
    }
}
