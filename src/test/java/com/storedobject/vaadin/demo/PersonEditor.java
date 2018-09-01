package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.Application;
import com.storedobject.vaadin.DataEditor;
import com.storedobject.vaadin.demo.Person;
import com.vaadin.flow.component.HasValue;

import java.time.LocalDate;

public class PersonEditor extends DataEditor<Person> {

    public PersonEditor(Application a) {
        super(a, Person.class);
    }

    @Override
    protected int getFieldOrder(String fieldName) {
        switch (fieldName) {
            case "FirstName":
                return 100;
            case "LastName":
                return 200;
            case "DateOfBirth":
                return 300;
        }
        return super.getFieldOrder(fieldName);
    }

    @Override
    protected void customizeField(String fieldName, HasValue<?, ?> field) {
        switch (fieldName) {
            case "FirstName":
                setRequired(field);
                return;
            case "DateOfBirth":
                setRequired(field);
                addValidator((HasValue<?, LocalDate>)field, d -> d != null && d.getYear() <= LocalDate.now().getYear());
                return;
        }
        super.customizeField(fieldName, field);
    }

    @Override
    protected void validateData() throws Exception {
        getObject().validateData();
    }
}
