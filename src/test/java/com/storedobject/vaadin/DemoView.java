package com.storedobject.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

@Route("")
public class DemoView extends Div {

    public DemoView() {
        add(new PaperIconButton("device", "bluetooth",
                e -> Notification.show("Clicked on Paper Icon Button!")));
        add(new Button("Test", new Icon("editor", "functions"),
                e -> Notification.show("Please try clicking on Paper Icon Button too!")));
    }
}
