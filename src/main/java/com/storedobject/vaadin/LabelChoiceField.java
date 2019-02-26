package com.storedobject.vaadin;

import com.vaadin.flow.data.binder.HasItems;

import java.util.Collection;
import java.util.List;

/**
 * A field that shows text from a list and the index of the list is treated as it value (So, {@link LabelChoiceField#getValue()}
 * returns an {@link Integer}). At any moment, only one item is displayed and that is the currently selected one. Item selection can be
 * changed by clicking on it.
 *
 * @author Syam
 */
public class LabelChoiceField extends TranslatedField<Integer, String> implements HasItems<String> {

    /**
     * Constructor.
     * @param items List of text
     */
    public LabelChoiceField(List<String> items) {
        this(null, items);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items List of text
     */
    @SuppressWarnings("unchecked")
    public LabelChoiceField(String label, List<String> items) {
        super(new LabelField<>(items), (f, v) -> ((LabelField<String>)f).getIndex(v), (f, i) -> ((LabelField<String>)f).getValue(i));
        setLabel(label);
    }

    /**
     * Set item list.
     * @param items List of text
     */
    @SuppressWarnings("unchecked")
    public void setItems(Collection<String> items) {
        ((HasItems<String>)getField()).setItems(items);
    }
}