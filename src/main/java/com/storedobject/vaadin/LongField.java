package com.storedobject.vaadin;

import com.storedobject.vaadin.util.NumericField;

public class LongField extends NumericField<Long> {

    private static final Long ZERO = 0L;

    public LongField() {
        this(null, null);
    }

    public LongField(String label) {
        this(label, null);
    }

    public LongField(Long initialValue) {
        this(null, initialValue);
    }

    public LongField(String label, Long initialValue) {
        this(label, initialValue, 18);
    }

    public LongField(Long initialValue, int width) {
        this(null, initialValue, width);
    }

    public LongField(String label, Long initialValue, int width) {
        this(label, initialValue, width, false, false);
    }

    public LongField(Long initialValue, int width, boolean grouping) {
        this(null, initialValue, width, grouping);
    }

    public LongField(String label, Long initialValue, int width, boolean grouping) {
        this(label, initialValue, width, grouping, false);
    }

    public LongField(Long initialValue, int width, boolean grouping, boolean allowNegative) {
        this(null, initialValue, width, grouping, allowNegative);
    }

    public LongField(String label, Long initialValue, int width, boolean grouping, boolean allowNegative) {
        super(ZERO);
        this.grouping = grouping;
        this.allowNegative = allowNegative;
        setLength(width);
        setValue(initialValue);
        setPattern();
        setLabel(label);
    }

    @Override
    protected Long getModelValue(String string) {
        return Long.parseLong(string.replace(",", ""));
    }

    @Override
    protected String getPresentationValue(Long value) {
        if(!isGrouping()) {
            return value.toString();
        }
        return format(value);
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
