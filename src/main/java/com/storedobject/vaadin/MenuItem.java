package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ClickNotifier;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

public class MenuItem implements Runnable, ClickNotifier {

    private Component item;
    private String label;
    private Runnable action;
    private Clickable click;

    MenuItem(String label, Component item, Runnable action) {
        this.label = label;
        this.item = item;
        item.getElement().setAttribute("tabindex", "-1");
        item.getElement().getStyle().set("cursor", "pointer");
        setRunnable(action);
        this.click = new Clickable<>(item, e -> run());
    }

    public void hilite() {
        item.getElement().getStyle().set("background-color", "white");
    }


    public Component getComponent() {
        return item;
    }

    public String getLabel() {
        return label;
    }

    public void setVisible(boolean visible) {
        item.setVisible(visible);
    }

    public boolean isVisible() {
        return item.isVisible();
    }

    public void setEnabled(boolean enabled) {
        item.getElement().setEnabled(enabled);
    }

    public boolean isEnabled() {
        return item.getElement().isEnabled();
    }

    public static MenuItem create(String menuLabel) {
        return create(menuLabel, (Runnable)null);
    }

    public static MenuItem create(String menuLabel, Runnable action) {
        return new MenuItem(menuLabel, new Item(menuLabel), action);
    }

    public static MenuItem create(String menuLabel, String icon) {
        return create(menuLabel, null, icon);
    }

    public static MenuItem create(String menuLabel, String iconCollection, String icon) {
        return create(menuLabel, iconCollection, icon, null);
    }

    public static MenuItem create(String menuLabel, String icon, Runnable action) {
        return create(menuLabel, null, icon, action);
    }

    public static MenuItem create(String menuLabel, String iconCollection, String icon, Runnable action) {
        return new MenuItem(menuLabel, new IconItem(menuLabel, iconCollection, icon), action);
    }

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

    public void setRunnable(Runnable action) {
        this.action = action;
    }

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

    @Tag("paper-item")
    @HtmlImport("frontend://bower_components/paper-item/paper-item.html")
    static class Item extends Component {

        Item(String menu) {
            getElement().setText(menu);
        }
    }

    @Tag("paper-icon-item")
    @HtmlImport("frontend://bower_components/iron-icons/iron-icons.html")
    @HtmlImport("frontend://bower_components/paper-item/paper-icon-item.html")
    static class IconItem extends Component implements IconElement {

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
    }

    public void setIcon(String icon) {
        setIcon(null, icon);
    }

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