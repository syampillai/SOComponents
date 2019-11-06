package com.storedobject.vaadin;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.server.AbstractStreamResource;

/**
 * A PDF viewer component that uses browser's native PDF viewer.
 * This is written as a Vaadin Flow wrapper around "@lrnwebcomponents/pdf-browser-viewer/pdf-browser-viewer.js"
 *
 * @author Syam
 */
@Tag("pdf-browser-viewer")
@NpmPackage(value = "@lrnwebcomponents/pdf-browser-viewer", version = "2.1.4")
@JsModule("@lrnwebcomponents/pdf-browser-viewer/pdf-browser-viewer.js")
public class PDFViewer extends AbstractResourcedComponent {

    /**
     * Default constructor.
     */
    public PDFViewer() {
    }

    /**
     * Constructor with file to view.
     * @param fileURI URI of the file to view
     */
    public PDFViewer(String fileURI) {
        super(fileURI);
    }

    /**
     * Constructor with a stream resource to view.
     * @param streamResource Stream resource
     */
    public PDFViewer(AbstractStreamResource streamResource) {
        super(streamResource);
    }

    @Override
    protected String getURIAttributeName() {
        return "file";
    }

    /**
     * Clear the current content.
     */
    public void clear() {
        Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "').clear();");
        super.clear();
    }
}