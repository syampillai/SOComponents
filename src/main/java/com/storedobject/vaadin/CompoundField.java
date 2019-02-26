package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;

/**
 * A field that always returns its value as <code>null</code>. However, it's painted as a {@link ButtonLayout} and thus,
 * various components can be added to it.
 *
 * @author Syam
 */
public class CompoundField extends CustomField<String> {

    private ButtonLayout layout = new ButtonLayout();

    public CompoundField() {
        this(null);
    }

    public CompoundField(String label, Component... components) {
        super.add(layout);
        add(components);
        setLabel(label);
    }

    @Override
    protected String generateModelValue() {
        return null;
    }

    @Override
    protected void setPresentationValue(String value) {
    }

    public void add(Component... components) {
        layout.add(components);
    }

    public void remove(Component... components) {
        layout.remove(components);
    }
}
