package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;

public class MiscTest extends View {

    private ETextArea ta;
    private IntegerField nf;
    private int cols = 0;

    public MiscTest() {
        super("Misc. Tests");
        if(Application.get() == null) {
            return;
        }
        ChoicesField csf;
        FormLayout form = new FormLayout();
        form.add(new PopupTextField("Pop"));
        form.add(new UploadField("Upload"));
        NumberField numberField = new NumberField("Number Field");
        numberField.setRequiredIndicatorVisible(true);
        form.add(numberField);
        ArrayList<String> list = new ArrayList<String>() {{ add("One"); add("Two"); add("Three"); }};
        RadioField<String> rf = new RadioField<>("Radio", list);
        form.add(rf);
        rf.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        RadioChoiceField rbg = new RadioChoiceField("Radio", list);
        form.add(rbg);
        rbg.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        IntegerField integerField = new IntegerField("Integer");
        form.add(integerField);
        integerField.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        form.add(new EmailField("Email"));
        form.add(new Select<>("Select 1", "Select 2", "Select 3"));
        DateField df = new DateField("Date");
        df.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        form.add(df);
        ChoiceField cf = new ChoiceField("Choose X", new String[] {"One", "Two", "Three"});
        cf.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        form.add(cf);
        BooleanField b = new BooleanField("Boolean");
        form.add(b);
        b.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        ListBox<String> test = new ListBox<>();
        HorizontalLayout h = new HorizontalLayout();
        Form dataForm = new Form();
        h.add(new ButtonIcon("device", "bluetooth", e -> {
            test.setVisible(!test.isVisible());
            alert("Bluetooh clicked! Visibility of 'Item List' toggled!!");
        }));
        h.add(new Button("Save", new Icon("editor", "functions"), e -> {
            if(dataForm.commit()) {
                alert("Saved!");
            } else {
                alert("Save failed");
                dataForm.clearErrors();
            }
        }));
        h.add(new Button("Load", new Icon("editor", "functions"), e -> {
            dataForm.load();
            alert("Loaded");
        }));
        h.add(new Button("Dump Values", e -> dataForm.dumpValues()));
        h.add(new ActionTextField(e -> {
            alert("It works! " + e);
            ta.setValue(e);
        }));
        form.add(h);
        ArrayList<String> a = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            a.add("Item " + i);
        }
        test.setItems(a);
        test.setVisible(false);
        form.add(test);
        LabelField<String> lf = new LabelField<>("Label Field", a);
        form.add(lf);
        lf.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        ListField<String> lb = new ListField<>("List", a);
        form.add(lb);
        lb.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        dataForm.add(new TextField("Hello"));
        ta = new ETextArea("Text", "Hello World");
        ta.setMinRows(5);
        ta.setMaxRows(10);
        dataForm.addField(ta);
        ta.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        dataForm.addField(nf = new IntegerField("Number"));
        dataForm.setRequired(ta, "Enter somethine in multi-line Text Field");
        form.add(csf = new ChoicesField("Choose", new String[] { "One", "Two", "Three" }));
        csf.addValueChangeListener(e -> Notification.show("Value: " + e.getOldValue() + " to " + e.getValue()));
        form.add(new ConfirmButton("Hello", e -> { csf.setColumns(cols); ++cols; }));
        TextField tf = new TextField("Hello");
        dataForm.addField(tf);
        dataForm.setRequired(tf, "Text in the 'Hello' Text Field can not be empty!!!");
        IntegerField i = new IntegerField("Count");
        dataForm.addField(i);
        form.add(dataForm.getComponent());
        dataForm.addValidator(i, v -> v > 10, "Value of 'Count' must be greater that 10");
        form.add(new ListField<>("Another", a));
        setComponent(form);
    }

    private void alert(String message) {
        new Alert(message).show();
    }
}