package com.storedobject.vaadin;

/**
 * Slight enhancements to Vaadin's {@link com.vaadin.flow.component.HasElement} interface.
 *
 * @author Syam
 */
public interface HasElement extends HasThemeStyle {

    /**
     * Set style of the element.
     * @param style Style
     * @param styleValue Style value
     */
    default void setStyle(String style, String styleValue) {
        getElement().getStyle().set(style, styleValue);
    }

    /**
     * Get the specific style value of an element.
     * @param style Style for which value needs to retrieved
     * @return Style value.
     */
    default String getStyle(String style) {
        return getElement().getStyle().get(style);
    }

    /**
     * Set attribute of the element.
     * @param attribute Attribute
     * @param attributeValue Attribute value
     */
    default void setAttribute(String attribute, String attributeValue) {
        getElement().setAttribute(attribute, attributeValue);
    }

    /**
     * Get the specific attribute value of an element.
     * @param attribute Attribute for which value needs to retrieved
     * @return Attribute value.
     */
    default String getAttribute(String attribute) {
        return getElement().getAttribute(attribute);
    }
}
