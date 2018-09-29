package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ClickNotifier;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.shared.Registration;

public class Alert extends Notification implements HasText, ClickNotifier {

    private final StyledText content;
    private final ElementClick click;
    private final Runnable clickAction;

    public Alert(String htmlText) {
        this(null, htmlText, null);
    }

    public Alert(String htmlText, Runnable clickAction) {
        this(null, htmlText, clickAction);
    }

    public Alert(String caption, String htmlText) {
        this(caption, htmlText, null);
    }

    public Alert(String caption, String htmlText, Runnable clickAction) {
        this(styled(caption, htmlText), clickAction);
    }

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

    public void show() {
        open();
    }

    public void clicked() {
        if(clickAction == null) {
            close();
        } else {
            clickAction.run();
        }
    }

    @Override
    public void setText(String text) {
        content.setText(text);
        if(text != null && !text.isEmpty()) {
            open();
        } else {
            close();
        }
    }

    @Override
    public String getText() {
        return content.getText();
    }

    public StyledText getContent() {
        return  content;
    }
}