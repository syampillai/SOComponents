package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Dashboard is a "view" that can show multiple embedded "sub-views". Dashboard divides the display area into rows and columns and each
 * "sub-view" can span across one or more rows and columns.
 *
 * @author Syam
 */
public class Dashboard extends CSSGrid implements ExecutableView {

    private View view;
    private String caption;
    private final boolean boxing;
    private final Map<View, Registration> registrationMap = new HashMap<>();
    private final ViewMonitor viewMonitor = new ViewMonitor();

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
        setMinimumColumnWidth(null);
        setAutoarrange(true);
        style("align-items", "start");
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
     * Justify (horizontally) a view within its grid cell.
     *
     * @param view View
     * @param position Position
     */
    public void justify(View view, Position position) {
        justify(view.getComponent(), position);
    }

    /**
     * Align (vertically) a view within its grid cell.
     *
     * @param view View
     * @param position Position
     */
    public void align(View view, Position position) {
        align(view.getComponent(), position);
    }

    /**
     * Center (horizontally and vertically) a view within its grid cell.
     *
     * @param view View
     */
    public void center(View view) {
        Component component = view.getComponent();
        justify(component, Position.CENTER);
        align(component, Position.CENTER);
    }

    /**
     * Set the column-span for the particular sub-view. Minimum value is 1 and maximum allowed is 6.
     *
     * @param component Sub-view
     * @param numberOfColumns Number of columns to span
     */
    public void setColumnSpan(Component component, int numberOfColumns) {
        if(numberOfColumns > 6) {
            numberOfColumns = 6;
        }
        super.setColumnSpan(component, numberOfColumns);
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
     * @param view Sub-view
     * @param numberOfRows Number of rows to span
     */
    public void setRowSpan(View view, int numberOfRows) {
        setRowSpan(view.getComponent(), numberOfRows);
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
                new Box(c).alignSizing();
            }
            if(c instanceof HasSize) {
                ((HasSize)c).setWidth("100%");
                ((HasSize)c).setHeight("100%");
            }
        }
        super.add(components);
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
                    if(registrationMap.get(v) == null) {
                        registrationMap.put(v, v.addClosedListener(viewMonitor));
                        add(v.getComponent());
                    }
                }
            }
        }
    }

    /**
     * Remove sub-views to the dashboard.
     *
     * @param views Sub-views.
     */
    public void remove(View... views) {
        if(views != null) {
            for (View v : views) {
                if (v != null) {
                    v.abort();
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
    @Override
    public final View getView(boolean create) {
        if(view != null) {
            return view;
        }
        if(create) {
            view = createView();
        }
        if(view == null && create) {
            view = new View(this, getCaption()) {
                @Override
                public boolean isCloseable() {
                    return Dashboard.this.isCloseable();
                }
            };
        }
        if(view != null) {
            view.addClosedListener(v -> closeInt());
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
        } else {
            closeInt();
        }
    }

    /**
     * Closes the view by aborting if that exists
     */
    @Override
    public void abort() {
        if(view != null) {
            view.abort();
        } else {
            closeInt();
        }
    }

    private void closeInt() {
        new ArrayList<>(registrationMap.keySet()).forEach(View::abort);
        clean();
    }

    /**
     * This will be invoked whenever the dashboard is closed or aborted so that any resource clean-up can be run.
     */
    public void clean() {
    }

    /**
     * Set caption used when displaying the grid in a View.
     *
     * @param caption Caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    private class ViewMonitor implements ViewClosedListener {

        @Override
        public void viewClosed(View view) {
            remove(view.getComponent());
            Registration r = registrationMap.get(view);
            if(r != null) {
                r.remove();
                registrationMap.remove(view);
            }
        }
    }
}
