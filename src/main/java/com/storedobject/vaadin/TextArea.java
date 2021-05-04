package com.storedobject.vaadin;

import com.storedobject.vaadin.util.HasTextValue;
import com.vaadin.flow.component.dependency.CssImport;

/**
 * Slightly enhanced version of Vaadin's {@link com.vaadin.flow.component.textfield.TextArea}.
 *
 * @author Syam
 */
@CssImport(value = "./so/textarea/styles.css", themeFor = "vaadin-text-area")
public class TextArea extends com.vaadin.flow.component.textfield.TextArea implements HasTextValue {

    /**
     * Constructor.
     */
    public TextArea() {
    }

    /**
     * Constructor.
     *
     * @param label Label
     */
    public TextArea(String label) {
        super(label);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param placeholder Placeholder
     */
    public TextArea(String label, String placeholder) {
        super(label, placeholder);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param initialValue Initial value
     * @param placeholder Placeholder
     */
    public TextArea(String label, String initialValue, String placeholder) {
        super(label, initialValue, placeholder);
    }

    /**
     * Constructor.
     *
     * @param rows Visible rows
     * @param columns Visible columns
     */
    public TextArea(int rows, int columns) {
        setRows(rows);
        setColumns(columns);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param rows Visible rows
     * @param columns Visible columns
     */
    public TextArea(String label, int rows, int columns) {
        super(label);
        setRows(rows);
        setColumns(columns);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param placeholder Placeholder
     * @param rows Visible rows
     * @param columns Visible columns
     */
    public TextArea(String label, String placeholder, int rows, int columns) {
        super(label, placeholder);
        setRows(rows);
        setColumns(columns);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param initialValue Initial value
     * @param placeholder Placeholder
     * @param rows Visible rows
     * @param columns Visible columns
     */
    public TextArea(String label, String initialValue, String placeholder, int rows, int columns) {
        super(label, initialValue, placeholder);
        setRows(rows);
        setColumns(columns);
    }

    /**
     * Constructor.
     *
     * @param rows Visible rows
     */
    public TextArea(int rows) {
        setRows(rows);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param rows Visible rows
     */
    public TextArea(String label, int rows) {
        super(label);
        setRows(rows);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param placeholder Placeholder
     * @param rows Visible rows
     */
    public TextArea(String label, String placeholder, int rows) {
        super(label, placeholder);
        setRows(rows);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param initialValue Initial value
     * @param placeholder Placeholder
     * @param rows Visible rows
     */
    public TextArea(String label, String initialValue, String placeholder, int rows) {
        super(label, initialValue, placeholder);
        setRows(rows);
    }

    /**
     * Set number of visible rows by adjusting the height.
     *
     * @param minimumVisibleRows Rows to be made visible
     */
    public void setRows(int minimumVisibleRows) {
        if(minimumVisibleRows < 2) {
            return;
        }
        setHeight((minimumVisibleRows * 28) + "px");
    }

    /**
     * Set number of visible columns by adjusting the width.
     *
     * @param minimumVisibleColumns Columns to be made visible
     */
    public void setColumns(int minimumVisibleColumns) {
        if(minimumVisibleColumns < 5) {
            return;
        }
        setWidth(minimumVisibleColumns + "em");
    }

    @Override
    public void setValue(String value) {
        super.setValue(value == null ? "" : value);
    }

    @Override
    public void setPattern(String pattern) {
    }

    @Override
    public String getText() {
        return getValue();
    }

    @Override
    public void setText(String text) {
        setValue(text);
    }
}
