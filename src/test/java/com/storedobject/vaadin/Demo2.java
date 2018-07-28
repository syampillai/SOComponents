package com.storedobject.vaadin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

/**
 * Demo PDFViewer
 */
@Route("pdf")
public class Demo2 extends Div {

    public Demo2() {
        PDFViewer pdfViewer = new PDFViewer("http://www.cambridgeenglish.org/images/young-learners-sample-papers-2018-vol1.pdf");
        pdfViewer.setWidth("800px");
        pdfViewer.setHeight("600px");
        add(pdfViewer);
    }
}
