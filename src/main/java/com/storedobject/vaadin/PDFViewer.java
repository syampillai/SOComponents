package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ID;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

/**
 * A PDF viewer component that uses browser's native PDF viewer.
 * This is written as a Vaadin Flow wrapper around "@lrnwebcomponents/pdf-browser-viewer/pdf-browser-viewer.js"
 *
 * @author Syam
 */
@Tag("pdf-browser-viewer")
@NpmPackage(value = "@lrnwebcomponents/pdf-browser-viewer", version = "2.1.4")
@JsModule("@lrnwebcomponents/pdf-browser-viewer/pdf-browser-viewer.js")
public class PDFViewer extends Component implements ResourcedComponent, HasSize {

    private final ResourceSupport resourceSupport;

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
        resourceSupport = new ResourceSupport(this);
        if(fileURI != null) {
            setURI(fileURI);
        }
        setSizeFull();
        setID(this);
    }

    /**
     * Constructor with a stream resource to view.
     * @param streamResource Stream resource
     */
    public PDFViewer(StreamResource streamResource) {
        resourceSupport = new ResourceSupport(this);
        if(streamResource != null) {
            setSource(streamResource);
        }
        setSizeFull();
        setID(this);
    }

    private synchronized static void setID(PDFViewer v) {
        v.setId("sopdf" + ID.newID());
    }

    /**
     * Set the file to view.
     * @param fileURL URL of the file to view
     */
    public void setSource(String fileURL) {
        resourceSupport.clear();
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
    public void setSource(AbstractStreamResource streamResource) {
        resourceSupport.register(streamResource);
        String uri = resourceSupport.getURI();
        if(uri != null) {
            setURI(uri);
        }
    }

    /**
     * Clear the current content.
     */
    public void clear() {
        Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "').clear();");
        resourceSupport.clear();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        resourceSupport.register();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        resourceSupport.unregister();
    }
}