package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

public class ParentChildTest extends View {

    private Button child, close;
    private View cv;

    public ParentChildTest() {
        super("Parent");
        FormLayout form = new FormLayout();
        Video v = new Video("http://file-examples.com/wp-content/uploads/2018/04/file_example_OGG_480_1_7mg.ogg", "video/ogg");
        form.add(v);
        form.add(new StyledText("This is the parent"));
        form.add(child = new Button("Child", this));
        form.add(close = new Button("Close", this));
        setComponent(form);
    }

    @Override
    public void clicked(Component c) {
        if(c == child) {
            if(cv == null) {
                cv = new Child();
            }
            cv.invoke(this);
            return;
        }
        if(c == close) {
            close();
            return;
        }
        super.clicked(c);
    }

    private class Child extends View {

        public Child() {
            super("Child");
            FormLayout form = new FormLayout();
            form.add(new StyledText("This is the child"));
            form.add(new Button("Close", this));
            setComponent(form);
        }

        @Override
        public void clicked(Component c) {
            close();
        }
    }
}