package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;

/**
 * Using this class one can draw a "box" around any Vaadin component. If you have a component, in order to enclose it in a box, just use
 * <code>new Box(component)</code>. Box is drawn as per "HTML Box Model".
 *
 * @author Syam
 */
@CssImport("./so/hover/styles.css")
public class Box extends Composite<Component> {

    private static final String BACKGROUND = "var(--lumo-contrast-20pct)";
    private final Component component;

    /**
     * Put a component inside a box. Default behaviour of the box:
     * <p>Border style: solid, Border width: 2, Border colour: var(--lumo-contrast-20pct), Padding: 4, Border radius: 5</p>
     *
     * @param component Component to be boxed.
     */
    public Box(Component component) {
        this.component = component;
        setBorderStyle("solid");
        setBorderWidth(2);
        setBorderColor(BACKGROUND);
        setPadding(4);
        setBorderRadius(5);
    }

    @Override
    protected Component initContent() {
        return component;
    }

    @Override
    public Component getContent() {
        return component;
    }

    /**
     * Hilite the content on hover by making the background to "var(--lumo-primary-color-50pct)".
     *
     * @param hilite True if needs to be hilited
     */
    public void setHiliteOnHover(boolean hilite) {
        if(hilite) {
            component.getElement().getClassList().add("so-hover");
        } else {
            component.getElement().getClassList().remove("so-hover");
        }
    }

    /**
     * Set border radius.
     *
     * @param radius Radius in pixels
     */
    public void setBorderRadius(int radius) {
        setStyle("border-radius", radius + "px");
    }

    /**
     * Set border width.
     * @param widthInPixels Width in pixels
     */
    public void setBorderWidth(int widthInPixels) {
        setBorderWidth(widthInPixels + "px");
    }

    /**
     * Set border width.
     *
     * @param widths Width
     */
    public void setBorderWidth(String widths) {
        setStyle("border-width", widths);
    }

    /**
     * Set border color.
     *
     * @param color HTML color
     */
    public void setBorderColor(String color) {
        setStyle("border-color", color);
    }

    /**
     * Set border style.
     * @param style Border style
     */
    public void setBorderStyle(String style) {
        setStyle("border-style", style);
    }

    /**
     * Set padding.
     *
     * @param padding Padding in pixels
     */
    public void setPadding(int padding) {
        setPadding(padding + "px");
    }

    /**
     * Set padding.
     *
     * @param paddings Padding
     */
    public void setPadding(String paddings) {
        setStyle("padding", paddings);
    }

    /**
     * Set margin.
     *
     * @param margin Margin in pixels
     */
    public void setMargin(int margin) {
        setMargin(margin + "px");
    }

    /**
     * Set margin.
     *
     * @param margins Margin
     */
    public void setMargin(String margins) {
        setStyle("margin", margins);
    }

    /**
     * Directly set styles. For example, <code>setStyle("margin", "3px")</code>
     *
     * @param style Style to set
     * @param value Value of the style
     */
    public void setStyle(String style, String value) {
        getElement().getStyle().set(style, value);
    }

    /**
     * Align the box in such way that total width/height of the box will be same as component's width/height.
     */
    public void alignSizing() {
        setStyle("box-sizing", "border-box");
    }

    /**
     * Set read only style. (Vaadin's Lumo theme is emulated).
     *
     * @param readOnly Read only flag
     */
    public void setReadOnly(boolean readOnly) {
        setStyle("background-color", readOnly ? "white" : BACKGROUND);
        setBorderStyle(readOnly ? "dotted" : "solid");
    }

    /**
     * Set enabled/disabled style. (Vaadin's Lumo theme is emulated).
     *
     * @param enabled Enabled or not
     */
    public void setEnabled(boolean enabled) {
        setStyle("background", enabled ? BACKGROUND : "white");
    }

    /**
     * Create a grey background style. Same as setting setReadonly(true).
     */
    public void grey() {
        setReadOnly(false);
    }
}
