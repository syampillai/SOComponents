package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.ArrayList;

public class GridTest extends View {

    public GridTest() {
        super("Grid Test");
        if(Application.get() == null) {
            return;
        }
        setComponent(new Grid());
    }

    private static class Grid extends DataGrid<Person> {

        public Grid() {
            super(Person.class);
            setDataProvider(new ListDataProvider<>(new Persons()));
        }
    }

    private static class Persons extends ArrayList<Person> {

        private Persons() {
            Person p;
            for(int i = 1; i < 50; i++) {
                p = new Person("F Name " + i, "L Name " +i);
                add(p);
            }
        }
    }
}