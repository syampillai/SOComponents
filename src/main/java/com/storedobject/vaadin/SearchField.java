package com.storedobject.vaadin;

import java.util.function.Consumer;

public class SearchField extends ActionTextField {

    public SearchField(Consumer<String> action) {
        this(null, action);
    }

    public SearchField(String label, Consumer<String> action) {
        this(label, "Search", action);
    }

    public SearchField(String label, String placeholder, Consumer<String> action) {
        super(label, placeholder, action);
        setIcon("search");
        trim();
    }
}
