package com.storedobject.vaadin;

import com.vaadin.flow.component.Key;

import java.util.function.Consumer;

public class ActionTextField extends TextField {

    private Consumer<String> action;
    private String text = null;
    private int transform = 0;

    public ActionTextField(Consumer<String> action) {
        init(action);
    }

    public ActionTextField(String label, Consumer<String> action) {
        super(label);
        init(action);
    }

    public ActionTextField(String label, String placeholder, Consumer<String> action) {
        super(label, placeholder);
        init(action);
    }

    public ActionTextField(String label, String initialValue, String placeholder, Consumer<String> action) {
        super(label, initialValue, placeholder);
        init(action);
    }

    private void init(Consumer<String> action) {
        this.action = action;
        addKeyPressListener(Key.ENTER, e -> act());
        addValueChangeListener(e -> act());
    }

    public void setAction(Consumer<String> action) {
        this.action = action;
    }

    public Consumer<String> getAction() {
        return action;
    }

    private synchronized void act() {
        String t = getValue();
        if((transform & 1) == 1) {
            t = t.trim();
        }
        if((transform & 2) == 2) {
            t = t.toUpperCase();
        } else if((transform & 4) == 4) {
            t = t.toLowerCase();
        }
        if(text != null && text.equals(t)) {
            focus();
            return;
        }
        text = t;
        Consumer<String> a = getAction();
        if(a != null) {
            a.accept(t);
        }
        focus();
    }

    public ActionTextField trim() {
        transform |= 1;
        return this;
    }

    public ActionTextField toUpperCase() {
        transform |= 2;
        transform &= ~4;
        return this;
    }

    public ActionTextField toLowerCase() {
        transform |= 4;
        transform &= ~2;
        return this;
    }
}
