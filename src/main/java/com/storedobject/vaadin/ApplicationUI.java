package com.storedobject.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class ApplicationUI extends UI {

    private String link;
    private String error;

    @Override
    protected void init(VaadinRequest request) {
        addDetachListener(e -> close());
        String link = request.getContextPath();
        if (link != null && link.length() > 1 && link.startsWith("/")) {
            link = link.substring(1);
        } else {
            link = null;
        }
        this.link = link;
        link = request.getPathInfo();
        if (link != null && !link.equals("/")) {
            error = "Unknown application '" + (this.link == null ? "" : this.link) + link + "'.\nPlease use the correct link.";
        }
    }

    final String getLink() {
        return link;
    }

    final String getError() {
        return error;
    }

    @Override
    public void close() {
        if(isClosing()) {
            return;
        }
        VaadinSession vs = VaadinSession.getCurrent();
        super.close();
        if(vs != null) {
            Application a = Application.get(vs);
            if(a != null) {
                a.close();
            }
        }
    }
}
