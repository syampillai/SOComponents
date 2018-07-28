package com.storedobject.vaadin.util;

public abstract class CompositeTextField<T> extends CompositeSingleField<String, TextField, CompositeTextField<T>, T> {

    protected CompositeTextField(T defaultValue) {
        super(defaultValue);
    }

    @Override
    protected  void createField() {
        if(field != null) {
            return;
        }
        TF f = new TF();
        f.setPreventInvalidInput(true);
        customizeTextField(f);
        field = f;
    }

    @Override
    public TextField getField() {
        return (TextField)super.getField();
    }

    protected void customizeTextField(TextField textField) {
    }

    @Override
    protected String getPresentationValue(T value) {
        return value == null ? "" : value.toString();
    }

    private static class TF extends com.vaadin.flow.component.textfield.TextField implements TextField {

        @Override
        public void setReadOnly(boolean readOnly) {
            super.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() {
            return super.isReadOnly();
        }

        @Override
        public void focus() {
            super.focus();
        }

        @Override
        public void blur() {
            super.blur();
        }
    }
}