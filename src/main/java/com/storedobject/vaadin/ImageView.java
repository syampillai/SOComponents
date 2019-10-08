package com.storedobject.vaadin;

import com.vaadin.flow.server.AbstractStreamResource;

/**
 * A Home {@link View} that shows an image (typically used to show a background image).
 *
 * @author Syam
 */
public class ImageView extends View implements HomeView {

    private Image image;

    /**
     * Create the image from a URL.
     *
     * @param url Image URL
     */
    public ImageView(String url) {
        super("I");
        init();
        setSource(url);
    }

    /**
     * Create the image from a resource.
     *
     * @param resource Image resource
     */
    public ImageView(AbstractStreamResource resource) {
        super("I");
        init();
        setSource(resource);
    }

    private void init() {
        image = createImage();
        image.setSizeFull();
        setComponent(image);
    }

    /**
     * Construct the base image.
     *
     * @return Image
     */
    protected Image createImage() {
        return new Image((String)null);
    }

    /**
     * Set the URL resource of the image content.
     *
     * @param source URL resource
     */
    public void setSource(String source) {
        image.setSource(source);
    }

    /**
     * Set the stream resource of the image content.
     *
     * @param source Stream resource of the image content
     */
    public void setSource(AbstractStreamResource source) {
        image.setSource(source);
    }
}