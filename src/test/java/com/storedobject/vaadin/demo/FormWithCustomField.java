package com.storedobject.vaadin.demo;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

public class FormWithCustomField extends FormLayout {

    public FormWithCustomField() {
        add(new TextField("Text"));
        add(new CustomTextField("Custom Text"));
    }

    private static class CustomTextField extends CustomField<String> {

        private TextField internal = new TextField();

        private CustomTextField(String label) {
            add(internal);
            setLabel(label);
        }

        @Override
        protected String generateModelValue() {
            return internal.getValue();
        }

        @Override
        protected void setPresentationValue(String value) {
            internal.setValue(value);
        }
    }
}
