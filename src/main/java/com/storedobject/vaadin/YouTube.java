package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

/**
 * Component to play YouTube video.<BR>
 * This is written as a Vaadin Flow wrapper around "@google-web-components/google-youtube"
 *
 * @author Syam
 */
@Tag("google-youtube")
@NpmPackage(value = "@google-web-components/google-youtube", version = "3.0.1")
@JsModule("@google-web-components/google-youtube/google-youtube.js")
public class YouTube extends Component {

    /**
     * Constructor.
     */
    public YouTube() {
    }

    /**
     * Constructor.
     *
     * @param video YouTube video id to play
     */
    public YouTube(String video) {
        setVideo(video);
    }

    /**
     * Load a YouTube video.
     *
     * @param video YouTube video id to play
     */
    public void setVideo(String video) {
        getElement().setAttribute("video-id", video == null ? "" : video);
    }
}
