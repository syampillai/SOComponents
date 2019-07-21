package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.binder.HasItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A field that displays one item from a list. Item selection can be changed by clicking on it.
 * <p>By default, toString() method is used to convert the item into its displayable text unless a function is set
 * for this purpose using {@link LabelField#setItemLabelGenerator(ItemLabelGenerator)}.</p>
 *
 * @param <T> Value type of the field
 * @author Syam
 */
public class LabelField<T> extends CustomField<T> implements HasItems<T> {

    private Span container = new Span();
    private List<T> items;
    private int index = -1;
    private ItemLabelGenerator<T> labelGenerator;

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
        super(items.get(0));
        add(container);
        Box b = new Box(container);
        b.grey();
        b.setStyle("cursor", "pointer");
        b.setBorderWidth(0);
        b.setHiliteOnHover(true);
        ElementClick click = new ElementClick(this);
        click.addClickListener(e -> setIndex(index + 1, true));
        setItems(items);
        setLabel(label);
        setIndex(0);
    }

    @Override
    public void setItems(Collection<T> items) {
        this.items = new ArrayList<>(items);
        T newValue = getValue();
        if(newValue == null && index < 0) {
            index = 0;
            newValue = generateModelValue();
        }
        container.setText(toString(newValue));
        updateValue();
    }

    /**
     * Set a new value.
     * @param newIndex Index of the label value to be set
     */
    public void setIndex(int newIndex) {
        setIndex(newIndex, false);
    }

    private void setIndex(int newIndex, boolean fromClient) {
        if(index == newIndex) {
            return;
        }
        if(items == null || items.isEmpty()) {
            return;
        }
        if(newIndex >= items.size()) {
            newIndex = 0;
        }
        int oldIndex = index;
        if(newIndex != oldIndex) {
            index = newIndex;
            container.setText(toString(items.get(newIndex)));
            setModelValue(generateModelValue(), fromClient);
        }
    }

    /**
     * Get the index of the currently displayed label.
     * @return Index of the current label.
     */
    public int getIndex() {
        if((index < 0 || items == null || items.isEmpty())) {
            index = -1;
            return index;
        }
        if(index >= items.size()) {
            index = items.size() % index;
        }
        return index;
    }

    /**
     * Get the item for the index passed.
     *
     * @param index Index
     * @return Item at the index.
     */
    public T getValue(int index) {
        if(index < 0 || items == null || items.isEmpty() || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    /**
     * Get index of an item.
     *
     * @param value Item for which index needs to determined
     * @return Index of the item.
     */
    public int getIndex(T value) {
        if(value == null || items == null || items.isEmpty()) {
            return -1;
        }
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private String toString(T value) {
        return labelGenerator != null ? labelGenerator.apply(value) : (value == null ? "" : value.toString());
    }

    @Override
    protected T generateModelValue() {
        if(getIndex() < 0) {
            return null;
        }
        return items.get(index);
    }

    @Override
    protected void setPresentationValue(T value) {
        if(items == null || items.isEmpty()) {
            return;
        }
        setIndex(items.indexOf(value));
    }


    /**
     * Set a label generator to convert item values into displayable labels.
     * @param labelGenerator Label generator
     */
    public void setItemLabelGenerator(ItemLabelGenerator<T> labelGenerator) {
        this.labelGenerator = labelGenerator;
    }
}