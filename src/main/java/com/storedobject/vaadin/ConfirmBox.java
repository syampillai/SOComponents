package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.util.function.BooleanSupplier;

/**
 * Confirm box is used to confirm some action by the user by displaying a message and Yes/No buttons.
 *
 * @author Syam
 */
public class ConfirmBox extends DataForm {

    private Runnable actionYes, actionNo;
    private BooleanSupplier preconfirm;

    /**
     * Constructor.
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ConfirmBox(String message, Runnable action) {
        this(null, message, action, null, null, null);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ConfirmBox(Component message, Runnable action) {
        this(null, message, action, null, null, null);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(String message, Runnable action, String labelYes, String labelNo) {
        this(null, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(Component message, Runnable action, String labelYes, String labelNo) {
        this(null, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ConfirmBox(String message, Runnable actionYes, Runnable actionNo) {
        this(null, message, actionYes, actionNo, null, null);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ConfirmBox(Component message, Runnable actionYes, Runnable actionNo) {
        this(null, message, actionYes, actionNo, null, null);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(String message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(null, createLabel(message), actionYes, actionNo, labelYes, labelNo);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(Component message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(null, message, actionYes, actionNo, labelYes, labelNo);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ConfirmBox(String caption, String message, Runnable action) {
        this(caption, message, action, null, null, null);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ConfirmBox(String caption, Component message, Runnable action) {
        this(caption, message, action, null, null, null);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(String caption, String message, Runnable action, String labelYes, String labelNo) {
        this(caption, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(String caption, Component message, Runnable action, String labelYes, String labelNo) {
        this(caption, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ConfirmBox(String caption, String message, Runnable actionYes, Runnable actionNo) {
        this(caption, message, actionYes, actionNo, null, null);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ConfirmBox(String caption, Component message, Runnable actionYes, Runnable actionNo) {
        this(caption, message, actionYes, actionNo, null, null);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(String caption, String message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(caption, createLabel(message), actionYes, actionNo, labelYes, labelNo);
    }

    /**
     * Constructor.
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ConfirmBox(String caption, Component message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        super(caption == null ? "Confirm" : caption, labelYes == null ? "Yes" : labelYes, labelNo == null ? "No" : labelNo);
        this.actionYes = actionYes;
        this.actionNo = actionNo;
        getForm().add(message);
    }

    private static Component createLabel(String message) {
        Span s = new Span(message);
        s.getStyle().set("color", "red");
        return s;
    }

    /**
     * A {@link Div} container is created.
     * @return {@link Div} container.
     */
    @Override
    protected HasComponents createContainer() {
        return new Div();
    }

    @Override
    protected void buildFields() {
    }

    /**
     * Action defined for "No" button (if any) is invoked here.
     */
    @Override
    protected final void cancel() {
        super.cancel();
        if(actionNo != null) {
            actionNo.run();
        }
    }


    /**
     * Action defined for "Yes" button is invoked here.
     */
    @Override
    protected final boolean process() {
        close();
        if(actionYes != null) {
            actionYes.run();
        }
        return true;
    }

    /**
     * Set a "preconfirm" check. If this is set, confirm box is displayed only if the "preconfirm" returns <code>true</code>
     * ({@link BooleanSupplier#getAsBoolean()}).
     * @param preconfirm Preconfirm
     */
    public void setPreconfirm(BooleanSupplier preconfirm) {
        this.preconfirm = preconfirm;
    }

    /**
     * Overriden to implement "preconfirm" check.
     * @see #setPreconfirm(BooleanSupplier)
     * @param parent Parent view to lock
     * @param doNotLock True if parent should not be locked
     */
    @Override
    protected void execute(View parent, boolean doNotLock) {
        if(preconfirm != null && !preconfirm.getAsBoolean()) {
            return;
        }
        super.execute(parent, doNotLock);
    }
}