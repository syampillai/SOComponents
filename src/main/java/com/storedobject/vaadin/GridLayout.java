package com.storedobject.vaadin;

import com.vaadin.flow.component.*;

@Tag("div")
public class GridLayout extends Component implements HasOrderedComponents<Component>, HasStyle, HasSize {

    public GridLayout(int numberOfColumns) {
        init();
        setColumnSizes(sizes(numberOfColumns, 0));
    }

    public GridLayout(int... columnSizes) {
        init();
        setColumnSizes(columnSizes);
    }

    public GridLayout(String... columnSizes) {
        init();
        setColumnSizes(columnSizes);
    }

    private void init() {
        style("display", "grid");
        style("align-items", "center");
        setColumnGap("4px");
        style("transition", "all 1s");
    }

    public void setColumnSizes(String... sizes) {
        if(sizes == null || sizes.length == 0) {
            return;
        }
        StringBuilder s = new StringBuilder();
        for(String size: sizes) {
            if(s.length() > 0) {
                s.append(' ');
            }
            if(size != null) {
                try {
                    if(Integer.parseInt(size) > 0) {
                        size += "fr";
                    } else {
                        size = null;
                    }
                } catch(Throwable ignored) {
                }
            }
            s.append(size == null || size.isEmpty() ? "auto" : size);
        }
        style("grid-template-columns", s.toString());
    }

    public void setColumnSizes(int... sizes) {
        if(sizes == null || sizes.length == 0) {
            return;
        }
        StringBuilder s = new StringBuilder();
        for(int size: sizes) {
            if(s.length() > 0) {
                s.append(' ');
            }
            s.append(size <= 0 ? "auto" : (size + "fr"));
        }
        style("grid-template-columns", s.toString());
    }

    public void setColumnGap(String size) {
        style("grid-column-gap", size == null ? "1px" : size);
    }

    public void setRowGap(String size) {
        style("grid-row-gap", size == null ? "1px" : size);
    }
    
    private void style(String styleName, String styleValue) {
        getStyle().set(styleName, styleValue);
    }

    static int[] sizes(int numberOfColumns, int value) {
        if(numberOfColumns <= 1) {
            numberOfColumns = 4;
        }
        int sizes[] = new int[numberOfColumns];
        for(int i = 0; i < numberOfColumns; i++) {
            sizes[i] = value;
        }
        return sizes;
    }
}