package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;

/**
 * A field that accepts free format text and converts it into a value of specific type. The value of the field is
 * the return value of {@link #getModelValue(String)}.
 *
 * @param <T> Value type.
 * @author Syam
 */
public abstract class FreeFormatField<T> extends CustomTextField<T> {

    private final TextField display = new TextField();
    private ItemLabelGenerator<T> displayGenerator;

    /**
     * Constructor.
     *
     * @param defaultValue Default value
     */
    protected FreeFormatField(String label, T defaultValue) {
        super(defaultValue);
        add(display);
        setItemLabelGenerator(null);
        //noinspection unchecked
        ((HasValue<?, String>)getField()).addValueChangeListener(e -> {
            display.setText(displayGenerator.apply(getModelValue(e.getValue())));
            display.setVisible(true);
            ((Component)getField()).setVisible(false);
        });
        ((Focusable<?>)getField()).addBlurListener(e -> {
            display.setVisible(true);
            ((Component)getField()).setVisible(false);
        });
        display.addFocusListener(e -> {
            boolean ro = isReadOnly();
            display.setVisible(ro);
            ((Component)getField()).setVisible(!ro);
            ((Focusable<?>)getField()).focus();
        });
        new Clickable<>(display, e -> {
            if(!isReadOnly()) {
                display.setVisible(false);
                ((Component)getField()).setVisible(true);
                ((Focusable<?>)getField()).focus();
            }
        });
        display.setWidthFull();
        display.setVisible(true);
        display.setReadOnly(true);
        ((Component)getField()).setVisible(false);
        if(label != null) {
            setLabel(label);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if(readOnly) {
            ((Component)getField()).setVisible(false);
        }
    }

    @Override
    protected final String format(T value) {
        String v = getField().getValue();
        return v == null || v.isBlank() ? displayGenerator.apply(value) : v;
    }

    /**
     * Set the function that translates the field's value into a string to display.
     *
     * @param displayGenerator Display generator.
     */
    public void setItemLabelGenerator(ItemLabelGenerator<T> displayGenerator) {
        if(displayGenerator == null) {
            ApplicationEnvironment ae = ApplicationEnvironment.get();
            if(ae == null) {
                displayGenerator = t -> t == null ? "" : t.toString();
            } else {
                displayGenerator = ae::toDisplay;
            }
        }
        this.displayGenerator = displayGenerator;
    }
}
