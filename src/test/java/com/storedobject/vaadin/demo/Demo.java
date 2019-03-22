package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;

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
        public Component getContent() {
            return getSecondaryComponent();
        }

        @Override
        public void drawMenu(Application application) {
            getMenuPane().add(new HtmlComponent("hr"));
            UploadProcessor u = new UploadProcessor("Upload", "Upload File");
            u.setProcessor((in, ct) -> {
                long count = 0;
                try {
                    while (in.read() != -1) {
                        u.setMessage("Read " + (++count) + " bytes");
                        try {
                            if(count % 10 == 0) {
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                } catch (IOException ignore) {
                    u.setMessage("Error reading data");
                }
            });
            add(MenuItem.create("Test Custom Field", new TestCustomField()));
            u.setMaxFiles(2);
            add(MenuItem.create("Upload Processor Test", u));
            add(MenuItem.create("Edit Person Details", new PersonEditor()));
            add(MenuItem.create("Veiw Sample PDF", new PDFTest()));
            add(MenuItem.create("Test Alert Component", "vaadin:user", new AlertTest()));
            add(MenuItem.create("Misc. Test", new MiscTest()));
            add(MenuItem.create("Test Grid", new GridTest()));
            add(MenuItem.create("Test Parent/Child", new ParentChildTest()));
            add(MenuItem.create("Test Dashboard", new DashboardTest()));
        }
    }

    @WebServlet(urlPatterns = "/*", name = "DemoServlet", asyncSupported = true, loadOnStartup = 0)
    @VaadinServletConfiguration(ui = Demo.class, productionMode = false, closeIdleSessions = true)
    public static class DemoServlet extends VaadinServlet {
    }

    @Route("")
    @Push(PushMode.MANUAL)
    @BodySize(height = "100vh", width = "100vw")
    @Theme(value = Lumo.class, variant = Lumo.LIGHT)
    public static class DemoView extends ApplicationView {
    }
}
