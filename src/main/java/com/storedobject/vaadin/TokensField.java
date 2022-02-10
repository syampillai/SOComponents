package com.storedobject.vaadin;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.HasItems;

import java.util.*;
import java.util.stream.Stream;

/**
 * Field that enables selection of multiple items (as a Set) from a list of items.
 *
 * @param <T> Type of the item.
 * @author Syam
 */

public class TokensField<T> extends CustomField<Set<T>> implements HasItems<T>, RequiredField, ValueRequired {

    private final Chips chips = new Chips();
    private List<T> items;
    private final Combo combo = new Combo();
    private boolean required;

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
        Set<T> v = new HashSet<>();
        v.addAll(getValue());
        v.addAll(value);
        setValue(v);
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
        combo.setItems(new ArrayList<>(this.items));
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
        this.items.addAll(items);
        combo.setItems(new ArrayList<>(this.items));
    }

    /**
     * Add more items to the selection list.
     *
     * @param items Items to be added
     */
    @SafeVarargs
    public final void addItems(T... items) {
        if(items == null || items.length == 0) {
            return;
        }
        this.items.addAll(Arrays.asList(items));
        combo.setItems(new ArrayList<>(this.items));
    }

    /**
     * Add more items to the selection list.
     *
     * @param streamOfItems Items to be added
     */
    public void addItems(Stream<T> streamOfItems) {
        if(items == null || items.isEmpty()) {
            return;
        }
        streamOfItems.forEach(this.items::add);
        combo.setItems(new ArrayList<>(this.items));
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

    /**
     * Set this value is required or not.
     *
     * @param required True/false.
     */
    @Override
    public void setRequired(boolean required) {
        setRequiredIndicatorVisible(required);
        this.required = required;
    }

    /**
     * Whether this value is required or not.
     *
     * @return True/false.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Select all items. The {@link #getValue()} will return all the items after invoking this method.
     */
    public void selectAll() {
        setValue(new HashSet<>(items));
    }

    /**
     * Deselect all items. This is equivalent to setValue(null). The {@link #getValue()} will return an empty
     * set after invoking this method.
     */
    public void deselectAll() {
        setValue(null);
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
            Chip chip = new Chip(item);
            chips.add(chip);
            refreshCombo();
            add(chip);
            if(fire) {
                TokensField.this.updateValue();
            }
        }

        void remove(T item, boolean fire) {
            Chip chip = chips.stream().filter(c -> c.item.equals(item)).findAny().orElse(null);
            if(chip != null) {
                chips.remove(chip);
                remove(chip);
                refreshCombo();
                if(fire) {
                    TokensField.this.updateValue();
                }
            }
        }

        private void refreshCombo() {
            combo.clear();
            List<T> items = new ArrayList<>(TokensField.this.items);
            chips.forEach(c -> items.remove(c.item));
            combo.setItems(items);
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

    /**
     * Set the placeholder text to be displayed while editing the tokens.
     *
     * @param placeHolder Placeholder text.
     */
    public void setPlaceholder(String placeHolder) {
        combo.setPlaceholder(placeHolder);
    }

    @Override
    public boolean isInvalid() {
        return required && isEmpty();
    }
}
