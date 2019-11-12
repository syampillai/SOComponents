package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.server.AbstractStreamResource;

/**
 * Enhancement to Vaadin's {@link com.vaadin.flow.component.html.Image} component with proper resource handling.
 *
 * @author Syam
 */
public class Image extends com.vaadin.flow.component.html.Image implements ResourcedComponent {

    private static final String NOT_LOADED = "Image not loaded";
    private static final String NO_IMAGE = "No image";
    private final ResourceSupport resourceSupport;

    /**
     * Image constructor.
     */
    public Image() {
        super();
        resourceSupport = new ResourceSupport(this);
    }

    /**
     * Image from a URL.
     *
     * @param url Image URL
     */
    public Image(String url) {
        super();
        resourceSupport = new ResourceSupport(this);
        setSource(url);
    }

    /**
     * Image from a stream.
     *
     * @param resource Stream resource
     */
    public Image(AbstractStreamResource resource) {
        super();
        resourceSupport = new ResourceSupport(this);
        setSource(resource);
    }

    /**
     * Set the image resource source URL.
     *
     * @param source Image resource source URL
     */
    public void setSource(String source) {
        resourceSupport.clear();
        if(source == null) {
            setSource(new PaintedImageResource(null));
            setAlt(NO_IMAGE);
        } else {
            super.setSrc(source);
            setAlt(NOT_LOADED);
        }
    }


    /**
     * Set the image resource source.
     *
     * @param src Image resource source
     */
    @Override
    public void setSrc(String src) {
        setSource(src);
    }


    /**
     * Set the image resource source.
     *
     * @param source Image resource source
     */
    public void setSource(AbstractStreamResource source) {
        if(source == null) {
            setSource((String)null);
        } else {
            resourceSupport.register(source);
            setAlt(NOT_LOADED);
            super.setSrc(source);
        }
    }


    /**
     * Set the image resource source.
     *
     * @param source Image resource source
     */
    @Override
    public void setSrc(AbstractStreamResource source) {
        setSource(source);
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

    /**
     * Clear the content.
     */
    public void clear() {
        setSource((String)null);
    }
}