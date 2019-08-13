package com.storedobject.vaadin;

import com.storedobject.vaadin.util.PatternField;

/**
 * Field to edit an IP addresses.
 *
 * @author Syam
 */
public class IPAddressField extends PatternField {

    private static String pattern = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";

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
