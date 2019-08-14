package com.storedobject.vaadin.util;

import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

public class MACAddressTextField extends PatternField {

    private static CustomStringBlockFormatter pattern =
            new CustomStringBlockFormatter(new int[] { 2, 2, 2, 2, 2, 2 }, new String[] { ":", ":", ":", ":", ":", ":" }, CustomStringBlockFormatter.ForceCase.LOWER, "", false);
    private static final String EMPTY = "00:00:00:00:00:00";

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