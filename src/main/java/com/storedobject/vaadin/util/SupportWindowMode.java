package com.storedobject.vaadin.util;

import com.storedobject.vaadin.View;
import com.storedobject.vaadin.Window;

/**
 * An interface to denote that a class supports "window mode" and can create a {@link com.storedobject.vaadin.Window}
 * when asked for.
 *
 * @author Syam
 */
public interface SupportWindowMode {

    /**
     * Create a {@link Window} with the given {@link View} as its content. (Default implementation returns null).
     *
     * @param view View to set as content.
     * @return Window created.
     */
    default Window createWindow(View view) {
        return null;
    }
}
