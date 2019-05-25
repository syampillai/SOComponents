package com.storedobject.vaadin;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.util.ArrayList;

/**
 * Default implementation of {@link ApplicationMenuItem} and {@link ApplicationMenuItemGroup}.
 *
 * @see Application
 * @see ApplicationMenu
 * @author Syam
 */
@HtmlImport("so-menu-styles.html")
public class MenuItem extends Div implements ApplicationMenuItem {

    private Span caption = new Span();
    Icon icon;

    MenuItem(String label, Icon icon, Runnable runnable, boolean closeable) {
        this.icon = icon;
        setClassName("so-menu");
        setLabel(label);
        if(icon != null) {
            icon.setSize("1.25em");
            icon.setStyle("margin-right", "0.4em");
            add(icon);
            icon.getStyle().set("min-width", "1.25em");
        }
        if(runnable != null) {
            new Clickable<>(closeable ? caption : this, e -> runnable.run());
        }
        caption.getStyle().set("flex-grow", "100");
        add(caption);
    }

    public static ApplicationMenuItem create(String menuLabel, String icon, Runnable menuAction) {
        return new MenuItem(menuLabel, createIcon(icon), menuAction, false);
    }

    public static ApplicationMenuItem create(View view, String menuLabel, Runnable menuAction, boolean closeable) {
        Icon icon = createIcon(closeable ? "vaadin:close-circle" : null);
        icon.setColor(closeable ? "var(--lumo-error-color-50pct)" : "var(--lumo-primary-color-50pct)");
        if(closeable) {
            icon.setAttribute("title", "Close");
            new Clickable<>(icon, e -> view.abort());
        }
        return new MenuItem(menuLabel, icon, menuAction, closeable);
    }

    public static ApplicationMenuItemGroup createGroup(String menuLabel) {
        return new MenuItemGroup(menuLabel);
    }

    private static Icon createIcon(String icon) {
        return new Icon(icon == null || icon.isEmpty() ? "go" : icon);
    }

    @Override
    public void hilite() {
        setClassName("so-menu-hilite");
    }

    @Override
    public void dehilite() {
        setClassName("so-menu-active");
    }

    @Override
    public void setLabel(String caption) {
        this.caption.setText(caption);
    }

    private static class MenuItemGroup extends MenuItem implements ApplicationMenuItemGroup {

        private boolean plus = true;
        private ArrayList<ApplicationMenuItem> items = new ArrayList<>();

        MenuItemGroup(String label) {
            super(label, new Icon("vaadin:plus-circle"), null, false);
            new Clickable<>(this, e -> toggle());
        }

        private void toggle() {
            plus = !plus;
            icon.setIcon("vaadin:" + (plus ? "plus" : "minus") + "-circle");
            items.forEach(mi -> mi.getElement().setVisible(!plus));
        }

        @Override
        public void add(ApplicationMenuItem menuItem) {
            menuItem.getElement().setVisible(false);
            ((MenuItem)menuItem).icon.setColor("var(--lumo-contrast-30pct)");
            items.add(menuItem);
        }

        @Override
        public void remove(ApplicationMenuItem menuItem) {
            items.remove(menuItem);
        }
    }
}