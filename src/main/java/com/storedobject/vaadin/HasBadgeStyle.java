package com.storedobject.vaadin;

/**
 * Interface to mark that a class has theme styles of badge.
 *
 * @author Syam
 */
public interface HasBadgeStyle extends HasThemeStyle {

    /**
     * Set theme styles. {@link ThemeStyle#BADGE} will be automatically added.
     *
     * @param styles Style to set
     */
    @Override
    default void setTheme(ThemeStyle... styles) {
        HasThemeStyle.super.setTheme(styles);
        addTheme(ThemeStyle.BADGE);
    }

    /**
     * Clear all theme styles. {@link ThemeStyle#BADGE} will not be removed.
     */
    @Override
    default void clearThemes() {
        HasThemeStyle.super.clearThemes();
        addTheme(ThemeStyle.BADGE);
    }

    /**
     * Remove theme styles. {@link ThemeStyle#BADGE} will not be removed.
     *
     * @param styles Style to remove
     */
    @Override
    default void removeTheme(ThemeStyle... styles) {
        HasThemeStyle.super.removeTheme(styles);
        addTheme(ThemeStyle.BADGE);
    }
}
