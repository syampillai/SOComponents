package com.storedobject.vaadin;

import com.storedobject.vaadin.util.SelectedItemsChangedEvent;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.*;

/**
 * Token Field is a multi-select "Combobox".
 * <p>This is written as a Vaadin Flow wrapper around the Web Component:
 * <a href="https://github.com/gatanaso/multiselect-combo-box" target="_blank">Multi-select Combobox</a>
 * (Author: Goran Gatanaso)</p>
 *
 * @param <T> Value type
 * @author Syam
 */
@Tag("multiselect-combo-box")
@HtmlImport("bower_components/multiselect-combo-box/multiselect-combo-box.html")
public class TokenField<T> extends AbstractField<TokenField<T>, Set<T>> implements HasItems<T> {

    private static final PropertyDescriptor<String, String> labelProperty =
            PropertyDescriptors.propertyWithDefault("label", "");
    private static final PropertyDescriptor<String, String> placeholderProperty =
            PropertyDescriptors.propertyWithDefault("placeholder", "");
    private static final PropertyDescriptor<String, String> itemLabelPathProperty =
            PropertyDescriptors.propertyWithDefault("itemLabelPath", "");
    private static final PropertyDescriptor<String, String> itemValuePathProperty =
            PropertyDescriptors.propertyWithDefault("itemValuePath", "");
    private Set<T> selectedItems = new HashSet<>();
    private ArrayList<T> items;
    private ItemLabelGenerator<T> itemLabelGenerator;
    private ArrayList<ValueChangeListener<? super ComponentValueChangeEvent<TokenField<T>, Set<T>>>> listeners;

    /**
     * Constructor.
     */
    public TokenField() {
        this((String)null);
    }

    /**
     * Constructor.
     * @param label Label
     */
    public TokenField(String label) {
        this(label, (Collection<T>)null);
    }

    /**
     * Constructor.
     * @param items Items
     */
    public TokenField(Collection<T> items) {
        this(null, items);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items Items
     */
    public TokenField(String label, Collection<T> items) {
        this(label, items, null);
    }

    /**
     * Constructor.
     * @param itemLabelGenerator Label generator
     */
    public TokenField(ItemLabelGenerator<T> itemLabelGenerator) {
        this(null, null, itemLabelGenerator);
    }

    /**
     * Constructor.
     * @param label Label
     * @param itemLabelGenerator Label generator
     */
    public TokenField(String label, ItemLabelGenerator<T> itemLabelGenerator) {
        this(label, null, itemLabelGenerator);
    }

    /**
     * Constructor.
     * @param label Label
     * @param items Items
     * @param itemLabelGenerator Label generator
     */
    public TokenField(String label, Collection<T> items, ItemLabelGenerator<T> itemLabelGenerator) {
        super(Collections.emptySet());
        setItemLabelGenerator(itemLabelGenerator);
        getElement().addSynchronizedProperty("title");
        setItemLabelGenerator(null);
        itemLabelPathProperty.set(this, "itemLabelPath");
        itemValuePathProperty.set(this, "itemValuePath");
        addListener(SelectedItemsChangedEvent.class, this::fireChange);
        setItems(items);
        setLabel(label);
    }

    /**
     * Get the placeholder to display.
     *
     * @return Placeholder.
     */
    public String getPlaceholder() {
        return placeholderProperty.get(this);
    }

    /**
     * Set the placeholder to display.
     *
     * @param placeholder Placeholder
     */
    public void setPlaceholder(final String placeholder) {
        placeholderProperty.set(this, placeholder);
    }

    /**
     * Get the label.
     * @return Label.
     */
    public String getLabel() {
        return labelProperty.get(this);
    }

    /**
     * Set the label.
     * @param label Label
     */
    public void setLabel(final String label) {
        labelProperty.set(this, label == null ? "" : label);
    }

    /**
     * Set items.
     * @param items Items
     */
    @Override
    public void setItems(Collection<T> items) {
        if(items == null) {
            items = Collections.emptyList();
        }
        final Iterator<T> it = items.iterator();
        final JsonArray jsonItems = Json.createArray();
        this.items = new ArrayList<>(items.size());
        int n = 0;
        while (it.hasNext()) {
            final JsonObject object = Json.createObject();
            object.put("itemValuePath", n);
            final T item = it.next();
            object.put("itemLabelPath", itemLabelGenerator.apply(item));
            jsonItems.set(n, object);
            this.items.add(item);
            n++;
        }
        getElement().setPropertyJson("items", jsonItems);
    }

    /**
     * Set label generator for the items. If no "label generator" is set, items {@link T#toString()} is used to
     * generate the label.
     * @param itemLabelGenerator Label generator.
     */
    public void setItemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
        this.itemLabelGenerator = itemLabelGenerator == null ? String::valueOf : itemLabelGenerator;
    }

