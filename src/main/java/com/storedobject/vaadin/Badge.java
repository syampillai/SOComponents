package com.storedobject.vaadin;

import com.vaadin.flow.component.html.Span;

/**
 * A component that is decorated like a badge.
 *
 * @author Syam
 */
public class Badge extends Span implements HasBadgeStyle {

    private Icon icon;
    private final Span text = new Span();

    /**
     * Constructor.
     */
    public Badge() {
        this("", ThemeStyle.BADGE);
    }

    /**
     * Constructor.
     *
     * @param text Text to be displayed
     * @param styles Styles
     */
    public Badge(String text, ThemeStyle... styles) {
        this.text.setText(text);
        add(this.text);
        setTheme(styles);
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param text Text to be displayed
     * @param styles Styles
     */
    public Badge(String icon, String text, ThemeStyle... styles) {
        this(text, styles);
        setIcon(icon);
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param text Text to be displayed
     * @param styles Styles
     */
    public Badge(Icon icon, String text, ThemeStyle... styles) {
        this(text, styles);
        setIcon(icon);
    }

    /**
     * Set icon.
     *
     * @param icon Icon
     */
    public void setIcon(String icon) {
        if(icon == null || icon.isEmpty()) {
            removeIcon();
            return;
        }
        if(this.icon == null) {
            this.icon = new Icon(icon);
            getElement().insertChild(0, this.icon.getElement());
            return;
        }
        this.icon.setIcon(icon);
    }

    /**
     * Set icon.
     *
     * @param icon Icon
     */
    public void setIcon(Icon icon) {
        if(icon == null) {
            removeIcon();
            return;
        }
        if(this.icon != null) {
            remove(this.icon);
        }
        this.icon = icon;
        getElement().insertChild(0, icon.getElement());
    }

    /**
     * Remove icon.
     */
    public void removeIcon() {
        if(icon != null) {
            remove(icon);
            icon = null;
        }
    }

    /**
     * Set the text content.
     *
     * @param text Text
     */
    @Override
    public void setText(String text) {
        this.text.setText(text == null ? "" : text);
    }

    /**
     * Get the text content.
     *
     * @return Text content.
     */
    @Override
    public String getText() {
        return text.getText();
    }
}