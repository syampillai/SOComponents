package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;

/**
 * A PDF viewer component that uses browser's native PDF viewer.
 * Vaadin Flow wrapper around
 * <a href="https://github.com/IngressoRapidoWebComponents/pdf-browser-viewer" target="_blank">PDF Browser Viewer</a>
 * @author Syam
 */
@Tag("pdf-browser-viewer")
@HtmlImport("bower_components/pdf-browser-viewer/pdf-browser-viewer.html")
public class PDFBrowserViewer extends Component implements HasSize {

    /**
     * Default constructor
     */
    public  PDFBrowserViewer() {
        this(null);
    }

    /**
     * Constructor with file to view
     * @param fileURL URL of the file to view
     */
    public PDFBrowserViewer(String fileURL) {
        if(fileURL != null) {
            setFileURL(fileURL);
        }
    }

    /**
     * Set the file to view
     * @param fileURL URL of the file to view
     */
    public void setFileURL(String fileURL) {
        if(fileURL == null) {
            getElement().removeAttribute("file");
            clear();
        } else {
            getElement().setAttribute("file", fileURL);
        }
    }

    /**
     * Clear the current content
     */
    public void clear() {
        getElement().callFunction("clear");
    }

    @Override
    public void setWidth(String width) {
        getElement().setAttribute("width", width);
    }

    @Override
    public String getWidth() {
        return getElement().getAttribute("width");
    }

    @Override
    public void setHeight(String height) {
        getElement().setAttribute("height", height);
    }

    @Override
    public String getHeight() {
        return getElement().getAttribute("height");
    }
}