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

    /**
     * Set the size in such a way that it expands to the whole content area. The default height of the content area is
     * 90vh when the default implementation of the {@link ApplicationLayout} is used.
     */
    default void setViewFull() {
        setHeight("90vh");
        setWidthFull();
    }
}
