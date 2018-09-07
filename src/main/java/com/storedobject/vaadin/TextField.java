package com.storedobject.vaadin;

public class TextField extends com.vaadin.flow.component.textfield.TextField {

    public TextField() {
    }

    public TextField(String label) {
        super(label);
    }

    public TextField(String label, String placeholder) {
        super(label, placeholder);
    }

    public TextField(String label, String initialValue, String placeholder) {
        super(label, initialValue, placeholder);
    }

    public void setIcon(Icon icon) {
        addToPrefix(icon);
    }

    public void setIcon(String icon) {
        setIcon(new Icon(icon));
    }
}
