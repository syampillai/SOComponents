package com.storedobject.vaadin;

import com.vaadin.flow.component.html.Div;

public class Dashboard extends Div {

    public Dashboard() {
        getElement().setAttribute("style", "display: grid;" +
                "grid-auto-flow: column dense;" +
                "grid-auto-rows: minmax(100px, auto);" +
                "column-gap: 10px;" +
                "row-gap: 1em;");
    }
}
