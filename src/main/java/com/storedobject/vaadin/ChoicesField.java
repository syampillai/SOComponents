package com.storedobject.vaadin;

import com.vaadin.flow.component.checkbox.Checkbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A field that allows you select mutliple values from a fixed list of Strings. The value is returned as
 * a bit pattern with its positional values set for the selected items. The first item in the list uses the least significant bit.
 * @author Syam
 */
public class ChoicesField extends CustomField<Integer> {

    private static final Integer ZERO = 0;
    private ArrayList<Checkbox> list = new ArrayList<>();
    private Box box;

    /**
     * Constructor.
     * @param choices Choices delimited by comma
     */
    public ChoicesField(String choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices delimited by comma
     */
    public ChoicesField(String label, String choices) {
        this(label, Arrays.asList(choices.split(",")));
    }

    /**
     * Constructor.
     * @param choices Choices
     */
    public ChoicesField(String choices[]) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    public ChoicesField(String label, String choices[]) {
        this(label, Arrays.asList(choices));
    }

    /**
     * Constructor.
     * @param choices Choices
     */
    public ChoicesField(Iterable<?> choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    public ChoicesField(String label, Iterable<?> choices) {
        this(label, createList(choices));
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    public ChoicesField(String label, Collection<String> choices) {
        super(ZERO);
        ButtonLayout container = new ButtonLayout();
        sanitize(choices).forEach(item -> {
            Checkbox cb = new Checkbox(item);
            this.list.add(cb);
            container.add(cb);
        });
        box = new Box(container);
        box.setReadOnly(false);
        add(container);
        setValue(ZERO);
        setLabel(label);
    }

    private static Collection<String> createList(Iterable<?> list) {
        ArrayList<String> a = new ArrayList<>();
        list.forEach(item -> {
            String s = null;
            if(item != null) {
                s = item.toString();
            }
            if(s != null) {
                a.add(s.trim());
            }
        });
        return a;
    }

    private static Collection<String> sanitize(Collection<String> collection) {
        if(!(collection instanceof List)) {
            return collection;
        }
        List<String> list = (List<String>) collection;
        String item;
        for(int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if(item.isEmpty()) {
                list.set(i, " ");
            }
        }
        return list;
    }

    @Override
    protected Integer generateModelValue() {
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
    protected void setPresentationValue(Integer value) {
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