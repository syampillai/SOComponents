package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;

/**
 * This class is a special "menu item" that keeps a group of other "menu items". The action associated with this
 * toggles the visibility of "menu items" under it.
 *
 * @author Syam
 */
public class MenuItemGroup extends MenuItem {

    private boolean expanded = false;
    private Div layout;
    private IconItemGroup header;

    /**
     * For internal use only.
     * @param label Label
     * @param header Header
     */
    MenuItemGroup(String label, IconItemGroup header) {
        super(label, header, null);
        this.header = header;
        layout = new Div();
        layout.add(header);
        setVisible(false);
    }

    /**
     * Create a menu item group.
     * @param menuLabel Label
     * @return Menu item group created.
     */
    public static MenuItemGroup create(String menuLabel) {
        return create(menuLabel, "vaadin:plus");
    }

    /**
     * Create a menu item group.
     * @param menuLabel Label
     * @param icon Name of the icon
     * @return Menu item group created.
     */
    public static MenuItemGroup create(String menuLabel, String icon) {
        return create(menuLabel, null, icon);
    }

    /**
     * Create a menu item group.
     * @param menuLabel Label
     * @param iconCollection Name of the icon collection
     * @param icon Name of the icon
     * @return Menu item group created.
     */
    public static MenuItemGroup create(String menuLabel, String iconCollection, String icon) {
        return new MenuItemGroup(menuLabel, new IconItemGroup(menuLabel, iconCollection, icon));
    }

    /**
     * Get the internal layout component of this menu item group.
     * @return Component.
     */
    @Override
    public Component getComponent() {
        return layout;
    }

    /**
     * Add a menu item to this.
     * @param menuItem Menu item
     */
    public void add(MenuItem menuItem) {
        layout.add(menuItem.getComponent());
        setVisible(true);
        menuItem.getComponent().setVisible(expanded);
        menuItem.hilite();
    }

    /**
     * Remove a menu item from this.
     * @param menuItem Menu item
     */
    public void remove(MenuItem menuItem) {
        if(layout.getChildren().noneMatch(c -> c == menuItem.getComponent())) {
            return;
        }
        layout.remove(menuItem.getComponent());
        setVisible(layout.getChildren().anyMatch(c -> c != header));
    }

    /**
     * Toggles the visiblity of the "menu items" under this menu item group.
     */
    @Override
    public void run() {
        expanded = !expanded;
        layout.getChildren().filter(c -> c != header).forEach(c -> c.setVisible(expanded));
        setIcon(header.expandIcon, "vaadin", expanded ? "angle-down" : "angle-right");
    }

    @HtmlImport("frontend://bower_components/paper-item/paper-item-body.html")
    static class IconItemGroup extends IconItem {

        private Element expandIcon;

        IconItemGroup(String menu, String iconCollection, String icon) {
            super(null, iconCollection, icon);
            Element body = new Element("paper-item-body");
            Div d = new Div();
            d.setText(menu);
            body.appendChild(d.getElement());
            getElement().appendChild(body);
            expandIcon = new Element("iron-icon");
            expandIcon.setAttribute("icon", "vaadin:angle-right");
            getElement().appendChild(expandIcon);
        }
    }
}
