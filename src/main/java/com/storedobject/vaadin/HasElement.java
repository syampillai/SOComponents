package com.storedobject.vaadin;

/**
 * Enhancements to the standard HasElement in Vaadin
 * @author Syam
 */
public interface HasElement extends com.vaadin.flow.component.HasElement {

    /**
     * Set the style
     * @param style Style to set
     * @param styleValue Style value to set. Passing null will remove the current value.
     */
    default void setStyle(String style, String styleValue) {
        if(styleValue == null) {
            getElement().getStyle().remove(style);
        } else {
            getElement().getStyle().set(style, styleValue);
        }
    }

    /**
     * Get the current value of the style
     * @param style Style for which value needs to retrieved
     * @return Current style value
     */
    default String getStyle(String style) {
        return getElement().getStyle().get(style);
    }
}