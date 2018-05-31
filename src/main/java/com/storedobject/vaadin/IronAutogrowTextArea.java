package com.storedobject.vaadin;

import com.storedobject.vaadin.util.TextFieldComponent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;

/**
 * Vaadin Flow warpper around the Web Component: <a href="https://github.com/PolymerElements/iron-autogrow-textarea" target="_blank">Iron Autogrow TextArea</a>
 * @author Syam
 */
@Tag("iron-autogrow-textarea")
@HtmlImport("bower_components/iron-autogrow-textarea/iron-autogrow-textarea.html")
public class IronAutogrowTextArea extends TextFieldComponent<IronAutogrowTextArea> {

    private static final PropertyDescriptor<Integer, Integer> maxRowsProperty =
            PropertyDescriptors.propertyWithDefault("maxRows", 0);
    private static final PropertyDescriptor<Integer, Integer> minRowsProperty =
            PropertyDescriptors.propertyWithDefault("rows", 0);

    /**
     * Default constructor. Initial value is blank string.
     */
    public IronAutogrowTextArea() {
        this("", null, null);
    }

    /**
     * Constructor with initial value and placeholder
     * @param initialValue Initial value
     * @param placeholder Placeholder
     */
    public IronAutogrowTextArea(String initialValue, String placeholder) {
        this(initialValue, placeholder, null);
    }

    /**
     * Constructor with no initial value (blank string) and a 'value change' listener
     * @param listener Value change listener
     */
    public IronAutogrowTextArea(HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<IronAutogrowTextArea, String>> listener) {
        this("", null, listener);
    }

    /**
     * Constructor with initial value, placeholder and a 'value change' listener
     * @param initialValue Initial value
     * @param placeholder Placeholder
     * @param listener Value change listener
     */
    public IronAutogrowTextArea(String initialValue, String placeholder,
                                HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<IronAutogrowTextArea, String>> listener) {
        super(initialValue, placeholder);
        if(listener != null) {
            this.addValueChangeListener(listener);
        }
    }

    /**
     * Get maximum visible rows
     * @return Maximum visible rows
     */
    public int getMaxRows() {
        return maxRowsProperty.get(this);
    }

    public void setMaxRows(int maxRows) {
        maxRowsProperty.set(this, maxRows);
    }

    public int getMinRows() {
        return minRowsProperty.get(this);
    }

    public void setMinRows(int rows) {
        minRowsProperty.set(this, rows);
    }
}