package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
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

    public StyledText() {
        this(null);
    }

    public StyledText(String htmlText) {
        setText(htmlText);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        setText(text);
        super.onAttach(attachEvent);
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
        this.text = htmlText;
        content.getElement().executeJavaScript("this.innerHTML = $0", htmlText);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getHTML() {
        return getText();
    }
}