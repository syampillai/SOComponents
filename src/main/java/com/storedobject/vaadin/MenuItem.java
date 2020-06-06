package com.storedobject.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
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
@CssImport("./so/menu/styles.css")
public class MenuItem extends Div implements ApplicationMenuItem {

    final Icon icon;
    int level = 0;
    private final Span caption = new Span();

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
            new Clickable<>(closeable ? caption : this, e -> {
                UI ui = getUI().orElse(null);
                if(ui != null && UI.getCurrent() == ui) {
                    runnable.run();
                }
            });
        }
        caption.getStyle().set("flex-grow", "100");
        add(caption);
    }

    /**
     * Create a menu item.
     *
     * @param menuLabel Label.
     * @param icon Icon.
     * @param menuAction Action.
     * @return Menu item.
     */
    public static ApplicationMenuItem create(String menuLabel, String icon, Runnable menuAction) {
        return new MenuItem(menuLabel, createIcon(icon), menuAction, false);
    }

    /**
     * Create a menu item for a {@link View}.
     *
     * @param view View.
     * @param menuLabel Label.
     * @param menuAction Action.
     * @return Menu item.
     */
    public static ApplicationMenuItem create(ExecutableView view, String menuLabel, Runnable menuAction) {
        boolean closeable = view.isCloseable();
        Icon icon = createIcon(closeable ? "vaadin:close-circle" : view.getMenuIconName());
        if(closeable) {
            icon.setAttribute("title", "Close");
            new Clickable<>(icon, e -> view.abort());
        }
        return new MenuItem(menuLabel, icon, menuAction, closeable);
    }

    /**
     * Create a group menu item.
     *
     * @param menuLabel Label.
     * @return Group menu item.
     */
    public static ApplicationMenuItemGroup createGroup(String menuLabel) {
        return new MenuItemGroup(menuLabel);
    }

    private static Icon createIcon(String icon) {
        return new Icon(icon == null ? "vaadin:chevron_circle_right" : icon);
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

    /**
     * Default implementation of {@link ApplicationMenuItemGroup}.
     *
     * @author Syam
     */
    private static class MenuItemGroup extends MenuItem implements ApplicationMenuItemGroup {

        private boolean plus = true;
        private final ArrayList<MenuItem> items = new ArrayList<>();

        private MenuItemGroup(String label) {
            this(label, new Icon("vaadin:plus-circle"));
        }

        private MenuItemGroup(String label, Icon icon) {
            super(label, icon, null, false);
            new Clickable<>(this, e -> toggle());
        }

        private void toggle() {
            plus = !plus;
            icon.setIcon("vaadin:" + (plus ? "plus" : "minus") + "-circle");
            items.forEach(mi -> mi.setVisible(!plus));
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            items.forEach(mi -> mi.setVisible(!plus && isVisible()));
        }

        @Override
        public void add(ApplicationMenuItem menuItem) {
            if(!(menuItem instanceof MenuItem)) {
                return;
            }
            MenuItem mi = (MenuItem) menuItem;
            mi.level = level + 1;
            int n = 40 + (mi.level * 125);
            mi.icon.setStyle("margin-left", (n / 100) + "." + (n % 100) + "em");
            mi.setVisible(!plus && isVisible());
            items.add(mi);
        }

        @Override
        public void remove(ApplicationMenuItem menuItem) {
            if(!(menuItem instanceof MenuItem)) {
                return;
            }
            items.remove(menuItem);
        }
    }
}