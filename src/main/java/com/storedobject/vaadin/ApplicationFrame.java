package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.BundleParser;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.polymertemplate.TemplateParser;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.jsoup.nodes.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * An implementaion of {@link ApplicationLayout}.
 *
 * @author Syam
 */
@Tag("app-frame")
@NpmPackage(value = "@polymer/app-layout", version = "3.0.2")
@JsModule("./so/app-frame/app-frame.js")
public abstract class ApplicationFrame extends PolymerTemplate<ApplicationFrame.ApplicationFrameModel> implements ApplicationLayout, ComponentSlot {

    private Content content = new Content();
    private Menu menu = new Menu();

    public interface ApplicationFrameModel extends TemplateModel {
    }

    /**
     * Constructor.
     */
    public ApplicationFrame() {
        super(new AFTemplateParser());
        Div m = new Div();
        m.getStyle().set("margin-top", "70px");
        Component c = getMenuSearcher();
        if(c != null) {
            m.setWidthFull();
            m.add(c);
        }
        m.add(menu);
        add(m, "menu");
        add(content, "content");
        int trimHeight = getMenuSearcher() == null ? 90 : 110;
        menu.getElement().setAttribute("style", "height: calc(100vh - " + trimHeight + "px); overflow: auto;");
    }

    /**
     * Set the caption of the {@link Application}.
     *
     * @param caption Caption
     */
    public void setCaption(String caption) {
        add(new Span(caption), "caption");
    }

    /**
     * Get the "menu searcher" component.
     *
     * @return Default implementation returns <code>null</code>.
     */
    public Component getMenuSearcher() {
        return null;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Component getContent() {
        return content;
    }

    @Override
    public ApplicationMenu getMenu() {
        return menu;
    }

    private static class Content extends Div implements HasElement, HasComponents {

        private Content() {
            setHeight("calc(100vh - 80px)");
            new Scrollable(this);
            Box b = new Box(this);
            b.setMargin(2);
            b.setBorderWidth(0);
            setStyle("display", "grid");
            setStyle("grid-template-columns", "100%");
            setStyle("grid-template-rows", "100%");
            setStyle("place-content", "stretch");
        }
    }

    @Tag("div")
    private static class Menu extends Component implements ApplicationMenu, HasSize {

        private static long ID = 0;
        private long id;

        public Menu() {
            synchronized (Menu.class) {
                id = ++ID;
                if(ID == Long.MAX_VALUE) {
                    ID = 0;
                }
            }
            getElement().setAttribute("id", "appmenu" + id);
        }

        @Override
        public void insert(int position, ApplicationMenuItem menuItem) {
            ApplicationMenu.super.insert(position, menuItem);
            top();
        }

        @Override
        public void remove(ApplicationMenuItem menuItem) {
            ApplicationMenu.super.remove(menuItem);
            top();
        }

        private void top() {
            Application.get().getPage().executeJs("document.getElementById('appmenu" + id + "').scrollTop=0;");
        }
    }

    private static class AFTemplateParser implements TemplateParser {
        @Override
        public TemplateData getTemplateContent(Class<? extends PolymerTemplate<?>> aClass, String tag, VaadinService vaadinService) {
            BufferedReader r;
            r = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ApplicationFrame.class.getClassLoader().getResourceAsStream("META-INF/resources/frontend/so/app-frame/app-frame.js")),
                    StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while((line = r.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (Exception ignored) {
            } finally {
                try {
                    r.close();
                } catch (IOException ignored) {
                }
            }
            String source = sb.toString();
            Element parent = new Element(tag);
            parent.attr("id", tag);
            Element templateElement = BundleParser.parseTemplateElement("so-app-frame", source);
            templateElement.appendTo(parent);
            return new TemplateData("so-app-frame", templateElement);
        }
    }
}