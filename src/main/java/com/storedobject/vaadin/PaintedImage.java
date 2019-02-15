package com.storedobject.vaadin;

import com.storedobject.vaadin.util.PaintedImageResource;
import com.vaadin.flow.component.html.Image;

import java.awt.*;

/**
 * Create an image from an AWT image painted on a {@link Graphics2D}. See {@link #paint(Graphics2D)}.
 *
 * @author Syam
 */
public abstract class PaintedImage extends Image {

    /**
     * Image type.
     */
    public enum Type {SVG, PNG, JPG, GIF}
    private static long fileId = 0;
    private final Type type;
    private int widthInPixels, heightInPixels;
    private String fileName;

    /**
     * Create a SVG image.
     */
    public PaintedImage() {
        this(Type.SVG);
    }

    /**
     * Create a 100x100 pixels image.
     * @param type Type of image to create.
     */
    public PaintedImage(Type type) {
        this(type, 100, 100);
    }

    /**
     * Create an image.
     * @param type Type of the image to create
     * @param widthInPixels Image width in pixels
     * @param heightInPixels Image height in pixels
     */
    public PaintedImage(Type type, int widthInPixels, int heightInPixels) {
        this.type = type;
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
        this.fileName = createBaseFileName() + "." + type.toString().toLowerCase();
        redraw();
    }

    /**
     * Create a base file name for the image.
     * @return Base file name (without extension).
     */
    public static String createBaseFileName() {
        synchronized (PaintedImage.class) {
            if (fileId == Long.MAX_VALUE) {
                fileId = 0;
            }
            ++fileId;
            return "no-so" + fileId;
        }
    }

    /**
     * Get the type of the image.
     * @return Type of the image.
     */
    public Type getType() {
        return type;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        redraw();
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        redraw();
    }

    /**
     * Redraw the image.
     */
    public void redraw() {
        setSrc(new PaintedImageResource(fileName, this, widthInPixels, heightInPixels));
    }

    /**
     * Paint the image to the {@link Graphics2D}
     * @param graphics {@link Graphics2D} on which image needs to be drawn
     */
    public abstract void paint(Graphics2D graphics);
}
