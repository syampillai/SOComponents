package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Box;
import com.storedobject.vaadin.Scrollable;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.shared.Registration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ListField<T> extends Div
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<ListField<T>, T>, T>, HasItems<T> {

    private Listeners listeners = new ValueChangeListeners();
    private CList<T> comboList;
    private Div text;

    public ListField(T... list) {
        this(Arrays.asList(list));
    }


    public ListField(Collection<T> items) {
        text = new Div();
        text.getStyle().set("cursor", "pointer");
        Box b = new Box(this);
        b.setStyle("background", "var(--lumo-contrast-20pct)");
        b.setBorderWidth(0);
        comboList = new CList<T>(items);
        new ElementClick(text).addClickListener(e -> {
            comboList.setVisible(!comboList.isVisible());
        });
        v(getValue());
        add(text);
        add(comboList.encloser);
        comboList.setVisible(false);
        comboList.addValueChangeListener(e -> {
            if(e.isFromClient()) {
                v(e.getValue());
                comboList.setVisible(false);
                listeners.fire(new AbstractField.ComponentValueChangeEvent(this, this, e.getOldValue(),true));
            }
        });
        new ElementClick(comboList).addClickListener(e -> comboList.setVisible(false));
    }

    private void v(T v) {
        if(v == null) {
            text.setText("");
        }
        String s = v.toString();
        text.setText(s == null ? "" : s);
    }

    @Override
    public void setItems(Collection<T> items) {
        T oldValue = getValue();
        comboList.setItems(items);
        comboList.setValue(oldValue);
        T newValue = getValue();
        if(newValue == null) {
            comboList.setFirstValue();
            newValue = getValue();
        }
        v(newValue);
        if(!Objects.equals(oldValue, newValue)) {
            listeners.fire(new AbstractField.ComponentValueChangeEvent(this, this, oldValue, false));
        }
    }

    @Override
    public void setValue(T value) {
        setValue(value, false);
    }

    private void setValue(T value, boolean fromClient) {
        T oldValue = getValue();
        comboList.setValue(oldValue);
        T newValue = getValue();
        if(newValue == null) {
            comboList.setFirstValue();
            newValue = getValue();
        }
        v(newValue);
        if(!Objects.equals(oldValue, newValue)) {
            listeners.fire(new AbstractField.ComponentValueChangeEvent(this, this, oldValue, false));
        }
    }

    public void setIndex(int newIndex) {
        setValue(comboList.getValue(newIndex));
    }

    @Override
    public T getValue() {
        return comboList.getValue();
    }

    public int getIndex() {
        return comboList.getIndex();
    }

    public T getValue(int index) {
        return comboList.getValue(index);
    }

    public int getIndex(T value) {
        return comboList.getIndex(value);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ListField<T>, T>> listener) {
        return listeners.add(listener);
    }

    public void setScrollHeight(String height, int minimumItemCount) {
        comboList.height = height;
        comboList.minimumItems = minimumItemCount;
        comboList.setVisible(comboList.isVisible());
    }

    private class CList<T> extends ComboList<T> {

        private Div encloser;
        private String height = "140px";
        private int minimumItems = 4;

        public CList(Collection<T> list) {
            super((Collection<T>)null);
            encloser = new Div();
            encloser.add(this);
            setItems(list);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if(visible && list.size() > minimumItems) {
                encloser.setHeight(height);
            } else {
                encloser.setHeight(null);
            }
        }

        @Override
        public void setItems(Collection<T> items) {
            if(items == null) {
                return;
            }
            if(items.size() > minimumItems) {
                encloser.setHeight(height);
                new Scrollable(encloser);
            } else {
                encloser.setHeight(null);
                new Scrollable(encloser, false);
            }
            super.setItems(items);
        }
    }
}