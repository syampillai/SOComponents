package com.storedobject.vaadin.util;

import com.storedobject.vaadin.ComboField;

import java.util.Collection;

/**
 * BasicComboBox is a specific implementation of a ComboField allowing for the creation of combo boxes
 * with various constructors for initialization. This class provides initialization logic
 * tailored to set certain default behaviors such as disallowing custom values and requiring a value to be set.
 *
 * @param <T> The type of items contained in the combo box.
 *
 * @author Syam
 */
public class BasicComboBox<T> extends ComboField<T> {

    /**
     * Constructs a new instance of BasicComboBox with default configurations.
     * This constructor initializes the combo box by calling the superclass's no-argument
     * constructor and then sets up default behavior by invoking the init method.
     */
    public BasicComboBox() {
        super();
        init();
    }

    /**
     * Constructs a BasicComboBox with a specified label.
     * The label represents the text associated with the combo box.
     * This constructor initializes the combo box with default behaviors
     * such as disallowing custom values to be input and requiring a value to be set.
     *
     * @param label the label text associated with the combo box
     */
    public BasicComboBox(String label) {
        super(label);
        init();
    }

    /**
     * Constructs a BasicComboBox with the specified collection of items.
     *
     * @param items The collection of items to be included in the combo box.
     */
    public BasicComboBox(Collection<T> items) {
        this(null, items);
    }

    /**
     * Constructs a BasicComboBox with a specified label and collection of items.
     *
     * @param label the label of the combo box
     * @param items the collection of items to populate the combo box
     */
    public BasicComboBox(String label, Collection<T> items) {
        super(label, items);
        init();
    }

    /**
     * Constructs a BasicComboBox with a label and a variable number of items.
     * This constructor initializes the combo box with the provided label and items,
     * while also applying default settings through the init method.
     *
     * @param label The label for the combo box.
     * @param items A variable number of items to be included in the combo box.
     */
    @SafeVarargs
    public BasicComboBox(String label, T... items) {
        super(label, items);
        init();
    }

    /**
     * Initializes the BasicComboBox instance with default settings.
     * This method sets the combo box to disallow custom values and marks it as required.
     * It is invoked during the construction of BasicComboBox to ensure consistent behavior.
     */
    private void init() {
        setAllowCustomValue(false);
        setRequired(true);
    }
}