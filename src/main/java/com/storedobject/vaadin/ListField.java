package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicListField;
import com.storedobject.vaadin.util.CompositeField;
import com.vaadin.flow.component.AbstractField;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * A field that shows one selected item from a list.
 * <p>By default, toString() method is used to convert the item into its displayable text unless a function is set
 * for this purpose using {@link LabelField#setLabelGenerator(Function)}.</p>
 *
 * @param <T> Value type of the field
 * @author Syam
 */
public class ListField<T> extends
        CompositeField.SingleField<T, ListField<T>, BasicListField<T>, AbstractField.ComponentValueChangeEvent<BasicListField<T>, T>> {

    /**
     * Constructor.
     *
     * @param items List items
     */
    public ListField(Collection<T> items) {
        this(null, items);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param items List items
     */
    public ListField(String label, Collection<T> items) {
        super(new BasicListField<>(items), getDefault(items));
        setLabel(label);
    }

    private static <O> O getDefault(Collection<O> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        if(items instanceof List) {
            return ((List<O>) items).get(0);
        }
        return items.stream().findFirst().get();
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

    /**
     * Set item list.
     *
     * @param items Item list
     */
    public void setItems(Collection<T> items) {
        getField().getField().setItems(items);
    }

    /**
     * Set the scroll height of the list shown.
     * @param height Height
     * @param minimumItemCount Minimum number of items to be displayed anyway
     */
    public void setScrollHeight(String height, int minimumItemCount) {
        getField().getField().setScrollHeight(height, minimumItemCount);
    }

    /**
     * Set a label generator to convert item values into displayable labels.
     * @param labelGenerator Label generator
     */
    public void setLabelGenerator(Function<T, String> labelGenerator) {
        getField().getField().setLabelGenerator(labelGenerator);
    }
}