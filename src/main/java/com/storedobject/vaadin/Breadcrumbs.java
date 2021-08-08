package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Breadcrumbs component. A {@link ClickHandler} may be used to make the {@link Breadcrumb}s clickable.
 * If the {@link Breadcrumb}s are clickable, the {@link ClickHandler}'s {@link ClickHandler#clicked(Component)}
 * method will be invoked when it is clicked and the {@link Breadcrumb}s up to the clicked one will be pruned.
 *
 * @author Syam
 */
public class Breadcrumbs extends Composite<ButtonLayout> {

    private final List<Breadcrumb> breadcrumbs = new ArrayList<>();
    private final ButtonLayout layout = new ButtonLayout();
    private final ClickHandler clickHandler;

    /**
     * Constructor.
     */
    public Breadcrumbs() {
        this(null);
    }

    /**
     * Constructor with a "Click Handler".
     * @param clickHandler Click handler
     */
    public Breadcrumbs(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    protected ButtonLayout initContent() {
        return layout;
    }

    /**
     * Create a new {@link Breadcrumb} and add it to this.
     *
     * @param caption Caption text of the {@link Breadcrumb} to be created
     * @return The {@link Breadcrumb} that is created and added.
     */
    public Breadcrumb add(String caption) {
        return caption == null ? null : new Breadcrumb(this, caption);
    }

    /**
     * Remove the {@link Breadcrumb} from the tail end if one exists.
     */
    public void remove() {
        if(!breadcrumbs.isEmpty()) {
            remove(breadcrumbs.get(breadcrumbs.size() - 1));
        }
    }

    /**
     * Remove a given {@link Breadcrumb}. All {@link Breadcrumb} appearing after the given one will be removed.
     *
     * @param breadcrumb {@link Breadcrumb} to be removed
     */
    public void remove(Breadcrumb breadcrumb) {
        if(breadcrumb == null) {
            return;
        }
        int i = breadcrumbs.indexOf(breadcrumb);
        if(i < 0) {
            return;
        }
        removeInt(i);
    }

    private void removeInt(int i) {
        int n = breadcrumbs.size() - 1;
        while(n >= i) {
            breadcrumbs.get(n).remove();
            --n;
        }
        if(clickHandler != null && !breadcrumbs.isEmpty()) {
            breadcrumbs.get(breadcrumbs.size() - 1).disable();
        }
    }

    /**
     * Jump to a given {@link Breadcrumb}. All {@link Breadcrumb} appearing after the given one will be removed.
     *
     * @param breadcrumb {@link Breadcrumb} to go to
     */
    public void goTo(Breadcrumb breadcrumb) {
        if(breadcrumb == null) {
            return;
        }
        int i = breadcrumbs.indexOf(breadcrumb);
        if(i < 0 || i == (breadcrumbs.size() - 1)) {
            return;
        }
        goToInt(i);
    }

    private void goToInt(int i) {
        removeInt(i + 1);
    }

    /**
     * Get the parent of the given breadcrumb.
     *
     * @param breadcrumb Breadcrumb for which parent needs to be obtained.
     * @return Parent or <code>null</code> if it is the first breadcrumb.
     */
    public Breadcrumb getParent(Breadcrumb breadcrumb) {
        int i = breadcrumbs.indexOf(breadcrumb);
        return i <= 0 ? null : breadcrumbs.get(i - 1);
    }

    /**
     * Get the child of the given breadcrumb.
     *
     * @param breadcrumb Breadcrumb for which child needs to be obtained.
     * @return Parent or <code>null</code> if it is the last breadcrumb.
     */
    public Breadcrumb getChild(Breadcrumb breadcrumb) {
        int i = breadcrumbs.indexOf(breadcrumb);
        if(i < 0) {
            return null;
        }
        ++i;
        return i < breadcrumbs.size() ? breadcrumbs.get(i) : null;
    }

    /**
     * Get the head breadcrumb.
     *
     * @return The breadcrumb at the head. Could be <code>null</code>.
     */
    public Breadcrumb getHead() {
        return breadcrumbs.isEmpty() ? null : breadcrumbs.get(0);
    }

    /**
     * Stream all child breadcrumbs.
     *
     * @return Stream of child breadcrumbs.
     */
    public Stream<Breadcrumb> streamChildren() {
        return breadcrumbs.stream();
    }

    /**
     * Component that represents a "breadcrumb".
     */
    public static class Breadcrumb extends Badge {

        private final long id = ID.newID();
        private final Breadcrumbs bc;
        private Icon icon;
        private boolean disabled = true;
        private List<BreadcrumbRemovalListener> listeners;

        private Breadcrumb(Breadcrumbs bc, String caption) {
            super(caption);
            this.bc = bc;
            if(!bc.breadcrumbs.isEmpty()) {
                icon = new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT);
                bc.layout.add(icon);
                if(bc.clickHandler != null) {
                    bc.breadcrumbs.get(bc.breadcrumbs.size() - 1).enable();
                }
            }
            bc.layout.add(this);
            bc.breadcrumbs.add(this);
            disable();
            if(bc.clickHandler != null) {
                new Clickable<>(this, e -> clicked());
            }
        }

        private void disable() {
            Style style = getElement().getStyle();
            style.remove("cursor");
            style.set("color", "grey");
            disabled = true;
        }

        private void enable() {
            if(bc.clickHandler != null) {
                Style style = getElement().getStyle();
                style.set("cursor", "pointer");
                style.remove("color");
                disabled = false;
            }
        }

        private void remove() {
            bc.breadcrumbs.remove(this);
            bc.layout.remove(this);
            if(icon != null) {
                bc.layout.remove(icon);
            }
            if(listeners != null) {
                listeners.forEach(listener -> listener.removed(this));
            }
        }

        private void clicked() {
            if(!disabled && bc.clickHandler != null) {
                bc.goToInt(bc.breadcrumbs.indexOf(this));
                bc.clickHandler.clicked(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        /**
         * Add a listener to this so that it will be notified when this "breadcrumb" is removed.
         *
         * @param listener Listener
         * @return Registration object that can be used for removing the listener.
         */
        public Registration addRemovalListener(BreadcrumbRemovalListener listener) {
            if(listeners == null) {
                listeners = new ArrayList<>();
            }
            listeners.add(listener);
            return () -> listeners.remove(listener);
        }
    }

    /**
     * Listener interface for "breadcrumb removal listener".
     */
    @FunctionalInterface
    public interface BreadcrumbRemovalListener {
        /**
         * This method will be invoked whenever the specific breadcrumb is removed.
         * @param breadcrumb "Breadcrumb" that is removed
         */
        void removed(Breadcrumb breadcrumb);
    }
}
