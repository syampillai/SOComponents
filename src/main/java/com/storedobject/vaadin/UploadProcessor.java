package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;

import java.io.*;
import java.util.function.BiConsumer;

/**
 * A class to process uploaded content. The content will be available to the "processor" as an {@link InputStream}.
 * The content must be read in completely from stream by the processor's {@link BiConsumer#accept(Object, Object)}
 * method becasue the stream is automatically closed after invoking this method.
 * By default, the "view" is automatically closed when all the uploaded files are processed unless "auto close" flag is
 * unset.
 *
 * @author Syam
 */
public class UploadProcessor extends CloseableView {

    private Upload upload;
    private int fileCount = 0, maxFileCount = 1;
    private BiConsumer<InputStream, String> processor;
    private String fileName;
    private HasText message;
    private Application application;
    private boolean autoClose = true;

    /**
     * Constructor.
     * @param caption Caption
     * @param message Message to be shown
     */
    public UploadProcessor(String caption, String message) {
        this(caption, new StyledText(message), null);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param message Message to be shown
     */
    public UploadProcessor(String caption, Component message) {
        this(caption, message, null);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param message Message to be shown
     * @param processor Processor to process the content (Parameters: content, mime type)
     */
    public UploadProcessor(String caption, String message, BiConsumer<InputStream, String> processor) {
        this(caption, new StyledText(message), processor);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param message Message to be shown
     * @param processor Processor to process the content (Parameters: content, mime type)
     */
    public UploadProcessor(String caption, Component message, BiConsumer<InputStream, String> processor) {
        super(caption);
        this.processor = processor;
        VerticalLayout v = new VerticalLayout();
        upload = new Upload(this::createStream);
        upload.setMaxFiles(maxFileCount);
        upload.addFailedListener(e -> { Application.error(e); abort(); });
        upload.addFinishedListener(e -> ++fileCount);
        v.add(message, upload);
        setComponent(v);
        if(message instanceof HasText) {
            this.message = (HasText)message;
        }
    }

    /**
     * Get the {@link Upload} component internally used. Any customization of this component is possible.
     * @return Upload component
     */
    public Upload getUploadComponent() {
        return upload;
    }

    private OutputStream createStream(String fileName, String mimeType) {
        application.startPolling(this);
        this.fileName = fileName;
        PipedOutputStream out = new PipedOutputStream();
        try {
            PipedInputStream in = new PipedInputStream(out);
            new Thread(() -> {
                process(in, mimeType);
                try {
                    out.close();
                } catch (IOException ignore) {
                }
                try {
                    in.close();
                } catch (IOException ignore) {
                }
                application.stopPolling(this);
                if(autoClose && fileCount == maxFileCount) {
                    application.access(this::close);
                }
            }).start();
        } catch (IOException ignored) {
        }
        return out;
    }

    /**
     * Set the processor
     * @param processor Processor to process the content (Parameters: content, mime type)
     */
    public void setProcessor(BiConsumer<InputStream, String> processor) {
        this.processor = processor;
    }

    /**
     * Set the message.
     * @param message Message to be set
     */
    public void setMessage(String message) {
        if(this.message != null && application != null) {
            application.startPolling(this);
            application.access(() -> this.message.setText(message));
        }
    }

    @Override
    public void clean() {
        application.stopPolling(this);
        super.clean();
    }

    @Override
    protected void execute(View parent, boolean doNotLock) {
        super.execute(parent, doNotLock);
        this.application = Application.get();
    }

    /**
     * Get name of the file that is being processed. This could be called from within the "processor" if required.
     * @return File name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set number of files that are allowed upload.
     * @param fileCount Number of files
     */
    public void setMaxFiles(int fileCount) {
        this.maxFileCount = fileCount <= 0 ? 1 : fileCount;
    }

    /**
     *Set autoclose flag.
     * @param autoClose Whether to automatcally closed or not when the file content is completely processed.
     */
    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    /**
     * Add additional components to this view. This will switch off the "auto close" flag.
     * @param components Components to be added
     */
    public void add(Component... components) {
        ((HasComponents)getComponent()).add(components);
        autoClose = false;
    }

    /**
     * Get the application.
     * @return Application.
     */
    public Application getApplication() {
        if(application == null) {
            application = Application.get();
        }
        return application;
    }

    /**
     * This is where the real proceesing happens. This method is invoked to process the uploaded content and by default,
     * it asks the "processor" to process it. However, this can be overriden.
     * @param data Uploaded content
     * @param mimeType Mime type of the content
     */
    public void process(InputStream data, String mimeType) {
        if(processor == null) {
            try {
                //noinspection StatementWithEmptyBody
                while (data.read() != -1);
            } catch(IOException ignore) {
            }
            return;
        }
        processor.accept(data, mimeType);
    }
}
