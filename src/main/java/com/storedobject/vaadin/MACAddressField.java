package com.storedobject.vaadin;

import com.storedobject.vaadin.util.MACAddressTextField;

/**
 * Field to accept valid MAC address.
 *
 * @author Syam
 */
public class MACAddressField extends TranslatedField<byte[], String> {

    public MACAddressField() {
        this(null);
    }

    public MACAddressField(String label) {
        super(new MACAddressTextField(), (f, a) -> MACAddressTextField.getAddress(a), (f, b) -> MACAddressTextField.getAddress(b));
        setLabel(label);
    }
}