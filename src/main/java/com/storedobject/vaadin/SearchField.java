package com.storedobject.vaadin;

import java.util.function.Consumer;

/**
 * An text field that can be used for triggering search action. Search action is triggered when "Enter" key
 * is pressed, text value change happened or the embedded button is pressed.
 *
 * @author Syam
 */
public class SearchField extends ActionTextField {

    public SearchField(Consumer<String> searchAction) {
        this(null, searchAction);
    }

    public SearchField(String label, Consumer<String> searchAction) {
        this(label, "Search", searchAction);
    }

    public SearchField(String label, String placeholder, Consumer<String> searchAction) {
        super(label, placeholder, searchAction);
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
