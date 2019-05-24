package com.storedobject.vaadin;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.HtmlImport;

/**
 * Interface to mark that a class has theme styles.
 *
 * @author Syam
 */
@HtmlImport("so-badge-styles.html")
public interface HasThemeStyle extends HasElement {

    /**
     * Set theme styles.
     *
     * @param styles Style to set
     */
    default void setTheme(ThemeStyle... styles) {
        ThemeStyle.set(this, styles);
    }

    /**
     * Add theme styles.
     *
     * @param styles Style to add
     */
    default void addTheme(ThemeStyle... styles) {
        ThemeStyle.add(this, styles);
    }

    /**
     * Remove theme styles.
     *
     * @param styles Style to remove
     */
    default void removeTheme(ThemeStyle... styles) {
        ThemeStyle.remove(this, styles);
    }

    /**
     * Clear all theme styles.
     */
    default void clearThemes() {
        ThemeStyle.clear(this);
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
}
