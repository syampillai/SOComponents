package com.storedobject.vaadin.util;

import com.storedobject.vaadin.ComboField;

import java.util.Collection;

public class ComboBox<T> extends ComboField<T> {

    private T emptyValue;

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
        addSelectedItemChangeListener(e -> {
           if(e.isFromClient()) {
               if(getValue().equals(getEmptyValue())) {
                   setValue(getEmptyValue());
               }
           }
        });
    }

    @Override
    public void setValue(T value) {
        if(emptyValue == null && value != null) {
            setEmptyValue(value);
        }
        super.setValue(value);
    }

    @Override
    public T getValue() {
        T v = super.getValue();
        if(v == null) {
            v = getEmptyValue();
        }
        return v;
    }

    public void setEmptyValue(T emptyValue) {
        this.emptyValue = emptyValue;
    }

    @Override
    public T getEmptyValue() {
        return emptyValue;
    }
}
