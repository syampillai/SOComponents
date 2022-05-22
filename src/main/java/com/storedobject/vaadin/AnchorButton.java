package com.storedobject.vaadin;

import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

/**
 * Anchor that acts like a button. The click on the anchor is handled by the {@link ClickHandler}s set.
 *
 * @author Syam
 */
public class AnchorButton extends com.vaadin.flow.component.html.Anchor {

    private final List<ClickHandler> clickHandlers = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param text Text label to display
     */
    public AnchorButton(String text) {
        this(text, null);
    }

    /**
     * Constructor.
     *
     * @param text Text label to display
     * @param clickHandler Click handler
     */
    public AnchorButton(String text, ClickHandler clickHandler) {
        super("", text);
        addClickHandler(clickHandler);
        new Clickable<>(this, e -> click());
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
        clickHandlers.add(clickHandler);
        return () -> clickHandlers.remove(clickHandler);
    }

    /**
     * To invoke the click programmatically.
     */
    public void click() {
        clickHandlers.forEach(ch -> ch.clicked(this));
    }

    @Override
    public final void setHref(String href) {
        super.setHref("");
    }

    @Override
    public final void setHref(AbstractStreamResource href) {
        super.setHref("");
    }
}
