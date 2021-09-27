package com.storedobject.vaadin;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.dom.Element;

/**
 * Interface to mark that a class has theme styles.
 *
 * @author Syam
 */
@JsModule("./so/badge/styles.js")
public interface HasThemeStyle extends HasElement {

    /**
     * Set theme styles.
     *
     * @param styles Style to set
     */
    default void setTheme(ThemeStyle... styles) {
        ThemeStyle.set(getInternalElement(), styles);
    }

    /**
     * Add theme styles.
     *
     * @param styles Style to add
     */
    default void addTheme(ThemeStyle... styles) {
        ThemeStyle.add(getInternalElement(), styles);
    }

    /**
     * Remove theme styles.
     *
     * @param styles Style to remove
     */
    default void removeTheme(ThemeStyle... styles) {
        ThemeStyle.remove(getInternalElement(), styles);
    }

    /**
     * Clear all theme styles.
     */
    default void clearThemes() {
        ThemeStyle.clear(getInternalElement());
    }

    /**
     * Make it small.
     *
     * @return Self.
     */
    default HasThemeStyle asSmall() {
        addTheme(ThemeStyle.SMALL);
        return this;
    }

    /**
     * Make it primary style.
     *
     * @return Self.
     */
    default HasThemeStyle asPrimary() {
        addTheme(ThemeStyle.PRIMARY);
        return this;
    }

    /**
     * Get the internal element where the theme is ultimately applied.
     *
     * @return The default implementation returns the same element.
     */
    default Element getInternalElement() {
        return getElement();
    }
}
