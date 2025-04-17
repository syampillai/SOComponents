package com.storedobject.vaadin.util;

import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Represents a specialized text field designed for validated input of IPv4 addresses.
 * The format enforced ensures that the input string conforms to the typical dotted
 * quad notation (e.g., 192.168.001.001).
 *
 * This class extends the PatternField to leverage its pattern-based validation and
 * customizes additional rules specific to IPv4 addresses, including checking that:
 * - Each block in the address (separated by '.') is a valid numeric value.
 * - Each block is in the range of 0 to 255.
 *
 * This field also provides utility methods for working with IPv4 addresses, such as
 * converting between string representations to `Inet4Address` objects and vice versa.
 *
 * Key behaviors include:
 * - Default value for the field is represented as `000.000.000.000`.
 * - Input is marked as invalid if any block is non-numeric or falls outside the valid range.
 *
 * @author Syam
 */
public class IPAddressTextField extends PatternField {

    private static CustomStringBlockFormatter pattern =
            new CustomStringBlockFormatter(new int[] { 3, 3, 3, 3},
                    new String[] { ".", ".", ".", "." },
                    CustomStringBlockFormatter.ForceCase.NONE, "", true);
    private static final String EMPTY = "000.000.000.000";

    /**
     * Constructs an IPAddressTextField instance with a predefined pattern to validate
     * IPv4 addresses in dotted quad format (e.g., 192.168.000.001).
     *
     * The instance leverages the custom pattern formatter to enforce:
     * - Exactly four blocks of three digits separated by periods.
     * - Each block must be numeric and within the range of 0 to 255.
     *
     * Additional features include automatic formatting of input and default value
     * handling. If the input does not comply with the IPv4 address formatting rules,
     * the field is marked as invalid and cannot proceed with its associated operations.
     */
    public IPAddressTextField() {
        super(null,15, pattern);
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
        String[] blocks = getValue().split("\\.");
        int v;
        for(String b: blocks) {
            try {
                v = Integer.parseInt(b);
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
     * Resolves a given string representation of an IPv4 address into an {@link Inet4Address} object.
     *
     * @param a the string representing the IPv4 address to be resolved. Should conform to the format
     *          of a dotted quad (e.g., 192.168.1.1).
     * @return an {@link Inet4Address} instance corresponding to the provided IPv4 address string, or
     *         null if the input is invalid or an error occurs during resolution.
     */
    public static Inet4Address getAddress(String a) {
        try {
            return (Inet4Address) InetAddress.getByName(a);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * Retrieves the formatted address of the given IPv4 address.
     * If the input address is null, an empty string is returned. Otherwise, the
     * address is padded to enforce a consistent format.
     *
     * @param a the {@code Inet4Address} object representing the IPv4 address
     *          to be formatted; may be null
     * @return the formatted IPv4 address as a string, or an empty string if
     *         the input is null
     */
    public static String getAddress(Inet4Address a) {
        return a == null ? EMPTY : pad(a.getHostAddress());
    }

    private static String pad(String a) {
        StringBuilder s = new StringBuilder();
        String[] blocks = a.split("\\.");
        for(String block: blocks) {
            if(s.length() > 0) {
                s.append('.');
            }
            s.append(String.format("%03d", Integer.parseInt(block)));
        }
        return s.toString();
    }
}