    /**
     * For internal use only. Does nothing.
     * @param value Value
     */
    @Override
    protected void setPresentationValue(Set<T> value) {
    }

    /**
     * Set the current value.
     * @param value Value to set
     */
    @SafeVarargs
    public final void setValue(T... value) {
        setValue(Arrays.asList(value));
    }

    /**
     * Set the current value.
     * @param value Value to set
     */
    public void setValue(Collection<T> value) {
        if(value == null) {
            clear();
            return;
        }
        Set<T> v = new HashSet<>(value);
        setValue(v);
    }

    /**
     * Set the current value.
     * @param value Value to set
     */
    @Override
    public void setValue(Set<T> value) {
        System.err.println("Here!!");
        Set<T> oldValue = selectedItems;
        selectedItems = new HashSet<>(value);
        fireListeners(oldValue, false);
        final Iterator<T> it = selectedItems.iterator();
        final JsonArray jsonItems = Json.createArray();
        int i = 0, n;
        T item;
        while (it.hasNext()) {
            item = it.next();
            n = items.indexOf(item);
            if(n >= 0) {
                final JsonObject object = Json.createObject();
                object.put("itemValuePath", n);
                object.put("itemLabelPath", itemLabelGenerator.apply(item));
                jsonItems.set(i, object);
                ++i;
            }
        }
        getElement().setPropertyJson("selectedItems", jsonItems);
    }

    /**
     * Get the current value.
     *
     * @return Current value.
     */
    @Override
    public Set<T> getValue() {
        return Collections.unmodifiableSet(selectedItems);
    }

    /**
     * Check whether the current value is empty or not.
     *
     * @return True or false.
     */
    @Override
    public boolean isEmpty() {
        return selectedItems.size() == 0;
    }

    /**
     * Get the empty value.
     *
     * @return An empty collection.
     */
    @Override
    public Set<T> getEmptyValue() {
        return Collections.emptySet();
    }

    /**
     * Add a "value change listener".
     * @param listener Lisener to add.
     *
     * @return Registration for the listener.
     */
    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ComponentValueChangeEvent<TokenField<T>, Set<T>>> listener) {
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    /**
     * Checks if two values are equal.
     * @param value1 Value 1
     * @param value2 Value 2
     *
     * @return True if both values contain the same elements.
     */
    @Override
    protected boolean valueEquals(Set<T> value1, Set<T> value2) {
        if(value1 == null && value2 == null) {
            return true;
        }
        if(value1 == null || value2 == null) {
            return false;
        }
        if(value1.isEmpty() && value2.isEmpty()) {
            return true;
        }
        return value1.containsAll(value2) && value2.containsAll(value1);
    }

    private void fireChange(SelectedItemsChangedEvent event) {
        Set<T> newValue = new HashSet<>();
        T v;
        JsonObject jsonItem;
        int key;
        for (int i = 0; i < event.getNewItems().length(); i++) {
            jsonItem = event.getNewItems().getObject(i);
            key = Double.valueOf(jsonItem.getNumber("itemValuePath")).intValue();
            v = items.get(key);
            if(v != null) {
                newValue.add(v);
            }
        }
        Set<T> oldValue = selectedItems;
        selectedItems = newValue;
        fireListeners(oldValue, event.isFromClient());
    }

    private void fireListeners(Set<T> oldValue, boolean fromClient){
        if(listeners == null || valueEquals(oldValue, selectedItems)) {
            return;
        }
        ComponentValueChangeEvent<TokenField<T>, Set<T>> vc;
        vc = new ComponentValueChangeEvent<>(this, this, oldValue, fromClient);
        listeners.forEach(l -> l.valueChanged(vc));
    }
}