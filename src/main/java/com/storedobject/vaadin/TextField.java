package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Validation;

/**
 * Slightly enhanced version of Vaadin's {@link com.vaadin.flow.component.textfield.TextField}.
 *
 * @author Syam
 */
public class TextField extends com.vaadin.flow.component.textfield.TextField implements Validation {

    /**
     * Constructor.
     */
    public TextField() {
    }

    /**
     * Constructor.
     *
     * @param label Label
     */
    public TextField(String label) {
        super(label);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param placeholder Placeholder
     */
    public TextField(String label, String placeholder) {
        super(label, placeholder);
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
}
