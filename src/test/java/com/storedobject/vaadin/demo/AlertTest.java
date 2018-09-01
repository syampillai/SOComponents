package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.Alert;
import com.storedobject.vaadin.Application;

public class AlertTest extends AbstractTest {

    private Alert alert;

    public AlertTest(Application a) {
        super(a, "Alert Test");
        alert = new Alert("Alert");
    }

    @Override
    protected void test() {
        switch (state) {
            case 0:
                status.setText("Alert closed");
                alert.close();
                break;
            case 1:
                status.setText("Alert opened");
                alert.setText("Alert: State = " + state);
                break;
            default:
                reset();
        }
    }
}
