package com.storedobject.vaadin;

import com.storedobject.vaadin.util.CompositeField;
import com.storedobject.vaadin.util.IronAutogrowTextArea;
import com.vaadin.flow.component.AbstractField;

/**
 * A auto-expanding multi-line text field. The height of the field will be automatically increased (up to the value of maximum rows set)
 * when it contains more lines of text. By default, no value is set for the maximum rows and height will increase without any limit.
 * Similarly, value for minimum numner of rows can also be set.
 * <p>This is written as a Vaadin Flow wrapper around the Web Component:
 * <a href="https://github.com/PolymerElements/iron-autogrow-textarea" target="_blank">Iron Autogrow TextArea</a>
 * (<a href="https://github.com/PolymerElements/iron-autogrow-textarea/graphs/contributors" target="_blank">Authors</a>)
 * </p>
 *
 * @author Syam
 */
public class ETextArea
        extends CompositeField.SingleField<String, ETextArea, IronAutogrowTextArea, AbstractField.ComponentValueChangeEvent<IronAutogrowTextArea, String>> {

    /**
     * Constructor.
     */
    public ETextArea() {
        super(new IronAutogrowTextArea(), "", true);
        createField();
    }

    /**
     * Constructor.
     * @param label Label
     */
    public ETextArea(String label) {
        this(label, null);
    }

    /**
     * Constructor.
     * @param label Label
     * @param initialValue Initial value
     */
    public ETextArea(String label, String initialValue) {
        this();
        setLabel(label);
        if(initialValue == null) {
            setPresentationValue(getEmptyValue());
        } else {
            setValue(initialValue);
        }
    }

    /**
     * Get maximum visible rows (without scrolling)
     * @return Maximum visible rows
     */
    public int getMaxRows() {
        return getField().getField().getMaxRows();
    }

    /**
     * Set maximum visible rows (without scrolling)
     * @param maxRows Maximim visible rows
     */
    public void setMaxRows(int maxRows) {
        getField().getField().setMaxRows(maxRows);
    }

    /**
     * Get minimum visible rows
     * @return Minimum visible rows
     */
    public int getMinRows() {
        return getField().getField().getMinRows();
    }

    /**
     * Set minimum visible rows
     * @param rows Minimum visible rows
     */
    public void setMinRows(int rows) {
        getField().getField().setMinRows(rows);
    }
}