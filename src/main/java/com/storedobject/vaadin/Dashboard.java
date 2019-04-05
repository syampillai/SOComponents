package com.storedobject.vaadin;

import com.vaadin.flow.component.*;

/**
 * Dashboard is a "view" that can show multiple embedded "sub-views". Dashoard divides the display area into rows and columns and each
 * "sub-view" can span across one or more rows and columns.
 *
 * @author Syam
 */
@Tag("div")
public class Dashboard extends Component implements HasOrderedComponents<Component>, HasStyle, HasSize, ExecutableView {

    private View view;
    private String caption;
    private final boolean boxing;

    /**
     * Constructor. Sub-views are shown in a box by default.
     */
    public Dashboard() {
        this(null, true);
    }

    /**
     * Constructor.
     *
     * @param boxing Whether the sub-views should be shown inside a box or not
     */
    public Dashboard(boolean boxing) {
        this(null, boxing);
    }

    /**
     * Constructor. Sub-views are shown in a box by default.
     *
     * @param caption Caption
     */
    public Dashboard(String caption) {
        this(caption, true);
    }

    /**
     * Constructor.
     *
     * @param caption Caption
     * @param boxing Whether the sub-views should be shown inside a box or not
     */
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

    /**
     * Set minimum width for the column. Default width is 250 pixels.
     *
     * @param width Width
     */
    public void setMinimumColumnWidth(String width) {
        if(width == null || width.isEmpty()) {
            width = "250px";
        }
        style("grid-template-columns", "repeat(auto-fill, minmax(" + width + ", 1fr))");
    }

    private void style(String styleName, String styleValue) {
        getStyle().set(styleName, styleValue);
    }

    /**
     * Set the auto-arrange flag. If set, sub-views are arranged automatically to optimize space used. Default value for this is
     * <code>true</code>.
     *
     * @param autoarrange Whether to auto-arrange sub-views or not
     */
    public void setAutoarrange(boolean autoarrange) {
        style("grid-auto-flow", autoarrange ? "row dense" : "row");
    }

    /**
     * Check the current value of auto-arrange flag.
     *
     * @return Current auto-arrange flag
     * @see #setAutoarrange(boolean)
     */
    public boolean isAutoarrange() {
        return "row dense".equals(getStyle().get("grid-auto-flow"));
    }

    /**
     * Set the column-span for the particular sub-view. Minimum value is 1 and maximum allowed is 6.
     *
     * @param component Sub-view
     * @param numberOfColumns Number of columns to span
     */
    public void setColumnSpan(Component component, int numberOfColumns) {
        if(numberOfColumns < 1) {
            numberOfColumns = 1;
        }
        if(numberOfColumns > 6) {
            numberOfColumns = 6;
        }
        component.getElement().getStyle().set("grid-column", "span " + numberOfColumns);
    }

    /**
     * Set the column-span for the particular sub-view. Minimum value is 1 and maximum allowed is 6.
     *
     * @param view Sub-view
     * @param numberOfColumns Number of columns to span
     */
    public void setColumnSpan(View view, int numberOfColumns) {
        setColumnSpan(view.getComponent(), numberOfColumns);
    }

    /**
     * Set the row-span for the particular sub-view. Minimum value is 1.
     *
     * @param component Sub-view
     * @param numberOfRows Number of rows to span
     */
    public void setRowSpan(Component component, int numberOfRows) {
        if(numberOfRows < 1) {
            numberOfRows = 1;
        }
        component.getElement().getStyle().set("grid-row", "span " + numberOfRows);
    }

    /**
     * Set the row-span for the particular sub-view. Minimum value is 1.
     *
     * @param view Sub-view
     * @param numberOfRows Number of rows to span
     */
    public void setRowSpan(View view, int numberOfRows) {
        setRowSpan(view.getComponent(), numberOfRows);
    }

    /**
     * Set gap between sub-views.
     *
     * @param size Gap
     */
    public void setGap(String size) {
        style("grid-gap", size);
    }

    /**
     * Add sub-views to the dashboard.
     *
     * @param components Sub-views.
     */
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

    /**
     * Add sub-views to the dashboard.
     *
     * @param views Sub-views.
     */
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

    /**
     * Create a View to display the grid when executed.
     *
     * @param create If true is passed, a view will be created if no current view exists
     * @return A View with this grid as the component.
     */
    public View getView(boolean create) {
        if(view == null && create) {
            view = createView();
        }
        if(view == null && create) {
            view = new View(this, getCaption());
        }
        return view;
    }

    /**
     * Create a View to display the grid when executed. If this method returns null, a default View will be created.
     *
     * @return A View with this grid as the component.
     */
    protected View createView() {
        return null;
    }

    /**
     * Caption used when displaying it in a View. If no caption was set using setCaption method, "Dashboard" will be returned.
     *
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
     *
     * @param caption Caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }
}
