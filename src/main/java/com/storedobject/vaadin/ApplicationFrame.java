package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;

/**
 * An implementation of {@link ApplicationLayout} using Vaadin's {@link AppLayout}. The "drawer" area acts
 * as the "menu" area of the {@link ApplicationLayout}.
 *
 * @author Syam
 */
@CssImport(value = "./so/appframe/styles.css", themeFor = "vaadin-app-layout")
public abstract class ApplicationFrame extends AppLayout implements ApplicationLayout {

    private Menu menu = new Menu();
    private Component logo;
    private HasText captionComponent, userNameComponent;
    private ButtonLayout toolbox = new ButtonLayout();
    private boolean initialized = false;
    private Focusable<?> menuSearcher;

    /**
     * Constructor.
     */
    public ApplicationFrame() {
        ID.set(this);
        Component c;
        addToNavbar(c = new DrawerToggle());
        Style s = c.getElement().getStyle();
        s.set("color", "var(--lumo-primary-contrast-color)");
        s.set("cursor", "pointer");
        c = getMenuSearcher();
        if(c != null) {
            if(c instanceof Focusable) {
                menuSearcher = (Focusable<?>) c;
            }
            c.getElement().getStyle().set("flex-flow", "0");
            addToDrawer(c);
        }
        addToDrawer(menu);
    }

    private void noWrap(Component c) {
        Style s = c.getElement().getStyle();
        s.set("display", "block");
        s.set("white-space", "nowrap");
        s.set("overflow", "hidden");
        s.set("text-overflow", "ellipsis");
    }

    private Span filler() {
        Span filler = new Span();
        filler.getElement().getStyle().set("flex-grow", "1");
        return filler;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(!initialized) {
            initialized = true;
            if(getLogoInt() != null) {
                addToNavbar(logo);
            }
            if(getCaptionComponent() != null && captionComponent instanceof Component) {
                addToNavbar(true, (Component)captionComponent);
                noWrap((Component) captionComponent);
            }
            addToNavbar(filler());
            addToNavbar(true, filler());
            if(getUserNameComponent() != null && userNameComponent instanceof Component) {
                addToNavbar(true, (Component)userNameComponent);
                userNameComponent.getElement().getStyle().set("text-align", "right");
                noWrap((Component) userNameComponent);
            }
            if(getToolbox() != null) {
                Style s = toolbox.getStyle();
                s.set("margin-left", "10px");
                s.set("flex-wrap", "nowrap");
                addToNavbar(toolbox);
            }
        }
        super.onAttach(attachEvent);
    }

    /**
     * Set the caption of the {@link Application}.
     *
     * @param caption Caption
     */
    @Override
    public void setCaption(String caption) {
        if(getCaptionComponent() != null) {
            captionComponent.setText(caption);
        }
    }

    private Component getLogoInt() {
        if(logo == null) {
            logo = getLogo();
        }
        return logo;
    }

    /**
     * Get the Logo (typically an {@link com.vaadin.flow.component.html.Image} component) to be displayed on the "Nav Bar",
     * next to the "drawer menu" icon.
     *
     * @return Default implementation returns <code>null</code> so that no icon will be displayed.
     */
    public Component getLogo() {
        return null;
    }

    /**
     * Get the "toolbox" to display at the top-right corner of the screen (right most side of the "Nav Bar").
     *
     * @return A default {@link ButtonLayout} is returned but one can override this too. Otherwise, one may add his/her
     * "tool" buttons or icons into it.
     */
    public ButtonLayout getToolbox() {
        if(toolbox == null) {
            toolbox = new ButtonLayout();
        }
        return toolbox;
    }

    /**
     * Get the component to display the "caption" of the application. This will be displayed to the right of the "Logo"
     * on the "Nav Bar" with "touchOptimized" as <code>true</code> (see {@link AppLayout#addToNavbar(boolean, Component...)}).
     *
     * @return The default implementation returns an {@link H2} component.
     */
    public HasText getCaptionComponent() {
        if(captionComponent == null) {
            H2 c = new H2();
            c.getStyle().set("color", "var(--lumo-primary-contrast-color)");
            captionComponent = c;
        }
        return captionComponent;
    }

    /**
     * Get the component to display the "user name" for the application. This will be displayed just before the "toolbox"
     * on the "Nav Bar".
     *
     * @return The default implementation returns a {@link Span} component.
     */
    public HasText getUserNameComponent() {
        if(userNameComponent == null) {
            userNameComponent = new Span();
        }
        return userNameComponent;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public ApplicationMenu getMenu() {
        return menu;
    }

    /**
     * Open the side menu ("drawer" part of the {@link AppLayout}) programmatically.
     */
    @Override
    public void openMenu() {
        setDrawerOpened(true);
        if(menuSearcher != null) {
            menuSearcher.focus();
        }
    }


    /**
     * Close the side menu ("drawer" part of the {@link AppLayout}) programmatically.
     */
    @Override
    public void closeMenu() {
        setDrawerOpened(false);
    }

    /**
     * Toggle the side menu ("drawer" part of the {@link AppLayout}) programmatically.
     */
    @Override
    public void toggleMenu() {
        setDrawerOpened(!isDrawerOpened());
        if(menuSearcher != null && isDrawerOpened()) {
            menuSearcher.focus();
        }
    }

    /**
     * Closes the menu if it is displayed as an overlay.
     *
     * @param view Currently selected view
     */
    @Override
    public void viewSelected(View view) {
        if(isOverlay()) {
            setDrawerOpened(false);
        }
    }

    private static class Menu extends Div implements ApplicationMenu, HasSize {

        public Menu() {
            ID.set(this);
            getStyle().set("flex-grow", "1");
            new Scrollable(this);
        }

        @Override
        public void insert(int position, ApplicationMenuItem menuItem) {
            ApplicationMenu.super.insert(position, menuItem);
            top();
        }

        @Override
        public void remove(ApplicationMenuItem menuItem) {
            ApplicationMenu.super.remove(menuItem);
            top();
        }

        private void top() {
            Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "').scrollTop=0;");
        }
    }
}