package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;

/**
 * A helper {@link com.storedobject.vaadin.View} class that is used to view a single component.
 *
 * @author Syam
 */
public class Viewer extends View implements CloseableView {

    /**
     * Constructor.
     *
     * @param caption Caption.
     */
    public Viewer(String caption) {
        super(caption);
    }

    /**
     * Constructor.
     *
     * @param component Component to set.
     * @param caption Caption.
     * @param windowMode Whether to shown as a pop-window or not.
     */
    public Viewer(Component component, String caption, boolean windowMode) {
        this(caption);
        setComponent(windowMode ? createWindow(component) : component);
    }

    /**
     * Create the "window" to show the content. This should be invoked to obtain the {@link Window} component
     * only if the {@link View} has to appear as a popped-up window. The window will have a size of 80vw X 80vh by
     * default unless .
     *
     * @param component Component containing the actual content to display.
     * @return A window containing the content that is passed.
     */
    protected Window createWindow(Component component) {
        Window w = new Window(new WindowDecorator(this), component);
        w.setWidth(Math.min(95, Math.max(20, getViewWidth())) + "vw");
        w.setHeight(Math.min(95, Math.max(20, getViewWidth())) + "vh");
        if(component instanceof HasSize) {
            ((HasSize) component).setHeight("90%");
        }
        new Scrollable(component);
        return w;
    }

    /**
     * Get the width of the view (as a percentage). Default is 80.
     *
     * @return Percentage width of the view.
     */
    protected int getViewWidth() {
        return 80;
    }

    /**
     * Get the height of the view (as a percentage). Default is 80.
     *
     * @return Percentage height of the view.
     */
    protected int getViewHeight() {
        return 80;
    }

    /**
     * Decorate the component to remove the default padding.
     */
    @Override
    public void decorateComponent() {
        super.decorateComponent();
        getComponent().getElement().getStyle().set("padding", "0px");
    }
}
