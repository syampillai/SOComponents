package com.storedobject.vaadin;

/**
 * Enhancement to Vaadin's {@link com.vaadin.flow.component.HasSize} interface.
 * Vaadin's {@link com.vaadin.flow.component.HasSize} sets width/height as "styles"
 * whereas this interface's default methods sets it as "attributes" too.
 *
 * @author Syam
 */
public interface HasSize extends com.vaadin.flow.component.HasSize {

    @Override
    default void setWidth(String width) {
        com.vaadin.flow.component.HasSize.super.setWidth(width);
        getElement().setAttribute("width", width);
    }

    @Override
    default void setHeight(String height) {
        com.vaadin.flow.component.HasSize.super.setHeight(height);
        getElement().setAttribute("height", height);
    }
}
