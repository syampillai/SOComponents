package com.storedobject.vaadin;

import com.storedobject.vaadin.util.IP6AddressTextField;
import java.net.Inet6Address;

/**
 * Field to edit an IPv6 addresses.
 *
 * @author Syam
 */
public class IP6AddressField extends TranslatedField<Inet6Address, String> {

    public IP6AddressField() {
        this(null);
    }

    public IP6AddressField(String label) {
        super(new IP6AddressTextField(), (f, a) -> IP6AddressTextField.getAddress(a), (f, b) -> IP6AddressTextField.getAddress(b));
        setLabel(label);
    }
}