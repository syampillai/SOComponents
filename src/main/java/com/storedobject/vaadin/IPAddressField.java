package com.storedobject.vaadin;

import com.storedobject.vaadin.util.PatternField;

public class IPAddressField extends PatternField {

    private static String pattern = "^(?:(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])(\\.(?!$)|$)){4}$";

    public IPAddressField() {
        this(null);
    }

    public IPAddressField(String label) {
        super(label, 15, pattern);
    }

    @Override
    public String getEmptyValue() {
        return "000.000.000.000";
    }
}
