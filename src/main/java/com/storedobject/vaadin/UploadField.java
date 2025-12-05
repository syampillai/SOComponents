package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.*;

import java.io.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A class to process uploaded content. The content will be available to the "processor" as an {@link InputStream}.
 * The content must be read completely from the stream by the processor's {@link BiConsumer}
 * because the stream is automatically closed after invoking this method.
 *
 * <p>This component is a {@link com.vaadin.flow.component.HasValue} and the value returned is the number of files
 * successfully uploaded.</p>
 *
 * <p>The default maximum file size is set to 10,000,000 bytes, but it can be changed via the
 * {@link Upload#setMaxFileSize(int)} method and the {@link Upload} component can be obtained via the
 * {@link #getUploadComponent()} method.</p>
 *
 * @author Syam
 */
@CssImport(value = "./so/upload/styles.css", themeFor = "vaadin-upload-file")
public class UploadField extends CustomField<Integer> {

    private final Upload upload;
    private final Handler handler = new Handler();
    private int fileCount = 0, maxFileCount = Integer.MAX_VALUE;
    private final BiConsumer<InputStream, String> processor;
    private String fileName;
    private final Div description = new Div();
    private Application application;
    private UI ui;
    private Consumer<Throwable> processErrorConsumer;

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
        upload = new Upload(handler);
        if(upload.getUploadButton() instanceof Button b) {
            b.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        upload.setMaxFiles(maxFileCount);
        upload.setMaxFileSize(10000000);
        upload.addFileRejectedListener(e -> {
            --fileCount;
            inform("File Rejected - " + e.getErrorMessage());
        });
        upload.addAllFinishedListener(e -> inform(null));
        add(upload, description);
        setLabel(label);
        this.processor = processor;
    }

    private void inform(String m) {
        if(getU() == null) {
            return;
        }
        ui.access(() -> inf(m));
    }

    private void inf(String m) {
        String s = "Files: " + (fileCount + 1);
        if(maxFileCount < Integer.MAX_VALUE) {
            s += "/" + maxFileCount;
        }
        if(m != null) {
            s += " - " + m;
        }
        description.setText(s);
        if(fileCount >= maxFileCount) {
            setReadOnly(true);
        }
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
        application = Application.get(getU());
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
        if(getU() == null) {
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

    private class Handler implements UploadHandler {

        @Override
        public void handleUploadRequest(UploadEvent uploadEvent) {
            handleUpload(uploadEvent);
        }
    }

    private void handleUpload(UploadEvent event) {
        if (application != null) {
            application.startPolling(this);
        }
        try {
            this.fileName = event.getFileName();
            InputStream in = event.getInputStream();
            try {
                process(in, event.getContentType());
                ++fileCount;
                inform(null);
                handler.responseHandled(true, event.getResponse());
            } catch (Throwable processingError) {
                if(processErrorConsumer != null) {
                    processErrorConsumer.accept(processingError);
                    ui.access(() -> setReadOnly(true));
                } else {
                    processingError.printStackTrace();
                    if (application != null) {
                        application.log(processingError);
                    } else {
                        processingError.printStackTrace();
                    }
                    StyledText error = new StyledText("<span style=\"color:red\">Processing error!</span>");
                    ui.access(() -> {
                        UploadField.this.add(error);
                        setReadOnly(true);
                    });
                }
                handler.responseHandled(false, event.getResponse());
            }
            try {
                //noinspection StatementWithEmptyBody
                while (in.read() != -1) ;
            } catch (IOException ignore) {
            }
            if (fileCount == maxFileCount) {
                ui.access(() -> {
                    new Box(description);
                    setReadOnly(true);
                });
            }
            try {
                in.close();
            } catch (IOException ignore) {
            }
        } finally {
            if (application != null) {
                application.stopPolling(this);
            }
        }
    }

    /**
     * This is where the real processing happens. This method is invoked to process the uploaded content, and by default,
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
     * Get the name of the file that is being processed. This could be called from within the "processor" if required.
     * @return File name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the number of files that are allowed to upload.
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

    /**
     * Sets the consumer to handle errors that occur during the processing of uploaded content.
     *
     * @param processErrorConsumer The consumer that will handle processing errors, which accepts a {@link Throwable} representing the error.
     */
    public void setProcessErrorConsumer(Consumer<Throwable> processErrorConsumer) {
        this.processErrorConsumer = processErrorConsumer;
    }
}
