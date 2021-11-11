package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * An information message to be shown to the user. Some action can be specified when the "Ok" button is pressed.
 *
 * @author Syam
 */
public class InformationMessage extends ActionForm {

    /**
     * Constructor without any action (can be set later).
     *
     * @param message Message to be shown
     */
    public InformationMessage(String message) {
        super(null, message, null, null, null, null);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     */
    public InformationMessage(String message, Runnable action) {
        super(null, message, action, action, null, null);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     */
    public InformationMessage(Component message, Runnable action) {
        super(null, message, action, action, null, null);
    }

    /**
     * Constructor.
     *
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     * @param labelOk Label for "Ok" button
     */
    public InformationMessage(Component message, Runnable action, String labelOk) {
        super(null, message, action, action, null, null);
    }

    /**
     * Constructor.
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     * @param labelOk Label for "Ok" button
     */
    public InformationMessage(String message, Runnable action, String labelOk) {
        super(message, action, action, labelOk, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     */
    public InformationMessage(String caption, String message, Runnable action) {
        super(caption, message, action, action, null, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     */
    public InformationMessage(String caption, Component message, Runnable action) {
        super(caption, message, action, action, null, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     * @param labelOk Label for "Ok" button
     */
    public InformationMessage(String caption, String message, Runnable action, String labelOk) {
        super(caption, message, action, action, labelOk, null);
    }

    /**
     * Constructor.
     *
     * @param caption Caption to be displayed
     * @param message Message to be shown
     * @param action Action when "Ok" button is pressed
     * @param labelOk Label for "Ok" button
     */
    public InformationMessage(String caption, Component message, Runnable action, String labelOk) {
        super(caption, message, action, null, labelOk, null);
    }

    @Override
    protected void buildButtons() {
        super.buildButtons();
        cancel.setVisible(false);
    }

    @Override
    protected String getDefaultCaption() {
        return "Message";
    }

    @Override
    protected String getDefaultYesLabel() {
        return "Ok";
    }

    @Override
    public void setConfirmAction(Runnable action) {
        super.setConfirmAction(action);
        super.setCancelAction(action);
    }

    /**
     * Set the confirm action.
     *
     * @param action Action from "Ok" button is pressed
     */
    public void setAction(Runnable action) {
        super.setConfirmAction(action);
        super.setCancelAction(action);
    }

    @Override
    public void setCancelAction(Runnable actionNo) {
    }

    /**
     * Execute an action after showing a informational message to the user.
     *
     * @param message Message to be shown
     * @param action Action to be executed
     */
    public static void execute(String message, Runnable action) {
        new InformationMessage(message, action).execute();
    }

    /**
     * Execute an action after showing a informational message to the user.
     *
     * @param message Message to be shown
     * @param action Action to be executed
     */
    public static void execute(Component message, Runnable action) {
        new InformationMessage(message, action).execute();
    }
}
