package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ClickNotifier;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.shared.Registration;

/**
 * Slightly enhanced version of Vaadin's {@link Notification}. Supports HTML text and the notification is "clickable" (an associated
 * action can be defined).
 * @author Syam
 */
public class Alert extends Notification implements HasText, ClickNotifier {

    private final StyledText content;
    private final ElementClick click;
    private final Runnable clickAction;

    /**
     * Constructor.
     * @param htmlText HTML text
     */
    public Alert(String htmlText) {
        this(null, htmlText, null);
    }

    /**
     * Constructor.
     * @param htmlText HTML text
     * @param clickAction Click action
     */
    public Alert(String htmlText, Runnable clickAction) {
        this(null, htmlText, clickAction);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param htmlText HTML text
     */
    public Alert(String caption, String htmlText) {
        this(caption, htmlText, null);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param htmlText HTML text
     * @param clickAction Click action
     */
    public Alert(String caption, String htmlText, Runnable clickAction) {
        this(styled(caption, htmlText), clickAction);
    }

    /**
     * Constructor.
     * @param htmlContent HTML content
     * @param clickAction Click action
     */
    protected Alert(StyledText htmlContent, Runnable clickAction) {
        this.clickAction = clickAction;
        content = htmlContent;
        content.getElement().getStyle().set("cursor", "pointer");
        add(content);
        click = new ElementClick(content);
        addClickListener(e -> clicked());
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
     * This method is invoked when clicked on the alert. Default behaviour ot this method is to execute the action is already defined.
     */
    public void clicked() {
        if(clickAction == null) {
            close();
        } else {
            clickAction.run();
        }
    }

    /**
     * Set some text on the alert. If the text is non-empty, alert will be shown.
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
     * @return Text.
     */
    @Override
    public String getText() {
        return content.getText();
    }

    /**
     * Get the content as "styled text".
     * @return Styled text.
     */
    public StyledText getContent() {
        return  content;
    }
}