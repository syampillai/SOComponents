package com.storedobject.vaadin;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Radio field of choices from a list of items.
 *
 * @param <T> Item type
 * @author Syam
 */
public class RadioField<T> extends RadioButtonGroup<T> {

    private List<T> items = new ArrayList<>();

    /**
     * Constructor.
     * @param choices Choices
     */
    public RadioField(T[] choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    public RadioField(String label, T[] choices) {
        this(label, Arrays.asList(choices));
    }

    /**
     * Constructor.
     * @param choices Choices
     */
    public RadioField(Iterable<T> choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    public RadioField(String label, Collection<T> choices) {
        setItems(choices);
        setLabel(label);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    public RadioField(String label, Iterable<T> choices) {
        this.items = createList(choices);
        setItems();
        setLabel(label);
    }

    private static <O> List<O> createList(Iterable<O> choices) {
        ArrayList<O> a = new ArrayList<>();
        choices.forEach(a::add);
        return a;
    }

    /**
     * This does not do anything.
     * @param dataProvider Data provider
     */
    @Override
    public void setDataProvider(DataProvider<T, ?> dataProvider) {
    }

    @Override
    public void setItems(Collection<T> items) {
        this.items.clear();
        this.items.addAll(items);
        setItems();
    }
    
    private void setItems() {
        super.setDataProvider(DataProvider.ofCollection(items));
        if(!items.isEmpty()) {
            setValue(items.get(0));
        }
    }

    /**
     * Get the index of the current value.
     * @return Index of the current value.
     */
    public int getIndex() {
        return getIndex(getValue());
    }

    /**
     * Get the index of a value.
     * @param value Value for which index need to be retrieved
     * @return Index of a value.
     */
    public int getIndex(T value) {
        return items.indexOf(value);
    }

    /**
     * Get the value corresponding to an index.
     * @param index Index
     * @return Value for the index.
     */
    public T getValue(int index) {
        if(index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    /**
     * Set the index as the current value.
     * @param index Index
     */
    public void setIndex(int index) {
        if(index >= 0 && index < items.size()) {
            setValue(items.get(index));
        }
    }
}