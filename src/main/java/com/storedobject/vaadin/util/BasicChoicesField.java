package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Box;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;

import java.util.ArrayList;
import java.util.Collection;

public class BasicChoicesField extends CompositeField.MultiField<HasValue.ValueChangeEvent<Integer>, Integer> {

    private ArrayList<Checkbox> list = new ArrayList<>();
    private Component container;
    private Box box;

    public BasicChoicesField(Collection<String> list, HasComponents container) {
        this.container = (Component) container;
        list.forEach(item -> {
            Checkbox cb = new Checkbox(item);
            this.list.add(cb);
            container.add(cb);
        });
        box = new Box((Component)container);
        box.setReadOnly(false);
    }

    @Override
    protected Component initComponent() {
        return container;
    }

    @Override
    protected Component getContent() {
        return container;
    }

    @Override
    protected HasValue[] fieldList() {
        return list.toArray(new HasValue[list.size()]);
    }

    @Override
    protected void loadValue(Integer value) {
        int v = value, i = 1;
        for(Checkbox c: list) {
            if(c.getValue()) {
                if((v & i) == 0) {
                    c.setValue(false);
                }
            } else {
                if((v & i) > 0) {
                    c.setValue(true);
                }
            }
            i <<= 1;
        }
    }

    @Override
    protected Integer saveValue(Integer value) {
        int v = 0, i = 1;
        for(Checkbox c: list) {
            if(c.getValue()) {
                v |= i;
            }
            i <<= 1;
        }
        return v;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        box.setReadOnly(readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        box.setEnabled(enabled);
    }
}
