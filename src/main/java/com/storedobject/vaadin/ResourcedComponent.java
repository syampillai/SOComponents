package com.storedobject.vaadin;

import com.vaadin.flow.server.AbstractStreamResource;

/**
 * A component that requires a resource.
 * 
 * @author Syam
 */
public interface ResourcedComponent {

    /**
     * Set the resource.
     *
     * @param streamResource Resource to set
     */
    void setSource(AbstractStreamResource streamResource);
}