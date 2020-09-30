package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

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
     * Set the "content" portion of the "layout". (Content portion - This is where the content of all {@link View}s
     * get added to.) This will the set by the {@link Application} instance.
     *
     * @param content Content component to be set
     */
    void setContent(Component content);

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
    default void loggedin(Application application) {
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
     * This method is invoked whenever a view is selected for display by application.
     *
     * @param view Currently selected view
     */
    default void viewSelected(View view) {
    }
}