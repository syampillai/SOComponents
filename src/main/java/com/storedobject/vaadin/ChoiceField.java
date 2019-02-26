package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicComboBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A {@link com.vaadin.flow.component.combobox.ComboBox} style field wihth an {@link Integer} value type.
 * @author Syam
 */
public class ChoiceField extends TranslatedField<Integer, String> implements ValueRequired {

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

    @SuppressWarnings("unchecked")
    public ChoiceField(String label, Collection<String> list) {
        super(new BasicComboBox<>(sanitize(list)), (f, s) -> ((BasicComboBox<String>)f).getIndex(s),
                (f, v) -> ((BasicComboBox<String>)f).getValue(v));
        setValue(0);
        setLabel(label);
    }

    private static Collection<String> createList(Iterable<?> list) {
        ArrayList<String> a = new ArrayList<>();
        list.forEach(item -> {
            String s = null;
            if(item != null) {
                s = item.toString();
            }
            if(s != null) {
                a.add(s.trim());
            }
        });
        return a;
    }

    private static Collection<String> sanitize(Collection<String> collection) {
        if(!(collection instanceof List)) {
            return collection;
        }
        List<String> list = (List<String>) collection;
        String item;
        for(int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if(item.isEmpty()) {
                list.set(i, " ");
            }
        }
        return list;
    }

    public String getChoice() {
        return getField().getValue();
    }

    public void setChoice(String value) {
        getField().setValue(value);
    }

    @Override
    public boolean isEmpty() {
        return getValue() < 0;
    }
}