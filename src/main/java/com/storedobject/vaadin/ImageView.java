package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.server.AbstractStreamResource;

/**
 * A Home {@link View} that shows an image (typically used to show a background image).
 *
 * @author Syam
 */
public class ImageView extends View implements HomeView {

    private Image image;

    /**
     * Create an empty image view. Image source can be set later.
     */
    public ImageView() {
        this((Image)null);
    }

    /**
     * Create the image view from a URL.
     *
     * @param url Image URL
     */
    public ImageView(String url) {
        this((Image)null);
        setSource(url);
    }

    /**
     * Create the image view from a resource.
     *
     * @param resource Image resource
     */
    public ImageView(AbstractStreamResource resource) {
        this((Image)null);
        setSource(resource);
    }

    /**
     * Create the image view from the given image.
     * @param image Image
     */
    public ImageView(Image image) {
        super("I");
        this.image = image == null ? new Image((String)null) : image;
    }

    @Override
    protected void initUI() {
        Component c = getImageComponent(image);
        setComponent(c == null ? image : c);
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

    /**
     * Get the image component that will be set as the component of this view. The default implementation returns the
     * image itself after setting its maximum width to (90vh - 10px). View components, typically, have 5 pixels margin on
     * all sides and 90vh height. So, the image will be scaled to fill up the entire background, leaving 5 pixels
     * margins.
     *
     * @param image Image of this view
     * @return A component that will be set as the component of this view using {@link #setComponent(Component)}.
     */
    public Component getImageComponent(Image image) {
        image.setSizeFull();
        image.getElement().getStyle().set("max-height", "calc(90vh - 10px)");
        return image;
    }
}