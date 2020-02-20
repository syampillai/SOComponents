package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

/**
 * A simple {@link Component} with a {@link ButtonLayout} at the header portion and a scrollable "body" portion.
 *
 * @author Syam
 */
public class ContentWithHeaderButtons extends Composite<CSSGrid> {

    private final CSSGrid layout = new CSSGrid();
    protected final ButtonLayout buttonPanel = new ButtonLayout();
    protected Component content;

    /**
     * Constructor.
     */
    public ContentWithHeaderButtons() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param body Body portion
     */
    public ContentWithHeaderButtons(Component body) {
        layout.add(buttonPanel);
        layout.justify(buttonPanel, CSSGrid.Position.CENTER);
        layout.align(buttonPanel, CSSGrid.Position.START);
        setBody(body);
    }

    @Override
    protected CSSGrid initContent() {
        return layout;
    }

    /**
     * Get the {@link ButtonLayout} from the header portion.
     *
     * @return The header.
     */
    public ButtonLayout getHeader() {
        return buttonPanel;
    }

    /**
     * Get the {@link Component} from the body portion.
     *
     * @return The body.
     */
    public Component getBody() {
        return content;
    }

    /**
     * Set the body portion.
     *
     * @param body Component to set as the body portion
     */
    public void setBody(Component body) {
        if(this.content == body) {
            return;
        }
        if(this.content != null) {
            layout.remove(this.content);
        }
        if(body != null) {
            this.content = body;
            layout.add(body);
        }
    }

    /**
     * Position the header portion horizontally.
     *
     * @param position Position
     */
    public void justifyHeader(CSSGrid.Position position) {
        layout.justify(buttonPanel, position);
    }

    /**
     * Position the header portion vertically.
     *
     * @param position Position
     */
    public void alignHeader(CSSGrid.Position position) {
        layout.align(buttonPanel, position);
    }


    /**
     * Position the body portion horizontally.
     *
     * @param position Position
     */
    public void justifyBody(CSSGrid.Position position) {
        if(content != null) {
            layout.justify(content, position);
        }
    }

    /**
     * Position the body portion vertically.
     *
     * @param position Position
     */
    public void alignBody(CSSGrid.Position position) {
        if(content != null) {
            layout.align(content, position);
        }
    }
}