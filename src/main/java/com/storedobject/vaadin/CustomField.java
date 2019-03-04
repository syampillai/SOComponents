package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.HtmlImport;

@Tag("vaadin-custom-field")
@HtmlImport("frontend://bower_components/vaadin-custom-field/src/vaadin-custom-field.html")
public abstract class CustomField<T> extends AbstractField<CustomField<T>, T> implements HasComponents, HasSize, HasValidation, Focusable<CustomField> {

    public CustomField(T defaultValue) {
        super(defaultValue);
        getElement().addEventListener("change", (e) -> setModelValue(this.generateModelValue(), true));
    }

    public void addField(HasValue<?, ?>... fields) {
        addField(this, fields);
    }

    public void addField(HasComponents container, HasValue<?, ?>... fields) {
        for(HasValue<?, ?> field: fields) {
            if(field instanceof Component) {
                container.add((Component)field);
            }
            field.addValueChangeListener(e -> {
                if(e.isFromClient()) {
                    setModelValue(this.generateModelValue(), true);
                }
            });
        }
    }

    protected abstract T generateModelValue();

    protected abstract void setPresentationValue(T var1);

    protected void updateValue() {
        this.setModelValue(this.generateModelValue(), false);
    }

    @Synchronize(
            property = "invalid",
            value = {"invalid-changed"}
    )
    public boolean isInvalid() {
        return this.getElement().getProperty("invalid", false);
    }

    public void setInvalid(boolean invalid) {
        this.getElement().setProperty("invalid", invalid);
    }

    public void setErrorMessage(String errorMessage) {
        this.getElement().setProperty("errorMessage", errorMessage);
    }

    public String getErrorMessage() {
        return this.getElement().getProperty("errorMessage");
    }

    public String getLabel() {
        return this.getElement().getProperty("label", null);
    }

    public void setLabel(String label) {
        this.getElement().setProperty("label", label);
    }
}