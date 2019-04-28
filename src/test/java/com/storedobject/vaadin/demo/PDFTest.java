package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.CloseableView;
import com.storedobject.vaadin.PDFViewer;
import com.storedobject.vaadin.View;

public class PDFTest extends View implements CloseableView {

    public PDFTest() {
        super("PDF");
        PDFViewer pdfViewer = new PDFViewer("http://www.cambridgeenglish.org/images/young-learners-sample-papers-2018-vol1.pdf");
        setComponent(pdfViewer);
    }
}