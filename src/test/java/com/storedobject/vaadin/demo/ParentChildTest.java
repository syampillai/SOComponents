package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.Application;
import com.storedobject.vaadin.Button;
import com.storedobject.vaadin.StyledText;
import com.storedobject.vaadin.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

public class ParentChildTest extends View {

    private Button child, close;
    private View cv;

    public ParentChildTest(Application a) {
        super(a, "Parent");
        FormLayout form = new FormLayout();
        form.add(new StyledText("This is the parent"));
        form.add(child = new Button("Child", this));
        form.add(close = new Button("Close", this));
        setComponent(form);
    }

    @Override
    public void clicked(Component c) {
        if(c == child) {
            if(cv == null) {
                cv = new Child(getApplication());
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

        public Child(Application a) {
            super(a, "Child");
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
