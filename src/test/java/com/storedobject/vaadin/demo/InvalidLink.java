package com.storedobject.vaadin.demo;

import com.storedobject.vaadin.util.InvalidLinkError;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;

import javax.servlet.http.HttpServletResponse;

public class InvalidLink extends InvalidLinkError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        System.err.println("Here!");
        event.rerouteTo(Demo.DemoView.class);
        return HttpServletResponse.SC_OK;
    }
}
