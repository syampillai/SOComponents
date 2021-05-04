package com.storedobject.vaadin;

import com.vaadin.flow.component.combobox.ComboBox;

import java.util.Map;

/**
 * A combo field where the values are set as a "map" (key-value pairs). Keys in the map are the actual
 * field-values and map values are used for displaying the combo.
 * <p>Please note that if you update the map, the field is not updated unless you call the {@link #refresh()} method.</p>
 *
 * @param <T> Type of the field-value.
 * @author Syam
 */
public class MapComboField<T> extends ComboBox<T> implements SpellCheck {

    private final Map<T, ?> map;

    /**
     * Constructor.
     *
     * @param map Map of key-value pairs.
     */
    public MapComboField(Map<T, ?> map) {
        this(null, map);
    }

    /**
     * Constructor.
     *
     * @param label Label for the field.
     * @param map Map of key-value pairs.
     */
    public MapComboField(String label, Map<T, ?> map) {
        this.map = map;
        setItems(map.keySet());
        setItemLabelGenerator(this::toDisplay);
        if(label != null) {
            setLabel(label);
        }
        setPlaceholder("Select");
    }

    @Override
    public void setValue(T value) {
        if(value != null && map.get(value) == null) {
            value = null;
        }
        super.setValue(value);
    }

    /**
     * Refresh the combo. This should be called to refresh the combo if the map is updated from outside.
     */
    public void refresh() {
        setItems(map.keySet());
    }

    private String toDisplay(T key) {
        Object value = map.get(key);
        ApplicationEnvironment ae = ApplicationEnvironment.get();
        return ae == null ? (value == null ? "" : value.toString()) : ae.toDisplay(value);
    }

    /**
     * Get the map.
     *
     * @return The map.
     */
    public Map<T, ?> getMap() {
        return map;
    }
}
