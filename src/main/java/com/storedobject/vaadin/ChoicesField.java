package com.storedobject.vaadin;

import com.storedobject.vaadin.util.CompositeField;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ChoicesField extends CompositeField<Integer, ChoicesField, HasValue.ValueChangeEvent<Integer>, com.storedobject.vaadin.util.ChoicesField> {

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
        super(new com.storedobject.vaadin.util.ChoicesField(list, container()), 0);
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

    private static HasComponents container() {
        FlexLayout c = new FlexLayout();
        new Box(c);
        return c;
    }
}