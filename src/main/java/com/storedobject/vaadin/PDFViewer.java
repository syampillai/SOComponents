package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceRegistry;
import com.vaadin.flow.server.VaadinSession;

/**
 * A PDF viewer component that uses browser's native PDF viewer.
 * This is written as a Vaadin Flow wrapper around "@lrnwebcomponents/pdf-browser-viewer/pdf-browser-viewer.js"
 *
 * @author Syam
 */
@Tag("pdf-browser-viewer")
@NpmPackage(value = "@lrnwebcomponents/pdf-browser-viewer", version = "2.1.4")
@JsModule("@lrnwebcomponents/pdf-browser-viewer/pdf-browser-viewer.js")
public class PDFViewer extends Component implements HasSize {

    private StreamRegistration streamRegistration;
    private StreamResource streamResource;

    /**
     * Default constructor.
     */
    public PDFViewer() {
        this((String)null);
    }

    /**
     * Constructor with file to view.
     * @param fileURI URI of the file to view
     */
    public PDFViewer(String fileURI) {
        if(fileURI != null) {
            setURI(fileURI);
        }
        setSizeFull();
    }

    /**
     * Constructor with a stream resource to view.
     * @param streamResource Stream resource
     */
    public PDFViewer(StreamResource streamResource) {
        if(streamResource != null) {
            setSource(streamResource);
        }
        setSizeFull();
    }

    /**
     * Set the file to view.
     * @param fileURL URL of the file to view
     */
    public void setSource(String fileURL) {
        if (streamRegistration != null) {
            streamRegistration.unregister();
            streamRegistration = null;
        }
        streamResource = null;
        setURI(fileURL);
    }

    private void setURI(String url) {
        if(url == null) {
            getElement().removeAttribute("file");
            clear();
        } else {
            getElement().setAttribute("file", url);
        }
    }

    /**
     * Set the file to view from a {@link StreamResource}.
     * @param streamResource Stream source
     */
    public void setSource(StreamResource streamResource) {
        if(streamResource == null) {
            setSource((String)null);
            return;
        }
        if(streamRegistration != null) {
            streamRegistration.unregister();
        }
        this.streamResource = streamResource;
        streamRegistration = VaadinSession.getCurrent().getResourceRegistry().registerResource(streamResource);
        setURI(StreamResourceRegistry.getURI(streamResource).toASCIIString());
    }

    /**
     * Clear the current content.
     */
    public void clear() {
        getElement().callJsFunction("clear");
        if (streamRegistration != null) {
            streamRegistration.unregister();
            streamRegistration = null;
        }
        streamResource = null;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(streamResource != null && streamRegistration == null) {
            setSource(streamResource);
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        if (streamRegistration != null) {
            streamRegistration.unregister();
            streamRegistration = null;
        }
    }
}