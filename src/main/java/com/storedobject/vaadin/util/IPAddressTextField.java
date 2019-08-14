package com.storedobject.vaadin.util;

import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

import java.net.Inet4Address;
import java.net.InetAddress;

public class IPAddressTextField extends PatternField {

    private static CustomStringBlockFormatter pattern =
            new CustomStringBlockFormatter(new int[] { 3, 3, 3, 3}, new String[] { ".", ".", ".", "." }, CustomStringBlockFormatter.ForceCase.NONE, "", true);
    private static final String EMPTY = "000.000.000.000";

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

    public static Inet4Address getAddress(String a) {
        try {
            return (Inet4Address) InetAddress.getByName(a);
        } catch (Throwable ignored) {
        }
        return null;
    }

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