package com.storedobject.vaadin;

import com.storedobject.vaadin.util.IPAddressTextField;

import java.net.Inet4Address;

/**
 * Field to edit an IP addresses.
 *
 * @author Syam
 */
public class IPAddressField extends TranslatedField<Inet4Address, String> {

    public IPAddressField() {
        this(null);
    }

    public IPAddressField(String label) {
        super(new IPAddressTextField(), (f, a) -> IPAddressTextField.getAddress(a), (f, a) -> IPAddressTextField.getAddress(a));
        setLabel(label);
    }
}
