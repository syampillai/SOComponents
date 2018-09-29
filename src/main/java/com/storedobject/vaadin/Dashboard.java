package com.storedobject.vaadin;

import com.vaadin.flow.component.*;

@Tag("div")
public class Dashboard extends Component implements HasOrderedComponents<Component>, HasStyle, HasSize, ExecutableView {

    private View view;
    private String caption;
    private final boolean boxing;

    public Dashboard() {
        this(null, true);
    }

    public Dashboard(boolean boxing) {
        this(null, boxing);
    }

    public Dashboard(String caption) {
        this(caption, true);
    }

    public Dashboard(String caption, boolean boxing) {
        this.boxing = boxing;
        setCaption(caption);
        style("display", "grid");
        style("align-items", "center");
        setGap("4px");
        style("transition", "all 5s");
        setMinimumColumnWidth(null);
        setAutoarrange(true);
        style("place-items", "stretch");
        style("place-content", "stretch");
    }

    public void setMinimumColumnWidth(String width) {
        if(width == null || width.isEmpty()) {
            width = "250px";
        }
        style("grid-template-columns", "repeat(auto-fill, minmax(" + width + ", 1fr))");
    }

    private void style(String styleName, String styleValue) {
        getStyle().set(styleName, styleValue);
    }

    public void setAutoarrange(boolean autoarrange) {
        style("grid-auto-flow", autoarrange ? "row dense" : "row");
    }

    public boolean isAutoarrange() {
        return "row dense".equals(getStyle().get("grid-auto-flow"));
    }

    public void setColumnSpan(Component component, int numberOfColumns) {
        if(numberOfColumns < 1) {
            numberOfColumns = 1;
        }
        if(numberOfColumns > 6) {
            numberOfColumns = 6;
        }
        component.getElement().getStyle().set("grid-column", "span " + numberOfColumns);
    }

    public void setColumnSpan(View view, int numberOfColumns) {
        setColumnSpan(view.getComponent(), numberOfColumns);
    }

    public void setRowSpan(Component component, int numberOfRows) {
        if(numberOfRows < 1) {
            numberOfRows = 1;
        }
        component.getElement().getStyle().set("grid-row", "span " + numberOfRows);
    }

    public void setRowSpan(View view, int numberOfRows) {
        setRowSpan(view.getComponent(), numberOfRows);
    }

    public void setGap(String size) {
        style("grid-gap", size);
    }

    @Override
    public void add(Component... components) {
        if(components == null) {
            return;
        }
        for(Component c: components) {
            if(c instanceof ExecutableView) {
                ((ExecutableView) c).getView(true);
            }
            if(boxing) {
                new Box(c);
            }
            if(c instanceof HasSize) {
                ((HasSize)c).setWidth("100%");
                ((HasSize)c).setHeight("100%");
            }
        }
        HasOrderedComponents.super.add(components);
    }

    public void add(View... views) {
        if(views != null) {
            for(View v: views) {
                if(v != null) {
                    add(v.getComponent());
                }
            }
        }
    }

    @Override
    public void clicked(Component c) {
    }

    @Override
    public void valueChanged(ChangedValues changedValues) {
    }

    public View getView(boolean create) {
        if(view == null && create) {
            view = createView();
        }
        if(view == null && create) {
            view = new View(getApplication(), this, getCaption());
        }
        return view;
    }

    /**
     * Create a View to display the grid when executed. If this method returns null, a default View will be created.
     * @return A View with this grid as the component.
     */
    protected View createView() {
        return null;
    }

    /**
     * Get the Application
     * @return The Application instance
     */
    public Application getApplication() {
        return Application.get();
    }

    /**
     * Caption used when displaying it in a View. If no caption was set using setCaption method, "Dashboard" will be returned.
     * @return Caption
     */
    public String getCaption() {
        return caption == null || caption.trim().isEmpty() ? "Dashboard" : caption;
    }

    /**
     * Closes the view if that exists
     */
    @Override
    public void close() {
        if(view != null) {
            view.close();
        }
    }

    /**
     * Closes the view by aborting if that exists
     */
    @Override
    public void abort() {
        if(view != null) {
            view.abort();
        }
    }

    /**
     * Set caption used when displaying the grid in a View.
     * @param caption Caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }
}
