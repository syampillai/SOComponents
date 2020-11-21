package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;

import java.util.function.BooleanSupplier;

/**
 * Action form is used to execute some action if the user confirms it by pressing a "Yes" button.
 *
 * @author Syam
 */
public class ActionForm extends DataForm {

    private Runnable actionYes, actionNo;
    private BooleanSupplier preconfirm;

    /**
     * Constructor with out any action (can be set later).
     *
     * @param message Message to be shown
     */
    public ActionForm(String message) {
        this(null, message, null, null, null, null);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ActionForm(String message, Runnable action) {
        this(null, message, action, null, null, null);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ActionForm(Component message, Runnable action) {
        this(null, message, action, null, null, null);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ActionForm(String message, Runnable action, String labelYes, String labelNo) {
        this(null, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ActionForm(Component message, Runnable action, String labelYes, String labelNo) {
        this(null, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ActionForm(String message, Runnable actionYes, Runnable actionNo) {
        this(null, message, actionYes, actionNo, null, null);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ActionForm(Component message, Runnable actionYes, Runnable actionNo) {
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
    public ActionForm(String message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(null, createLabel(message), actionYes, actionNo, labelYes, labelNo);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ActionForm(Component message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(null, message, actionYes, actionNo, labelYes, labelNo);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ActionForm(String caption, String message, Runnable action) {
        this(caption, message, action, null, null, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     */
    public ActionForm(String caption, Component message, Runnable action) {
        this(caption, message, action, null, null, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ActionForm(String caption, String message, Runnable action, String labelYes, String labelNo) {
        this(caption, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Yes" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ActionForm(String caption, Component message, Runnable action, String labelYes, String labelNo) {
        this(caption, message, action, null, labelYes, labelNo);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ActionForm(String caption, String message, Runnable actionYes, Runnable actionNo) {
        this(caption, message, actionYes, actionNo, null, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     */
    public ActionForm(String caption, Component message, Runnable actionYes, Runnable actionNo) {
        this(caption, message, actionYes, actionNo, null, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ActionForm(String caption, String message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(caption, createLabel(message), actionYes, actionNo, labelYes, labelNo);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param actionYes Action when "Yes" button is pressed
     * @param actionNo Action when "No" button is pressed
     * @param labelYes Label for "Yes" button
     * @param labelNo Label for "No" button
     */
    public ActionForm(String caption, Component message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        super(caption == null ? "Confirm" : caption, labelYes == null ? "Yes" : labelYes, labelNo == null ? "No" : labelNo);
        setConfirmAction(actionYes);
        setCancelAction(actionNo);
        add(message);
    }

    private static Component createLabel(String message) {
        Span s;
        if(message == null || !message.contains("\n")) {
            s = new Span(message);
            hilite(s.getStyle());
        } else {
            s = new Span();
            message.lines().forEach(m -> {
                Div div = new Div();
                div.setText(m);
                hilite(div.getStyle());
                s.add(div);
            });
        }
        return s;
    }

    private static void hilite(Style style) {
        style.set("font-size", "15px");
        style.set("font-weight", "BOLD");
        style.set("color", "red");
    }

    /**
     * A {@link Div} container is created.
     *
     * @return {@link Div} container.
     */
    @Override
    protected HasComponents createFieldContainer() {
        return new Div();
    }

    @Override
    protected void buildFields() {
    }

    /**
     * Get the confirm action.
     *
     * @return Confirm action.
     */
    public Runnable getConfirmAction() {
        return actionYes;
    }

    /**
     * Set the confirm action.
     *
     * @param actionYes Action from "Yes" button is pressed
     */
    public void setConfirmAction(Runnable actionYes) {
        this.actionYes = actionYes;
    }

    /**
     * Get the cancel action.
     *
     * @return Cancel action.
     */
    public Runnable getCancelAction() {
        return actionNo;
    }

    /**
     * Set the cancel action.
     *
     * @param actionNo Action from "No" button is pressed
     */
    public void setCancelAction(Runnable actionNo) {
        this.actionNo = actionNo;
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
     *
     * @param preconfirm Preconfirm
     */
    public void setPreconfirm(BooleanSupplier preconfirm) {
        this.preconfirm = preconfirm;
    }

    /**
     * Overridden to implement "preconfirm" check.
     *
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
        getContent().getElement().getStyle().set("min-height", "10px");
    }

    /**
     * Execute an action if the user confirms it.
     *
     * @param message Message to be shown
     * @param action Action to be executed
     */
    public static void execute(String message, Runnable action) {
        new ActionForm(message, action).execute();
    }

    /**
     * Execute an action if the user confirms it.
     *
     * @param message Message to be shown
     * @param action Action to be executed
     */
    public static void execute(Component message, Runnable action) {
        new ActionForm(message, action).execute();
    }
}