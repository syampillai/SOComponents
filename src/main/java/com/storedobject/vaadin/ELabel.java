package com.storedobject.vaadin;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.html.Div;

public class ELabel extends Composite<Div> implements HasText {

    private Div content = new Div();

    public ELabel(String htmlText) {
        if(htmlText != null) {
            setText(htmlText);
        }
    }

    @Override
    protected Div initContent() {
        return content;
    }

    public void setText(String htmlText) {
        if(htmlText == null) {
            htmlText = "<div></div>";
        }
        if(htmlText.equals(getText())) {
            return;
        }
        getElement().setProperty("innerHTML", htmlText);
    }

    public String getText() {
        return getElement().getProperty("innerHTML");
    }
}