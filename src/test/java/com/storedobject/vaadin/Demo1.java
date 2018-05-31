package com.storedobject.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Demo PaperIconButton, Icon, IronAutogrowTextArea
 */
@Route("1")
public class Demo1 extends VerticalLayout {

    public Demo1() {
        add(new PaperIconButton("device", "bluetooth",
                e -> Notification.show("Clicked on Paper Icon Button!")));
        add(new Button("Button with Icon", new Icon("editor", "functions"),
                e -> Notification.show("Please try clicking on Paper Icon Button too!")));
        IronAutogrowTextArea ta = new IronAutogrowTextArea();
        ta.setMinRows(5);
        ta.setMaxRows(10);
        add(ta);
    }
}
