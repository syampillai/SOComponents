package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

/**
 * Abstract "resourced component" that knows how to register/unregister the resource during attach/detach events.
 *
 * @author Syam
 */
public abstract class AbstractResourcedComponent extends Component implements ResourcedComponent, HasSize {

    private final ResourceSupport resourceSupport;

    /**
     * Default constructor.
     */
    public AbstractResourcedComponent() {
        this((String)null);
    }

    /**
     * Constructor with file to view.
     * @param fileURI URI of the file to view
     */
    public AbstractResourcedComponent(String fileURI) {
        resourceSupport = new ResourceSupport(this);
        if(fileURI != null) {
            setURI(fileURI);
        }
        setSizeFull();
        ID.set(this);
    }

    /**
     * Constructor with a stream resource to view.
     * @param streamResource Stream resource
     */
    public AbstractResourcedComponent(AbstractStreamResource streamResource) {
        resourceSupport = new ResourceSupport(this);
        if(streamResource != null) {
            setSource(streamResource);
        }
        setSizeFull();
        ID.set(this);
    }

    /**
     * Set the file to view.
     * @param fileURL URL of the file to view
     */
    public void setSource(String fileURL) {
        resourceSupport.clear();
        setURI(fileURL);
    }

    /**
     * Get the name of the URI attribute.
     *
     * @return URI attribute name.
     */
    protected abstract String getURIAttributeName();

    /**
     * Set the URI attribute for the source.
     *
     * @param url URL
     */
    protected void setURI(String url) {
        if(url == null) {
            getElement().removeAttribute(getURIAttributeName());
            clear();
        } else {
            getElement().setAttribute(getURIAttributeName(), url);
        }
    }

    /**
     * Set the file to view from a {@link StreamResource}.
     * @param streamResource Stream source
     */
    public void setSource(AbstractStreamResource streamResource) {
        resourceSupport.register(streamResource);
        String uri = resourceSupport.getURI();
        if(uri != null) {
            setURI(uri);
        }
    }

    /**
     * Clear the current content.
     */
    public void clear() {
        resourceSupport.clear();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        resourceSupport.register();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        resourceSupport.unregister();
    }
}