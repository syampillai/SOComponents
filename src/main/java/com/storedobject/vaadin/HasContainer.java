package com.storedobject.vaadin;

import com.vaadin.flow.component.HasComponents;

/**
 * An interface to denote that the class contains a {@link com.vaadin.flow.component.HasComponents} (component
 * container) embedded in it.
 *
 * @author Syam
 */
public interface HasContainer {

    /**
     * Get the component container of this class.
     *
     * @return Container.
     */
    HasComponents getContainer();
}