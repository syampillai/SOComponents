package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dialog.Dialog;

/**
 * {@link Application} requires an instance of this class to determine the "layout" of its page.
 *
 * @author Syam
 */
public interface ApplicationLayout {

    /**
     * Set the caption of the application.
     *
     * @param caption Caption component
     */
    default void setCaption(String caption) {
    }

    /**
     * The component of the "layout".
     *
     * @return The main layout component.
     */
    Component getComponent();

    /**
     * The "content" portion of the "layout".
     *
     * @return The content portion.
     */
    Component getContent();

    /**
     * The "menu" of the layout.
     *
     * @return Menu of the layout.
     */
    ApplicationMenu getMenu();

    /**
     * This method should draw the "menu". It will be invoked when the application comes up.
     *
     * @param application Application
     */
    void drawMenu(Application application);

    /**
     * Get the "menu searcher" component.
     *
     * @return Default implementation returns <code>null</code>.
     */
    default Component getMenuSearcher() {
        return null;
    }


    /**
     * This method is invoked when user authentication completed. Typically, its used to display the user names
     * and other details in some part of the screen.
     *
     * @param application Application.
     */
    default void loggedin(@SuppressWarnings("unused") Application application) {
    }

    /**
     * Add a "view" to the application. The default implementation adds the view's component to the "content" area.
     * If the content of the view supports sizing and it's not a {@link Dialog} , the width will be set to 100% and height to 90vh.
     *
     * @param view View to be added.
     */
    default void addView(View view) {
        Component c = view.getComponent();
        if(!(c instanceof Dialog)) {
            if(c instanceof HasSize) {
                ((HasSize)c).setWidth("100%");
                ((HasSize)c).setHeight("90vh");
            }
            getContent().getElement().appendChild(c.getElement());
        }
    }

    /**
     * Remove a "view" from the application. The default implementation removes view's component from its parent.
     *
     * @param view View to be removed.
     */
    default void removeView(View view) {
        view.getComponent().getElement().removeFromParent();
    }

    /**
     * This method is for toggling the menu display (default implementation does nothing).
     */
    default void toggleMenu() {
    }

    /**
     * This method is for opening the menu display (default implementation does nothing).
     */
    default void openMenu() {
    }

    /**
     * This method is for closing the menu display (default implementation does nothing).
     */
    default void closeMenu() {
    }

    /**
     * Speak out the given sentence.
     *
     * @param sentence Text to be spoken out.
     */
    default void speak(String sentence) {
    }
}