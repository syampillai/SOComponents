package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;

/**
 * Enhanced version of Vaadin's {@link com.vaadin.flow.component.formlayout.FormLayout}.
 *
 * @author Syam
 */
public class FormLayout extends com.vaadin.flow.component.formlayout.FormLayout {

    private int columns = 2;

    /**
     * Constructor.
     */
    public FormLayout() {
        setColumns(columns);
    }

    /**
     * Constructor.
     *
     * @param components Components to add
     */
    public FormLayout(Component... components) {
        super(components);
        setColumns(columns);
    }

    /**
     * Set number of responsive columns for the form.
     *
     * @param columns Number of responsive columns required
     */
    public void setColumns(int columns) {
        Application a = Application.get();
        int w = a == null ? 1024 : a.getDeviceWidth();
        if(columns < 0) {
            columns = 2;
        } else if(columns > 6) {
            columns = 6;
        }
        if(w <= 0) {
            w = 1024;
        }
        w /= columns;
        ResponsiveStep[] steps = new ResponsiveStep[columns];
        for(int c = 0; c < columns; c++) {
            steps[c] = new ResponsiveStep((c * w) + "px", c + 1);
        }
        setResponsiveSteps(steps);
        this.columns = columns;
    }

    /**
     * Get number of responsive columns for the form.
     *
     * @return Number of responsive columns of the form.
     */
    public final int getColumns() {
        return columns;
    }

    /**
     * Set number of columns to span for a particular component.
     *
     * @param component Component for which column span to be set
     * @param columnSpan Number of columns to span
     */
    public void setColumnSpan(Component component, int columnSpan) {
        component.getElement().setAttribute("colspan", "" + Math.min(Math.max(1, columnSpan), columns));
    }

    /**
     * Get the column span of a component.
     *
     * @param component omponent for which column span to be retrieved
     * @return Column span for the component.
     */
    public int getColumnSpan(Component component) {
        try {
            return Integer.valueOf(component.getElement().getAttribute("colspan"));
        } catch (Throwable error) {
            return 1;
        }
    }
}
