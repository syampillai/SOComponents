package com.storedobject.vaadin;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.textfield.TextField;

/**
 * A field that accepts free format text and converts it into a value of specific type. The value of the field is
 * the return value of {@link #getModelValue(String)}.
 *
 * @param <T> Value type.
 * @author Syam
 */
public abstract class FreeFormatField<T> extends CustomTextField<T> {

    private ItemLabelGenerator<T> displayGenerator;

    /**
     * Constructor.
     *
     * @param label Label.
     * @param defaultValue Default value.
     */
    protected FreeFormatField(String label, T defaultValue) {
        this(label, defaultValue, null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param defaultValue Default value.
     * @param displayComponent Display component to use. The display component should be a {@link Component}
     *                         and it should implement {@link Focusable} and {@link HasSize}.
     *                         (If passed null, {@link TextField} will be used).
     */
    protected FreeFormatField(String label, T defaultValue, HasValue<?, String> displayComponent) {
        super(defaultValue);
        HasValue<?, String> display = displayComponent == null ? new TextField() : displayComponent;
        add((Component) display);
        setItemLabelGenerator(null);
        //noinspection unchecked
        ((HasValue<?, String>)getField()).addValueChangeListener(e -> {
            display.setValue(displayGenerator.apply(getModelValue(e.getValue())));
            ((Component) display).setVisible(true);
            ((Component)getField()).setVisible(false);
        });
        ((Focusable<?>)getField()).addBlurListener(e -> {
            ((Component) display).setVisible(true);
            ((Component)getField()).setVisible(false);
        });
        ((Focusable<?>)display).addFocusListener(e -> {
            boolean ro = isReadOnly();
            ((Component) display).setVisible(ro);
            ((Component)getField()).setVisible(!ro);
            ((Focusable<?>)getField()).focus();
        });
        new Clickable<>((Component) display, e -> {
            if(!isReadOnly()) {
                ((Component) display).setVisible(false);
                ((Component)getField()).setVisible(true);
                ((Focusable<?>)getField()).focus();
            }
        });
        ((HasSize)display).setWidthFull();
        ((Component) display).setVisible(true);
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
    public void setValue(T value) {
        if(value == null) {
            value = getEmptyValue();
        }
        getField().setValue(displayGenerator.apply(value));
        super.setValue(value);
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
