package com.storedobject.vaadin;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.function.Consumer;

/**
 * A TextField that triggers a Consumer action (when value changed or "Enter" key is pressed). A value-change-timeout
 * of 1000 milliseconds is set as the default but it may be changed.
 *
 * @author Syam
 */
public class ActionTextField extends TextField {

    private Consumer<String> action;
    private boolean ignoreProgrammaticChanges = false;
    private String text = null;
    private int transform = 0;

    /**
     * Constructor.
     *
     * @param action Action to be executed when value changed or "Enter" key is pressed
     */
    public ActionTextField(Consumer<String> action) {
        init(action);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param action Action to be executed when value changed or "Enter" key is pressed
     */
    public ActionTextField(String label, Consumer<String> action) {
        super(label);
        init(action);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param placeholder Placeholder
     * @param action Action to be executed when value changed or "Enter" key is pressed
     */
    public ActionTextField(String label, String placeholder, Consumer<String> action) {
        super(label, placeholder);
        init(action);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param initialValue Initial value
     * @param placeholder Placeholder
     * @param action Action to be executed when value changed or "Enter" key is pressed
     */
    public ActionTextField(String label, String initialValue, String placeholder, Consumer<String> action) {
        super(label, initialValue, placeholder);
        init(action);
    }

    private void init(Consumer<String> action) {
        this.action = action;
        addKeyPressListener(Key.ENTER, e -> act());
        addValueChangeListener(e -> {
            if(ignoreProgrammaticChanges && !e.isFromClient()) {
                return;
            }
            act();
        });
        setValueChangeMode(ValueChangeMode.TIMEOUT);
        setValueChangeTimeout(1000);
    }

    /**
     * Flag to ignore programmatic changes. By default, all value changes will trigger the action.
     *
     * @param ignore Whether to ignore programmatic changes or not
     */
    public void setIgnoreProgrammaticChanges(boolean ignore) {
        this.ignoreProgrammaticChanges = ignore;
    }

    /**
     * Set the action.
     *
     * @param action Action
     */
    public void setAction(Consumer<String> action) {
        this.action = action;
    }

    /**
     * Get the action.
     *
     * @return Action.
     */
    public Consumer<String> getAction() {
        return action;
    }

    /**
     * Action is carried out in this method.
     */
    synchronized void act() {
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

    /**
     * If this method is used, values will be trimmed before action is carried out.
     *
     * @return This (for chained use).
     */
    public ActionTextField trim() {
        transform |= 1;
        return this;
    }

    /**
     * If this method is used, values will be converted to uppercase before action is carried out.
     *
     * @return This (for chained use).
     */
    public ActionTextField toUpperCase() {
        transform |= 2;
        transform &= ~4;
        return this;
    }

    /**
     * If this method is used, values will be converted to lowercase before action is carried out.
     *
     * @return This (for chained use).
     */
    public ActionTextField toLowerCase() {
        transform |= 4;
        transform &= ~2;
        return this;
    }
}
