package com.storedobject.vaadin.util;

import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * A specialized text field component for IPv6 address input. This class extends
 * the functionality of a {@link PatternField} and ensures proper formatting and validation of
 * 8-block hexadecimal IPv6 addresses (e.g., "2001:0db8:85a3:0000:0000:8a2e:0370:7334").
 *
 * The component enforces a specific pattern and provides auto-padding capabilities
 * for input values to maintain compatibility with the standard 8-block
 * representation of IPv6 addresses.
 *
 * @author Syam
 */
public class IP6AddressTextField extends PatternField {

    private static final CustomStringBlockFormatter pattern =
            new CustomStringBlockFormatter(new int[] { 4, 4, 4, 4, 4, 4, 4, 4 },
                    new String[] { ":", ":", ":", ":", ":", ":", ":", ":" },
                    CustomStringBlockFormatter.ForceCase.LOWER, "", false);
    private static final String EMPTY = "0000:0000:0000:0000:0000:0000:0000:0000";

    /**
     * Constructs an instance of IP6AddressTextField, a customized text input component
     * designed specifically for entering and validating IPv6 addresses in the standard
     * 8-block hexadecimal format.
     *
     * This default constructor initializes the text field with a fixed width of 39
     * characters and uses a pre-configured {@code CustomStringBlockFormatter} to enforce
     * the required format. The minimum width of the component is set to "22em" to ensure
     * adequate space for displaying the entire address, maintaining consistency with the
     * 8-block representation.
     */
    public IP6AddressTextField() {
        super(null, 39, pattern);
        setMinWidth("22em");
    }

    @Override
    public String getEmptyValue() {
        return EMPTY;
    }

    @Override
    public boolean isInvalid() {
        if(super.isInvalid()) {
            return true;
        }
        String[] blocks = getValue().split(":");
        int v;
        for(String b: blocks) {
            try {
                v = Integer.parseInt(b, 16);
                if(v < 0 || v > 0xFFFF) {
                    setInvalid(true);
                    return true;
                }
            } catch (Throwable notNumeric) {
                setInvalid(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves an {@code Inet6Address} object for the specified IPv6 address string.
     *
     * @param a the IPv6 address represented as a {@code String}. It should follow the standard
     *          8-block hexadecimal format. If the input is null or invalid, the method will return null.
     * @return an {@code Inet6Address} instance corresponding to the specified IPv6 address,
     *         or {@code null} if the address could not be resolved or an error occurs.
     */
    public static Inet6Address getAddress(String a) {
        try {
            return (Inet6Address) InetAddress.getByName(a);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * Retrieves the formatted IPv6 address as a string.
     * If the provided address is null, it returns an empty string.
     *
     * @param a the {@link Inet6Address} to be formatted; can be null.
     * @return the padded and formatted string representation of the IPv6 address,
     *         or an empty string if the input is null.
     */
    public static String getAddress(Inet6Address a) {
        return a == null ? EMPTY : pad(a.getHostAddress());
    }

    private static String pad(String a) {
        StringBuilder s = new StringBuilder();
        String[] blocks = a.split(":");
        for(String block: blocks) {
            if(s.length() > 0) {
                s.append(':');
            }
            s.append(String.format("%04X", Integer.parseInt(block, 16)));
        }
        return s.toString();
    }
}