package com.storedobject.vaadin;

import com.storedobject.vaadin.util.TranslatedField;

import java.util.List;

/**
 * A field that shows text from a list and the index of the list is treated as it value (So, {@link LabelChoiceField#getValue()}
 * returns an {@link Integer}). At any moment, only one item is displayed and that is the currently selected one. Item selection can be
 * changed by clicking on it.
 *
 * @author Syam
 */
public class LabelChoiceField extends TranslatedField<Integer, String, LabelField<String>> {

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
        super(getDefault(items), new LabelField(items), LabelField::getIndex, LabelField::getValue);
        setLabel(label);
    }

    private static Integer getDefault(List<String> items) {
        if (items == null || items.isEmpty()) {
            return -1;
        }
        return 0;
    }

    /**
     * Set item list.
     * @param items List of text
     */
    public void setItems(List<String> items) {
        getField().setItems(items);
    }
}