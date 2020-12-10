package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.*;

import java.util.ArrayList;

/**
 * Base class for other media classes such as {@link Audio}, {@link Video} etc. Memory leak is prevented from all the
 * {@link StreamResource} set as sources.
 *
 * @author Syam
 */
public abstract class Media extends Component implements HasSize {

    private final ArrayList<StreamResource> resources = new ArrayList<>();
    private final ArrayList<StreamRegistration> registrations = new ArrayList<>();
    private final ArrayList<Element> uriSources = new ArrayList<>();

    /**
     * Constructor.
     */
    public Media() {
        ID.set(this);
        showControls(true);
    }

    /**
     * Consructor.
     * @param resources Sources to be set
     */
    public Media(StreamResource... resources) {
        ID.set(this);
        addSource(resources);
        showControls(true);
    }

    /**
     * Constructor.
     * @param uri URI source
     * @param type Content type of the media
     */
    public Media(String uri, String type) {
        ID.set(this);
        addSource(uri, type);
        showControls(true);
    }

    private void reload() {
        String id = getId().orElse(null);
        if(id != null) {
            UI ui = getUI().orElse(null);
            if(ui == null) {
                ui = UI.getCurrent();
            }
            if (ui != null) {
                ui.getPage().executeJs("document.getElementById('" + id + "').load();");
            }
        }
    }

    /**
     * Set sources.
     * @param resources Sources to be set
     */
    public void setSource(StreamResource... resources) {
        clear();
        addSource(resources);
    }

    /**
     * Add sources.
     * @param resources Sources to be added
     */
    public void addSource(StreamResource... resources) {
        registerAll();
        for(StreamResource sr: resources) {
            if(sr == null) {
                continue;
            }
            this.resources.add(sr);
            register(sr);
        }
        reload();
    }

    /**
     * Set a URI source.
     * @param uri URI source to be set
     * @param type Content type of the media
     */
    public void setSource(String uri, String type) {
        clear();
        addSource(uri, type);
    }

    /**
     * Add a URI source.
     * @param uri URI source to be added
     * @param type Content type of the media
     */
    public void addSource(String uri, String type) {
        registerAll();
        Element s = new Element("source");
        s.setAttribute("src", uri);
        s.setAttribute("type", type);
        getElement().appendChild(s);
        uriSources.add(s);
        reload();
    }

    /**
     * Clear the current sources.
     */
    public void clear() {
        registrations.forEach(StreamRegistration::unregister);
        registrations.clear();
        resources.clear();
        getElement().removeAllChildren();
        getElement().setText("Media not supported on this browser!");
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        registrations.forEach(StreamRegistration::unregister);
        registrations.clear();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        registerAll();
    }

    private void registerAll() {
        if(!registrations.isEmpty()) {
            return;
        }
        resources.forEach(this::register);
        uriSources.forEach(ur -> getElement().appendChild(ur));
    }

    private void register(StreamResource sr) {
        if(sr == null) {
            return;
        }
        StreamRegistration r = VaadinSession.getCurrent().getResourceRegistry().registerResource(sr);
        registrations.add(r);
        Element s = new Element("source");
        s.setAttribute("src", StreamResourceRegistry.getURI(sr).toASCIIString());
        s.setAttribute("type", sr.getContentTypeResolver().apply(sr, VaadinServlet.getCurrent().getServletContext()));
        getElement().appendChild(s);
    }

    /**
     * Show controls.
     * @param on Whether to show or not
     */
    public void showControls(boolean on) {
        getElement().setAttribute("controls", on);
    }
}
