package com.storedobject.vaadin.util;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;

import jakarta.servlet.http.HttpServletResponse;

/**
 * The InvalidLinkError class is an error handler that extends the
 * RouteNotFoundError class and is used to manage invalid or non-existent
 * application links.
 *
 * When triggered, it provides a notification to the user indicating that
 * the application link is invalid and sets the HTTP response status to 404
 * (Not Found).
 *
 * @author Syam
 */
public class InvalidLinkError extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        getElement().setText("");
        String path = event.getLocation().getPath();
        Notification.show("Invalid application link" + (path.isEmpty() ? "" : (" - " + path)));
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
