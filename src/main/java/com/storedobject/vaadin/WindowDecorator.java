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
    private final ButtonLayout header = new ButtonLayout();
    private final Span titleText;
    private final ImageButton close;

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
        header.setWidthFull();
        close = new ImageButton(VaadinIcon.CLOSE,
                view instanceof DataForm ? e -> ((DataForm) view).cancel() : e -> view.abort());
        close.withBox();
        close.setColor(COLOR);
        header.add(titleText);
        if(headerComponents != null) {
            header.add(headerComponents);
        }
        close.getElement().setAttribute("title", view instanceof DataForm ? "Cancel" : "Close");
        header.add(close);
        close.getStyle().set("margin-right", "2px");
        Box box = new Box(header);
        box.alignSizing();
        box.setStyle("background-color", "var(--so-header-background-50pct)");
        header.getStyle().set("flex-wrap", "nowrap");
        setCaption(view.getCaption());
        view.windowDecorator = this;
    }

    @Override
    protected final ButtonLayout initContent() {
        return header;
    }

    /**
     * Set the caption. (By default the caption from the view must have been already set).
     *
     * @param caption Caption to set
     */
    public void setCaption(String caption) {
        titleText.setText(caption == null ? "" : caption);
    }

    /**
     * Toggle the visibility of the "Close" button so that the associated Window's closeability can be controlled.
     *
     * @param closeable True/false.
     */
    public void setCloseable(boolean closeable) {
        close.setVisible(closeable);
    }
}
