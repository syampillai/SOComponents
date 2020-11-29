package com.storedobject.vaadin;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.AbstractStreamResource;

/**
 * A PDF viewer component that uses browser's native object tag.
 *
 * @author Syam
 */
@Tag("object")
public class PDFViewer extends AbstractResourcedComponent {

    /**
     * Default constructor.
     */
    public PDFViewer() {
        super();
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

    /**
     * This will be invoked by the constructor for initial set up.
     */
    protected void init() {
        super.init();
        getElement().setAttribute("type", "application/pdf");
    }

    @Override
    protected String getURIAttributeName() {
        return "data";
    }
}