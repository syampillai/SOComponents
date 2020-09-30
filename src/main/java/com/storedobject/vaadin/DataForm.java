package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * This class is used to accept some quick data from the users. It has an "Ok" and a "Cancel" button. By default, is is displayed
 * as a {@link com.vaadin.flow.component.dialog.Dialog} box.
 *
 * @author Syam
 */
public abstract class DataForm extends AbstractDataForm<Object> {

    /**
     * The Ok button.
     */
    protected Button ok;
    /**
     * The Cancel button.
     */
    protected Button cancel;
    /**
     * Button panel where buttons are added.
     */
    protected HasComponents buttonPanel;
    private boolean buttonsAtTop = false;
    private final boolean windowMode;

    /**
     * Constructor.
     * @param caption Caption
     */
    public DataForm(String caption) {
        this(caption, null, null);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param windowMode True if the view to be displayed as a window/dialog
     */
    public DataForm(String caption, boolean windowMode) {
        this(caption, null, null, windowMode);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param labelOK "Ok" label
     * @param labelCancel "Cancel" label
     */
    public DataForm(String caption, String labelOK, String labelCancel) {
        this(caption, labelOK, labelCancel, true);
    }

    /**
     * Constructor.
     * @param caption Caption
     * @param labelOK "Ok" label
     * @param labelCancel "Cancel" label
     * @param windowMode True if the view to be displayed as a window/dialog
     */
    public DataForm(String caption, String labelOK, String labelCancel, boolean windowMode) {
        this.form = new Form();
        this.form.setView(this);
        setErrorDisplay(null);
        setCaption(caption);
        ok = new Button(labelOK == null || labelOK.isEmpty() ? "Ok" : labelOK,this).asPrimary();
        ok.setDisableOnClick(true);
        cancel = new Button(labelCancel == null || labelCancel.isEmpty() ? "Cancel" : labelCancel, this);
        this.windowMode = windowMode;
        if(windowMode) {
            setColumns(1);
        }
    }

    @Override
    protected final void initUI() {
        buttonPanel = createButtonLayout();
        if(buttonPanel == null) {
            buttonPanel = createDefaultButtonLayout();
        }
        HasComponents v = createLayout();
        if(v == null) {
            v = createDefaultLayout();
        }
        if(buttonsAtTop) {
            v.add((Component)buttonPanel, getForm().getComponent());
        } else {
            v.add(getForm().getComponent(), (Component)buttonPanel);
        }
        buildFields();
        buildButtons();
        Component c = (Component) v;
        if(windowMode) {
            Window window = createWindow(c);
            if(window == null) {
                window = new Window(c);
            }
            setComponent(window);
            getContent().getElement().getStyle().
                    set("min-width", minMax(getMinimumContentWidth()) + "vw").
                    set("min-height", minMax(getMinimumContentHeight()) + "vh").
                    set("max-width", maxMin(getMaximumContentWidth()) + "vw").
                    set("max-height", maxMin(getMaximumContentHeight()) + "vh");
        } else {
            setComponent(c);
        }
    }

    /**
     * Get the minimum "content width" (Percentage value of the view port width). Value should be between 10 and 80.
     * This value is used only in "window mode".
     *
     * @return Percentage value of the view port width. Default is 10%.
     */
    public int getMinimumContentWidth() {
        return 10;
    }

    /**
     * Get the maximum "content width" (Percentage value of the view port width). Value should be between 10 and 90.
     * This value is used only in "window mode".
     *
     * @return Percentage value of the view port width. Default is 80%.
     */
    public int getMaximumContentWidth() {
        return 80;
    }

    /**
     * Get the minimum "content height" (Percentage value of the view port height. Value should be between 10 and 80.
     * This value is used only in "window mode".
     *
     * @return Percentage value of the view port height. Default is 10%.
     */
    public int getMinimumContentHeight() {
        return 10;
    }

    /**
     * Get the maximum "content height" (Percentage value of the view port height. Value should be between 10 and 90.
     * This value is used only in "window mode".
     *
     * @return Percentage value of the view port height. Default is 80%.
     */
    public int getMaximumContentHeight() {
        return 80;
    }

    private static int minMax(int v) {
        return Math.max(10, Math.min(80, v));
    }

    private static int maxMin(int v) {
        return Math.min(90, Math.max(10, v));
    }

    @Override
    public void setWindowMode(boolean windowOn) {
        throw new UnsupportedOperationException("Window mode can't be changed");
    }

    @Override
    public boolean skipFirstFocus(Focusable<?> skipFocus) {
        return skipFocus == ok || skipFocus == cancel;
    }

    private HasComponents createDefaultLayout() {
        return new VerticalLayout();
    }

    private HasComponents createDefaultButtonLayout() {
        return new ButtonLayout();
    }

    /**
     * This is where the fields are built. Typically, we create the fields and add it using {@link #addField(HasValue...)} or
     * {@link #addField(String, HasValue)} methods.
     */
    protected void buildFields() {
    }

    /**
     * Build the buttons. Button "Ok" and "Cancel" are already created. This method add those to the {@link #buttonPanel}.
     * You can add additional custom buttons and components to the {@link #buttonPanel}.
     */
    protected void buildButtons() {
        buttonPanel.add(ok, cancel);
    }

    /**
     * This method is invoked if the "Cancel" button is pressed. Default action closes the "data entry screen".
     */
    protected void cancel() {
        abort();
    }

    /**
     * This method is invoked if the "Ok" button is pressed and there is no error raised by {@link #validateData()} method. This method has to
     * return <code>true</code> in order to close the "data entry screen".
     * @return True or false.
     */
    protected abstract boolean process();

    @Override
    public void clicked(Component c) {
        if(c == cancel) {
            cancel();
            return;
        }
        if(c == ok) {
            if(commit()) {
                try {
                    validateData();
                } catch (Exception e) {
                    warning(e);
                    return;
                }
                if(process()) {
                    close();
                }
            }
            ok.setEnabled(true);
            ok.setDisableOnClick(true);
        }
    }

    /**
     * Button panel is normally shown at the bottom. This method makes it appear at the top.
     * @param buttonsAtTop Whether the button panel should be shown at the top or not
     */
    public void setButtonsAtTop(boolean buttonsAtTop) {
        this.buttonsAtTop = buttonsAtTop;
    }

    /**
     * Validate data. This method is invoked when the "Ok" button is pressed. {@link #process()} will be invoked only if this
     * method did not raise any exception.
     * @throws Exception Exception raised will be displayed as a warning message.
     */
    @SuppressWarnings("RedundantThrows")
    protected void validateData() throws Exception {
    }
}
