package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.stream.Stream;

/**
 * This class is used to accept some quick data from the users. It has an "Ok" and a "Cancel" button. By default, is is displayed
 * as a {@link com.vaadin.flow.component.dialog.Dialog} box (you can switch off this feature using {@link #setWindowMode(boolean)}).
 * @author Syam
 */
public abstract class DataForm extends AbstractDataForm {

    protected Button ok, cancel;
    protected HasComponents buttonPanel;
    private boolean buttonsOnTop = false;

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
     * @param labelOK "Ok" label
     * @param labelCancel "Cancel" label
     */
    public DataForm(String caption, String labelOK, String labelCancel) {
        this.form = new DForm();
        setErrorDisplay(null);
        setCaption(caption);
        ok = new Button(labelOK == null ? "Ok" : labelOK, "ok", this).asPrimary();
        cancel = new Button(labelCancel == null ? "Cancel" : labelCancel, "cancel", this);
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
        if(buttonsOnTop) {
            v.add((Component)buttonPanel, getForm().getComponent());
        } else {
            v.add(getForm().getComponent(), (Component)buttonPanel);
        }
        buildFields();
        buildButtons();
        setComponent(new Window((Component)v));
    }

    private HasComponents createDefaultLayout() {
        return new VerticalLayout();
    }

    private HasComponents createDefaultButtonLayout() {
        return new ButtonLayout();
    }

    /**
     * This is where the fields are built. Typically, we create the fields and add it using {@link #addField(HasValue)} or
     * {@link #addField(String, HasValue)} methods.
     */
    protected abstract void buildFields();

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
        }
    }

    /**
     * Button panel is normally shown at the bottom. This method makes it appear at the top.
     * @param buttonsOnTop Whether the button panel should be shown at the top or not
     */
    public void setButtonsOnTop(boolean buttonsOnTop) {
        this.buttonsOnTop = buttonsOnTop;
    }

    /**
     * Validate data. This method is invoked when the "Ok" button is pressed. {@link #process()} will be invoked only if this
     * method did not raise any exception.
     * @throws Exception Exception raised will be displayed as a warning message.
     */
    protected void validateData() throws Exception {
    }

    private class DForm extends Form {

        public DForm() {
            setView(DataForm.this);
        }

        @Override
        public Stream<String> getFieldNames() {
            return DataForm.this.getFieldNames();
        }

        @Override
        protected HasComponents createContainer() {
            return DataForm.this.createContainer();
        }

        @Override
        protected HasValue<?, ?> createField(String fieldName) {
            return DataForm.this.createField(fieldName);
        }

        @Override
        protected void attachField(String fieldName, HasValue<?, ?> field) {
            try {
                DataForm.this.attachField(fieldName, field);
            } catch (FieldError e) {
                super.attachField(fieldName, field);
            }
        }

        @Override
        protected void detachField(String fieldName, HasValue<?, ?> field) {
            try {
                DataForm.this.detachField(fieldName, field);
            } catch (FieldError e) {
                super.detachField(fieldName, field);
            }
        }
    }
}
