package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicChoicesField;
import com.storedobject.vaadin.util.CompositeField;
import com.vaadin.flow.component.HasValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ChoicesField extends CompositeField<Integer, ChoicesField, HasValue.ValueChangeEvent<Integer>, BasicChoicesField> {

    public ChoicesField(String choices) {
        this(null, choices);
    }

    public ChoicesField(String label, String choices) {
        this(label, Arrays.asList(choices.split(",")));
    }

    public ChoicesField(String choices[]) {
        this(null, choices);
    }

    public ChoicesField(String label, String choices[]) {
        this(label, Arrays.asList(choices));
    }

    public ChoicesField(Iterable<?> list) {
        this(null, list);
    }

    public ChoicesField(String label, Iterable<?> list) {
        this(label, createList(list));
    }

    public ChoicesField(String label, Collection<String> list) {
        super(new BasicChoicesField(sanitize(list), new ButtonLayout()), 0);
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
}