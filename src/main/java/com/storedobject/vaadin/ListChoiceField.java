package com.storedobject.vaadin;

import com.storedobject.vaadin.util.CompositeField;
import com.storedobject.vaadin.util.TranslatedField;
import com.vaadin.flow.component.AbstractField;

import java.util.Collection;
import java.util.List;

public class ListChoiceField extends TranslatedField<Integer, String, ListField<String>> {

    public ListChoiceField(Collection<String> items) {
        this(null, items);
    }

    public ListChoiceField(String label, Collection<String> items) {
        super(getDefault(items), new ListField(items), (f, s) -> f.getIndex(s), (f, i) -> f.getValue(i));
        setLabel(label);
    }

    private static Integer getDefault(Collection<String> items) {
        if (items == null || items.isEmpty()) {
            return -1;
        }
        return 0;
    }

    public void setItems(Collection<String> items) {
        getField().setItems(items);
    }


    public void setScrollHeight(String height, int minimumItemCount) {
        getField().setScrollHeight(height, minimumItemCount);
    }
}