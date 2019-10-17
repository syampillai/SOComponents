package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ID;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * An implementation of {@link ApplicationLayout}.
 *
 * @author Syam
 */
@Tag("app-frame")
@NpmPackage(value = "@polymer/app-layout", version = "3.1.0")
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
        setId("appframe" + ID.newID());
    }

    /**
     * Set the caption of the {@link Application}.
     *
     * @param caption Caption
     */
    @Override
    public void setCaption(String caption) {
        set(new Span(caption), "caption");
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

    @Override
    public void openMenu() {
        js("doOpen()");
    }

    @Override
    public void closeMenu() {
        js("doClose()");
    }

    @Override
    public void toggleMenu() {
        js("doToggle()");
    }

    @Override
    public void speak(String sentence) {
        if(sentence.indexOf('"') >= 0) {
            sentence = sentence.replace("\"", "");
        }
        js("speak(\"" + sentence + "\")");
    }

    private void js(String js) {
        Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "')." + js + ";");
    }

    @Tag("div")
    private static class Menu extends Component implements ApplicationMenu, HasSize {

        public Menu() {
            setId("appmenu" + ID.newID());
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
            Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "').scrollTop=0;");
        }
    }
}