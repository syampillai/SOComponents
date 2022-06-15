package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.storedobject.helper.LitComponent;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

/**
 * A simple clock. By default, it will display the UTC time in 24-hours format with its date-part.
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
     * Set the UTC mode.
     *
     * @param utc True/false.
     */
    public void setUTC(boolean utc) {
        executeJS("setUTC", utc);
    }

    /**
     * Set AM/PM mode.
     *
     * @param ampm True/false.
     */
    public void setAMPM(boolean ampm) {
        executeJS("setAMPM", ampm);
    }

    /**
     * Whether to display date-part or not.
     *
     * @param showDate True/false.
     */
    public void showDate(boolean showDate) {
        executeJS("showDate", showDate);
    }
}
