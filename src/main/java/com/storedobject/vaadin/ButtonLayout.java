package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
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
            if(isBL(c)) {
                continue;
            }
            Gap g = new Gap();
            super.add(g);
            gaps.put(c, g);
        }
    }

    private static boolean isBL(Component c) {
        if(c instanceof ButtonLayout) {
            return true;
        }
        if(c instanceof Composite) {
            return isBL(((Composite) c).getContent());
        }
        Component inner = c.getChildren().reduce((c1, c2) -> c2).orElse(null);
        if(inner != null) {
            return isBL(inner);
        }
        return false;
    }

    @Override
    public void remove(Component... components) {
        if(components == null) {
            return;
        }
        for(Component c: components) {
            if(c != null) {
                super.remove(c);
                c = gaps.remove(c);
                if(c != null) {
                    super.remove(c);
                }
            }
        }
    }

    @Override
    public void removeAll() {
        super.removeAll();
        gaps.clear();
    }

    public Component getGap(Component component) {
        return gaps.get(component);
    }

    private class Gap extends StyledText {

        private Gap() {
            super("&nbsp;&nbsp;");
        }
    }
}