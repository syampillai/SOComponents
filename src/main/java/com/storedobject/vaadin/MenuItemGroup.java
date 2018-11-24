package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;

public class MenuItemGroup extends MenuItem {

    private boolean expanded = false;
    private Div layout;
    private IconItemGroup header;

    MenuItemGroup(String label, IconItemGroup header) {
        super(label, header, null);
        this.header = header;
        layout = new Div();
        layout.add(header);
        setVisible(false);
    }

    public static MenuItemGroup create(String menuLabel) {
        return create(menuLabel, "vaadin:plus");
    }

    public static MenuItemGroup create(String menuLabel, String icon) {
        return create(menuLabel, null, icon);
    }

    public static MenuItemGroup create(String menuLabel, String iconCollection, String icon) {
        return new MenuItemGroup(menuLabel, new IconItemGroup(menuLabel, iconCollection, icon));
    }

    @Override
    public Component getComponent() {
        return layout;
    }

    public void add(MenuItem menuItem) {
        layout.add(menuItem.getComponent());
        setVisible(true);
        menuItem.getComponent().setVisible(expanded);
        menuItem.hilite();
    }

    public void remove(MenuItem menuItem) {
        if(layout.getChildren().noneMatch(c -> c == menuItem.getComponent())) {
            return;
        }
        layout.remove(menuItem.getComponent());
        setVisible(layout.getChildren().anyMatch(c -> c != header));
    }

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
