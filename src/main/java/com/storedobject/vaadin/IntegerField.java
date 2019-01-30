package com.storedobject.vaadin;

import com.storedobject.vaadin.util.NumericField;

/**
 * Field to edit {@link Integer}.
 *
 * @author Syam
 */
public class IntegerField extends NumericField<Integer> {

    private static final Integer ZERO = 0;

    public IntegerField() {
        this(null, null);
    }

    public IntegerField(String label) {
        this(label, null);
    }

    public IntegerField(Integer initialValue) {
        this(null, initialValue);
    }

    public IntegerField(String label, Integer initialValue) {
        this(label, initialValue, 18);
    }

    public IntegerField(Integer initialValue, int width) {
        this(null, initialValue, width);
    }

    public IntegerField(String label, Integer initialValue, int width) {
        this(label, initialValue, width, false, false);
    }

    public IntegerField(Integer initialValue, int width, boolean grouping) {
        this(null, initialValue, width, grouping);
    }

    public IntegerField(String label, Integer initialValue, int width, boolean grouping) {
        this(label, initialValue, width, grouping, false);
    }

    public IntegerField(Integer initialValue, int width, boolean grouping, boolean allowNegative) {
        this(null, initialValue, width, grouping, allowNegative);
    }

    public IntegerField(String label, Integer initialValue, int width, boolean grouping, boolean allowNegative) {
        super(ZERO);
        this.grouping = grouping;
        this.allowNegative = allowNegative;
        createField();
        setLength(width);
        setValue(initialValue);
        setPattern();
        setLabel(label);
    }

    @Override
    protected Integer getModelValue(String string) {
        return Integer.parseInt(string.replace(",", ""));
    }

    @Override
    protected String getPresentationValue(Integer value) {
        if(isGrouping()) {
            return format(value);
        }
        return value.toString();
    }

    public void setLength(int width) {
        int min = 1;
        if(allowNegative) {
            ++min;
        }
        if(width < min) {
            width = min;
        }
        this.width = width;
        getField().setMaxLength(width);
    }

    protected void setPattern() {
        String p = "(\\d{1,3})(,?(?1))*";
        if(allowNegative) {
            p = "-?" + p;
        }
        p = "^" + p + "$";
        getField().setPattern(p);
        setPresentationValue(getValue());
    }
}
