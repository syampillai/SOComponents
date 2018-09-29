package com.storedobject.vaadin;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/*", name = "SOServlet", asyncSupported = true, loadOnStartup = 0)
@VaadinServletConfiguration(ui = ApplicationUI.class, productionMode = false, closeIdleSessions = true)
public abstract class SOServlet extends VaadinServlet {

    protected abstract Application createApplication();

    @SuppressWarnings("unused")
    public static class InvalidLinkError extends RouteNotFoundError {

        @Override
        public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
            getElement().setText("");
            Notification.show("Invalid application link");
            return HttpServletResponse.SC_NOT_FOUND;
        }
    }
}
