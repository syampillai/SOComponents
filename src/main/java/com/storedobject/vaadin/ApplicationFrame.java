package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.shared.Registration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * An implementation of {@link ApplicationLayout} using Vaadin's {@link AppLayout}. The "drawer" area acts
 * as the "menu" area of the {@link ApplicationLayout}.
 *
 * @author Syam
 */
@CssImport(value = "./so/appframe/styles.css", themeFor = "vaadin-app-layout")
public abstract class ApplicationFrame extends AppLayout implements ApplicationLayout {

    private Content content;
    private Menu menu = new Menu();
    private Component logo;
    private HasText captionComponent, userNameComponent;
    private ButtonLayout toolbox = new ButtonLayout();
    private boolean initialized = false;

    /**
     * Constructor.
     */
    public ApplicationFrame() {
        ID.set(this);
        content = new Content();
        Component c;
        addToNavbar(c = new DrawerToggle());
        Style s = c.getElement().getStyle();
        s.set("color", "var(--lumo-primary-contrast-color)");
        s.set("cursor", "pointer");
        c = getMenuSearcher();
        if(c == null) {
            menu.setHeightFull();
        } else {
            addToDrawer(c);
            menu.setHeight("95%");
        }
        addToDrawer(menu);
        setContent(content);
    }

    private void toGrow(Component c) {
        c.getElement().getStyle().set("flex-grow", "1");
    }

    private void noWrap(Component c) {
        Style s = c.getElement().getStyle();
        s.set("display", "block");
        s.set("white-space", "nowrap");
        s.set("overflow", "hidden");
        s.set("text-overflow", "ellipsis");
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
            if(getUserNameComponent() != null && userNameComponent instanceof Component) {
                addToNavbar(true, (Component)userNameComponent);
                userNameComponent.getElement().getStyle().set("text-align", "right");
                toGrow((Component) userNameComponent);
                noWrap((Component) userNameComponent);
            } else {
                if(captionComponent instanceof Component) {
                    toGrow((Component) captionComponent);
                } else if(logo != null) {
                    toGrow(logo);
                }
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
    public Component getContent() {
        return content;
    }

    @Override
    public ApplicationMenu getMenu() {
        return menu;
    }

    /**
     * Add a "content resized listener" to this layout so that the listener will be alerted whenever the content area is resized.
     * This will be useful for those "views" that want to react to the content area size changes.
     *
     * @param listener Listener
     *
     * @return Registration.
     */
    public Registration addContentResizedListener(ContentResizedListener listener) {
        return content.addContentResizedListener(listener);
    }

        @Tag("so-app-content")
    @JsModule("./so/appframe/content.js")
    private static class Content extends Component {

        private ArrayList<WeakReference<ContentResizedListener>> resizeListeners;
        private int width = -1, height = -1;

        private Content() {
            getElement().setProperty("idContent", "so" + ID.newID());
        }

        @Override
        protected void onAttach(AttachEvent attachEvent) {
            super.onAttach(attachEvent);
            getElement().setProperty("size", "x");
        }

        @ClientCallable
        private void resized(int w, int h) {
            if(width == w && height == h) {
                return;
            }
            this.width = w;
            this.height = h;
            if(resizeListeners != null) {
                resizeListeners.removeIf(wr -> wr.get() == null);
                resizeListeners.forEach(wr -> {
                    ContentResizedListener l = wr.get();
                    if(l != null) {
                        l.contentResized(width, height);
                    }
                });
            }
        }

        /**
         * Add a browser resized listener to this application so that the listener will be alerted whenever the browser is resized.
         *
         * @param listener Listener
         * @return Registration.
         */
        private Registration addContentResizedListener(ContentResizedListener listener) {
            if(listener == null) {
                return null;
            }
            if(resizeListeners == null) {
                resizeListeners = new ArrayList<>();
            }
            resizeListeners.add(new WeakReference<>(listener));
            return () -> {
                for(WeakReference<ContentResizedListener> w: resizeListeners) {
                    if(w.get() == listener) {
                        resizeListeners.remove(w);
                        return;
                    }
                }
            };
        }
    }

    /**
     * Open the side menu ("drawer" part of the {@link AppLayout}) programmatically.
     */
    @Override
    public void openMenu() {
        setDrawerOpened(true);
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
    }

    /**
     * Speak out a sentence through the speaker.
     *
     * @param sentence Text to be spoken out.
     */
    @Override
    public void speak(String sentence) {
        if(sentence.indexOf('"') >= 0) {
            sentence = sentence.replace("\"", "");
        }
        js("speak(\"" + sentence + "\")");
    }

    private void js(String js) {
        Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "')." + js + ";");
    }

    private static class Menu extends Div implements ApplicationMenu, HasSize {

        public Menu() {
            ID.set(this);
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

    @FunctionalInterface
    public interface ContentResizedListener {
        void contentResized(int width, int height);
    }
}