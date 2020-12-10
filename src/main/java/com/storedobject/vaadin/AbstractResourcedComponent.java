package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

/**
 * Abstract "resourced component" that knows how to register/unregister the resource during attach/detach events.
 * By default, it will set he "display" style to "flex" and size to "full". Inherited classes may override this if
 * required.
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
        init();
        resourceSupport = new ResourceSupport(this);
        if(fileURI != null) {
            setURI(fileURI);
        }
    }

    /**
     * Constructor with a stream resource to view.
     * @param streamResource Stream resource
     */
    public AbstractResourcedComponent(AbstractStreamResource streamResource) {
        init();
        resourceSupport = new ResourceSupport(this);
        if(streamResource != null) {
            setSource(streamResource);
        }
    }

    /**
     * This will be invoked by the constructor for initial set up. The default implementation sets the
     * "display" style to "flex" and size to "full". I also sets a unique "id" attribute.
     */
    protected void init() {
        ID.set(this);
        getElement().getStyle().set("display", "flex");
        setSizeFull();
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
     * Get the name of the URI attribute. The default value returned is "src".
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