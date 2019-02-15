package com.storedobject.vaadin;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

/**
 * Component to play video streams.
 *
 * @author Syam
 */
@Tag("video")
public class Video extends Media {

    /**
     * Constructor.
     */
    public Video() {
    }

    /**
     * Consructor.
     * @param resources Sources to be set
     */
    public Video(StreamResource... resources) {
        super(resources);
    }

    /**
     * Constructor.
     * @param uri URI source
     * @param type Content type of the media
     */
    public Video(String uri, String type) {
        super(uri, type);
    }
}
