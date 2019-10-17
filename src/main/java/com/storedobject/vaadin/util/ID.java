package com.storedobject.vaadin.util;

public class ID {

    private static long ID = 1L;

    private ID() {
    }

    public synchronized static long newID() {
        long id = ++ID;
        if(ID == Long.MAX_VALUE) {
            ID = 1L;
        }
        return id;
    }
}