package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.HashMap;
import java.util.Map;

public class ButtonLayout extends FlexLayout {

    private Map<Component, Gap> gaps = new HashMap<>();

    public ButtonLayout() {
        this((Component[])null);
    }

    public ButtonLayout(Component... components) {
        getStyle().set("flex-wrap", "wrap");
        getStyle().set("align-items", "center");
        if(components != null) {
            add(components);
        }
    }

    @Override
    public void add(Component... components) {
        if(components == null) {
            return;
        }
        for(Component c: components) {
            super.add(c);
            Gap g = new Gap();
            super.add(g);
            gaps.put(c, g);
        }
    }

    @Override
    public void remove(Component... components) {
        if(components == null) {
            return;
        }
        for(Component c: components) {
            if(c != null) {
                super.remove(c);
                super.remove(gaps.remove(c));
            }
        }
    }

    @Override
    public void removeAll() {
        super.removeAll();
        gaps.clear();
    }

    private class Gap extends ELabel {

        private Gap() {
            super("&nbsp;&nbsp;");
        }
    }
}