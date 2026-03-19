package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;

import java.util.*;
import java.util.stream.Stream;

/**
 * This was providing functionality similar to the new TabsSheet component in Vaadin. Now, this is deprecated and the
 * implementation is extended from Vaadin's TabSheet.
 *
 * @author Syam
 * @deprecated Use Vaadin's TabSheet instead.
 */
public class Tabs extends TabSheet {

    /**
     * Constructor.
     */
    public Tabs() {
        addSelectedChangeListener(e -> tabSelected(e.getSelectedTab()));
    }

    /**
     * This method will be invoked whenever a tab is selected.
     *
     * @param selected The selected tab
     */
    protected void tabSelected(Tab selected) {
    }

    /**
     * Get components under a given tab.
     *
     * @param tab Tab
     * @return Components as a Stream.
     */
    protected Stream<Component> getChildren(Tab tab) {
        return tab.getChildren();
    }

    /**
     * Create and add a tab with specified components.
     *
     * @param tabLabel Label for the tab to be created and added
     * @param components Components to be added to the tab
     * @return Newly created tab.
     */
    public Tab createTab(String tabLabel, Component... components) {
        Tab tab = new Tab(tabLabel);
        add(tab, components);
        return tab;
    }

    /**
     * Add a tab with its components.
     *
     * @param tab Tab to be added (it could be a tab that was already added)
     * @param components Components to be added under it
     */
    public void add(Tab tab, Component... components) {
        int i = getIndexOf(tab);
        if(i >= 0) {
            remove(tab);
        }
        if(components == null || components.length == 0) {
            super.add(tab, new Div(), i);
            return;
        }
        if(components.length == 1) {
            super.add(tab, components[0], i);
            return;
        }
        super.add(tab, new Div(components), i);
    }

    /**
     * Remove tabs.
     *
     * @param tabs Tabs to be removed.
     */
    public void remove(Tab... tabs) {
        for(Tab tab: tabs) {
            super.remove(tab);
        }
    }

    /**
     * Remove all tabs.
     */
    public void removeAll() {
        int c;
        while((c = getTabCount()) > 0) {
            super.remove(getTabAt(c - 1));
        }
    }
}