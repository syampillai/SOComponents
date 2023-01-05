package com.storedobject.vaadin;

import com.vaadin.flow.data.binder.HasItems;

import java.util.Arrays;
import java.util.Collection;

/**
 * Radio choice field.
 *
 * @author Syam
 */
public class RadioChoiceField extends TranslatedField<Integer, String> implements HasItems<String> {

    /**
     * Constructor.
     * @param choices Choices
     */
    public RadioChoiceField(String[] choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    public RadioChoiceField(String label, String[] choices) {
        this(label, Arrays.asList(choices));
    }

    /**
     * Constructor.
     * @param choices Choices delimited with comma
     */
    public RadioChoiceField(String choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices delimited with comma
     */
    public RadioChoiceField(String label, String choices) {
        this(label, Arrays.asList(choices.split(",")));
    }

    /**
     * Constructor.
     * @param choices Choices
     */
    public RadioChoiceField(Iterable<String> choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    @SuppressWarnings("unchecked")
    public RadioChoiceField(String label, Collection<String> choices) {
        super(new RadioField<>(choices), (f, s) -> ((RadioField<String>)f).getIndex(s), (f, i) -> ((RadioField<String>)f).getValue(i));
        setValue(0);
        setLabel(label);
    }

    /**
     * Constructor.
     * @param label Label
     * @param choices Choices
     */
    @SuppressWarnings("unchecked")
    public RadioChoiceField(String label, Iterable<String> choices) {
        super(new RadioField<>(choices), (f, s) -> ((RadioField<String>)f).getIndex(s), (f, i) -> ((RadioField<String>)f).getValue(i));
        setValue(0);
        setLabel(label);
    }

    /**
     * Set choices.
     * @param choices Choices
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setItems(Collection<String> choices) {
        ((RadioField<String>)getField()).setItems(choices);
    }


    /**
     * Display the items vertically.
     */
    public void setVertical() {
        ((RadioField<?>)getField()).setVertical();
    }

    /**
     * Display the items horizontally. (This is the default).
     */
    public void setHorizontal() {
        ((RadioField<?>)getField()).setHorizontal();
    }
}
