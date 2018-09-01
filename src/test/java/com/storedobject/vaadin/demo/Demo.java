package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;

import javax.servlet.annotation.WebServlet;

public class Demo extends Application {

    public Demo() {
    }

    @Override
    protected ApplicationLayout createLayout() {
        return new SimpleLayout();
    }

    private class SimpleLayout extends SplitLayout implements ApplicationLayout, ApplicationMenu {

        private SimpleLayout() {
            super(new Div(), new Div());
            setSplitterPosition(15);
            setHeight("100vh");
            setWidth("100vw");
            getPrimaryComponent().getElement().getStyle().set("background-color", "lightblue");
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public ApplicationMenu getMenu() {
            return this;
        }

        @Override
        public HasComponents getMenuPane() {
            return (Div)getPrimaryComponent();
        }

        @Override
        public HasComponents getContentPane() {
            return (Div)getSecondaryComponent();
        }

        @Override
        public void drawMenu(Application application) {
            getMenuPane().add(new HtmlComponent("hr"));
            add(MenuItem.create("Edit Person Details", new PersonEditor(application)));
            add(MenuItem.create("Veiw Sample PDF", new PDFTest(application)));
            add(MenuItem.create("Test Alert Component", new AlertTest(application)));
            add(MenuItem.create("Test", new MiscTest(application)));
        }
    }

    @WebServlet(urlPatterns = "/*", name = "SOServlet", asyncSupported = true, loadOnStartup = 0)
    @VaadinServletConfiguration(ui = Demo.class, productionMode = false, closeIdleSessions = true)
    public static class SOServlet extends VaadinServlet {
    }

    private class PDFTest extends View {

        public PDFTest(Application a) {
            super(a, "PDF");
            PDFViewer pdfViewer = new PDFViewer("http://www.cambridgeenglish.org/images/young-learners-sample-papers-2018-vol1.pdf");
            pdfViewer.setHeight("700px");
            setComponent(pdfViewer);
        }
    }
}