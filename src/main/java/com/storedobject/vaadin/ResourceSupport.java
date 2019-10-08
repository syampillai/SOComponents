package com.storedobject.vaadin;

import com.vaadin.flow.server.*;

/**
 * A class to support resource handling.
 *
 * @author Syam
 */
public class ResourceSupport {

    private StreamRegistration streamRegistration;
    private AbstractStreamResource streamResource;
    private final ResourcedComponent component;

    public ResourceSupport(ResourcedComponent component) {
        this.component = component;
    }

    /**
     * Clear the resource (will be unregistered if required).
     */
    public void clear() {
        unregister();
        streamResource = null;
    }

    /**
     * Register a resource.
     *
     * @param streamResource Stream source
     */
    public void register(AbstractStreamResource streamResource) {
        if(streamResource == null) {
            clear();
            return;
        }
        unregister();
        this.streamResource = streamResource;
        streamRegistration = VaadinSession.getCurrent().getResourceRegistry().registerResource(streamResource);
    }

    /**
     * Register the resource if not yet done (typically invoked when the component is attached).
     */
    public void register() {
        if(streamResource != null && streamRegistration == null) {
            component.setSource(streamResource);
        }
    }

    /**
     * Unregister the resource (typically invoked when the component is detached).
     */
    public void unregister() {
        if (streamRegistration != null) {
            streamRegistration.unregister();
            streamRegistration = null;
        }
    }

    /**
     * Get the URI of the resource.
     *
     * @return The URI of the resource.
     */
    public String getURI() {
        return streamResource == null ? null : StreamResourceRegistry.getURI(streamResource).toASCIIString();
    }
}