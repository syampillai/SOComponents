package com.storedobject.vaadin;

import com.vaadin.flow.component.html.Image;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Create an image from an AWT image painted on a {@link Graphics2D}. See {@link #paint(Graphics2D)}.
 *
 * @author Syam
 */
public class PaintedImage extends Image {

    private Consumer<Graphics2D> painter;

    /**
     * Image type.
     */
    public enum Type { SVG, PNG, JPG, GIF }
    private final Type type;
    private int widthInPixels, heightInPixels;

    /**
     * Create a 100x100 pixels SVG image.
     */
    public PaintedImage() {
        this(Type.SVG);
    }

    /**
     * Create a 100x100 pixels image.
     *
     * @param type Type of image to create.
     */
    public PaintedImage(Type type) {
        this(type, null);
    }

    /**
     * Create an image.
     *
     * @param type Type of the image to create
     * @param widthInPixels Image width in pixels
     * @param heightInPixels Image height in pixels
     */
    public PaintedImage(Type type, int widthInPixels, int heightInPixels) {
        this(type, null, widthInPixels, heightInPixels);
    }

    /**
     * Create a 100x100 pixels SVG image.
     *
     * @param painter Image painter
     */
    public PaintedImage(Consumer<Graphics2D> painter) {
        this(Type.SVG, painter);
    }

    /**
     * Create a 100x100 pixels image.
     *
     * @param painter Image painter
     * @param type Type of image to create.
     */
    public PaintedImage(Type type, Consumer<Graphics2D> painter) {
        this(type, painter,100,100);
    }

    /**
     * Create an image.
     *
     * @param type Type of the image to create
     * @param painter Image painter
     * @param widthInPixels Image width in pixels
     * @param heightInPixels Image height in pixels
     */
    public PaintedImage(Type type, Consumer<Graphics2D> painter, int widthInPixels, int heightInPixels) {
        this.type = type;
        this.painter = painter;
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
        redraw();
    }

    /**
     * Get the type of the image.
     *
     * @return Type of the image.
     */
    public Type getType() {
        return type;
    }

    /**
     * Set width of the image.
     *
     * @param width Width of the image in pixels
     */
    public void setImageWidth(int width) {
        this.widthInPixels = width;
        redraw();
    }

    /**
     * Get image width.
     *
     * @return Width of the image in pixels.
     */
    public int getImageWidth() {
        return widthInPixels;
    }

    /**
     * Set height of the image.
     *
     * @param height Height of the image in pixels
     */
    public void setImageHeight(int height) {
        this.heightInPixels = height;
        redraw();
    }

    /**
     * Get image height.
     *
     * @return Height of the image in pixels.
     */
    public int getImageHeight() {
        return heightInPixels;
    }

    /**
     * Redraw the image.
     */
    public void redraw() {
        setSrc(new PaintedImageResource(type, this::paint, widthInPixels, heightInPixels));
    }

    /**
     * Set a painter. This will redraw the image.
     *
     * @param painter Painter that paints the image to the {@link Graphics2D}.
     */
    public void setPainter(Consumer<Graphics2D> painter) {
        this.painter = painter;
        redraw();
    }

    /**
     * Get the current painter.
     *
     * @return Current painter.
     */
    public Consumer<Graphics2D> getPainter() {
        return painter;
    }

    /**
     * Paint the image to the {@link Graphics2D}. if there is a painter already set, it will be invoked to do the painting.
     *
     * @param graphics {@link Graphics2D} on which image needs to be drawn
     */
    public void paint(Graphics2D graphics) {
        if(painter != null) {
            painter.accept(graphics);
        }
    }
}