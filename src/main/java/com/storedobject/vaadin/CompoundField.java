package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.customfield.CustomField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Add components.
     *
     * @param components Components to be added
     */
    public void add(Component... components) {
        layout.add(components);
    }

    /**
     * Remove components.
     *
     * @param components Components to be removed
     */
    public void remove(Component... components) {
        layout.remove(components);
    }

    /**
     * Get a field from this.
     *
     * @param index Index of the field to be retrieved
     * @return Field is exists, otherwise <code>null</code>.
     */
    public HasValue<?, ?> getField(int index) {
        if(index < 0) {
            return null;
        }
        Object[] result = getField(index, 0, layout);
        return (HasValue<?, ?>) (result == null ? null : result[1]);
    }

    private static Object[] getField(int index, int startIndex, HasComponents container) {
        List<Component> components =((Component)container).getChildren().collect(Collectors.toList());
        Object[] result;
        for(Component c: components) {
            if(c.isVisible() && c instanceof HasValue) {
                if(index == startIndex) {
                    return new Object[] { startIndex, c };
                }
                ++startIndex;
            }
            if(c instanceof HasComponents) {
                result = getField(index, startIndex, (HasComponents) c);
                if(result != null) {
                    if((Integer) result[0] == startIndex) {
                        return result;
                    }
                    startIndex = (Integer) result[0];
                }
            }
        }
        return null;
    }
}