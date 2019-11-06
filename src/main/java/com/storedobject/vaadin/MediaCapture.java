package com.storedobject.vaadin;

import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import java.io.OutputStream;

/**
 * Interface that defines capturing of streams from devices such as video camera, mic etc.
 *
 * @author Syam
 */
public interface MediaCapture {

    /**
     * Get the {@link Application} to which this is attached.
     *
     * @return The application.
     */
    Application getApplication();

    /**
     * Start the recording of the media stream to a "data receiver".
     *
     * @param dataReceiver Data receiver to receive the stream
     */
    void startRecording(DataReceiver dataReceiver);

    /**
     * Check whether recording is in progress or not.
     *
     * @return True or false.
     */
    boolean isRecording();

    /**
     * Stop the recording of the video stream that may be currently in progress.
     */
    void stopRecording();

    /**
     * Check whether previewing is in progress or not. (Only applicable to certain media such as Video).
     *
     * @return True or false.
     */
    default boolean isPreviewing() {
        return false;
    }

    /**
     * Stop / switch off the device (activities such as previewing, recording etc. will be stopped).
     */
    void stopDevice();

    /**
     * Add "status change" listener so that we can monitor recording/previewing status.
     *
     * @param listener Listener to add
     * @return Registration that can be used to remove the listener.
     */
    Registration addStatusChangeListener(StatusChangeListener listener);

    /**
     * Clear the stream sources attached to this.
     */
    void clear();

    /**
     * Set sources.
     * @param resources Sources to be set
     */
    void setSource(StreamResource... resources);

    /**
     * Add sources.
     * @param resources Sources to be added
     */
    void addSource(StreamResource... resources);

    /**
     * Interface to be implemented in order to receive data from the {@link MediaCapture} as a stream like audio/video/image etc.
     */
    interface DataReceiver {

        /**
         * Get the output stream to which streamed data can be saved.
         *
         * @param mimeType Mime type of the data stream
         * @return Output stream.
         */
        OutputStream getOutputStream(String mimeType);

        /**
         * This method is invoked when end-of-stream is reached.
         */
        void finished();

        /**
         * This method is invoked if streaming is aborted due to any errors.
         */
        void aborted();
    }

    /**
     * Interface to implement to receive capturing status changes of a {@link MediaCapture}.
     */
    @FunctionalInterface
    interface StatusChangeListener {
        /**
         * Fired when the recording/previewing status is changed.
         *
         * @param mediaCapture Device whose status is changed
         */
        void statusChanged(MediaCapture mediaCapture);
    }
}