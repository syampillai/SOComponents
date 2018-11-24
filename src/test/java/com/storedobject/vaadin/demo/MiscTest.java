package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.storedobject.vaadin.util.IronAutogrowTextArea;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;

public class MiscTest extends View {

    private ETextArea ta;
    private IntegerField nf;

    public MiscTest() {
        super("Misc. Tests");
        if(Application.get() == null) {
            return;
        }
        FormLayout form = new FormLayout();
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
        LabelField<String> lf = new LabelField<>("Label", a);
        form.add(lf);
        lf.addValueChangeListener(e -> Notification.show("Changed Labe!"));
        ListField<String> lb = new ListField<>("List", a);
        form.add(lb);
        lb.addValueChangeListener(e -> Notification.show("Changed List!"));
        ta = new ETextArea("Text", "Hello World");
        ta.setMinRows(5);
        ta.setMaxRows(10);
        dataForm.addField(ta);
        dataForm.addField(nf = new IntegerField("Number"));
        dataForm.setRequired(ta, "Enter somethine in multi-line Text Field");
        form.add(new ChoicesField("Choose", new String[] { "One", "Two", "Three" }));
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
