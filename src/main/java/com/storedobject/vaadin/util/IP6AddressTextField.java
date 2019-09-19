package com.storedobject.vaadin.util;

import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

import java.net.Inet6Address;
import java.net.InetAddress;

public class IP6AddressTextField extends PatternField {

    private static CustomStringBlockFormatter pattern =
            new CustomStringBlockFormatter(new int[] { 4, 4, 4, 4, 4, 4, 4, 4 }, new String[] { ":", ":", ":", ":", ":", ":", ":", ":" }, CustomStringBlockFormatter.ForceCase.LOWER, "", false);
    private static final String EMPTY = "0000:0000:0000:0000:0000:0000:0000:0000";

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

    public static Inet6Address getAddress(String a) {
        try {
            return (Inet6Address) InetAddress.getByName(a);
        } catch (Throwable ignored) {
        }
        return null;
    }

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