package com.storedobject.vaadin;

import com.storedobject.vaadin.util.CompositeField;
import com.storedobject.vaadin.util.IronAutogrowTextArea;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;

public class ETextArea
        extends CompositeField.SingleField<String, ETextArea, IronAutogrowTextArea, AbstractField.ComponentValueChangeEvent<IronAutogrowTextArea, String>> {

    public ETextArea() {
        super(new IronAutogrowTextArea(), "", true);
        createField();
    }

    public ETextArea(String label) {
        this(label, null);
    }

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