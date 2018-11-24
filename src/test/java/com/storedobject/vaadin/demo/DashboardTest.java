package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.Application;
import com.storedobject.vaadin.Box;
import com.storedobject.vaadin.Dashboard;
import com.storedobject.vaadin.StyledText;
import com.vaadin.flow.component.Component;

public class DashboardTest extends Dashboard {

    public DashboardTest() {
        super("My Dashboard");
        Component one = cc("I am One");
        Component two = cc("I am Two");
        Component three = cc("I am Three");
        Component four = cc("I am Four");
        Component five = cc("I am Five");
        Component six = cc("I am Six");
        Component seven = cc("I am Seven");
        Component eight = cc("I am Eight");
        Component nine = cc("I am Nine");
        add(one, two, three, four, five, six, seven, eight, nine);
        setColumnSpan(two, 2);
        //setRowSpan(four, 3);
    }

    private Component cc(@SuppressWarnings("unused") String text) {
        if(Application.get() == null) {
            return new Box(new StyledText("No application"));
        }
        PersonEditor pe = new PersonEditor();
        return new Box(pe.getComponent());
    }
}
