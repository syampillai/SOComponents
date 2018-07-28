package com.storedobject.vaadin;

public interface HasElement extends com.vaadin.flow.component.HasElement {

    default void setStyle(String style, String styleValue) {
        getElement().getStyle().set(style, styleValue);
    }

    default String getStyle(String style) {
        return getElement().getStyle().get(style);
    }

    default void setAttribute(String attribute, String attributeValue) {
        getElement().setAttribute(attribute, attributeValue);
    }

    default String getAttribute(String attribute) {
        return getElement().getAttribute(attribute);
    }
}
