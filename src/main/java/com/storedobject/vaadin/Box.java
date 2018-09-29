package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;

public class Box extends Composite {

    private static final String BACKGROUND = "var(--lumo-contrast-20pct)";
    private Component component;

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

    public void setBorderRadius(int radius) {
        setStyle("border-radius", radius + "px");
    }

    public void setBorderWidth(int widthInPixels) {
        setBorderWidth(widthInPixels + "px");
    }

    public void setBorderWidth(String widths) {
        setStyle("border-width", widths);
    }

    public void setBorderColor(String color) {
        setStyle("border-color", color);
    }

    public void setBorderStyle(String style) {
        setStyle("border-style", style);
    }

    public void setPadding(int padding) {
        setPadding(padding + "px");
    }

    public void setPadding(String paddings) {
        setStyle("padding", paddings);
    }

    public void setMargin(int margin) {
        setMargin(margin + "px");
    }

    public void setMargin(String margins) {
        setStyle("margin", margins);
    }

    public void setStyle(String style, String value) {
        getElement().getStyle().set(style, value);
    }

    public void alignSizing() {
        setStyle("box-sizing", "border-box");
    }

    public void setReadOnly(boolean readOnly) {
        setStyle("background", readOnly ? "white" : BACKGROUND);
        setBorderStyle(readOnly ? "dotted" : "solid");
    }

    public void setEnabled(boolean enabled) {
        setStyle("background", enabled ? BACKGROUND : "white");
    }
}
