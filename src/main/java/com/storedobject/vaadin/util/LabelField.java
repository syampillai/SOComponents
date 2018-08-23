package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Box;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LabelField<T> extends Div
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<LabelField<T>, T>, T> {

    private List<T> items;
    private int index = -1;
    private ElementClick click;
    private Listeners listeners = new ValueChangeListeners();

    public LabelField(List<T> items) {
        Box b = new Box(this);
        b.setStyle("background", "var(--lumo-contrast-20pct)");
        b.setStyle("cursor", "pointer");
        b.setBorderWidth(0);
        click = new ElementClick(this);
        click.addClickListener(e -> {
            setIndex(index + 1, true);
            T v = getValue();
            setText(v == null ? "" : v.toString());
        });
        setItems(items);
    }

    public void setItems(List<T> items) {
        T oldValue = getValue();
        this.items = items;
        T newValue = getValue();
        if(newValue == null && index < 0) {
            index = 0;
            newValue = getValue();
        }
        setText(newValue == null ? "" : newValue.toString());
        if(!Objects.equals(oldValue, newValue)) {
            listeners.fire(new AbstractField.ComponentValueChangeEvent(this, this, oldValue, false));
        }
    }

    @Override
    public void setValue(T value) {
        setValue(value, false);
    }

    private void setValue(T value, boolean fromClient) {
        if(items == null || items.isEmpty()) {
            return;
        }
        setIndex(items.indexOf(value), fromClient);
    }

    public void setIndex(int newIndex) {
        setIndex(newIndex, false);
    }

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
            setText(items.get(newIndex).toString());
            listeners.fire(new AbstractField.ComponentValueChangeEvent(this, this, oldValue, fromClient));
        }
    }

    @Override
    public T getValue() {
        getIndex();
        if(index < 0) {
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
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<LabelField<T>, T>> listener) {
        return listeners.add(listener);
    }
}