package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.BooleanSupplier;

/**
 * A confirm button is a button whose "click handler" will be invoked only after a further confirmation obtained from the user.
 * (Typical use case is to ask further confirmation when a "Delete" button is pressed).
 *
 * @author Syam
 */
public class ConfirmButton extends Button {

    private static final String SURE = ", are you sure?";
    private CH handler;

    /**
     * Constructor.
     * @param text Text on the button
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, ClickHandler clickHandler) {
        this(text, clickHandler, text + SURE);
    }

    /**
     * Constructor.
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(Component icon, ClickHandler clickHandler) {
        this(icon, clickHandler, null);
    }

    /**
     * Constructor.
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, String icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, text + SURE);
    }

    /**
     * Constructor.
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, Component icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, text + SURE);
    }

    /**
     * Constructor.
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(VaadinIcon icon, ClickHandler clickHandler) {
        this(icon, clickHandler, null);
    }

    /**
     * Constructor.
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, VaadinIcon icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, text + SURE);
    }

    /**
     * Constructor.
     * @param text Text on the button
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, ClickHandler clickHandler, String message) {
        super(text, null);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(Component icon, ClickHandler clickHandler, String message) {
        super(icon, null);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, String icon, ClickHandler clickHandler, String message) {
        super(text, icon, null);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, Component icon, ClickHandler clickHandler, String message) {
        super(text, icon, null);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(VaadinIcon icon, ClickHandler clickHandler, String message) {
        super(icon, null);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, VaadinIcon icon, ClickHandler clickHandler, String message) {
        super(text, icon, null);
        init(clickHandler, message);
    }

    private void init(ClickHandler clickHandler, String message) {
        handler = new CH(clickHandler, message);
        addClickHanlder(handler);
        getElement().getStyle().set("background", "var(--lumo-error-color-10pct)");
    }

    /**
     * Set a "preconfirm" check. If this is set, confirm box is displayed only if the "preconfirm" returns <code>true</code>
     * ({@link BooleanSupplier#getAsBoolean()}).
     * @param preconfirm Preconfirm
     */
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