package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.DataEditor;
import com.vaadin.flow.component.HasValue;

import java.time.LocalDate;

public class PersonEditor extends DataEditor<Person> {

    public PersonEditor() {
        super(Person.class);
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
    @SuppressWarnings("unchecked")
    protected void customizeField(String fieldName, HasValue<?, ?> field) {
        switch (fieldName) {
            case "FirstName":
                setRequired(field);
                return;
            case "DateOfBirth":
                setRequired(field);
                HasValue<?, LocalDate> f = (HasValue<?, LocalDate>) field;
                addValidator(f, d -> d != null && d.getYear() <= LocalDate.now().getYear());
                return;
        }
        super.customizeField(fieldName, field);
    }

    @Override
    protected void validateData() throws Exception {
        getObject().validateData();
    }
}
