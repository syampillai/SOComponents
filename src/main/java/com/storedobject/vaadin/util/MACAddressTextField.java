package com.storedobject.vaadin.util;

import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

/**
 * A specialized text field for handling MAC address input and validation.
 * This text field ensures consistency in MAC address format, adhering to
 * the 6-block hexadecimal structure separated by colons (e.g., 00:00:00:00:00:00).
 * Includes functionality for validation, formatting, and conversion between
 * string and byte array representations of MAC addresses.
 *
 * @author Suam
 */
public class MACAddressTextField extends PatternField {

    private static final CustomStringBlockFormatter pattern =
            new CustomStringBlockFormatter(
                    new int[] { 2, 2, 2, 2, 2, 2 },
                    new String[] { ":", ":", ":", ":", ":", ":" },
                    CustomStringBlockFormatter.ForceCase.LOWER, "", false);
    private static final String EMPTY = "00:00:00:00:00:00";

    /**
     * Constructs a new MACAddressTextField instance.
     *
     * This constructor initializes the text field with a predefined
     * custom formatter to enforce proper*/
    public MACAddressTextField() {
        super(null, 17, pattern);
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
                if(v < 0 || v > 255) {
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
     * Converts a MAC address string into its corresponding byte array representation.
     * If the input string is invalid or does not follow the standard 6-block hexadecimal
     * format separated by colons, it falls back to a default empty MAC address.
     *
     * @param address the MAC address string to convert, formatted as six
     *                hexadecimal blocks separated by colons (e.g., "00:11:22:33:44:55")
     * @return a byte array representing the MAC address, or the default empty
     *         MAC address byte array if the input is invalid
     */
    public static byte[] getAddress(String address) {
        try {
            String[] blocks = address.split(":");
            if(blocks.length != 6) {
                return getAddress(EMPTY);
            }
            byte[] bytes = new byte[blocks.length];
            int v, i = 0;
            for(String b: blocks) {
                v = Integer.parseInt(b, 16);
                if (v < 0 || v > 255) {
                    break;
                }
                bytes[i++] = (byte)(v & 0xFF);
            }
            return bytes;
        } catch (Throwable ignored) {
        }
        return getAddress(EMPTY);
    }

    /**
     * Converts a byte array representation of a MAC address into a string format.
     * The string is formatted as six two-digit hexadecimal blocks separated by colons
     * (e.g., "00:11:22:33:44:55"). Returns null if the input byte array is null or does not
     * have exactly 6 bytes.
     *
     * @param address the byte array representing the MAC address; must be 6 bytes in length
     * @return the MAC address as a formatted string, or null if the input is invalid
     */
    public static String getAddress(byte[] address) {
        if(address == null || address.length != 6) {
            return null;
        }
        StringBuilder s = new StringBuilder();
        int i;
        for(byte b: address) {
            i = b & 0xFF;
            if(s.length() > 0) {
                s.append(':');
            }
            s.append(String.format("%02X", i));
        }
        return s.toString();
    }
}