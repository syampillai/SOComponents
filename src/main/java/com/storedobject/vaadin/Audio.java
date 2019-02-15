package com.storedobject.vaadin;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

/**
 * Component to play audio streams.
 *
 * @author Syam
 */
@Tag("audio")
public class Audio extends Media {

    /**
     * Constructor.
     */
    public Audio() {
    }

    /**
     * Consructor.
     * @param resources Sources to be set
     */
    public Audio(StreamResource... resources) {
        super(resources);
    }

    /**
     * Constructor.
     * @param uri URI source
     * @param type Content type of the media
     */
    public Audio(String uri, String type) {
        super(uri, type);
    }
}
