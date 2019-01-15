package com.storedobject.vaadin.demo;

import java.time.LocalDate;

public class Person {

    private String firstName, lastName;
    private LocalDate dateOfBirth;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public void validateData() throws Exception {
        if(dateOfBirth == null) {
            throw new Exception("Date of Birth can not be empty");
        }
        if(firstName == null || firstName.trim().isEmpty()) {
            throw new Exception("First Name can not be empty");
        }
        if(getAge() < 0) {
            throw new Exception("Date of birth can not be a future date!");
        }
    }
}