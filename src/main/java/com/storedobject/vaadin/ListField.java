package com.storedobject.vaadin;

import com.storedobject.vaadin.util.BasicComboList;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;

import java.util.Collection;
import java.util.function.Function;

/**
 * A field that shows one selected item from a list.
 * <p>By default, toString() method is used to convert the item into its displayable text unless a function is set
 * for this purpose using {@link LabelField#setLabelGenerator(Function)}.</p>
 *
 * @param <T> Value type of the field
 * @author Syam
 */
public class ListField<T> extends CustomField<T> {

    private Div container = new Div();
    private CList comboList;
    private Div text;
    private Function<T, String> labelGenerator;

    /**
     * Constructor.
     *
     * @param items List items
     */
    public ListField(Collection<T> items) {
        this(null, items);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param items List items
     */
    public ListField(String label, Collection<T> items) {
        add(container);
        text = new Div();
        text.getStyle().set("cursor", "pointer");
        Box b = new Box(container);
        b.setStyle("background", "var(--lumo-contrast-20pct)");
        b.setBorderWidth(0);
        comboList = new CList(items);
        comboList.setVisible(false);
        new ElementClick(text).addClickListener(e -> comboList.setVisible(!comboList.isVisible()));
        container.add(text);
        container.add(comboList.encloser);
        new ElementClick(comboList).addClickListener(e -> comboList.setVisible(false));
        setLabel(label);
        comboList.setFirstValue();
        v(comboList.getValue(), false);
    }

    private void v(T v, boolean fromClient) {
        String s = toString(v);
        text.setText(s == null ? "" : s);
        setModelValue(generateModelValue(), fromClient);
    }

    private String toString(T value) {
        return labelGenerator != null ? labelGenerator.apply(value) : (value == null ? "" : value.toString());
    }

    /**
     * Get index of an item.
     *
     * @param value Item for which index needs to determined
     * @return Index of the item.
     */
    public int getIndex(T value) {
        return comboList.getIndex(value);
    }

    /**
     * Get the item for the index passed.
     *
     * @param index Index
     * @return Item at the index.
     */
    public T getValue(int index) {
        return comboList.getValue(index);
    }

    /**
     * Set item list.
     *
     * @param items Item list
     */
    public void setItems(Collection<T> items) {
        T oldValue = getValue();
        comboList.setItems(items);
        comboList.setValue(oldValue);
        T newValue = getValue();
        if(newValue == null) {
            comboList.setFirstValue();
            newValue = getValue();
        }
        v(newValue, false);
    }

    /**
     * Set the scroll height of the list shown.
     * @param height Height
     * @param minimumItemCount Minimum number of items to be displayed anyway
     */
    public void setScrollHeight(String height, int minimumItemCount) {
        comboList.height = height;
        comboList.minimumItems = minimumItemCount;
        comboList.setVisible(comboList.isVisible());
    }

    /**
     * Set a label generator to convert item values into displayable labels.
     * @param labelGenerator Label generator
     */
    public void setLabelGenerator(Function<T, String> labelGenerator) {
        this.labelGenerator = labelGenerator;
    }

    @Override
    protected T generateModelValue() {
        return comboList.getValue();
    }

    @Override
    protected void setPresentationValue(T value) {
        text.setText(toString(value));
        comboList.setValue(value);
    }

    private class CList extends BasicComboList<T> {

        private Div encloser;
        private String height = "140px";
        private int minimumItems = 4;

        public CList(Collection<T> list) {
            super((Collection<T>)null);
            encloser = new Div();
            encloser.add(this);
            setItems(list);
            addValueChangeListener(e -> v(e.getValue(), e.isFromClient()));
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if(visible && list.size() > minimumItems) {
                encloser.setHeight(height);
            } else {
                encloser.setHeight(null);
            }
        }

        @Override
        public void setItems(Collection<T> items) {
            if(items == null) {
                return;
            }
            if(items.size() > minimumItems) {
                encloser.setHeight(height);
                new Scrollable(encloser);
            } else {
                encloser.setHeight(null);
                new Scrollable(encloser, false);
            }
            super.setItems(items);
        }

        @Override
        protected void setFirstValue() {
            super.setFirstValue();
        }
    }
}