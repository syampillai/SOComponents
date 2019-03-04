package com.storedobject.vaadin;

import com.vaadin.flow.data.binder.HasItems;

import java.util.Collection;

/**
 * A field that shows text from a list and the index of the list is treated as it value (So, {@link ListChoiceField#getValue()}
 * returns an {@link Integer}).
 *
 * @author Syam
 */
public class ListChoiceField extends TranslatedField<Integer, String> implements HasItems<String> {

    /**
     * Constructor.
     * @param items List of text
     */
    public ListChoiceField(Collection<String> items) {
        this(null, items);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items List of text
     */
    @SuppressWarnings("unchecked")
    public ListChoiceField(String label, Collection<String> items) {
        super(new ListField<>(items), (f, v) -> ((ListField<String>)f).getIndex(v), (f, i) -> ((ListField<String>)f).getValue(i));
        setLabel(label);
    }

    /**
     * Set item list.
     * @param items List of text
     */
    @SuppressWarnings("unchecked")
    public void setItems(Collection<String> items) {
        ((ListField<String>)getField()).setItems(items);
    }
}