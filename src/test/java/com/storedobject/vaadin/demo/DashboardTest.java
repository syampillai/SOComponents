package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.Box;
import com.storedobject.vaadin.Dashboard;
import com.storedobject.vaadin.StyledText;
import com.vaadin.flow.component.Component;

public class DashboardTest extends Dashboard {

    private Component one, two, three, four, five, six, seven, eight, nine;

    public DashboardTest() {
        super("My Dashboard");
        one = cc("I am One");
        two = cc("I am Two");
        three = cc("I am Three");
        four = cc("I am Four");
        five = cc("I am Five");
        six = cc("I am Six");
        seven = cc("I am Seven");
        eight = cc("I am Eight");
        nine = cc("I am Nine");
        add(one, two, three, four, five, six, seven, eight, nine);
        setColumnSpan(two, 2);
        //setRowSpan(four, 3);
    }

    private Component cc(String text) {
        if(getApplication() == null) {
            return new Box(new StyledText("No application"));
        }
        PersonEditor pe = new PersonEditor(getApplication());
        return new Box(pe.getComponent());
    }
}
