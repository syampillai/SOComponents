package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
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

    private class SimpleLayout extends HorizontalLayout implements ApplicationLayout, ApplicationMenu {

        VerticalLayout menu = new VerticalLayout(), content = new VerticalLayout();

        private SimpleLayout() {
            add(menu, content);
            menu.setMargin(false);
            menu.setHeight("100vh");
            menu.setWidth("");
            content.setMargin(false);
            content.setHeight("100vh");
            content.setWidth("calc(100vw - 80px)");
            setHeight("100vh");
            setWidth("100vw");
            menu.getElement().getStyle().set("background-color", "lightblue");
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
            return menu;
        }

        @Override
        public Component getContent() {
            return content;
        }

        @Override
        public void drawMenu(Application application) {
            getMenuPane().add(new HtmlComponent("hr"));
            add(application.createMenuItem("Edit Person Details", new PersonEditor()));
            add(application.createMenuItem("Veiw Sample PDF", new PDFTest()));
            add(application.createMenuItem("Test Alert Component", "vaadin:user", new AlertTest()));
            add(application.createMenuItem("Misc. Test", new MiscTest()));
            add(application.createMenuItem("Test Grid", new GridTest()));
            add(application.createMenuItem("Test Parent/Child", new ParentChildTest()));
            add(application.createMenuItem("Test Dashboard", new DashboardTest()));
            add(application.createMenuItem("Test Form", new TestForm()));
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
    @PreserveOnRefresh
    public static class DemoView extends ApplicationView {
    }
}
