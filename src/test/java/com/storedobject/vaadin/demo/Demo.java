package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.polymertemplate.BundleParser;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.polymertemplate.TemplateParser;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Demo extends Application {

    public Demo() {
    }

    @Override
    public ApplicationLayout createLayout() {
        return new SimpleLayout();
        //return new ComplexLayout();
    }

    private interface AppMenu {

        ApplicationMenu getMenu();

        default void drawMenu(Application application) {
            ApplicationMenu m = getMenu();
            m.add(application.createMenuItem("Edit Person Details", new PersonEditor()));
            m.add(application.createMenuItem("Veiw Sample PDF", new PDFTest()));
            m.add(application.createMenuItem("Misc. Test", new MiscTest()));
            m.add(application.createMenuItem("Test Grid", new GridTest()));
            m.add(application.createMenuItem("Test Dashboard", new DashboardTest()));
        }
    }

    private static class SimpleLayout extends HorizontalLayout implements ApplicationLayout, ApplicationMenu, AppMenu {

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
            AppMenu.super.drawMenu(application);
        }
    }

    private static class ComplexLayout extends ApplicationFrame implements AppMenu {

        @Override
        public void drawMenu(Application application) {
            AppMenu.super.drawMenu(application);
        }

        @Override
        public Component getMenuSearcher() {
            return new TextField("", "Hello World");
        }
    }

    @Route("")
    @Push(PushMode.MANUAL)
    @BodySize(height = "100vh", width = "100vw")
    @Theme(value = Lumo.class, variant = Lumo.LIGHT)
    @PreserveOnRefresh
    public static class DemoView extends ApplicationView {

        @Override
        protected Application createApplication() {
            return new Demo();
        }
    }
}
