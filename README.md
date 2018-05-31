# SO Components

A set of Vaadin Flow Components derived from various Web Components

## Components

### (1) PaperIconButton
Vaadin Flow wrapper around https://github.com/PolymerElements/paper-icon-button (Web Component License: BSD-3-CLAUSE)

### (2) Icon
Enhancements to Vaadin's Icon to handle all Iron icon collections

### (3) IronAutogrowTextArea
A multi-line text field.
Vaadin Flow wrapper around https://github.com/PolymerElements/iron-autogrow-textarea (Web Component License: BSD-3-CLAUSE)

### (4) PDFBrowserViewer
A PDF viewer component that uses browser's native PDF viewer.
Vaadin Flow wrapper around https://github.com/IngressoRapidoWebComponents/pdf-browser-viewer (Web Component License: MIT)

## Source code

Available at https://github.com/syampillai/SOComponents

## Code samples
```
import com.storedobject.vaadin.PaperIconButton;
import com.storedobject.vaadin.Icon;
import com.storedobject.vaadin.IronAutogrowTextArea;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Demo PaperIconButton, Icon, IronAutogrowArea
 */
@Route("")
public class Demo1 extends VerticalLayout {

    public Demo1() {
        add(new PaperIconButton("device", "bluetooth",
                e -> Notification.show("Clicked on Paper Icon Button!")));
        add(new Button("Button with Icon", new Icon("editor", "functions"),
                e -> Notification.show("Please try clicking on Paper Icon Button too!")));
        IronAutogrowTextArea ta = new IronAutogrowTextArea();
        ta.setMinRows(5);
        ta.setMaxRows(10);
        add(ta);
    }
}
```
```
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

/**
 * Demo PDFBrowserViewer
 */
@Route("")
public class Demo2 extends Div {

    public Demo2() {
        PDFBrowserViewer pdfViewer = new PDFBrowserViewer("https://xxx.yyy.com/sample.pdf");
        pdfViewer.setWidth("800px");
        pdfViewer.setHeight("600px");
        add(pdfViewer);
    }
}
```
