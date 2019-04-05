package com.storedobject.vaadin;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Span;

/**
 * A component to show HTML text.
 *
 * @author Syam
 */
public class StyledText extends Composite<Span> implements HasText, HasSize, HTMLGenerator {

    private Span content = new Span();
    private String text;

    public StyledText(String htmlText) {
        setText(htmlText);
    }

    @Override
    protected Span initContent() {
        return content;
    }

    @Override
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

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getHTML() {
        return text == null ? "" : text;
    }
}