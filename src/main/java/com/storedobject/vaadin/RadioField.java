package com.storedobject.vaadin;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.dataview.RadioButtonGroupListDataView;

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

    private RadioButtonGroupListDataView<T> view;

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
        setItems(createList(choices));
        setLabel(label);
    }

    private static <O> List<O> createList(Iterable<O> choices) {
        ArrayList<O> a = new ArrayList<>();
        choices.forEach(a::add);
        return a;
    }

    @Override
    public RadioButtonGroupListDataView<T> setItems(Collection<T> items) {
        view = super.setItems(items);
        if(!items.isEmpty()) {
            setValue(view.getItem(0));
        }
        return view;
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
        for(int i = 0; i < view.getItemCount(); i++) {
            if(view.getItem(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the value corresponding to an index.
     * @param index Index
     * @return Value for the index.
     */
    public T getValue(int index) {
        if(index >= 0 && index < view.getItemCount()) {
            return view.getItem(index);
        }
        return null;
    }

    /**
     * Set the index as the current value.
     * @param index Index
     */
    public void setIndex(int index) {
        if(index >= 0 && index < view.getItemCount()) {
            setValue(view.getItem(index));
        }
    }
}