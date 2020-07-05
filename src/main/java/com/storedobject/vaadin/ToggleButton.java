package com.storedobject.vaadin;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
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
@CssImport("./so/togglebutton/styles.css")
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
            setValue(initialValue);
        }
        setClassName("so-toggle-button");
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        setClassName("so-toggle-button" + (readOnly ? "-ro" : ""));
    }

    @Override
    public void setEnabled(boolean enabled) {
        setReadOnly(!enabled);
    }
}
