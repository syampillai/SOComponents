package com.storedobject.vaadin;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.binder.HasItems;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Field that enables selection of multiple items (as a Set) from a list of items.
 * (Based on the {@link MultiselectComboBox} by Goran Atanasovski).
 *
 * @param <T> Type of the item.
 * @author Syam
 */
@CssImport(value = "./so/tokens/styles.css", themeFor = "multiselect-combo-box")
public class TokensField<T> extends MultiselectComboBox<T> implements HasItems<T> {

    private Collection<T> items;

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
        super();
        setItems(items);
        setLabel(label);
        if(itemLabelGenerator != null) {
            setItemLabelGenerator(itemLabelGenerator);
        }
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
        this.items = items == null ? Collections.emptyList() : items;
        super.setItems(this.items);
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
}