package com.storedobject.vaadin;

import com.storedobject.vaadin.util.TranslatedField;

import java.util.Collection;

/**
 * A field that shows text from a list and the index of the list is treated as it value (So, {@link ListChoiceField#getValue()}
 * returns an {@link Integer}).
 *
 * @author Syam
 */
public class ListChoiceField extends TranslatedField<Integer, String, ListField<String>> {

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
        super(getDefault(items), new ListField(items), ListField::getIndex, ListField::getValue);
        setLabel(label);
    }

    private static Integer getDefault(Collection<String> items) {
        if (items == null || items.isEmpty()) {
            return -1;
        }
        return 0;
    }

    /**
     * Set item list.
     * @param items List of text
     */
    public void setItems(Collection<String> items) {
        getField().setItems(items);
    }

    /**
     * Set the scroll height of the list shown.
     * @param height Height
     * @param minimumItemCount Minimum number of items to be displayed anyway
     */
    public void setScrollHeight(String height, int minimumItemCount) {
        getField().setScrollHeight(height, minimumItemCount);
    }
}