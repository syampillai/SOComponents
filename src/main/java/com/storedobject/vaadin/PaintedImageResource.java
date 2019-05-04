package com.storedobject.vaadin;

import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Create an image resource from an AWT image painted on a {@link Graphics2D}.
 *
 * @author Syam
 */
public class PaintedImageResource extends StreamResource {

    private static long fileId = 0;

    /**
     * Create a 100x100 pixels SVG image resource.
     *
     * @param painter Image painter
     */
    public PaintedImageResource(Consumer<Graphics2D> painter) {
        this(PaintedImage.Type.SVG, painter);
    }

    /**
     * Create a 100x100 pixels image resource.
     *
     * @param painter Image painter
     * @param type Type of image to create.
     */
    public PaintedImageResource(PaintedImage.Type type, Consumer<Graphics2D> painter) {
        this(type, painter,100,100);
    }

    /**
     * Create an image resource.
     *
     * @param imageType Type of the image to create
     * @param painter Image painter
     * @param widthInPixels Image width in pixels
     * @param heightInPixels Image height in pixels
     */
    public PaintedImageResource(PaintedImage.Type imageType, Consumer<Graphics2D> painter, int widthInPixels, int heightInPixels) {
        super(createBaseFileName() + "." + imageType.toString().toLowerCase(), new PaintedImageResource.ImageStream(imageType, widthInPixels, heightInPixels, painter));
        setContentType(imageType == PaintedImage.Type.SVG ? "image/svg+xml" : "image/" + imageType.toString().toLowerCase());
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

    private static class ImageStream implements InputStreamFactory {

        private PaintedImage.Type imageType;
        private int width, height;
        private ByteArrayInputStream bytestream = null;
        private Consumer<Graphics2D> painter;

        private ImageStream(PaintedImage.Type imageType, int width, int height, Consumer<Graphics2D> painter) {
            this.imageType = imageType;
            this.width = width;
            this.height = height;
            this.painter = painter;
        }

        @Override
        public InputStream createInputStream() {
            if(bytestream == null) {
                BufferedImage bi = null;
                Graphics2D graphics = null;
                if(imageType == PaintedImage.Type.SVG) {
                    DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
                    Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
                    graphics = new SVGGraphics2D(document);
                } else {
                    if(width > 0 && height > 0) {
                        bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                        graphics = bi.createGraphics();
                    }
                }
                if(width > 0 && height > 0) {
                    graphics.setClip(0, 0, width, height);
                    if(imageType != PaintedImage.Type.SVG) {
                        graphics.setColor(Color.BLACK);
                        graphics.setBackground(new Color(0xB4, 0xC0, 0xC5));
                        graphics.clearRect(0, 0, width, height);
                    }
                }
                if(graphics != null && painter != null) {
                    painter.accept(graphics);
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    if(imageType == PaintedImage.Type.SVG) {
                        OutputStreamWriter ow = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                        if (graphics != null) {
                            ((SVGGraphics2D)graphics).stream(ow);
                        }
                        ow.close();
                    } else {
                        ImageIO.write(Objects.requireNonNull(bi), imageType.toString().toLowerCase(), outputStream);
                    }
                    outputStream.close();
                    bytestream = new ByteArrayInputStream(outputStream.toByteArray());
                    if (graphics != null) {
                        graphics.dispose();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                bytestream.reset();
            }
            return bytestream;
        }
    }
}