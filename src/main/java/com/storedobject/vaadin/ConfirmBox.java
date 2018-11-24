package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.util.function.BooleanSupplier;

public class ConfirmBox extends DataForm {

    private Runnable actionYes, actionNo;
    private BooleanSupplier preconfirm;

    public ConfirmBox(String message, Runnable action) {
        this(null, message, action, null, null, null);
    }

    public ConfirmBox(Component message, Runnable action) {
        this(null, message, action, null, null, null);
    }

    public ConfirmBox(String message, Runnable action, String labelYes, String labelNo) {
        this(null, message, action, null, labelYes, labelNo);
    }

    public ConfirmBox(Component message, Runnable action, String labelYes, String labelNo) {
        this(null, message, action, null, labelYes, labelNo);
    }

    public ConfirmBox(String message, Runnable actionYes, Runnable actionNo) {
        this(null, message, actionYes, actionNo, null, null);
    }

    public ConfirmBox(Component message, Runnable actionYes, Runnable actionNo) {
        this(null, message, actionYes, actionNo, null, null);
    }

    public ConfirmBox(String message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(null, createLabel(message), actionYes, actionNo, labelYes, labelNo);
    }

    public ConfirmBox(Component message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(null, message, actionYes, actionNo, labelYes, labelNo);
    }

    public ConfirmBox(String caption, String message, Runnable action) {
        this(caption, message, action, null, null, null);
    }

    public ConfirmBox(String caption, Component message, Runnable action) {
        this(caption, message, action, null, null, null);
    }

    public ConfirmBox(String caption, String message, Runnable action, String labelYes, String labelNo) {
        this(caption, message, action, null, labelYes, labelNo);
    }

    public ConfirmBox(String caption, Component message, Runnable action, String labelYes, String labelNo) {
        this(caption, message, action, null, labelYes, labelNo);
    }

    public ConfirmBox(String caption, String message, Runnable actionYes, Runnable actionNo) {
        this(caption, message, actionYes, actionNo, null, null);
    }

    public ConfirmBox(String caption, Component message, Runnable actionYes, Runnable actionNo) {
        this(caption, message, actionYes, actionNo, null, null);
    }

    public ConfirmBox(String caption, String message, Runnable actionYes, Runnable actionNo, String labelYes, String labelNo) {
        this(caption, createLabel(message), actionYes, actionNo, labelYes, labelNo);
    }

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

    @Override
    protected HasComponents createContainer() {
        return new Div();
    }

    @Override
    protected void buildFields() {
    }

    @Override
    protected final void cancel() {
        super.cancel();
        if(actionNo != null) {
            actionNo.run();
        }
    }

    @Override
    protected final boolean process() {
        close();
        if(actionYes != null) {
            actionYes.run();
        }
        return true;
    }

    public void setPreconfirm(BooleanSupplier preconfirm) {
        this.preconfirm = preconfirm;
    }

    @Override
    protected void execute(View parent, boolean doNotLock) {
        if(preconfirm != null && !preconfirm.getAsBoolean()) {
            return;
        }
        super.execute(parent, doNotLock);
    }
}