package com.storedobject.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

@Route("")
public class DemoView extends FormLayout {

    public DemoView() {
        addFormItem(new PaperIconButton("device", "bluetooth",
                e -> Notification.show("Clicked on Paper Icon Button!")), "Paper Icon Button");
        add(new Button("Button with Icon", new Icon("editor", "functions"),
                e -> Notification.show("Please try clicking on Paper Icon Button too!")));
        IronAutogrowTextArea ta = new IronAutogrowTextArea();
        ta.setMinRows(5);
        ta.setMaxRows(10);
        addFormItem(ta, "Autogrow TextArea");
    }
}
