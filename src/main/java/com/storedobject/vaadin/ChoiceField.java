package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ComboBox;
import com.storedobject.vaadin.util.TranslatedField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ChoiceField extends TranslatedField<Integer, String, ComboBox<String>> {

    public ChoiceField(String choices) {
        this(null, choices);
    }

    public ChoiceField(String label, String choices) {
        this(label, Arrays.asList(choices.split(",")));
    }

    public ChoiceField(String choices[]) {
        this(null, choices);
    }

    public ChoiceField(String label, String choices[]) {
        this(label, Arrays.asList(choices));
    }

    public ChoiceField(Iterable<?> list) {
        this(null, list);
    }

    public ChoiceField(String label, Iterable<?> list) {
        this(label, createList(list));
    }

    public ChoiceField(String label, Collection<String> list) {
        super(-1, new ComboBox<String>(list), (f, s) -> f.getIndex(s), (f, i) -> f.getValue(i));
        setValue(0);
        setLabel(label);
    }

    private static Collection<String> createList(Iterable<?> list) {
        ArrayList<String> a = new ArrayList<>();
        list.forEach(item -> {
            if(item != null && item instanceof String && ((String)item).isEmpty()) {
                item = null;
            }
            if(item != null) {
                a.add(item.toString().trim());
            }
        });
        return a;
    }

    public String getChoice() {
        return getField().getValue();
    }

    public void setChoice(String value) {
        getField().setValue(value);
    }
}
