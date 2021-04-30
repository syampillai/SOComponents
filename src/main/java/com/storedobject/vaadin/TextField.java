package com.storedobject.vaadin;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.dependency.CssImport;

/**
 * Slightly enhanced version of Vaadin's {@link com.vaadin.flow.component.textfield.TextField}.
 *
 * @author Syam
 */
@CssImport(value = "./so/textfield/styles.css", themeFor = "vaadin-text-field")
public class TextField extends com.vaadin.flow.component.textfield.TextField implements HasText, DisablePaste {

    /**
     * Constructor.
     */
    public TextField() {
        setSpellCheck(false);
    }

    /**
     * Constructor.
     *
     * @param label Label
     */
    public TextField(String label) {
        super(label);
        setSpellCheck(false);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param placeholder Placeholder
     */
    public TextField(String label, String placeholder) {
        super(label, placeholder);
        setSpellCheck(false);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param initialValue Initial value
     * @param placeholder Placeholder
     */
    public TextField(String label, String initialValue, String placeholder) {
        super(label, initialValue, placeholder);
        setSpellCheck(false);
    }

    /**
     * Set an icon on the left side of the text.
     *
     * @param icon Icon to be set
     */
    public void setIcon(Icon icon) {
        addToPrefix(icon);
    }

    /**
     * Set an icon on the left side of the text.
     *
     * @param icon Name of the icon to be set
     */
    public void setIcon(String icon) {
        setIcon(new Icon(icon));
    }

    @Override
    public void setValue(String value) {
        super.setValue(value == null ? "" : value);
    }

    @Override
    public String getText() {
        return getValue();
    }

    @Override
    public void setText(String text) {
        setValue(text);
    }

    /**
     * Convert input into uppercase characters.
     */
    public void uppercase() {
        getClassNames().remove("lowercase");
        getClassNames().remove("capitalize");
        getClassNames().add("uppercase");
    }

    /**
     * Convert input into lowercase characters.
     */
    public void lowercase() {
        getClassNames().add("lowercase");
        getClassNames().remove("capitalize");
        getClassNames().remove("uppercase");
    }

    /**
     * Capitalize words while inputting.
     */
    public void capitalize() {
        getClassNames().remove("lowercase");
        getClassNames().add("capitalize");
        getClassNames().remove("uppercase");
    }

    /**
     * Set spelling check on/off. (Default is off.)
     *
     * @param on True to set it on.
     */
    public void setSpellCheck(boolean on) {
        getElement().setAttribute("spellcheck", "" + on);
    }
}
