package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.html.Span;

/**
 * A component to show HTML text.
 *
 * @author Syam
 */
public class StyledText extends Composite<Span> implements HasText, HasSize, HTMLGenerator {

    private final Span content = new Span();
    private String text;

    /**
     * Constructor.
     */
    public StyledText() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param htmlText HTML text to set (No validation is done)
     */
    public StyledText(String htmlText) {
        setText(htmlText);
        content.getStyle().set("display", "inline");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        setTextInt(text);
        super.onAttach(attachEvent);
    }

    @Override
    protected Span initContent() {
        return content;
    }

    @Override
    public void setText(String htmlText) {
        setTextInt(htmlText);
    }

    private void setTextInt(String htmlText) {
        if(htmlText == null) {
            htmlText = "";
        }
        this.text = htmlText;
        content.getElement().executeJs("this.innerHTML = $0", htmlText);
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