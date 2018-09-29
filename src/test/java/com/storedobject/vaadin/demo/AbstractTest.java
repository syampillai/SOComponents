package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AbstractTest extends View {

    protected ButtonLayout buttons;
    protected HasComponents layout;
    protected StyledText status;
    protected int state = -1;

    public AbstractTest(Application a, String caption) {
        super(a, caption);
        layout = new VerticalLayout();
        buttons = new ButtonLayout();
        layout.add(buttons);
        buttons.add(new Button("Test", VaadinIcon.TOOLS, e -> {
            ++state;
            test();
        }));
        buttons.add(new Button("Reset", VaadinIcon.REFRESH, e -> reset()));
        addButtons();
        buttons.add(new Button("Exit", e -> close()));
        status = new StyledText("Test not started");
        layout.add(status);
        setComponent((Component)layout);
    }

    protected void addButtons() {
    }

    protected void test() {
        status.setText("Unknown state: " + state);
    }

    protected void reset() {
        state = -1;
        status.setText("Test reintialized");
    }
}
