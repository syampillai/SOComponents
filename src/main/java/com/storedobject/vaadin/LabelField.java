package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicLabelField;
import com.storedobject.vaadin.util.CompositeField;
import com.vaadin.flow.component.AbstractField;

import java.util.List;
import java.util.function.Function;

/**
 * A field that displays one item from a list. Item selection can be changed by clicking on it.
 * <p>By default, toString() method is used to convert the item into its displayable text unless a function is set
 * for this purpose using {@link LabelField#setLabelGenerator(Function)}.</p>
 *
 * @param <T> Value type of the field
 * @author Syam
 */
public class LabelField<T> extends
        CompositeField.SingleField<T, LabelField<T>, BasicLabelField<T>, AbstractField.ComponentValueChangeEvent<BasicLabelField<T>, T>> {

    /**
     * Constructor.
     *
     * @param items List of items
     */
    public LabelField(List<T> items) {
        this(null, items);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param items List items
     */
    public LabelField(String label, List<T> items) {
        super(new BasicLabelField<>(items), getDefault(items));
        setLabel(label);
        setValue(null);
    }

    private static <O> O getDefault(List<O> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    /**
     * Get index of an item.
     *
     * @param value Item for which index needs to determined
     * @return Index of the item.
     */
    public int getIndex(T value) {
        return getField().getField().getIndex(value);
    }

    /**
     * Get the item for the index passed.
     *
     * @param index Index
     * @return Item at the index.
     */
    public T getValue(int index) {
        return getField().getField().getValue(index);
    }


    public void setItems(List<T> items) {
        getField().getField().setItems(items);
    }

    /**
     * Set a label generator to convert item values into displayable labels.
     * @param labelGenerator Label generator
     */
    public void setLabelGenerator(Function<T, String> labelGenerator) {
        getField().getField().setLabelGenerator(labelGenerator);
    }
}