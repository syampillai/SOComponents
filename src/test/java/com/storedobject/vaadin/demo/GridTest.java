package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.Application;
import com.storedobject.vaadin.DataGrid;
import com.storedobject.vaadin.View;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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
            setItems(new ListDataProvider<>(new Persons()));
        }

        @Override
        public void constructed() {
            prependHeader().join().setText("Hello World", ColumnTextAlign.CENTER);
            //prependHeader().join().setText("Another World", ColumnTextAlign.CENTER);
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