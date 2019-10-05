package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

/**
 * A component with a "header" and a "detail" part. The "detail" part is normally hidden and only the "header" part is
 * visible. The visibility of the "detail" part can be toggled by clicking on the "header" part.
 *
 * @author Syam (Initial version was written by Anand Jeypal)
 */
@Tag("div")
public class DetailComponent extends Composite<Div> {

    private Div body = new Div();
    private Icon headerIcon;
    private Clickable<Component> header;
    private Div contentLayout = new Div();
    private ClickHandler clickHandler = (ClickHandler) c -> toggle(true);
    private List<ToggleListener> listeners;

    /**
     * Constructor with blank header.
     */
    public DetailComponent() {
        this("", null);
    }

    /**
     * Constructor.
     *
     * @param header Header text
     * @param content Content
     */
    public DetailComponent(String header, Component content) {
        this(header, null, content);
    }

    /**
     * Constructor.
     *
     * @param header Header component
     * @param content Content
     */
    public DetailComponent(Component header, Component content) {
        this(null, header, content);
    }

    private DetailComponent(String headerText, Component header, Component content) {
        contentLayout.setVisible(false);
        if(headerText != null) {
            header = constructHeader(headerText);
        }
        if(header == null) {
            header = constructHeader("");
        }
        this.header = new Clickable<>(header, clickHandler);
        if(content != null) {
            contentLayout.add(content);
        }
        body.add(header, contentLayout);
    }

    @Override
    protected final Div initContent() {
        return body;
    }

    /**
     * Set the header.
     *
     * @param header Header
     */
    public void setHeader(Component header) {
        if(header == null) {
            return;
        }
        body.getElement().removeChild(0);
        this.header = new Clickable<>(header, clickHandler);
        body.getElement().insertChild(0, header.getElement());
    }

    /**
     * Set the header.
     *
     * @param header Header
     */
    public void setHeader(String header) {
        setHeader(constructHeader(header));
    }

    /**
     * Get the content layout.
     *
     * @return The content section.
     */
    public Div getContentLayout() {
        return contentLayout;
    }

    /**
     * Add a component to the content section.
     *
     * @param component the component to add
     */
    public void addContent(Component component) {
        this.contentLayout.add(component);
    }

    /**
     * Set a component to the content section.
     *
     * @param component the component to set
     */
    public void setContent(Component component) {
        this.contentLayout.removeAll();
        this.contentLayout.add(component);
    }

    private Component constructHeader(String header) {
        ButtonLayout summaryLayout = new ButtonLayout();
        H3 titleText = new H3(header == null ? "" : header);
        titleText.getStyle().set("padding", "0px");
        titleText.getStyle().set("margin", "0px");
        titleText.getStyle().set("display", "flex");
        titleText.getStyle().set("flex-grow", "100");
        summaryLayout.setWidthFull();
        headerIcon = new Icon(contentLayout.isVisible() ? VaadinIcon.CHEVRON_CIRCLE_RIGHT : VaadinIcon.CHEVRON_CIRCLE_DOWN);
        summaryLayout.add(titleText, headerIcon);
        Box box = new Box(summaryLayout);
        box.setStyle("cursor", "pointer");
        return summaryLayout;
    }

    /**
     * Collapse the content section.
     */
    public void collapse() {
        if(contentLayout.isVisible()) {
            toggle(false);
        }
    }

    /**
     * Expand the content section.
     */
    public void expand() {
        if(!contentLayout.isVisible()) {
            toggle(false);
        }
    }

    /**
     * Toggle the content section.
     */
    public void toggle() {
        toggle(false);
    }

    private void toggle(boolean fromClient) {
        contentLayout.setVisible(!contentLayout.isVisible());
        if(headerIcon != null) {
            headerIcon.setIcon(contentLayout.isVisible() ? VaadinIcon.CHEVRON_CIRCLE_RIGHT : VaadinIcon.CHEVRON_CIRCLE_DOWN);
        }
        if(listeners != null) {
            ToggledEvent event = new ToggledEvent(this, fromClient, contentLayout.isVisible());
            listeners.forEach(listener -> listener.toggled(event));
        }
    }

    /**
     * Add content toggle listener.
     *
     * @param toggleListener Listener
     * @return {@link Registration}.
     */
    public Registration addToggleListener(ToggleListener toggleListener) {
        if(toggleListener == null) {
            return null;
        }
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(toggleListener);
        return () -> listeners.remove(toggleListener);
    }

    /**
     * Interface for the toggle listener.
     */
    @FunctionalInterface
    public interface ToggleListener {
        /**
         * Called when visibility of the content section is toggled.
         *
         * @param event Toggled event
         */
        void toggled(ToggledEvent event);
    }

    /**
     * Class that represents the "toggled event".
     */
    public static class ToggledEvent extends ComponentEvent<DetailComponent> {

        private final boolean expanded;

        /**
         * Constructor.
         *
         * @param source Event source (will be a {@link DetailComponent}).
         * @param fromClient Whether this happened from the client side or not
         * @param expanded Whether expanded or not
         */
        public ToggledEvent(DetailComponent source, boolean fromClient, boolean expanded) {
            super(source, fromClient);
            this.expanded = expanded;
        }

        /**
         * Check if this was an expanding event or not.
         *
         * @return True if it was aa expanding event.
         */
        public boolean isExpanded() {
            return expanded;
        }

        /**
         * Check if this was a collapsing event or not.
         *
         * @return True if it was a collapsing event.
         */
        public boolean isCollapsed() {
            return !expanded;
        }
    }
}