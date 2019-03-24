package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;

import java.io.*;
import java.util.function.BiConsumer;

/**
 * A class to process uploaded content. The content will be available to the "processor" as an {@link InputStream}.
 * The content must be read in completely from the stream by the processor's {@link BiConsumer#accept(Object, Object)}
 * method becasue the stream is automatically closed after invoking this method.
 *
 * This component is a {@link com.vaadin.flow.component.HasValue} and the value returned is the number of files
 * successfully uploaded.
 *
 * @author Syam
 */
public class UploadField extends CustomField<Integer> {

    private final Upload upload;
    private int fileCount = 0, maxFileCount = Integer.MAX_VALUE;
    private BiConsumer<InputStream, String> processor;
    private String fileName;
    private Div description = new Div();
    private UI ui;

    /**
     * Constructor.
     */
    public UploadField() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param label Label
     */
    public UploadField(String label) {
        this(label, null);
    }

    /**
     * Constructor.
     *
     * @param processor Processor to process the content (Parameters: content, mime type)
     */
    public UploadField(BiConsumer<InputStream, String> processor) {
        this(null, processor);
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param processor Processor to process the content (Parameters: content, mime type)
     */
    public UploadField(String label, BiConsumer<InputStream, String> processor) {
        super(0);
        upload = new Upload(this::createStream);
        upload.setMaxFiles(maxFileCount);
        upload.addFailedListener(e -> { Application.error(e); getU().access(() -> setReadOnly(true)); });
        upload.addFinishedListener(e -> {
            ++fileCount;
            String s = "Files: " + fileCount;
            if(maxFileCount < Integer.MAX_VALUE) {
                s += "/" + maxFileCount;
            }
            description.setText(s);
        });
        add(upload, description);
        setLabel(label);
        this.processor = processor;
    }

    /**
     * Get the {@link Upload} component internally used. Any customization of this component is possible.
     * @return Upload component
     */
    public Upload getUploadComponent() {
        return upload;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getU();
    }

    private UI getU() {
        if(ui == null) {
            ui = getUI().orElse(null);
        }
        return ui;
    }

    @Override
    public void setValue(Integer value) {
        super.setValue(fileCount);
    }

    public void showDescription(boolean show) {
        description.setVisible(show);
    }

    public void setDescription(String description) {
        getU();
        if(ui == null) {
            this.description.setText(description);
        } else {
            ui.access(() -> this.description.setText(description));
        }
    }

    @Override
    protected Integer generateModelValue() {
        return fileCount;
    }

    @Override
    protected void setPresentationValue(Integer integer) {
    }

    private OutputStream createStream(String fileName, String mimeType) {
        if(getU() instanceof Application) {
            ((Application) ui).startPolling(this);
        }
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
                if(ui instanceof Application) {
                   ((Application) ui).stopPolling(this);
                }
                if(fileCount == maxFileCount) {
                    ui.access(() -> {
                        new Box(description);
                        setReadOnly(true);
                    });
                }
            }).start();
        } catch (IOException ignored) {
        }
        return out;
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
        upload.setMaxFiles(maxFileCount);
    }

    /**
     * Get the number of files allowed to upload.
     *
     * @return Number of files.
     */
    public final int getMaxFiles() {
        return maxFileCount;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if(readOnly) {
            upload.setVisible(false);
            description.setVisible(true);
        } else {
            if(fileCount < maxFileCount) {
                upload.setVisible(true);
            }
        }
    }
}
