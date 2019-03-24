package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.FormLayout;
import com.storedobject.vaadin.TextField;
import com.storedobject.vaadin.View;

public class FormTest extends View {

    public FormTest() {
        super("Form Test");
        FormLayout f = new FormLayout();
        f.setColumns(1);
        for(int i = 0; i < 20; i++) {
            TextField tf = new TextField("Text " + (i + 1));
            if(i == 5) {
                f.setColumnSpan(tf, 3);
            }
            f.add(tf);
        }
        setComponent(f);
    }
}
