package com.storedobject.vaadin;

import com.storedobject.vaadin.util.CompositeField;
import com.storedobject.vaadin.util.NullField;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;

/**
 * A field that always returns its value as <code>null</code>. However, it's painted as a {@link ButtonLayout} and thus,
 * various components can be added to it.
 *
 * @author Syam
 */
public class CompoundField
        extends CompositeField.SingleField<String, CompoundField, NullField, AbstractField.ComponentValueChangeEvent<NullField, String>> {

    public CompoundField() {
        this(null);
    }

    public CompoundField(String label, Component... components) {
        super(new NullField(), null);
        add(components);
        if(label != null) {
            setLabel(label);
        }
    }

    public void add(Component... components) {
        getInnerField().add(components);
    }

    public void remove(Component... components) {
        getInnerField().remove(components);
    }
}
