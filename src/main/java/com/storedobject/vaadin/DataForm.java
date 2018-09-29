package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.stream.Stream;

public abstract class DataForm extends AbstractDataForm {

    protected Button ok, cancel;
    protected HasComponents buttonPanel;
    private boolean buttonsOnTop = false;

    public DataForm(Application a, String caption) {
        this(a, caption, null, null);
    }

    public DataForm(Application a, String caption, String labelOK, String labelCancel) {
        super(a);
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

    protected abstract void buildFields();

    protected void buildButtons() {
        buttonPanel.add(ok, cancel);
    }

    protected void cancel() {
        abort();
    }

    protected abstract boolean process();

    @Override
    public void clicked(Component c) {
        if(c == cancel) {
            cancel();
            return;
        }
        if(c == ok) {
            if(process()) {
                close();
            }
        }
    }

    public void setButtonsOnTop(boolean buttonsOnTop) {
        this.buttonsOnTop = buttonsOnTop;
    }

    private class DForm extends Form {

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
