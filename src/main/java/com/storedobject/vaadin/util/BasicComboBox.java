package com.storedobject.vaadin.util;

import com.storedobject.vaadin.ComboField;

import java.util.Collection;

public class BasicComboBox<T> extends ComboField<T> {

    public BasicComboBox() {
        super();
        init();
    }

    public BasicComboBox(String label) {
        super(label);
        init();
    }

    public BasicComboBox(Collection<T> items) {
        this(null, items);
    }

    public BasicComboBox(String label, Collection<T> items) {
        super(label, items);
        init();
    }

    @SafeVarargs
    public BasicComboBox(String label, T... items) {
        super(label, items);
        init();
    }

    private void init() {
        setAllowCustomValue(false);
        setPreventInvalidInput(true);
        setRequired(true);
    }
}