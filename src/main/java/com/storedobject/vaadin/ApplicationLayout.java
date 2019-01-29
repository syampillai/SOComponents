package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dialog.Dialog;

/**
 * {@link Application} requires an instance of this class to determine the "layout" of its page.
 * @author Syam
 */
public interface ApplicationLayout {

    /**
     * The component of the "layout".
     * @return The main layout component.
     */
    Component getComponent();

    /**
     * The "content" portion of the "layout".
     * @return The content portion.
     */
    Component getContent();

    /**
     * The "menu" of the layout.
     * @return Menu of the layout.
     */
    ApplicationMenu getMenu();

    /**
     * This method should draw the "menu". It will be invoked when the application comes up.
     * @param application Application
     */
    void drawMenu(Application application);

    /**
     * This method is invoked when user authentication completed. Typically, its used to dusplay the user names
     * and other details in some part of the screen.
     * @param application Application.
     */
    default void loggedin(@SuppressWarnings("unused") Application application) {
    }

    /**
     * Add a "view" to the application. The default implementation adds the view's component to the "content" area.
     * @param view View to be added.
     */
    default void addView(View view) {
        Component c = view.getComponent();
        if(!(c instanceof Dialog) && c instanceof HasSize) {
            ((HasSize) c).setWidth("98%");
        }
        getContent().getElement().appendChild(c.getElement());
    }
}