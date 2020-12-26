package com.storedobject.vaadin;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

/**
 * Toggle button (wrapper around paper-toggle-button web component). Since it implements
 * {@link com.vaadin.flow.component.HasValue}, it can be used like a field.
 *
 * @author Syam
 */
@Tag("paper-toggle-button")
@NpmPackage(value = "@polymer/paper-toggle-button", version = "3.0.1")
@JsModule("@polymer/paper-toggle-button/paper-toggle-button.js")
public class ToggleButton extends AbstractSinglePropertyField<ToggleButton, Boolean> implements HasStyle {

    /**
     * Constructor.
     */
    public ToggleButton() {
        this(false);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     */
    public ToggleButton(boolean initialValue) {
        super("checked", false, false);
        if(initialValue) {
            setValue(true);
        }
        activate();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if(readOnly == isReadOnly()) {
            return;
        }
        super.setReadOnly(readOnly);
        if(readOnly) {
            deactivate();
        } else {
            activate();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        setReadOnly(!enabled);
    }

    private void activate() {
        getElement().getStyle().
                set("--paper-toggle-button-checked-bar-color", "var(--lumo-primary-color)").
                set("--paper-toggle-button-unchecked-bar-color", "var(--lumo-primary-color)").
                set("--paper-toggle-button-checked-button-color", "var(--lumo-primary-color)").
                set("--paper-toggle-button-checked-ink-color", "var(--lumo-primary-color-50pct)").
                set("--paper-toggle-button-unchecked-ink-color", "var(--lumo-primary-color-50pct)");
    }

    private void deactivate() {
        getElement().getStyle().
                set("--paper-toggle-button-checked-bar-color", "var(--lumo-contrast-50pct)").
                set("--paper-toggle-button-unchecked-bar-color", "var(--lumo-contrast-50pct)").
                set("--paper-toggle-button-checked-button-color", "var(--lumo-primary-color-50pct)").
                set("--paper-toggle-button-unchecked-button-color", "var(--lumo-contrast-50pct)").
                set("--paper-toggle-button-checked-ink-color", "var(--lumo-base-color)").
                set("--paper-toggle-button-unchecked-ink-color", "var(--lumo-base-color)");
    }
}
