package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ClickNotifier;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.shared.Registration;

import java.io.Closeable;
import java.util.function.Consumer;

/**
 * Slightly enhanced version of Vaadin's {@link Notification}. Supports HTML text and the notification is "clickable" (an associated
 * action can be defined).
 * 
 * @author Syam
 */
public class Alert extends Notification implements HasText, ClickNotifier {

    private final StyledText content;
    private final ElementClick click;
    private final Consumer<Alert> clickAction;

    /**
     * Constructor.
     *
     * @param htmlText HTML text
     */
    public Alert(String htmlText) {
        this(null, htmlText, null, null);
    }

    /**
     * Constructor.
     *
     * @param htmlText HTML text
     * @param clickAction Click action
     */
    public Alert(String htmlText, Consumer<Alert> clickAction) {
        this(null, htmlText, clickAction);
    }

    /**
     * Constructor.
     *
     * @param caption Caption
     * @param htmlText HTML text
     */
    public Alert(String caption, String htmlText) {
        this(caption, htmlText, null, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption
     * @param htmlText HTML text
     * @param clickAction Click action
     */
    public Alert(String caption, String htmlText, Consumer<Alert> clickAction) {
        this(styled(caption, htmlText), clickAction);
    }

    /**
     * Constructor.
     *
     * @param htmlText HTML text
     * @param style Style
     */
    public Alert(String htmlText, NotificationVariant style) {
        this(null, htmlText, null, style);
    }

    /**
     * Constructor.
     *
     * @param htmlText HTML text
     * @param clickAction Click action
     * @param style Style
     */
    public Alert(String htmlText, Consumer<Alert> clickAction, NotificationVariant style) {
        this(null, htmlText, clickAction, style);
    }

    /**
     * Constructor.
     *
     * @param caption Caption
     * @param htmlText HTML text
     * @param style Style
     */
    public Alert(String caption, String htmlText, NotificationVariant style) {
        this(caption, htmlText, null, style);
    }

    /**
     * Constructor.
     *
     * @param caption Caption
     * @param htmlText HTML text
     * @param clickAction Click action
     * @param style Style
     */
    public Alert(String caption, String htmlText, Consumer<Alert> clickAction, NotificationVariant style) {
        this(styled(caption, htmlText), clickAction, style);
    }

    /**
     * Constructor.
     *
     * @param htmlContent HTML content
     * @param clickAction Click action
     */
    protected Alert(StyledText htmlContent, Consumer<Alert> clickAction) {
        this(htmlContent, clickAction, null);
    }

    /**
     * Constructor.
     *
     * @param htmlContent HTML content
     * @param clickAction Click action
     * @param style Style
     */
    protected Alert(StyledText htmlContent, Consumer<Alert> clickAction, NotificationVariant style) {
        this.clickAction = clickAction;
        content = htmlContent;
        content.getElement().getStyle().set("cursor", "pointer");
        add(content);
        click = new ElementClick(content);
        addClickListener(e -> clicked());
        if(style != null) {
            addThemeVariants(style);
        }
    }

    private static StyledText styled(String caption, String text) {
        text = new StyledText(text).getText();
        if(caption == null || caption.isEmpty()) {
            return new StyledText(text);
        }
        return new StyledText("<span>" + caption + "</span><br/>" + text);
    }

    @Override
    public Registration addClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        return click.addClickListener(listener);
    }

    @Override
    public void removeClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        click.removeClickListener(listener);
    }

    @Override
    public Registration replaceClickListener(ComponentEventListener<ClickEvent<Component>> oldListener,
                                             ComponentEventListener<ClickEvent<Component>> newListener) {
        return click.replaceClickListener(oldListener, newListener);
    }

    /**
     * Show the alert. (Same as {@link #open()}).
     */
    public void show() {
        open();
    }

    /**
     * This method is invoked when clicked on the alert. Default behaviour of this method is to execute the action that is already defined or
     * if no action is defined, it will be closed.
     */
    public void clicked() {
        if(clickAction == null) {
            close();
        } else {
            clickAction.accept(this);
        }
    }

    /**
     * Set some text on the alert. If the text is non-empty, alert will be shown.
     *
     * @param text Text to show
     */
    @Override
    public void setText(String text) {
        content.setText(text);
        if(text != null && !text.isEmpty()) {
            open();
        } else {
            close();
        }
    }

    /**
     * Get the text of the alert.
     *
     * @return Text.
     */
    @Override
    public String getText() {
        return content.getText();
    }

    /**
     * Get the content as "styled text".
     *
     * @return Styled text.
     */
    public StyledText getContent() {
        return  content;
    }

    /**
     * Get the click action associated with this alert.
     *
     * @return The click action if any.
     */
    public Consumer<Alert> getClickAction() {
        return clickAction;
    }

    /**
     * Delete (and close) the alert so that it's no more associated with {@link Application}.
     */
    public void delete() {
        Application.get().removeAlert(this);
    }

    /**
     * Check whether this alert should be removed when closed or not.
     *
     * @return True if this should be removed when closed.
     */
    public boolean deleteOnClose() {
        return clickAction == null;
    }
}