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
    public ApplicationLayout createLayout() {
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
            add(MenuItem.create("Edit Person Details", new PersonEditor()));
            add(MenuItem.create("Veiw Sample PDF", new PDFTest()));
            add(MenuItem.create("Test Alert Component", "vaadin:user", new AlertTest()));
            add(MenuItem.create("Misc. Test", new MiscTest()));
            add(MenuItem.create("Test Grid", new GridTest()));
            add(MenuItem.create("Test Parent/Child", new ParentChildTest()));
            add(MenuItem.create("Test Dashboard", new DashboardTest()));
            add(MenuItem.create("Test Form", new TestForm()));
        }
    }

    @WebServlet(urlPatterns = "/*", name = "DemoServlet", asyncSupported = true, loadOnStartup = 0)
    @VaadinServletConfiguration(ui = Demo.class, productionMode = false, closeIdleSessions = true)
    public static class DemoServlet extends VaadinServlet {
    }

    @Route("test")
    @Push(PushMode.MANUAL)
    @BodySize(height = "100vh", width = "100vw")
    @Theme(value = Lumo.class, variant = Lumo.LIGHT)
    public static class DemoView extends ApplicationView {
    }
}
