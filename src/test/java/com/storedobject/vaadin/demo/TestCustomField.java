package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.View;

public class TestCustomField extends View {

    public TestCustomField() {
        setCaption("Test Custom Field");
        setComponent(new FormWithCustomField());
    }
}
