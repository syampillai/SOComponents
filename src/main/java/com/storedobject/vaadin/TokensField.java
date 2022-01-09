package com.storedobject.vaadin;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.HasItems;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Field that enables selection of multiple items (as a Set) from a list of items.
 *
 * @param <T> Type of the item.
 * @author Syam
 */

public class TokensField<T> extends CustomField<Set<T>> implements HasItems<T> {

    private final Chips chips = new Chips();
    private List<T> items;
    private final Combo combo = new Combo();

    /**
     * Constructor.
     */
    public TokensField() {
        this((String)null);
    }

    /**
     * Constructor.
     * @param label Label
     */
    public TokensField(String label) {
        this(label, (Collection<T>)null);
    }

    /**
     * Constructor.
     * @param items Items
     */
    public TokensField(Collection<T> items) {
        this(null, items);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items Items
     */
    public TokensField(String label, Collection<T> items) {
        this(label, items, null);
    }

    /**
     * Constructor.
     * @param itemLabelGenerator Label generator
     */
    public TokensField(ItemLabelGenerator<T> itemLabelGenerator) {
        this(null, null, itemLabelGenerator);
    }

    /**
     * Constructor.
     * @param label Label
     * @param itemLabelGenerator Label generator
     */
    public TokensField(String label, ItemLabelGenerator<T> itemLabelGenerator) {
        this(label, null, itemLabelGenerator);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items Items
     * @param itemLabelGenerator Label generator
     */
    public TokensField(String label, Collection<T> items, ItemLabelGenerator<T> itemLabelGenerator) {
        super(new HashSet<>());
        Holder holder = new Holder();
        holder.add(chips, combo);
        add(holder);
        setItems(items);
        setLabel(label);
        if(itemLabelGenerator != null) {
            setItemLabelGenerator(itemLabelGenerator);
        }
    }

    public void setItemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
        combo.setItemLabelGenerator(itemLabelGenerator);
    }

    /**
     * Add tokens to the existing set.
     *
     * @param value Tokens to be added
     */
    public void addValue(Set<T> value) {
        if(value == null || value.isEmpty()) {
            return;
        }
        setValue(new HashSet<>() {
            {
                addAll(getValue());
                addAll(value);
            }
        });
    }

    /**
     * Set a new set of items for the selection.
     *
     * @param items Items to be set
     */
    @Override
    public void setItems(Collection<T> items) {
        this.items = new ArrayList<>();
        if(items != null) {
            this.items.addAll(items);
        }
        combo.setItems(this.items);
    }

    /**
     * Add more items to the selection list.
     *
     * @param items Items to be added
     */
    public void addItems(Collection<T> items) {
        if(items == null || items.isEmpty()) {
            return;
        }
        ArrayList<T> list = new ArrayList<>(this.items);
        list.addAll(items);
        setItems(list);
    }

    /**
     * Add more items to the selection list.
     *
     * @param items Items to be added
     */
    @SafeVarargs
    public final void addItems(T... items) {
        this.addItems(Arrays.asList(items));
    }

    /**
     * Add more items to the selection list.
     *
     * @param streamOfItems Items to be added
     */
    public void addItems(Stream<T> streamOfItems) {
        this.addItems(streamOfItems.collect(Collectors.toList()));
    }

    @Override
    protected Set<T> generateModelValue() {
        Set<T> value = new HashSet<>();
        chips.chips.stream().map(c -> c.item).forEach(value::add);
        return value;
    }

    @Override
    protected void setPresentationValue(Set<T> value) {
        List<T> valueCopy = new ArrayList<>();
        if(value != null) {
            valueCopy.addAll(value);
        }
        T item;
        for(int i = 0; i < chips.chips.size(); i++) {
            item = chips.chips.get(i).item;
            if(!valueCopy.contains(item)) {
                chips.remove(item, false);
                --i;
            }
        }
        for(T v: valueCopy) {
            if(!chips.contains(v)) {
                chips.add(v, false);
            }
        }
    }

    @Override
    public void setValue(Set<T> value) {
        if(value == null) {
            value = new HashSet<>();
        }
        super.setValue(value);
    }

    public void setValue(Collection<T> value) {
        setValue(new HashSet<>(value));
    }

    private static class Holder extends VerticalLayout {

        Holder() {
            setMargin(false);
            setPadding(false);
        }
    }

    private class Combo extends ComboField<T> {

        Combo() {
            setWidthFull();
            addValueChangeListener(e -> set(e.getValue()));
        }

        private void set(T item) {
            if(item == null) {
                return;
            }
            chips.add(item, true);
        }
    }

    private class Chips extends Div {

        private final List<Chip> chips = new ArrayList<>();

        Chips() {
            setWidthFull();
        }

        void add(T item, boolean fire) {
            combo.clear();
            combo.removeItems(item);
            Chip chip = new Chip(item);
            chips.add(chip);
            add(chip);
            if(fire) {
                updateValue();
            }
        }

        void remove(T item, boolean fire) {
            Chip chip = chips.stream().filter(c -> c.item.equals(item)).findAny().orElse(null);
            if(chip != null) {
                chips.remove(chip);
                remove(chip);
                combo.addItems(item);
                if(fire) {
                    updateValue();
                }
            }
        }

        void readOnly(boolean readOnly) {
            visible(!readOnly && TokensField.this.isEnabled());
        }

        void enable(boolean enabled) {
            visible(!TokensField.this.isReadOnly() && enabled);
        }

        private void visible(boolean v) {
            combo.setVisible(v);
            chips.forEach(c -> c.cross.setVisible(v));
        }

        boolean contains(T item) {
            return chips.stream().anyMatch(c -> c.item.equals(item));
        }
    }

    private class Chip extends Span {

        private final T item;
        private final ImageButton cross;

        Chip(T item) {
            super(toS(item));
            this.item = item;
            cross = new ImageButton("Remove", "Close", e -> chips.remove(this.item, true));
            cross.withBox(18);
            cross.getStyle().set("margin-left", "4px").set("margin-bottom", "2px");
            getElement().appendChild(cross.getElement());
            new Box(this);
            cross.setVisible(TokensField.this.isEnabled() && !TokensField.this.isReadOnly());
        }
    }

    private String toS(T item) {
        ItemLabelGenerator<T> s = combo.getItemLabelGenerator();
        if(s != null) {
            return s.apply(item);
        }
        ApplicationEnvironment ae = ApplicationEnvironment.get();
        return ae == null ? item.toString() : ae.toDisplay(item);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        chips.readOnly(readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        chips.enable(enabled);
    }

    @Override
    public boolean isEmpty() {
        Set<T> v = getValue();
        return v == null || v.isEmpty();
    }

    @Override
    protected boolean valueEquals(Set<T> value1, Set<T> value2) {
        if(Objects.equals(value1, value2)) {
            return true;
        }
        if(value1.size() != value2.size()) {
            return false;
        }
        for(T v: value1) {
            if(!value2.contains(v)) {
                return false;
            }
        }
        return true;
    }
}
