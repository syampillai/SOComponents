package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;

import java.io.*;
import java.util.function.BiConsumer;

/**
 * A class to process uploaded content. The content will be available to the "processor" as an {@link InputStream}.
 * The content must be read completely from the stream by the processor's {@link BiConsumer}
 * because the stream is automatically closed after invoking this method.
 *
 * <p>This component is a {@link com.vaadin.flow.component.HasValue} and the value returned is the number of files
 * successfully uploaded.</p>
 *
 * <p>The default maximum file size is set to 10000000 bytes but it can be changed via the
 * {@link Upload#setMaxFileSize(int)} method and the {@link Upload} component can be obtained via the
 * {@link #getUploadComponent()} method.</p>
 *
 * @author Syam
 */
@CssImport(value = "./so/upload/styles.css", themeFor = "vaadin-upload-file")
public class UploadField extends CustomField<Integer> {

    private final Upload upload;
    private int fileCount = 0, maxFileCount = Integer.MAX_VALUE;
    private final BiConsumer<InputStream, String> processor;
    private String fileName;
    private final Div description = new Div();
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
        upload.setMaxFileSize(10000000);
        upload.addFileRejectedListener(e -> getU().access(() ->
                inform("Rejected: " + e.getErrorMessage())));
        upload.addFailedListener(e -> getU().access(() -> {
            inform("Error: Upload Failed");
            setReadOnly(true);
        }));
        upload.addFinishedListener(e -> {
            inform(null);
            ++fileCount;
        });
        add(upload, description);
        setLabel(label);
        this.processor = processor;
    }

    private void inform(String m) {
        String s = "Files: " + (fileCount + 1);
        if(maxFileCount < Integer.MAX_VALUE) {
            s += "/" + maxFileCount;
        }
        if(m != null) {
            s += " - " + m;
        }
        description.setText(s);
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

    /**
     * Whether to show the description or not.
     *
     * @param show True/false.
     */
    public void showDescription(boolean show) {
        description.setVisible(show);
    }

    /**
     * Set the description to show.
     *
     * @param description Description to show.
     */
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
        Application a = Application.get(ui);
        this.fileName = fileName;
        PipedOutputStream out = new PipedOutputStream();
        try {
            PipedInputStream in = new PipedInputStream(out);
            if(a != null) {
                a.startPolling(in);
            }
            new Thread(() -> {
                try {
                    process(in, mimeType);
                } catch(Throwable processingError) {
                    if(a != null) {
                        a.log(processingError);
                    } else {
                        processingError.printStackTrace();
                    }
                    StyledText error = new StyledText("<span style=\"color:red\">Processing error!</span>");
                    ui.access(() -> {
                        UploadField.this.add(error);
                        setReadOnly(true);
                    });
                }
                try {
                    //noinspection StatementWithEmptyBody
                    while (in.read() != -1);
                } catch(IOException ignore) {
                }
                if(fileCount == maxFileCount) {
                    ui.access(() -> {
                        new Box(description);
                        setReadOnly(true);
                    });
                }
                try {
                    out.close();
                } catch (IOException ignore) {
                }
                try {
                    if(a != null) {
                        a.stopPolling(in);
                    }
                    in.close();
                } catch (IOException ignore) {
                }
            }).start();
        } catch (IOException ignored) {
        }
        return out;
    }

    /**
     * This is where the real processing happens. This method is invoked to process the uploaded content and by default,
     * it asks the "processor" to process it. However, this can be overridden.
     * @param data Uploaded content
     * @param mimeType Mime type of the content
     */
    public void process(InputStream data, String mimeType) {
        if(processor != null) {
            processor.accept(data, mimeType);
        }
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
