package com.storedobject.vaadin.util;

import com.storedobject.vaadin.MediaCapture;
import com.vaadin.flow.server.StreamVariable;

import java.io.OutputStream;

/**
 * The MediaStreamVariable class implements the StreamVariable interface to handle
 * streaming data with associated MIME type provided by a MediaCapture.DataReceiver.
 * It manages the output stream for the streaming data and handles streaming events.
 *
 * @author Syam
 */
public class MediaStreamVariable implements StreamVariable {

    private String mime;
    private final MediaCapture.DataReceiver receiver;
    private OutputStream out;
    private boolean aborted = false;

    /**
     * Constructs a MediaStreamVariable instance with the specified MediaCapture.DataReceiver.
     *
     * @param receiver the MediaCapture.DataReceiver to receive streaming data
     */
    public MediaStreamVariable(MediaCapture.DataReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public OutputStream getOutputStream() {
        if(out == null) {
            out = receiver.getOutputStream(mime);
        } else {
            if(!aborted) {
                aborted = true;
                receiver.aborted();
            }
        }
        return out;
    }

    @Override
    public boolean isInterrupted() {
        return aborted;
    }

    @Override
    public boolean listenProgress() {
        return false;
    }

    @Override
    public void onProgress(StreamingProgressEvent event) {
    }

    @Override
    public void streamingFailed(StreamingErrorEvent event) {
        if(!aborted) {
            aborted = true;
            receiver.aborted();
        }
    }

    @Override
    public void streamingFinished(StreamingEndEvent event) {
        if(!aborted) {
            receiver.finished();
        }
    }

    @Override
    public void streamingStarted(StreamingStartEvent event) {
        mime = event.getMimeType();
    }

    /**
     * Returns the MIME type associated with the streaming media.
     *
     * @return the MIME type as a String
     */
    public String getMime() {
        return mime;
    }
}
