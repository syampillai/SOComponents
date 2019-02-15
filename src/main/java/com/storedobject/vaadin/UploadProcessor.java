package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;

import java.io.*;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * A class to process uploaded content. The content will be available to the "processor" as an {@link InputStream}.
 * The content must be read in completely from stream by the processor's {@link BiConsumer#accept(Object, Object)}
 * method becasue the stream is automatically closed after invoking this method.
 * The "view" is automatically closed when all the uploaded files are processed.
 *
 * @author Syam
 */
public class UploadProcessor extends CloseableView {

    private Upload upload;
    private int fileCount = 0, maxFileCount = 1;
    private BiConsumer<InputStream, String> processor;

    /**
     * Constructor.
     * @param caption Caption
     * @param message Message to be shown
     * @param processor Processor to process the content
     */
    public UploadProcessor(String caption, String message, BiConsumer<InputStream, String> processor) {
        this(caption, new StyledText(message), processor);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param message Message to be shown
     * @param processor Processor to process the content
     */
    public UploadProcessor(String caption, Component message, BiConsumer<InputStream, String> processor) {
        super(caption);
        this.processor = processor;
        VerticalLayout v = new VerticalLayout();
        upload = new Upload(this::createStream);
        upload.setMaxFiles(maxFileCount);
        upload.addFailedListener(e -> { Application.error(e); abort(); });
        upload.addSucceededListener(e -> {
            ++fileCount;
            if(fileCount == maxFileCount) {
                close();
            }
        });
        v.add(message, upload);
        setComponent(new Window(v));
    }

    /**
     * Get the {@link Upload} component internally used. Any customization of this component is possible.
     * @return Upload component
     */
    public Upload getUploadComponent() {
        return upload;
    }

    private OutputStream createStream(String fileName, @SuppressWarnings("unused") String contentType) {
        if(processor == null) {
            return new OutputStream() {
                @Override
                public void write(int b) {
                }
            };
        }
        PipedOutputStream out = new PipedOutputStream();
        try {
            PipedInputStream in = new PipedInputStream(out);
            new Thread(() -> {
                processor.accept(in, fileName);
                try {
                    out.close();
                } catch (IOException ignore) {
                }
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }).start();
        } catch (IOException ignored) {
        }
        return out;
    }

    /**
     * Set number of files that are allowed upload.
     * @param fileCount Number of files
     */
    public void setMaxFiles(int fileCount) {
        this.maxFileCount = fileCount <= 0 ? 1 : fileCount;
    }
}
