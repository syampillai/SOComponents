package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Box;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BasicLabelField<T> extends Div
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<BasicLabelField<T>, T>, T> {

    private List<T> items;
    private int index = -1;
    private Listeners listeners = new ValueChangeListeners();
    private Function<T, String> labelGenerator;

    public BasicLabelField(List<T> items) {
        Box b = new Box(this);
        b.setStyle("background", "var(--lumo-contrast-20pct)");
        b.setStyle("cursor", "pointer");
        b.setBorderWidth(0);
        ElementClick click = new ElementClick(this);
        click.addClickListener(e -> {
            setIndex(index + 1, true);
            T v = getValue();
            setText(toString(v));
        });
        setItems(items);
    }

    @SuppressWarnings("unchecked")
    public void setItems(List<T> items) {
        T oldValue = getValue();
        this.items = items;
        T newValue = getValue();
        if(newValue == null && index < 0) {
            index = 0;
            newValue = getValue();
        }
        setText(toString(newValue));
        if(!Objects.equals(oldValue, newValue)) {
            listeners.fire(new AbstractField.ComponentValueChangeEvent(this, this, oldValue, false));
        }
    }

    @Override
    public void setValue(T value) {
        if(items == null || items.isEmpty()) {
            return;
        }
        setIndex(items.indexOf(value), false);
    }

    public void setIndex(int newIndex) {
        setIndex(newIndex, false);
    }

    @SuppressWarnings("unchecked")
    private void setIndex(int newIndex, boolean fromClient) {
        if(index == newIndex) {
            return;
        }
        if(items == null || items.isEmpty()) {
            return;
        }
        if(newIndex >= items.size()) {
            newIndex = 0;
        }
        int oldIndex = index;
        if(newIndex != oldIndex) {
            T oldValue = getValue();
            index = newIndex;
            setText(toString(items.get(newIndex)));
            listeners.fire(new AbstractField.ComponentValueChangeEvent(this, this, oldValue, fromClient));
        }
    }

    @Override
    public T getValue() {
        if(getIndex() < 0) {
            return null;
        }
        return items.get(index);
    }

    public int getIndex() {
        if((index < 0 || items == null || items.isEmpty())) {
            index = -1;
            return index;
        }
        if(index >= items.size()) {
            index = items.size() % index;
        }
        return index;
    }

    public T getValue(int index) {
        if(index < 0 || items == null || items.isEmpty() || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    public int getIndex(T value) {
        if(value == null || items == null || items.isEmpty()) {
            return -1;
        }
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<BasicLabelField<T>, T>> listener) {
        return listeners.add(listener);
    }

    private String toString(T value) {
        return labelGenerator != null ? labelGenerator.apply(value) : (value == null ? "" : value.toString());
    }

    public void setLabelGenerator(Function<T, String> labelGenerator) {
        this.labelGenerator = labelGenerator;
    }
}