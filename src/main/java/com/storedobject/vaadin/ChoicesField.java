package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A field that allows you to select multiple values from a fixed list of Strings. The value is returned as
 * a bit pattern with its positional values set for the selected items. The first item in the list uses the least significant bit.
 *
 * @author Syam
 */
public class ChoicesField extends CustomField<Integer> {

    private static final Integer ZERO = 0;
    private HasComponents container;
    private ArrayList<Checkbox> list = new ArrayList<>();
    private int valueMask = 0;
    private RadioChoiceField fullSelect;

    /**
     * Constructor.
     *
     * @param choices Choices delimited by comma
     */
    public ChoicesField(String choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param choices Choices delimited by comma
     */
    public ChoicesField(String label, String choices) {
        this(label, Arrays.asList(choices.split(",")));
    }

    /**
     * Constructor.
     *
     * @param choices Choices
     */
    public ChoicesField(String[] choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param choices Choices
     */
    public ChoicesField(String label, String[] choices) {
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
     *
     * @param label Label
     * @param choices Choices
     */
    public ChoicesField(String label, Iterable<?> choices) {
        this(label, createList(choices));
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param choices Choices
     */
    public ChoicesField(String label, Collection<String> choices) {
        super(ZERO);
        sanitize(choices).forEach(item -> {
            Checkbox cb = new Checkbox(item);
            this.list.add(cb);
        });
        createGrid(choices.size() > 4 ? 2 : 0, createContainer());
        addValueChangeListener(e -> checkMask(e.getValue()));
        setValue(ZERO);
        setLabel(label);
    }

    private void checkMask(int newValue) {
        if((newValue & valueMask) != valueMask) {
            setValue(newValue | valueMask);
        }
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

    /**
     * Create the container for the check boxes to be shown.
     *
     * @return Default implementation creates an appropriate container based on the number of items.
     */
    protected HasComponents createContainer() {
        return null;
    }

    private void createGrid(int columns, HasComponents c) {
        fullSelect = null;
        if(c != null) {
            container = c;
        } else {
            if (columns > 0) {
                GridLayout layout = new GridLayout(columns);
                if (list.size() > 4) {
                    fullSelect = new RadioChoiceField(new String[]{"All", "None"});
                    fullSelect.setValue(1);
                    fullSelect.addValueChangeListener(e -> {
                        if(!isReadOnly()) {
                            setValue(e.getValue() == 0 ? 0xFFFF : 0);
                        }
                    });
                    Box b = new Box(fullSelect);
                    b.setPadding(0);
                    b.setBorderWidth("0px 0px 2px 0px");
                    layout.add(fullSelect);
                    layout.setColumnSpan(fullSelect, columns);
                    layout.justify(fullSelect, GridLayout.Position.CENTER);
                }
                container = layout;
            } else {
                container = new ButtonLayout();
            }
            Box box = new Box((Component) container);
            box.setPadding(0);
        }
        list.forEach(container::add);
        add((Component)container);
    }

    /**
     * Set number of columns to show the check boxes. Setting a value to zero will show it in a single row.
     *
     * @param columns Number of columns
     */
    public void setColumns(int columns) {
        if(columns > list.size()) {
            columns = list.size();
        }
        if(columns > 6) {
            columns = 6;
        }
        this.container.removeAll();
        remove((Component)this.container);
        createGrid(columns, null);
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

    /**
     * A "mask" allows one to fix certain bits in the value of the field. For example, if the mask is set to
     * 5 (bit pattern 101), the zeroth and the third bits from the right will be always set to 1.
     *
     * @param valueMask Mask to set.
     */
    public void setValueMask(int valueMask) {
        this.valueMask = valueMask;
        checkMask(getValue());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if(fullSelect != null) {
            fullSelect.setReadOnly(readOnly);
        }
    }
}