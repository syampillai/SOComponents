package com.storedobject.vaadin;

import com.storedobject.vaadin.util.IronAutogrowTextArea;
import com.vaadin.flow.component.customfield.CustomField;

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
public class ETextArea extends CustomField<String> {

    private IronAutogrowTextArea field;

    /**
     * Constructor.
     */
    public ETextArea() {
        field = new IronAutogrowTextArea();
        add(field);
    }

    /**
     * Constructor.
     * @param label Label
     */
    public ETextArea(String label) {
        this(label, null);
    }

    @Override
    protected String generateModelValue() {
        return field.getValue();
    }

    @Override
    protected void setPresentationValue(String value) {
        field.setValue(value);
    }

    /**
     * Constructor.
     * @param label Label
     * @param initialValue Initial value
     */
    public ETextArea(String label, String initialValue) {
        this();
        setLabel(label);
        setValue(initialValue);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value == null ? "" : value);
    }

    @Override
    public String getEmptyValue() {
        return "";
    }

    /**
     * Get maximum visible rows (without scrolling)
     * @return Maximum visible rows
     */
    public int getMaxRows() {
        return field.getMaxRows();
    }

    /**
     * Set maximum visible rows (without scrolling)
     * @param maxRows Maximim visible rows
     */
    public void setMaxRows(int maxRows) {
        field.setMaxRows(maxRows);
    }

    /**
     * Get minimum visible rows
     * @return Minimum visible rows
     */
    public int getMinRows() {
        return field.getMinRows();
    }

    /**
     * Set minimum visible rows
     * @param rows Minimum visible rows
     */
    public void setMinRows(int rows) {
        field.setMinRows(rows);
    }
}