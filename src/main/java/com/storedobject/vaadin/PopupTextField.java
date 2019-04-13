package com.storedobject.vaadin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Enhancement to TextField to pop up a window to accept multiline text.
 *
 * @author Syam
 */
public class PopupTextField extends TextField {

    private TextArea textArea;
    private Window window;
    private StyledText label;

    /**
     * Constructor.
     */
    public PopupTextField() {
        init();
    }

    /**
     * Constructor.
     *
     * @param label Label
     */
    public PopupTextField(String label) {
        super(label);
        init();
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param placeholder Placeholder
     */
    public PopupTextField(String label, String placeholder) {
        super(label, placeholder);
        init();
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param initialValue Initial value
     * @param placeholder Placeholder
     */
    public PopupTextField(String label, String initialValue, String placeholder) {
        super(label, initialValue, placeholder);
        init();
    }

    /**
     * Constructor.
     *
     * @param listener Value change listener
     */
    public PopupTextField(ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
        super(listener);
        init();
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param listener Value change listener
     */
    public PopupTextField(String label, ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
        super(label, listener);
        init();
    }

    /**
     * Constructor.
     *
     * @param label Label
     * @param initialValue Initial value
     * @param listener Value change listener
     */
    public PopupTextField(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
        super(label, initialValue, listener);
        init();
    }

    private void init() {
        Icon pop = new Icon(VaadinIcon.ELLIPSIS_DOTS_H);
        pop.getElement().getStyle().set("cursor", "pointer");
        setSuffixComponent(pop);
        pop.addClickListener(e -> pop());
    }

    private void pop() {
        if(window == null) {
            createWindow();
        }
        String label = getLabel();
        this.label.setText(label == null ? "" : label);
        textArea.setValue(getValue());
        window.open();
    }

    private void createWindow() {
        textArea = new TextArea();
        textArea.setWidth("600px");
        label = new StyledText();
        Button ok = new Button("Ok", e -> { PopupTextField.this.setValue(textArea.getValue()); window.close(); });
        Button cancel = new Button("Cancel", e -> window.close());
        window = new Window(new Div(new ButtonLayout(ok, cancel, label), textArea));
    }
}
