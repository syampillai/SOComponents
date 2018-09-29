package com.storedobject.vaadin;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Span;

public class StyledText extends Composite<Span> implements HasText {

    private Span content = new Span();
    private String text;

    public StyledText(String htmlText) {
        setText(htmlText);
    }

    @Override
    protected Span initContent() {
        return content;
    }

    public void setText(String htmlText) {
        if(htmlText == null) {
            htmlText = "";
        }
        if(htmlText.equals(text)) {
            return;
        }
        text = htmlText;
        content.removeAll();
        content.add(new Html("<span>" + htmlText + "</span>"));
    }

    public String getText() {
        return text;
    }
}