package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;

/**
 * A simple layout with a "header" portion and a scrollable "body" portion. The "header" portion will stick to the top.
 *
 * @author Syam
 */
public class ContentWithHeader extends Composite<Div> implements HasSize{

    private final Div layout = new Div();
    private Component header;
    private Component content;

    /**
     * Constructor.
     */
    public ContentWithHeader() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param header Header portion
     * @param body Body portion
     */
    public ContentWithHeader(Component header, Component body) {
        setHeader(header);
        setBody(body);
    }

    @Override
    protected Div initContent() {
        return layout;
    }

    /**
     * Set the header portion.
     *
     * @param header Component to set as the header portion.
     */
    public void setHeader(Component header) {
        if(header == this.header) {
            return;
        }
        layout.removeAll();
        if(header != null) {
            layout.add(header);
            StickyElement.sticky(header);
        }
        if(content != null) {
            layout.add(content);
        }
        this.header = header;
    }

    /**
     * Get the {@link ButtonLayout} from the header portion.
     *
     * @return The header.
     */
    public Component getHeader() {
        return header;
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
            layout.add(body);
        }
        this.content = body;
    }
}