package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * In order to display a "closeable vew", {@link Application} uses a special "menu item" with a "Close" icon
 * and the "view" can be closed by clicking on that icon.
 *
 * @author Syam
 */
public class CloseableView extends View {

    /**
     * Create a View with no caption.
     */
    public CloseableView() {
        this(null);
    }

    /**
     * Create a view with a caption.
     * @param caption Caption
     */
    public CloseableView(String caption) {
        this(null, caption);
    }

    /**
     * Create a view of the specific component with a caption
     * @param component Component to display
     * @param caption Caption
     */
    public CloseableView(Component component, String caption) {
        super(component, caption);
    }
}
