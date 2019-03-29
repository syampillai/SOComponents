package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;

/**
 * This class is a special "menu item" that can close a {@link View} by clicking on the "Close" icon displayed
 * along with its label.
 *
 * @author Syam
 */
public class CloseableMenuItem extends MenuItem {

    private boolean expanded = false;

    private CloseableMenuItem(String label, CloseableIconItem item, Runnable action) {
        super(label, item, action);
    }

    /**
     * Create a menu item group.
     * @param menuLabel Label
     * @param view View to be closed
     * @param action Action for the menu
     * @return Menu item group created.
     */
    public static CloseableMenuItem create(String menuLabel, Runnable action, View view) {
        return new CloseableMenuItem(menuLabel, new CloseableIconItem(menuLabel, view), action);
    }

    @HtmlImport("frontend://bower_components/paper-item/paper-item-body.html")
    static class CloseableIconItem extends Item {

        private Div label;

        CloseableIconItem(String menu, final View view) {
            super(null);
            Element body = new Element("paper-item-body");
            label = new Div();
            label.setText(menu);
            body.appendChild(label.getElement());
            getElement().appendChild(body);
            Element closeIcon = new Element("iron-icon");
            closeIcon.setAttribute("icon", "vaadin:close");
            closeIcon.setAttribute("title", "Close");
            getElement().appendChild(closeIcon);
            ElementClick click = new ElementClick(this, closeIcon);
            click.addClickListener(e -> view.close());
        }

        @Override
        public void setLabel(String label) {
            this.label.setText(label);
        }
    }
}
