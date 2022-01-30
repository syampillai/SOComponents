package com.storedobject.vaadin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.SubMenu;

/**
 * This component provides the same functionality of Vaadin's {@link com.vaadin.flow.component.menubar.MenuBar}.
 * However, it is easier to use it in your code.
 *
 * @author Syam
 */
public class MenuBar extends MenuBarItem {

    private final com.vaadin.flow.component.menubar.MenuBar bar = new com.vaadin.flow.component.menubar.MenuBar();

    @Override
    protected com.vaadin.flow.component.menubar.MenuBar initContent() {
        return bar;
    }

    @Override
    public MenuBarItem addMenuItem(String caption, ClickHandler clickHandler) {
        TransferredClickHandler ch = new TransferredClickHandler(clickHandler);
        MenuBarItem mi = new MenuItem(bar.addItem(caption, ClickHandler.convert(ch)), false);
        ch.setAnother(mi);
        return mi;
    }

    @Override
    public MenuBarItem addMenuItem(Component component, ClickHandler clickHandler) {
        TransferredClickHandler ch = new TransferredClickHandler(clickHandler);
        MenuBarItem mi = new MenuItem(bar.addItem(component, ClickHandler.convert(ch)), false);
        ch.setAnother(mi);
        return mi;
    }

    @Override
    public MenuBarItem addMenuItem(String caption) {
        return new MenuItem(bar.addItem(caption), true);
    }

    @Override
    public MenuBarItem addMenuItem(Component component) {
        return new MenuItem(bar.addItem(component), true);
    }

    /**
     * Removes the given menu items from this menu bar.
     * @param items Menu items to remove.
     */
    @Override
    public void remove(MenuBarItem... items) {
        if(items != null) {
            for(MenuBarItem item: items) {
                if(item instanceof MenuItem mi) {
                    bar.remove(mi.menuItem);
                }
            }
        }
    }

    /**
     * Removes all menu items from this menu bar.
     */
    @Override
    public void removeAll() {
        bar.removeAll();
    }

    /**
     * Sets the event which opens the sub menus of the root level buttons.
     * @param openOnHover <code>true</code> to make the sub menus open on hover (mouseover),
     *                    <code>false</code> to make them openable by clicking.
     */
    public void setOpenOnHover(boolean openOnHover) {
        bar.setOpenOnHover(openOnHover);
    }

    /**
     * Gets whether the sub menus open by clicking or hovering on the root level buttons.
     * @return <code>true</code> if the sub menus open by hovering on the root level buttons, <code>false</code>
     * if they open by clicking.
     */
    public boolean isOpenOnHover() {
        return bar.isOpenOnHover();
    }

    /**
     * Get Vaadin's {@link com.vaadin.flow.component.menubar.MenuBar} embedded in this.
     * @return Vaadin's menu bar. Direct manipulation of this for adding/removing components is discouraged.
     * However, you may use it for the manipulation themes, internationalization etc.
     */
    public com.vaadin.flow.component.menubar.MenuBar getBar() {
        return bar;
    }

    private static class MenuItem extends MenuBarItem {

        private final com.vaadin.flow.component.contextmenu.MenuItem menuItem;
        private final SubMenu subMenu;

        private MenuItem(com.vaadin.flow.component.contextmenu.MenuItem menuItem, boolean forSub) {
            this.menuItem = menuItem;
            if(forSub) {
                subMenu = menuItem.getSubMenu();
            } else {
                subMenu = null;
            }
        }

        @Override
        protected com.vaadin.flow.component.contextmenu.MenuItem initContent() {
            return menuItem;
        }

        @Override
        public MenuBarItem addMenuItem(String caption, ClickHandler clickHandler) {
            if(subMenu == null) {
                return null;
            } else {
                TransferredClickHandler ch = new TransferredClickHandler(clickHandler);
                MenuBarItem mi = new MenuItem(subMenu.addItem(caption, ClickHandler.convert(ch)), false);
                ch.setAnother(mi);
                return mi;
            }
        }

        public MenuBarItem addMenuItem(Component component, ClickHandler clickHandler) {
            if(subMenu == null) {
                return null;
            } else {
                TransferredClickHandler ch = new TransferredClickHandler(clickHandler);
                MenuBarItem mi = new MenuItem(subMenu.addItem(component, ClickHandler.convert(ch)), false);
                ch.setAnother(mi);
                return mi;
            }
        }

        @Override
        public MenuBarItem addMenuItem(String caption) {
            if(subMenu == null) {
                return null;
            } else {
                return new MenuItem(subMenu.addItem(caption), true);
            }
        }

        @Override
        public MenuBarItem addMenuItem(Component component) {
            if(subMenu == null) {
                return null;
            } else {
                return new MenuItem(subMenu.addItem(component), true);
            }
        }

        @Override
        public void remove(MenuBarItem... items) {
            if(subMenu != null) {
                for(MenuBarItem mi: items) {
                    if(mi instanceof MenuItem m) {
                        subMenu.remove(m.menuItem);
                    }
                }
            }
        }

        @Override
        public void removeAll() {
            if(subMenu != null) {
                subMenu.removeAll();
            }
        }

        @Override
        public void setCheckable(boolean checkable) {
            if(subMenu == null) {
                menuItem.setCheckable(checkable);
            }
        }

        @Override
        public boolean isCheckable() {
            return subMenu == null && menuItem.isCheckable();
        }

        @Override
        public void setChecked(boolean checked) {
            if(subMenu == null) {
                menuItem.setChecked(checked);
            }
        }

        @Override
        public boolean isChecked() {
            return subMenu == null && menuItem.isChecked();
        }
    }

    private static class TransferredClickHandler implements ClickHandler {

        private final ClickHandler clickHandler;
        private Component another;

        private TransferredClickHandler(ClickHandler clickHandler) {
            this.clickHandler = clickHandler;
        }

        void setAnother(Component another) {
            this.another = another;
        }

        @Override
        public void clicked(Component c) {
            if(clickHandler != null && another != null) {
                clickHandler.clicked(another);
            }
        }

        @Override
        public void onComponentEvent(ClickEvent<? extends Component> event) {
            if(clickHandler != null && another != null) {
                clickHandler.onComponentEvent(new ModifiedClickEvent<>(event, another));
            }
        }
    }
}
