package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.storedobject.helper.LitComponent;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

/**
 * A simple clock. By default, it will display the UTC time in 24-hours format.
 *
 * @author Syam
 */
@Tag("so-clock")
@JsModule("./so/clock/so-clock.js")
public class Clock extends LitComponent implements HasStyle {

    /**
     * Constructor.
     */
    public Clock() {
        getElement().setProperty("idClock", "soCLOCK" + ID.newID());
    }

    /**
     * Set the GMT/UTC mode.
     *
     * @param gmt True/false.
     */
    public void setGMT(boolean gmt) {
        executeJS("setGMT", gmt);
    }

    /**
     * Set AM/PM mode.
     *
     * @param ampm True/false.
     */
    public void setAMPM(boolean ampm) {
        executeJS("setAMPM", ampm);
    }
}
