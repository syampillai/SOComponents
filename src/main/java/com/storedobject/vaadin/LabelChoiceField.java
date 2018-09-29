package com.storedobject.vaadin;

import com.storedobject.vaadin.util.TranslatedField;

import java.util.List;

public class LabelChoiceField extends TranslatedField<Integer, String, LabelField<String>> {

    public LabelChoiceField(List<String> items) {
        this(null, items);
    }

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

    public void setItems(List<String> items) {
        getField().setItems(items);
    }
}