package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.StyledText;
import com.storedobject.vaadin.GridLayout;
import com.storedobject.vaadin.View;

public class GridTest extends View {

    public GridTest() {
        super("Grid Test");
        createGrid();
    }

    private void createGrid() {
        GridLayout g = new GridLayout("", "", "", "");
        for(int i = 0; i < 15; i++) {
            g.add(new StyledText("Test " + i));
        }
        setComponent(g);
    }
}
