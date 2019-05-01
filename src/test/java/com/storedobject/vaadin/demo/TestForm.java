package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.ChoicesField;
import com.storedobject.vaadin.DataForm;
import com.storedobject.vaadin.StyledText;

public class TestForm extends DataForm {

    private ChoicesField choicesField = new ChoicesField("Choose", new String[] { "One", "Two", "Three" });
    private StyledText label = new StyledText("Hello World!");

    public TestForm() {
        super("Field Test");
    }

    @Override
    protected void buildFields() {
        addField(choicesField);
        add(label);
        setRequired(choicesField);
    }

    @Override
    protected boolean process() {
        label.setText("<h2>Okay?</h2>");
        if(choicesField.isEmpty()) {
            warning("Should not come here!");
            return false;
        }
        return true;
    }
}
