package com.storedobject.vaadin;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

/**
 * Demo ButtonIcon, Button, Icon, ETextArea, IntegerField, ChoicesField, Form, ObjectForm
 */
@Route("")
public class Demo1 extends VerticalLayout {

    public Demo1() {
        ObjectForm<Person> of = new ObjectForm<>(Person.class);
        HorizontalLayout h = new HorizontalLayout();
        Form f = new Form();
        h.add(new ButtonIcon("device", "bluetooth", e -> {
            Notification.show("Bluetooh clicked!");
        }));
        h.add(new Button("Save", new Icon("editor", "functions"), e -> {
            Notification.show(f.commit() && of.commit() ? "Saved!" : "Not saved!! " + of.getObject());
        }));
        h.add(new Button("Load", new Icon("editor", "functions"), e -> {
            f.load();
            Notification.show("Loading: " + of.getObject());
            of.load();
            Notification.show("Loaded!");
        }));
        add(h);
        ETextArea ta = new ETextArea("Text");
        ta.setMinRows(5);
        ta.setMaxRows(10);
        f.addField(ta);
        f.setRequired(ta, "Enter multiple lines");
        add(new ChoicesField("Choose", new String[] { "One", "Two", "Three" }));
        TextField tf = new TextField("Hello");
        f.addField(tf);
        f.setRequired(tf, "Text can not be empty!!!");
        IntegerField i = new IntegerField("Count");
        f.addField(i);
        f.setRequired(i);
        add(f.getComponent());
        f.addValidator(i, v -> v > 10, "Value must be greater that 10");
        add(of.getComponent());
    }

    public static class Person {

        private String firstName = "Me", lastName = "You";

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getName() {
            return firstName + " " + lastName;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
