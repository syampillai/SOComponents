package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Focusable;

public class TestForm extends DataForm {

    private ChoicesField choicesField = new ChoicesField("Choose", new String[] { "One", "Two", "Three" });
    private StyledText label = new StyledText("Hello World!");
    private DateField dateField;

    public TestForm() {
        super("Field Test");
    }

    @Override
    public boolean skipFirstFocus(Focusable<?> skipFocus) {
        super.skipFirstFocus(skipFocus);
        return choicesField == skipFocus || skipFocus == dateField || skipFocus == ok;
    }

    @Override
    protected void buildFields() {
        if(getApplication() == null) {
            return;
        }
        addField(new IntegerField("Number"));
        addField(choicesField);
        add(label);
        setRequired(choicesField);
        dateField = new DateField("Date");
        addField(dateField);
        setRequired(dateField);
    }

    @Override
    protected boolean process() {
        label.setText("<h2>Okay?</h2>");
        if(choicesField.isEmpty()) {
            warning("Should not come here!");
            return false;
        }
        if(dateField.isEmpty()) {
            warning("Date is emtpy!");
            return false;
        }
        return true;
    }
}
