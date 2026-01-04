package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.vaadin.flow.server.HttpStatusCode;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * A utility class for downloading content via an InputStream. The class provides functionality
 * to specify the content type, file name, and source of the stream data, and it implements a
 * callback mechanism to handle download completion events.
 *
 * @author Syam
 */
public class StreamDownloader implements InputStreamDownloadCallback {

    private final Supplier<InputStream> streamProvider;
    private final String contentType;
    private String fileName;

    /**
     * Creates a StreamDownloader instance with the specified stream provider and content type.
     *
     * @param streamProvider A {@link Supplier} that provides an {@link InputStream} for downloading the content.
     * @param contentType The MIME type of the content to be downloaded.
     */
    public StreamDownloader(Supplier<InputStream> streamProvider, String contentType) {
        this(streamProvider, contentType, null);
    }

    /**
     * Constructs a StreamDownloader instance.
     *
     * @param streamProvider A supplier that provides the InputStream to be downloaded.
     * @param contentType    The content type of the download (e.g., "application/pdf").
     * @param fileName       The name of the file to be downloaded. If null, a default file name is generated.
     */
    public StreamDownloader(Supplier<InputStream> streamProvider, String contentType, String fileName) {
        this.streamProvider = streamProvider;
        this.contentType = contentType;
        this.fileName = fileName == null ? ("so" + ID.newID() + ".dat") : fileName;
    }

    /**
     * Retrieves the name of the file associated with the downloader.
     *
     * @return The name of the file as a String. If no file name was explicitly set,
     * it returns a default-generated file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name to be used when downloading or processing a file.
     * If the provided file name is null, no changes will be made.
     *
     * @param fileName The file name to set. Must not be null to take effect.
     */
    public void setFileName(String fileName) {
        if(fileName != null) {
            this.fileName = fileName;
        }
    }

    @Override
    public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
        InputStream stream = streamProvider == null ? null : streamProvider.get();
        if(stream == null) {
            return DownloadResponse.error(HttpStatusCode.NOT_FOUND);
        }
        return new DownloadResponse(streamProvider.get(), getFileName(), contentType, -1L);
    }
}
