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
import java.util.Objects;
import java.util.function.Function;

public class BasicListField<T> extends Div
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<BasicListField<T>, T>, T>, HasItems<T> {

    private Listeners listeners = new ValueChangeListeners();
    private CList comboList;
    private Div text;
    private Function<T, String> labelGenerator;

    @SafeVarargs
    public BasicListField(T... list) {
        this(Arrays.asList(list));
    }

    @SuppressWarnings("unchecked")
    public BasicListField(Collection<T> items) {
        text = new Div();
        text.getStyle().set("cursor", "pointer");
        Box b = new Box(this);
        b.setStyle("background", "var(--lumo-contrast-20pct)");
        b.setBorderWidth(0);
        comboList = new CList(items);
        new ElementClick(text).addClickListener(e -> comboList.setVisible(!comboList.isVisible()));
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
        String s = toString(v);
        text.setText(s == null ? "" : s);
    }

    @Override
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public void setValue(T value) {
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
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<BasicListField<T>, T>> listener) {
        return listeners.add(listener);
    }

    public void setScrollHeight(String height, int minimumItemCount) {
        comboList.height = height;
        comboList.minimumItems = minimumItemCount;
        comboList.setVisible(comboList.isVisible());
    }

    private String toString(T value) {
        return labelGenerator != null ? labelGenerator.apply(value) : (value == null ? "" : value.toString());
    }

    public void setLabelGenerator(Function<T, String> labelGenerator) {
        this.labelGenerator = labelGenerator;
    }

    private class CList extends BasicComboList<T> {

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