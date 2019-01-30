package com.storedobject.vaadin;

import com.storedobject.vaadin.util.PatternField;

/**
 * Field to accept valid MAC address.
 * <p>It uses a regular expression pattern:
 * "^(?:(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}))$"</p>
 *
 * @author Syam
 */
public class MACAddressField extends PatternField {

    private static String pattern = "^(?:(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}))$";

    public MACAddressField() {
        this(null);
    }

    public MACAddressField(String label) {
        super(label, 17, pattern);
    }

    @Override
    public String getEmptyValue() {
        return "aa:bb:cc:dd:ee:ff";
    }
}
