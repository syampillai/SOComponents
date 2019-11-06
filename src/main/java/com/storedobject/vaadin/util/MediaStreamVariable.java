package com.storedobject.vaadin.util;

import com.storedobject.vaadin.MediaCapture;
import com.vaadin.flow.server.StreamVariable;

import java.io.OutputStream;

public class MediaStreamVariable implements StreamVariable {

    private String mime;
    private MediaCapture.DataReceiver receiver;
    private OutputStream out;
    private boolean aborted = false;

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

    public String getMime() {
        return mime;
    }
}
