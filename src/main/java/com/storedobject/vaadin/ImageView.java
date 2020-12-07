package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.AbstractStreamResource;

/**
 * A Home {@link View} that shows an image (typically used to show a background image).
 *
 * @author Syam
 */
public class ImageView extends View implements HomeView {

    private final Image image;

    /**
     * Create an empty image view. Image source can be set later.
     */
    public ImageView() {
        this((Image)null);
    }

    /**
     * Create the image view from a URL.
     *
     * @param url Image URL.
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
     * @param image Image.
     */
    public ImageView(Image image) {
        super("I");
        this.image = image == null ? new Image((String)null) : image;
        this.image.setSizeFull();
        this.image.getStyle().set("object-fit", "fill");
    }

    @Override
    protected void initUI() {
        Component c = getImageComponent(image);
        setComponent(c == null ? image : c);
    }

    @Override
    public void decorateComponent() {
        super.decorateComponent();
        getComponent().getElement().getStyle().set("padding", "0px");
    }

    /**
     * Set the URL resource of the image content.
     *
     * @param source URL resource.
     */
    public void setSource(String source) {
        image.setSource(source);
    }

    /**
     * Set the stream resource of the image content.
     *
     * @param source Stream resource of the image content.
     */
    public void setSource(AbstractStreamResource source) {
        image.setSource(source);
    }

    /**
     * Get the image component that will be set as the component of this view. The default implementation returns
     * the {@link Div} with image embedded in it. The image will be stretched or squished to fit.
     *
     * @param image Image of this view.
     * @return A component that will be set as the component of this view using {@link #setComponent(Component)}.
     */
    public Component getImageComponent(Image image) {
        Div div = new Div(image);
        div.setSizeFull();
        div.getStyle().set("overflow", "hidden");
        return div;
    }
}