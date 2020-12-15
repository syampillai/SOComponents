package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.Style;

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
     * Get the width of the content area.
     *
     * @return Width.
     */
    default String getContentWidth() {
        return null;
    }

    /**
     * Get the height of the content area.
     *
     * @return Height.
     */
    default String getContentHeight() {
        return null;
    }

    /**
     * Set the "content" portion of the "layout". (Content portion - This is where the content of all {@link View}s
     * get added to.) This will the set by the {@link Application} instance.
     *
     * @param content Content component to be set
     */
    void setContent(Component content);

    /**
     * Resize the "content" portion of the "layout" to the desired size. This default implementation is suitable
     * for almost all the cases. Otherwise, your overridden method should invoke this as part of your
     * implementation because this sets some important style variables for sizing the "content" area.
     */
    default void resizeContent() {
        UI ui = UI.getCurrent();
        if(ui == null) {
            return;
        }
        Style s = ui.getElement().getStyle();
        String size = getContentWidth();
        if(size == null) {
            size = "100vw";
        }
        s.set("--so-content-width", size);
        size = getContentHeight();
        if(size == null) {
            size = "100vh";
        }
        s.set("--so-content-height", size);
    }

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