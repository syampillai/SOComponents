package com.storedobject.vaadin.util;

import com.storedobject.vaadin.PaintedImage;
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

public class PaintedImageResource extends StreamResource {

    public PaintedImageResource(String fileName, PaintedImage image, int widthInPixels, int heightInPixels) {
        super(fileName, new ImageStream(image, widthInPixels, heightInPixels));
        setContentType(image.getType() == PaintedImage.Type.SVG ? "image/svg+xml" : "image/" + image.getType().toString().toLowerCase());
    }

    private static class ImageStream implements InputStreamFactory {

        private PaintedImage image;
        private int width, height;
        private ByteArrayInputStream bytestream = null;

        private ImageStream(PaintedImage image, int width, int height) {
            this.image = image;
            this.width = width;
            this.height = height;
        }

        @Override
        public InputStream createInputStream() {
            if(bytestream == null) {
                BufferedImage bi = null;
                Graphics2D graphics = null;
                if(image.getType() == PaintedImage.Type.SVG) {
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
                    if(image.getType() != PaintedImage.Type.SVG) {
                        graphics.setColor(Color.BLACK);
                        graphics.setBackground(new Color(0xB4, 0xC0, 0xC5));
                        graphics.clearRect(0, 0, width, height);
                    }
                }
                if(graphics != null) {
                    image.paint(graphics);
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    if(image.getType() == PaintedImage.Type.SVG) {
                        OutputStreamWriter ow = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                        if (graphics != null) {
                            ((SVGGraphics2D)graphics).stream(ow);
                        }
                        ow.close();
                    } else {
                        ImageIO.write(Objects.requireNonNull(bi), image.getType().toString().toLowerCase(), outputStream);
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
