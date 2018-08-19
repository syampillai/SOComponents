package com.storedobject.vaadin.util;

import com.storedobject.vaadin.ComboField;

import java.util.Collection;

public class ComboBox<T> extends ComboField<T> {

    public ComboBox() {
        super();
        init();
    }

    public ComboBox(String label) {
        super(label);
        init();
    }

    public ComboBox(Collection<T> items) {
        this(null, items);
    }

    public ComboBox(String label, Collection<T> items) {
        super(label, items);
        init();
    }

    public ComboBox(String label, T... items) {
        super(label, items);
        init();
    }

    private void init() {
        setAllowCustomValue(false);
        setPreventInvalidInput(true);
        setRequired(true);
    }
}