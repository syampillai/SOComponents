package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.shared.Registration;

import java.util.*;
import java.util.stream.Stream;

/**
 * This class wraps Vaadin's {@link com.vaadin.flow.component.tabs.Tabs} with a {@link Div}. Components can be added under each
 * {@link Tab} and will go in the {@link Div} when the respective {@link Tab} is selected.
 *
 * @author Syam
 */
public class Tabs extends Composite<Div> {

    private final Div container = new Div();
    private final com.vaadin.flow.component.tabs.Tabs tabs;
    private List<ComponentEventListener<SelectedChangeEvent>> listeners;
    private final Map<Tab, List<Component>> componentMap = new HashMap<>();

    /**
     * Constructor.
     */
    public Tabs() {
        this.container.getStyle().set("display", "flex").set("flex-direction", "column");
        this.container.setSizeFull();
        this.tabs = new com.vaadin.flow.component.tabs.Tabs(false);
        this.tabs.addSelectedChangeListener(e -> {
            SelectedChangeEvent sce = new SelectedChangeEvent(this, e.getPreviousTab(), e.isFromClient());
            tabSelected(sce.selectedTab);
            if(listeners != null) {
                listeners.forEach(listener -> listener.onComponentEvent(sce));
            }
        });
        container.add(this.tabs);
    }

    /**
     * Initialize the wrapper.
     *
     * @return The Div that is created.
     */
    @Override
    protected final Div initContent() {
        return container;
    }

    /**
     * Get the container {@link Div}.
     *
     * @return The container.
     */
    protected Div getContainer() {
        return container;
    }

    /**
     * Get the embedded {@link com.vaadin.flow.component.tabs.Tabs}.
     *
     * @return The embedded tabs component.
     */
    public com.vaadin.flow.component.tabs.Tabs getTabs() {
        return tabs;
    }

    /**
     * This method will be invoked whenever a tab is selected. By default, the container will be cleared
     * and then, all components under the selected tab are added to the container. However, this method may be
     * overridden to change that behaviour.
     *
     * @param selected The selected tab
     */
    protected void tabSelected(Tab selected) {
        container.getChildren().filter(c -> c != tabs).forEach(container::remove);
        if(selected == null) {
            return;
        }
        getChildren(selected).forEach(container::add);
    }

    /**
     * Get components under a given tab.
     *
     * @param tab Tab
     * @return Components as a Stream.
     */
    protected Stream<Component> getChildren(Tab tab) {
        return componentMap.get(tab).stream();
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
        List<Component> componentList = componentMap.get(tab);
        if(componentList != null) {
            Collections.addAll(componentList, components);
            if(tabs.getSelectedTab() == tab) {
                tabSelected(tab);
            }
            return;
        }
        componentList = new ArrayList<>();
        Collections.addAll(componentList, components);
        componentMap.put(tab, componentList);
        boolean first = this.tabs.getTabCount() == 0;
        this.tabs.add(tab);
        if(first) {
            setSelectedTab(tab);
        }
    }

    /**
     * Remove tabs.
     *
     * @param tabs Tabs to be removed.
     */
    public void remove(Tab... tabs) {
        for(Tab tab: tabs) {
            getChildren(tab).forEach(container::remove);
            componentMap.remove(tab);
        }
        this.tabs.remove(tabs);
    }

    /**
     * Remove all tabs.
     */
    public void removeAll() {
        componentMap.clear();
        container.removeAll();
        this.tabs.removeAll();
    }

    /**
     * Replace an old tab with a new one.
     *
     * @param oldTab Old tab to be replaced
     * @param newTab New to tab to be set
     */
    public void replace(Tab oldTab, Tab newTab) {
        this.tabs.replace(oldTab, newTab);
        List<Component> components = componentMap.get(oldTab);
        componentMap.remove(oldTab);
        componentMap.put(newTab, components);
    }

    /**
     * Get the currently selected tab.
     *
     * @return Currently selected tab.
     */
    public Tab getSelectedTab() {
        return tabs.getSelectedTab();
    }

    /**
     * Set a tab as the selected one.
     *
     * @param tab Tab to be selected.
     */
    public void setSelectedTab(Tab tab) {
        tabs.setSelectedTab(tab);
    }

    /**
     * Set a tab that contains a particular component as the selected one.
     *
     * @param component Component
     */
    public void setSelectedTab(Component component) {
        setSelectedTab(getTab(component));
    }

    /**
     * Get the index of the selected tab.
     *
     * @return Index of the selected tab.
     */
    public int getSelectedIndex() {
        return tabs.getSelectedIndex();
    }

    /**
     * Set a tab with a given index as the selected one.
     *
     * @param index Index of the tab to be selected
     */
    public void setSelectedIndex(int index) {
        tabs.setSelectedIndex(index);
    }

    /**
     * Get the tab that contains the given component.
     *
     * @param component Component
     * @return Tab that contains the component.
     */
    public Tab getTab(Component component) {
        return componentMap.entrySet().stream().filter(set -> set.getValue().contains(component)).map(Map.Entry::getKey).findAny().orElse(null);
    }

    /**
     * Add a tab selection listener.
     *
     * @param listener Listener to be added
     * @return Registration for the listener that is added.
     */
    public Registration addSelectedChangeListener(ComponentEventListener<SelectedChangeEvent> listener) {
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    /**
     * Set the orientation of the tabs.
     *
     * @param orientation Orientation
     */
    public void setOrientation(com.vaadin.flow.component.tabs.Tabs.Orientation orientation) {
        tabs.setOrientation(orientation);
    }

    /**
     * Get the current orientation of the tabs.
     *
     * @return The current orientation.
     */
    public com.vaadin.flow.component.tabs.Tabs.Orientation getOrientation() {
        return tabs.getOrientation();
    }

    /**
     * The event class that will be fired when tab selection changes.
     */
    public static class SelectedChangeEvent extends ComponentEvent<Tabs> {

        private final Tab selectedTab;
        private final Tab previousTab;
        private final boolean initialSelection;

        /**
         * Constructor.
         *
         * @param source Source of the event
         * @param previousTab The previously selected tab
         * @param fromClient Whether fired from client side or not
         */
        public SelectedChangeEvent(Tabs source, Tab previousTab, boolean fromClient) {
            super(source, fromClient);
            this.selectedTab = source.tabs.getSelectedTab();
            this.initialSelection = source.tabs.isAutoselect() && previousTab == null && !fromClient;
            this.previousTab = previousTab;
        }

        /**
         * Get the selected tab.
         *
         * @return The selected tab.
         */
        public Tab getSelectedTab() {
            return this.selectedTab;
        }

        /**
         * Get the previously selected tab.
         *
         * @return The previously selected tab, could be <code>null</code>.
         */
        public Tab getPreviousTab() {
            return this.previousTab;
        }

        /**
         * Check if this is the initial selection or not.
         *
         * @return True or false.
         */
        public boolean isInitialSelection() {
            return this.initialSelection;
        }
    }
}