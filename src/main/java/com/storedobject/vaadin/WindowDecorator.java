package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * A component that can be added to {@link Window} as the first component so that it will look like a window header.
 *
 * @author Syam
 */
public class WindowDecorator extends Composite<ButtonLayout> {

    private ButtonLayout content = new ButtonLayout();
    private H3 titleText;

    /**
     * Constructor.
     *
     * @param view View used by the window (View will be aborted when "close" button is pressed)
     * @param headerComponents Additional header components to be added just before "close" button
     */
    public WindowDecorator(View view, Component... headerComponents) {
        titleText = new H3("");
        titleText.getStyle().set("color", "var(--lumo-primary-contrast-color)");
        titleText.getStyle().set("padding", "0px");
        titleText.getStyle().set("margin", "0px");
        titleText.getStyle().set("display", "flex");
        titleText.getStyle().set("flex-grow", "100");
        content.setWidthFull();
        ImageButton close = new ImageButton(VaadinIcon.CLOSE, view instanceof DataForm ? e -> ((DataForm) view).cancel() : e -> view.abort());
        content.add(titleText);
        if(headerComponents != null) {
            content.add(headerComponents);
        }
        close.getElement().setAttribute("title", view instanceof DataForm ? "Cancel" : "Close");
        content.add(close.withBox());
        Box box = new Box(content);
        box.alignSizing();
        content.getStyle().set("background-color", "var(--lumo-primary-color-50pct)");
        content.getStyle().set("color", "var(--lumo-primary-contrast-color)");
        setCaption(view.getCaption());
    }

    @Override
    protected final ButtonLayout initContent() {
        return content;
    }

    /**
     * Set the caption. (By default the caption from the view must have been already set).
     *
     * @param caption Caption to set
     */
    public void setCaption(String caption) {
        titleText.setText(caption == null ? "" : caption);
    }
}
