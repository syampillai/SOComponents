package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;

public class ButtonLayout extends VerticalLayout {

    private int maxCount = 8;
    private ArrayList<Bar> bars;

    public ButtonLayout() {
        setWidth(null);
        bars = new ArrayList<Bar>();
    }

    public void setMaximumButtons(int maxCount) {
        if(maxCount == this.maxCount) {
            return;
        }
        this.maxCount = maxCount <= 0 ? 8 : maxCount;
        add(null, 0);
    }

    public int getMaximumButtons() {
        return maxCount;
    }

    @Override
    public void add(Component... components) {
        for(Component c: components) {
            add(c);
        }
    }

    public void add(Component c) {
        if(c instanceof Bar) {
            bars.add((Bar)c);
            superAdd(c);
            return;
        }
        Bar b = null;
        if(bars.size() != 0) {
            b = bars.get(bars.size() - 1);
            if(isButton(c) && (b.getComponentCount() >= maxCount)) {
                b = null;
            }
        }
        if(b == null) {
            b = new Bar();
        }
        b.add(c);
        //b.setComponentAlignment(c, Alignment.MIDDLE_LEFT);
    }

    protected Alignment getRowAlignment() {
        return null;
    }

    protected boolean isButton(Component c) {
        return c instanceof Button || c instanceof ImageButton || c instanceof Checkbox;
    }

    @Override
    public void remove(Component... components) {
        for(Component c: components) {
            remove(c);
        }
    }

    public void remove(Component c) {
        clear();
        ArrayList<Bar> v = bars;
        bars = new ArrayList<Bar>();
        for(Bar b: v) {
            ArrayList<Component> cv = new ArrayList<Component>();
            b.getChildren().forEach(cc -> cv.add(cc));
            for(Component ic: cv) {
                if(ic != c) {
                    add(ic);
                }
            }
        }
    }

    private void clear() {
        for(HorizontalLayout b: bars) {
            super.remove(b);
        }
    }

    @Override
    public void removeAll() {
        clear();
        bars.clear();
    }

    public void add(Component c, int index) {
        clear();
        ArrayList<Bar> v = bars;
        bars = new ArrayList<Bar>();
        int pos = -1;
        for(HorizontalLayout b: v) {
            if(b instanceof NewLineBar) {
                newLine();
            }
            ArrayList<Component> cv = new ArrayList<Component>();
            b.getChildren().forEach(cc -> cv.add(cc));
            for(Component ic: cv) {
                ++pos;
                if(c != null && pos <= index) {
                    add(c);
                    c = null;
                }
                add(ic);
            }
        }
        if(c != null) {
            add(c);
        }
    }

    public void newLine() {
        new NewLineBar();
    }

    private void superAdd(Component c) {
        super.add(c);
        Alignment ra = getRowAlignment();
        if(ra != null) {
            //ButtonLayout.this.setComponentAlignment(c, ra);
        }
    }

    private class Bar extends HorizontalLayout {
        protected Bar() {
            setSpacing(true);
            bars.add(this);
            superAdd(this);
        }
    }

    private class NewLineBar extends Bar {
        private NewLineBar() {
        }
    }
}