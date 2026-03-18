package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

import java.util.function.BooleanSupplier;

/**
 * A confirmation button is a button whose "click handler" will be invoked only after a further confirmation obtained from the user.
 * (A typical use case is to ask for further confirmation when a "Delete" button is pressed).
 *
 * @author Syam
 */
public class ConfirmButton extends PopupButton {

    private BooleanSupplier preconfirm;
    private Yes yes;

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, ClickHandler clickHandler) {
        this(text, clickHandler, null);
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(Component icon, ClickHandler clickHandler) {
        this(icon, clickHandler, null);
    }

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, String icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, null);
    }

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, Component icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, null);
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(VaadinIcon icon, ClickHandler clickHandler) {
        this(icon, clickHandler, null);
    }

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     */
    public ConfirmButton(String text, VaadinIcon icon, ClickHandler clickHandler) {
        this(text, icon, clickHandler, null);
    }

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, ClickHandler clickHandler, String message) {
        super(text);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(Component icon, ClickHandler clickHandler, String message) {
        super(icon);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, String icon, ClickHandler clickHandler, String message) {
        super(text, icon);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, Component icon, ClickHandler clickHandler, String message) {
        super(text, icon);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     *
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(VaadinIcon icon, ClickHandler clickHandler, String message) {
        super(icon);
        init(clickHandler, message);
    }

    /**
     * Constructor.
     *
     * @param text Text on the button
     * @param icon Icon
     * @param clickHandler Click handler
     * @param message Message to be displayed when confirming (If <code>null</code>, it will use "Are you sure?")
     */
    public ConfirmButton(String text, VaadinIcon icon, ClickHandler clickHandler, String message) {
        super(text, icon);
        init(clickHandler, message);
    }

    private void init(ClickHandler clickHandler, String message) {
        add(new StyledText(message == null ? "Are you sure?" : message));
        getElement().getStyle().set("background", "var(--lumo-error-color-10pct)");
        yes = new Yes(clickHandler);
        add(new ButtonLayout(new Button("No", e -> getPopover().close()).asSmall(), yes));
        getPopover().addOpenedChangeListener(e -> {
            if(e.isOpened()) {
                if(preconfirm != null && !preconfirm.getAsBoolean()) {
                    e.getSource().close();
                }
            }
        });
    }

    @Override
    public Registration addClickHandler(ClickHandler clickHandler) {
        if(clickHandler == null || yes == null) {
            return null;
        }
        if(yes.handler == null) {
            yes.handler = clickHandler;
            return () -> yes.handler = null;
        }
        return super.addClickHandler(clickHandler);
    }

    /**
     * Set a "preconfirm" check. If this is set, confirm box is displayed only if the "preconfirm" returns <code>true</code>
     * ({@link BooleanSupplier#getAsBoolean()}).
     *
     * @param preconfirm Preconfirm
     */
    public void setPreconfirm(BooleanSupplier preconfirm) {
        this.preconfirm = preconfirm;
    }

    private class Yes extends Button implements ClickHandler {

        private ClickHandler handler;

        public Yes(ClickHandler clickHandler) {
            super("Yes", null);
            this.handler = clickHandler;
            addClickHandler(this);
            addTheme(ThemeStyle.ERROR, ThemeStyle.SMALL);
        }

        @Override
        public void clicked(Component c) {
            getPopover().close();
            if(handler != null) {
                handler.clicked(ConfirmButton.this);
            }
        }
    }
}