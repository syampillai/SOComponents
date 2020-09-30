package com.storedobject.vaadin;

import com.storedobject.vaadin.util.MACAddressTextField;

/**
 * Field to accept valid MAC address.
 *
 * @author Syam
 */
public class MACAddressField extends TranslatedField<byte[], String> {

    /**
     * Constructor.
     */
    public MACAddressField() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public MACAddressField(String label) {
        super(new MACAddressTextField(), (f, a) -> MACAddressTextField.getAddress(a), (f, b) -> MACAddressTextField.getAddress(b));
        setLabel(label);
    }
}