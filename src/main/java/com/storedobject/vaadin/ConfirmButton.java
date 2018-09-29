package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.BooleanSupplier;

public class ConfirmButton extends Button {

    private static final String SURE = ", are you sure?";
    private CH handler;

    public ConfirmButton(String text, ClickHandler clickHandler) {
        this(text, clickHandler, text + SURE);
    }

    public ConfirmButton(Component icon, ClickHandler clickHandler) {
        this(icon, clickHandler, null);
    }

    public ConfirmButton(String text, String icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, text + SURE);
    }

    public ConfirmButton(String text, Component icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, text + SURE);
    }

    public ConfirmButton(VaadinIcon icon, ClickHandler clickHandler) {
        this(icon, clickHandler, null);
    }

    public ConfirmButton(String text, VaadinIcon icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, text + SURE);
    }

    public ConfirmButton(String text, ClickHandler clickHandler, String message) {
        super(text, null);
        init(clickHandler, message);
    }

    public ConfirmButton(Component icon, ClickHandler clickHandler, String message) {
        super(icon, null);
        init(clickHandler, message);
    }

    public ConfirmButton(String text, String icon, ClickHandler clickHandler, String message) {
        super(text, icon, null);
        init(clickHandler, message);
    }

    public ConfirmButton(String text, Component icon, ClickHandler clickHandler, String message) {
        super(text, icon, null);
        init(clickHandler, message);
    }

    public ConfirmButton(VaadinIcon icon, ClickHandler clickHandler, String message) {
        super(icon, null);
        init(clickHandler, message);
    }

    public ConfirmButton(String text, VaadinIcon icon, ClickHandler clickHandler, String message) {
        super(text, icon, null);
        init(clickHandler, message);
    }

    private void init(ClickHandler clickHandler, String message) {
        handler = new CH(clickHandler, message);
        addClickHanlder(handler);
        getElement().getStyle().set("background", "var(--lumo-error-color-10pct)");
    }

    public void setPreconfirm(BooleanSupplier preconfirm) {
        handler.confirmBox.setPreconfirm(preconfirm);
    }

    private static class CH implements ClickHandler {

        private ClickHandler handler;
        private ConfirmBox confirmBox;
        private Component component;

        private CH(ClickHandler hanlder, String message) {
            if(message == null) {
                message = "Are you sure?";
            }
            this.handler = hanlder;
            confirmBox = new ConfirmBox(message, this::act);
        }

        @Override
        public void clicked(Component c) {
            this.component = c;
            confirmBox.execute();
        }

        private void act() {
            handler.clicked(component);
        }
    }
}