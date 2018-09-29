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
        ImageButton b = new ImageButton("search", e -> click());
        addToPrefix(b);
        trim();
    }

    @Override
    public SearchField toUpperCase() {
        return (SearchField)super.toUpperCase();
    }

    @Override
    public SearchField toLowerCase() {
        return (SearchField)super.toLowerCase();
    }

    @Override
    public SearchField trim() {
        return (SearchField)super.trim();
    }

    protected void click() {
        act();
    }
}
