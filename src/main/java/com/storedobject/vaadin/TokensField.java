package com.storedobject.vaadin;

import com.vaadin.componentfactory.multiselect.MultiComboBox;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.*;
import java.util.stream.Stream;

@CssImport(value = "./so/tokensfield/styles.css", themeFor = "vcf-multiselect-combo-box")
public class TokensField<T> extends MultiComboBox<T> implements RequiredField {

    private final Chips chips = new Chips();
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
        super(label, items == null ? new ArrayList<>() : items);
        addValueChangeListener(e -> valueChanged(e.getValue()));
        addClassNames("tokens", "tokens-no");
        getElement().appendChild(chips.getElement());
        if(itemLabelGenerator != null) {
            setItemLabelGenerator(itemLabelGenerator);
        }
    }

    private void removeValue(T item) {
        Set<T> v = new HashSet<>(getValue());
        if(v.remove(item)) {
            setValue(v);
        }
    }

    private void valueChanged(Set<T> value) {
        Chip chip;
        for(T item: value) {
            chip = chips.chips.stream().filter(c -> c.item.equals(item)).findAny().orElse(null);
            if(chip == null) {
                chips.add(item);
            }
        }
        List<Chip> toRemove = new ArrayList<>(chips.chips);
        toRemove.removeIf(c -> value.contains(c.item));
        toRemove.forEach(chips::removeChip);
        if(value.isEmpty()) {
            removeClassName("tokens-yes");
            addClassName("tokens-no");
            chips.setVisible(false);
        } else {
            removeClassName("tokens-no");
            addClassName("tokens-yes");
            chips.setVisible(true);
        }
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
        if(value1 == null || value2 == null || value1.size() != value2.size()) {
            return false;
        }
        for(T v: value1) {
            if(!value2.contains(v)) {
                return false;
            }
        }
        return true;
    }

    private class Chips extends ButtonLayout {

        private final List<Chip> chips = new ArrayList<>();

        Chips() {
            getElement().setAttribute("slot", "prefix");
            setVisible(false);
            setWidthFull();
            setGap(2);
        }

        void add(T item) {
            Chip chip = new Chip(item);
            chips.add(chip);
            add(chip);
        }

        void removeChip(Chip chip) {
            if(chip != null) {
                chips.remove(chip);
                remove(chip);
            }
        }

        void readOnly(boolean readOnly) {
            visible(!readOnly && TokensField.this.isEnabled());
        }

        void enable(boolean enabled) {
            visible(!TokensField.this.isReadOnly() && enabled);
        }

        private void visible(boolean v) {
            chips.forEach(c -> c.cross.setVisible(v));
        }
    }

    private class Chip extends Span {

        private final T item;
        private final ImageButton cross;

        Chip(T item) {
            super(toS(item));
            this.item = item;
            cross = new ImageButton("Remove", "Close", e -> removeValue(this.item));
            cross.withBox(18);
            cross.getStyle().set("margin-left", "4px").set("margin-bottom", "2px");
            getElement().appendChild(cross.getElement());
            new Box(this);
            cross.setVisible(TokensField.this.isEnabled() && !TokensField.this.isReadOnly());
        }
    }

    private String toS(T item) {
        ItemLabelGenerator<T> s = getItemLabelGenerator();
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
     * Add more items to the selection list.
     *
     * @param items Items to be added
     */
    public void addItems(Collection<T> items) {
        if(items == null || items.isEmpty()) {
            return;
        }
        addItems(items.stream());
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
        addItems(Stream.of(items));
    }

    /**
     * Add more items to the selection list.
     *
     * @param streamOfItems Items to be added
     */
    public void addItems(Stream<T> streamOfItems) {
        if(streamOfItems == null) {
            return;
        }
        List<T> items = new ArrayList<>(items());
        streamOfItems.forEach(items::add);
        setItems(items);
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
     * Select all items. The {@link #getValue()} will return all the items after invoking this method.
     */
    public void selectAll() {
        setValue(new HashSet<>(items()));
    }

    /**
     * Deselect all items. This is equivalent to setValue(null). The {@link #getValue()} will return an empty
     * set after invoking this method.
     */
    public void deselectAll() {
        setValue(null);
    }

    private Collection<T> items() {
        //noinspection unchecked
        return ((ListDataProvider<T>)getDataProvider()).getItems();
    }
}
