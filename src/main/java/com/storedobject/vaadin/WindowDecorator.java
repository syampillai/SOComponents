package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * A component that can be added to {@link Window} as the first component so that it will look like a window header.
 *
 * @author Syam
 */
public class WindowDecorator extends Composite<ButtonLayout> {

    private static final String COLOR = "var(--so-header-color)";
    private final ButtonLayout content = new ButtonLayout();
    private final Span titleText;

    /**
     * Constructor.
     *
     * @param view View used by the window (View will be aborted when "close" button is pressed)
     * @param headerComponents Additional header components to be added just before "close" button
     */
    public WindowDecorator(View view, Component... headerComponents) {
        titleText = new Span("");
        titleText.getStyle()
                .set("color", COLOR)
                .set("font-size", "var(--lumo-font-size-xl)")
                .set("font-weight", "bold")
                .set("padding", "0px").set("margin", "0px 0px 0px 2px").set("display", "flex").set("flex-grow", "100")
                .set("text-overflow", "ellipsis");
        content.setWidthFull();
        ImageButton close = new ImageButton(VaadinIcon.CLOSE, view instanceof DataForm ? e -> ((DataForm) view).cancel() : e -> view.abort());
        close.withBox();
        close.setColor(COLOR);
        content.add(titleText);
        if(headerComponents != null) {
            content.add(headerComponents);
        }
        close.getElement().setAttribute("title", view instanceof DataForm ? "Cancel" : "Close");
        content.add(close);
        close.getStyle().set("margin-right", "2px");
        Box box = new Box(content);
        box.alignSizing();
        box.setStyle("background-color", "var(--so-header-background-50pct)");
        content.getStyle().set("flex-wrap", "nowrap");
        setCaption(view.getCaption());
        view.windowDecorator = this;
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
