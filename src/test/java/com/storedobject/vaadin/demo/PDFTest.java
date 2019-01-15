package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.PDFViewer;
import com.storedobject.vaadin.View;

public class PDFTest extends View {

    public PDFTest() {
        super("PDF");
        PDFViewer pdfViewer = new PDFViewer("http://www.cambridgeenglish.org/images/young-learners-sample-papers-2018-vol1.pdf");
        pdfViewer.setHeight("700px");
        setComponent(pdfViewer);
    }
}
