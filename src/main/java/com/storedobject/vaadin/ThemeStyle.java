package com.storedobject.vaadin;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.dom.Element;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A utility class to handle "theme" attribute of an {@link Element}.
 *
 * @author Syam
 */
public enum ThemeStyle {

    /**
     * Success.
     */
    SUCCESS,
    /**
     * Error.
     */
    ERROR,
    /**
     * Pill.
     */
    PILL,
    /**
     * Contrast.
     */
    CONTRAST,
    /**
     * Small.
     */
    SMALL,
    /**
     * Primary.
     */
    PRIMARY,
    /**
     * Icon.
     */
    ICON,
    /**
     * Badge.
     */
    BADGE;

    private static final String THEME = "theme";

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    /**
     * Set theme styles.
     *
     * @param component Element to act upon
     * @param styles Theme styles to set
     */
    public static void set(HasElement component, ThemeStyle... styles) {
        set(component.getElement(), styles);
    }

    /**
     * Set theme styles.
     *
     * @param element Element to act upon
     * @param styles Theme styles to set
     */
    public static void set(Element element, ThemeStyle... styles) {
        if(styles == null || styles.length == 0) {
            clear(element);
        } else {
            element.setAttribute(THEME, Arrays.stream(styles).map(Enum::toString).collect(Collectors.joining(" ")));
        }
    }

    /**
     * Add theme styles.
     *
     * @param component Element to act upon
     * @param styles Theme styles to add
     */
    public static void add(HasElement component, ThemeStyle... styles) {
        add(component.getElement(), styles);
    }

    /**
     * Add theme styles.
     *
     * @param element Element to act upon
     * @param styles Theme styles to add
     */
    public static void add(Element element, ThemeStyle... styles) {
        String theme = element.getAttribute(THEME);
        if(theme != null) {
            theme = theme.trim();
        }
        if(theme == null || theme.isEmpty()) {
            set(element, styles);
            return;
        }
        String oldSet = " " + theme + " ";
        String newSet = Arrays.stream(styles).map(Enum::toString).filter(s -> !oldSet.contains(s)).collect(Collectors.joining(" "));
        if(!newSet.isEmpty()) {
            element.setAttribute(THEME, theme + " " + newSet);
        }
    }

    /**
     * Remove all theme styles.
     *
     * @param component Element to act upon
     */
    public static void clear(HasElement component) {
        clear((component.getElement()));
    }

    /**
     * Remove all theme styles.
     *
     * @param element Element to act upon
     */
    public static void clear(Element element) {
        element.removeAttribute(THEME);
    }

    /**
     * Remove theme styles.
     *
     * @param component Element to act upon
     * @param styles Theme styles to remove
     */
    public static void remove(HasElement component, ThemeStyle... styles) {
        remove(component.getElement(), styles);
    }

    /**
     * Remove theme styles.
     *
     * @param element Element to act upon
     * @param styles Theme styles to remove
     */
    public static void remove(Element element, ThemeStyle... styles) {
        String theme = element.getAttribute(THEME);
        if(theme != null) {
            theme = theme.trim();
        }
        if(theme == null || theme.isEmpty() || styles == null || styles.length == 0) {
            return;
        }
        theme = " " + theme + " ";
        for(ThemeStyle s: styles) {
            theme = theme.replace(" " + s + " ", " ");
        }
        theme = theme.trim();
        if(theme.isEmpty()) {
            element.removeAttribute(THEME);
        } else {
            element.setAttribute(THEME, theme);
        }
    }
}