package com.storedobject.vaadin;

import com.storedobject.vaadin.util.NumericField;

import java.math.BigDecimal;

public class BigDecimalField extends NumericField<BigDecimal> {

    private int decimals;

    public BigDecimalField() {
        this(null);
    }

    public BigDecimalField(String label) {
        this(label, null);
    }

    public BigDecimalField(String label, BigDecimal initialValue) {
        this(label, initialValue, 6);
    }

    public BigDecimalField(int decimals) {
        this(null, null, decimals);
    }

    public BigDecimalField(BigDecimal initialValue, int decimals) {
        this(null, initialValue, decimals);
    }

    public BigDecimalField(String label, int decimals) {
        this(label, null, decimals);
    }

    public BigDecimalField(String label, BigDecimal initialValue, int decimals) {
        this(label, initialValue, 18, decimals);
    }

    public BigDecimalField(int width, int decimals) {
        this(null, null, width, decimals);
    }

    public BigDecimalField(BigDecimal initialValue, int width, int decimals) {
        this(null, initialValue, width, decimals);
    }

    public BigDecimalField(String label, int width, int decimals) {
        this(label, width, decimals, false, false);
    }

    public BigDecimalField(String label, BigDecimal initialValue, int width, int decimals) {
        this(label, initialValue, width, decimals, false, false);
    }

    public BigDecimalField(int width, int decimals, boolean grouping) {
        this(null, null, width, decimals, grouping);
    }

    public BigDecimalField(BigDecimal initialValue, int width, int decimals, boolean grouping) {
        this(null, initialValue, width, decimals, grouping);
    }

    public BigDecimalField(String label, int width, int decimals, boolean grouping) {
        this(label, null, width, decimals, grouping);
    }

    public BigDecimalField(String label, BigDecimal initialValue, int width, int decimals, boolean grouping) {
        this(label, initialValue, width, decimals, grouping, false);
    }

    public BigDecimalField(int width, int decimals, boolean grouping, boolean allowNegative) {
        this(null, null, width, decimals, grouping, allowNegative);
    }

    public BigDecimalField(BigDecimal initialValue, int width, int decimals, boolean grouping, boolean allowNegative) {
        this(null, initialValue, width, decimals, grouping, allowNegative);
    }

    public BigDecimalField(String label, int width, int decimals, boolean grouping, boolean allowNegative) {
        this(label, null, width, decimals, grouping, allowNegative);
    }

    public BigDecimalField(String label, BigDecimal initialValue, int width, int decimals, boolean grouping, boolean allowNegative) {
        super(toBigDecimal(BigDecimal.ZERO, decimals(decimals)));
        this.grouping = grouping;
        this.allowNegative = allowNegative;
        this.decimals = decimals(decimals);
        setLength(width);
        setValue(initialValue);
        setPattern();
        setLabel(label);
    }

    private static int decimals(int decimals) {
        return decimals < 1 ? 2 : (decimals > 40 ? 40 : decimals);
    }

    @Override
    public void setValue(BigDecimal value) {
        super.setValue(toBigDecimal(value, decimals));
    }

    @Override
    protected BigDecimal getModelValue(String string) {
        return toBigDecimal(string, decimals);
    }

    @Override
    protected String getPresentationValue(BigDecimal value) {
        if(isGrouping()) {
            return format(value.doubleValue());
        }
        return value.toString();
    }

    public void setLength(int width) {
        int min = 1;
        if(allowNegative) {
            ++min;
        }
        if(decimals > 0) {
            min += decimals + 1;
        }
        if(width < min) {
            width = min;
        }
        this.width = width;
        getField().setMaxLength(width);
    }

    public final int getDecimals() {
        return decimals;
    }

    protected void setPattern() {
        String p = "(\\d{1,3})(,?(?1))*";
        if(decimals > 0) {
            p += "(.\\d{0," + decimals + "})";
        }
        if(allowNegative) {
            p = "-?" + p;
        }
        p = "^" + p + "$";
        getField().setPattern(p);
        setPresentationValue(getValue());
    }

    private static BigDecimal toBigDecimal(Object value) {
        String v;
        if(value == null) {
            return null;
        }
        if(value instanceof BigDecimal) {
            return (BigDecimal)value;
        }
        v = value.toString().trim();
        if(v.length() == 0) {
            return null;
        }
        try {
            return new BigDecimal(v.trim().replace(",", ""));
        } catch(Throwable ignored) {
        }
        return null;
    }

    private static BigDecimal toBigDecimal(Object value, int decimals) {
        BigDecimal bd = toBigDecimal(value);
        if(bd == null || decimals < 0 || bd.scale() == decimals) {
            return bd;
        }
        try {
            return bd.setScale(decimals, BigDecimal.ROUND_DOWN);
        } catch(Throwable ignored) {
        }
        return null;
    }
}
