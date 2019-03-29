package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ClickNotifier;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

/**
 * <p>WARNING: This class may be restructured or redesigned.</p>
 * Class that represents a "menu item".
 *
 * @see Application
 * @see ApplicationMenu
 * @author Syam
 */
public class MenuItem implements Runnable, ClickNotifier {

    private Component item;
    private String label;
    private Runnable action;
    private Clickable click;

    /**
     * Constructor (for internal use only).
     * @param label Label
     * @param item Item
     * @param action Action
     */
    MenuItem(String label, Component item, Runnable action) {
        this.label = label;
        this.item = item;
        item.getElement().setAttribute("tabindex", "-1");
        item.getElement().getStyle().set("cursor", "pointer");
        setRunnable(action);
        this.click = new Clickable<>(item, e -> run());
    }

    /**
     * Highlight this menu item.
     */
    public void hilite() {
        item.getElement().getStyle().set("background-color", "white");
    }


    /**
     * Get the component of the menu item.
     * @return Component
     */
    public Component getComponent() {
        return item;
    }

    /**
     * Get the label of the menu item.
     * @return Label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set thhe label of the menu item.
     *
     * @param label New label value
     */
    public void setLabel(String label) {
        this.label = label;
        if(item instanceof HasLabel) {
            ((HasLabel) item).setLabel(label);
        }
    }

    /**
     * Set the visibility of the menu item.
     * @param visible True or false
     */
    public void setVisible(boolean visible) {
        item.setVisible(visible);
    }

    /**
     * Check if the menu item is visible or not.
     * @return True ot false.
     */
    public boolean isVisible() {
        return item.isVisible();
    }

    /**
     * Emable or disable the menu item.
     * @param enabled True or false
     */
    public void setEnabled(boolean enabled) {
        item.getElement().setEnabled(enabled);
    }

    /**
     * Check if this is enabled or not.
     * @return True or false.
     */
    public boolean isEnabled() {
        return item.getElement().isEnabled();
    }

    /**
     * Create a menu item.
     * @param menuLabel Label
     * @return Menu item.
     */
    public static MenuItem create(String menuLabel) {
        return create(menuLabel, (Runnable)null);
    }

    /**
     * Create a menu item.
     * @param menuLabel Label
     * @param action Action associated with the menu item
     * @return Menu item.
     */
    public static MenuItem create(String menuLabel, Runnable action) {
        return new MenuItem(menuLabel, new Item(menuLabel), action);
    }

    /**
     * Create a menu item.
     * @param menuLabel Label
     * @param icon Icon name
     * @return Menu item.
     */
    public static MenuItem create(String menuLabel, String icon) {
        return create(menuLabel, null, icon);
    }

    /**
     * Create a menu item.
     * @param menuLabel Label
     * @param iconCollection Name of the icon collection
     * @param icon Icon name
     * @return Menu item.
     */
    public static MenuItem create(String menuLabel, String iconCollection, String icon) {
        return create(menuLabel, iconCollection, icon, null);
    }

    /**
     * Create a menu item.
     * @param menuLabel Label
     * @param icon Icon name
     * @param action Action associated with the menu item
     * @return Menu item.
     */
    public static MenuItem create(String menuLabel, String icon, Runnable action) {
        return create(menuLabel, null, icon, action);
    }

    /**
     * Create a menu item.
     * @param menuLabel Label
     * @param iconCollection Name of the icon collection
     * @param icon Icon name
     * @param action Action associated with the menu item
     * @return Menu item.
     */
    public static MenuItem create(String menuLabel, String iconCollection, String icon, Runnable action) {
        return new MenuItem(menuLabel, new IconItem(menuLabel, iconCollection, icon), action);
    }

    /**
     * Invoke the action associated with the menu item.
     */
    @Override
    public void run() {
        if(action != null) {
            try {
                action.run();
            } catch(Throwable any) {
                Application.error(any);
            }
        } else {
            Application.error("Don't know what to do!");
        }
    }

    /**
     * Set the action for this menu item.
     * @param action Action
     */
    public void setRunnable(Runnable action) {
        this.action = action;
    }

    /**
     * Get the action associated with this menu item.
     * @return Action
     */
    public Runnable getRunnable() {
        return action;
    }

    @Override
    public Registration addClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        return click.addClickListener(listener);
    }

    @Override
    public void removeClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        click.removeClickListener(listener);
    }

    interface HasLabel {
        void setLabel(String label);
    }

    @Tag("paper-item")
    @HtmlImport("frontend://bower_components/paper-item/paper-item.html")
    static class Item extends Component implements HasLabel {

        Item(String menu) {
            getElement().setText(menu);
        }

        @Override
        public void setLabel(String label) {
            getElement().removeAllChildren();
            if(label != null) {
                getElement().appendChild(Element.createText(label));
            }
        }
    }

    @Tag("paper-icon-item")
    @HtmlImport("frontend://bower_components/iron-icons/iron-icons.html")
    @HtmlImport("frontend://bower_components/paper-item/paper-icon-item.html")
    static class IconItem extends Component implements IconElement, HasLabel {

        private Element ironIcon;

        IconItem(String menu, String iconCollection, String icon) {
            ironIcon = new Element("iron-icon");
            setIcon(ironIcon, iconCollection, icon);
            ironIcon.setAttribute("slot", "item-icon");
            getElement().appendChild(ironIcon);
            if(menu != null) {
                getElement().appendChild(Element.createText(menu));
            }
        }

        @Override
        public Element getIconElement() {
            return ironIcon;
        }

        @Override
        public void setLabel(String label) {
            getElement().removeAllChildren();
            getElement().appendChild(ironIcon);
            if(label != null) {
                getElement().appendChild(Element.createText(label));
            }
        }
    }

    /**
     * Set icon for this menu item.
     * @param icon Icon name
     */
    public void setIcon(String icon) {
        setIcon(null, icon);
    }

    /**
     * Set icon for this menu item.
     * @param collection Name of the icon collection
     * @param icon Icon name
     */
    public void setIcon(String collection, String icon) {
        if(item instanceof IconElement) {
            setIcon(((IconElement) item).getIconElement(), collection, icon);
        }
    }

    static void setIcon(Element element, String collection, String icon) {
        if(icon.contains(":") || collection == null || collection.trim().isEmpty()) {
            icon = icon.toLowerCase();
        } else {
            icon = collection.toLowerCase().trim() + ":" + icon.toLowerCase().trim();
        }
        element.setAttribute("icon", icon);
    }

    private interface IconElement {
        Element getIconElement();
    }
}