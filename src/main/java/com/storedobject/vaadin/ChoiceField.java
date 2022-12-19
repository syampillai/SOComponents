package com.storedobject.vaadin;

import com.vaadin.flow.component.select.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A {@link com.vaadin.flow.component.combobox.ComboBox} style field with an {@link Integer} value type.
 *
 * @author Syam
 */
public class ChoiceField extends TranslatedField<Integer, String> implements ValueRequired {

    /**
     * Constructor.
     *
     * @param choices Choices
     */
    public ChoiceField(String choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     *
     * @param label   Label
     * @param choices Choices
     */
    public ChoiceField(String label, String choices) {
        this(label, Arrays.asList(choices.split(",")));
    }

    /**
     * Constructor.
     *
     * @param choices Choices
     */
    public ChoiceField(String[] choices) {
        this(null, choices);
    }

    /**
     * Constructor.
     *
     * @param label   Label
     * @param choices Choices
     */
    public ChoiceField(String label, String[] choices) {
        this(label, Arrays.asList(choices));
    }

    /**
     * Constructor.
     *
     * @param list Choices
     */
    public ChoiceField(Iterable<?> list) {
        this(null, list);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param list  Choices
     */
    public ChoiceField(String label, Iterable<?> list) {
        this(label, createList(list));
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param list  Choices
     */
    @SuppressWarnings("unchecked")
    public ChoiceField(String label, Collection<String> list) {
        super(createField(list), (f, s) -> ((ListField<String>) f).getIndex(s),
                (f, v) -> v == null ? null : ((ListField<String>) f).getValue(v), null);
        setPlaceholder("Select");
        setValue(0);
        setLabel(label);
    }

    private static ListField<String> createField(Collection<String> list) {
        ListField<String> field = new ListField<>(sanitize(list));
        field.setRequired(true);
        return field;
    }

    private static Collection<String> createList(Iterable<?> list) {
        ArrayList<String> a = new ArrayList<>();
        list.forEach(item -> {
            String s = null;
            if (item != null) {
                s = item.toString();
            }
            if (s != null) {
                a.add(s.trim());
            }
        });
        return a;
    }

    private static Collection<String> sanitize(Collection<String> collection) {
        if (!(collection instanceof List<String> list)) {
            return collection;
        }
        String item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.isEmpty()) {
                list.set(i, " ");
            }
        }
        return list;
    }

    /**
     * Get the current choice.
     *
     * @return Current choice.
     */
    public String getChoice() {
        return getField().getValue();
    }

    /**
     * Set the choice.
     *
     * @param value Choice to be set
     */
    public void setChoice(String value) {
        getField().setValue(value);
    }

    /**
     * Replace a choice at a specified index. (If the index is outside the range, it will not be replaced).
     *
     * @param index  Index
     * @param choice New choice
     */
    public void setChoice(int index, String choice) {
        //noinspection unchecked
        ((ListField<String>) getField()).setItem(index, choice);
    }

    /**
     * Set choices.
     *
     * @param choices Choices
     */
    public void setChoices(Collection<String> choices) {
        int v = getValue();
        //noinspection unchecked
        ListField<String> f = (ListField<String>) getField();
        f.setItems(choices);
        f.setValue(f.getValue(v));
    }

    /**
     * Set choices.
     *
     * @param choices Choices
     */
    public void setChoices(Iterable<String> choices) {
        int v = getValue();
        //noinspection unchecked
        ListField<String> f = (ListField<String>) getField();
        f.setItems(choices);
        f.setValue(f.getValue(v));
    }

    /**
     * Set choices.
     *
     * @param choices Choices
     */
    public void setChoices(String[] choices) {
        int v = getValue();
        //noinspection unchecked
        ListField<String> f = (ListField<String>) getField();
        f.setItems(choices);
        f.setValue(f.getValue(v));
    }

    /**
     * Set placeholder for this field.
     *
     * @param placeholder Placeholder.
     */
    public void setPlaceholder(String placeholder) {
        ((Select<?>)getField()).setPlaceholder(placeholder);
    }

    /**
     * Get the placeholder of this field.
     * @return Placeholder.
     */
    public String getPlaceholder() {
        return ((Select<?>)getField()).getPlaceholder();
    }

    @Override
    public void setRequired(boolean required) {
        ((ListField<?>) getField()).setRequired(required);
    }

    @Override
    public boolean isRequired() {
        return ((ListField<?>) getField()).isRequired();
    }

    @Override
    public void setValue(Integer value) {
        if(value == null && isRequired()) {
            value = 0;
        }
        super.setValue(value);
    }
}