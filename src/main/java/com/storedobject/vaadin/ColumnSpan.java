package com.storedobject.vaadin;

import com.vaadin.flow.component.HasElement;

/**
 * Components that support spanning of columns.
 *
 * @author Syam
 */
public interface ColumnSpan extends HasElement {

    /**
     * Set column span.
     *
     * @param columns Number of columns to span
     */
    default void setColumnSpan(int columns) {
        if(columns <= 0) {
            columns = 1;
        }
        getElement().setAttribute("colspan", "" + columns);
    }

    /**
     * Get column span.
     *
     * @return Current value of the column span. Will return 1 if undefined.
     */
    default int getColumnSpan() {
        String strColspan = getElement().getAttribute("colspan");
        return strColspan == null || !strColspan.matches("\\d+") ? 1 : Integer.parseInt(strColspan);
    }
}