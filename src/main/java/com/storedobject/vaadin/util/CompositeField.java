package com.storedobject.vaadin.util;

import com.storedobject.vaadin.Field;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.Objects;
import java.util.Optional;

public class CompositeField<T, S extends CompositeField<T, S, E, M>, E extends HasValue.ValueChangeEvent<T>, M extends CompositeField.MultiField<E, T>>
        extends CompositeSingleField<T, CompositeField.SOField<T, E, M>, S, T> {

    private SOField<T, E, M> soField;

    protected CompositeField(M field, T defaultValue) {
        super(defaultValue);
        this.field = field;
    }

    @Override
    protected Component initContent() {
        createField();
        return soField;
    }

    @Override
    protected void createTrackers() {
        getField().createTrackers();
    }

    @Override
    protected HasValue[] fieldList() {
        HasValue[] fields = getField().fieldList();
        return fields == null ? super.fieldList() : fields;
    }

    @Override
    protected T getModelValue(HasValue field) {
        if(field == getField()) {
            return super.getModelValue(field);
        }
        return getField().saveValue(getValue());
    }

    @Override
    protected void createField() {
        if(soField != null) {
            return;
        }
        getField().getContent();
        soField = new SOField<>(getField());
    }

    public void setWidth(String width) {
        createField();
        field.setWidth(width);
    }

    public void setHeight(String height) {
        createField();
        field.setHeight(height);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected M getField() {
        return (M)super.getField();
    }

    private SOField<T, E, M> getSOField() {
        if(soField == null) {
            createField();
        }
        return soField;
    }

    @Override
    protected boolean isValid() {
        return getField().isValid();
    }

        @Override
    protected T getPresentationValue(T value) {
        return value;
    }

    @Override
    protected T getModelValue(T presentationValue) {
        return presentationValue;
    }

    public void setLabel(String label) {
        getSOField().setLabel(label);
    }

    public String getLabel() {
        return getSOField().getLabel();
    }

    @Tag("so-field")
    @HtmlImport("bower_components/polymer/polymer-element.html")
    @HtmlImport("bower_components/vaadin-themable-mixin/vaadin-themable-mixin.html")
    @HtmlImport("bower_components/vaadin-element-mixin/vaadin-element-mixin.html")
    @HtmlImport("so-field.html")
    static class SOField<V, E extends HasValue.ValueChangeEvent<V>, C extends HasValueAndElement<E, V>>
            extends PolymerTemplate<SOField.SOFieldModel> implements Field<V> {

        private HasValueAndElement<E, V> field;
        private ValueChangeListeners listeners = new ValueChangeListeners();

        public interface SOFieldModel extends TemplateModel {
            void setLabel(String label);
            String getLabel();
        }

        public SOField(HasValueAndElement<E, V> field) {
            this.field = field;
            getElement().appendChild(field.getElement());
        }

        public void setValue(V value) {
            field.setValue(value);
        }

        public V getValue() {
            return field.getValue();
        }

        public V getEmptyValue() {
            return field.getEmptyValue();
        }

        public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
            field.setRequiredIndicatorVisible(requiredIndicatorVisible);
        }

        public boolean isRequiredIndicatorVisible() {
            return field.isRequiredIndicatorVisible();
        }

        public void setReadOnly(boolean readOnly) {
            field.setReadOnly(readOnly);
        }

        public boolean isReadOnly() {
            return field.isReadOnly();
        }

        @Override
        public void focus() {
            ((Focusable)field).focus();
        }

        @Override
        public void blur() {
            ((Focusable)field).blur();
        }

        public Registration addValueChangeListener(HasValue.ValueChangeListener<? super E> valueChangeListener) {
            return listeners.add(valueChangeListener);
        }

        public Optional<V> getOptionalValue() {
            return field.getOptionalValue();
        }

        public boolean isEmpty() {
            return field.isEmpty();
        }

        public void clear() {
            field.clear();
        }

        public void setEnabled(boolean enabled) {
            field.setEnabled(enabled);
        }

        public boolean isEnabled() {
            return field.isEnabled();
        }

        public void setLabel(String label) {
            getModel().setLabel(label);
        }

        public String getLabel() {
            return getModel().getLabel();
        }
    }

    public static abstract class MultiField<Event extends HasValue.ValueChangeEvent<Type>, Type>
            implements Field<Type>, HasValueAndElement<Event, Type> {

        private Component content;
        private Type value;

        protected abstract Component initComponent();

        protected HasValue[] fieldList() {
            return null;
        }

        protected void createTrackers() {
        }

        protected Component getContent() {
            if (content == null) {
                content = initComponent();
            }
            return content;
        }

        @Override
        public final Type getValue() {
            //getContent();
            //value = saveValue(value);
            return value;
        }

        @Override
        public final void setValue(Type value) {
            this.value = value;
            getContent();
            if (value == null) {
                clear();
            } else {
                loadValue(value);
            }
        }

        protected abstract void loadValue(Type value);

        protected abstract Type saveValue(Type value);

        @Override
        public void setReadOnly(boolean readOnly) {
            HasValueAndElement.super.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() {
            return HasValueAndElement.super.isReadOnly();
        }

        @Override
        public final Element getElement() {
            return getContent().getElement();
        }

        protected Focusable<?> getPrimaryFocus() {
            return focus(getContent());
        }

        @Override
        public final void focus() {
            Focusable<?> f = getPrimaryFocus();
            if (f != null) {
                f.focus();
            }
        }

        @Override
        public final void blur() {
            blur(getContent());
        }

        private static void blur(Component container) {
            if (container instanceof Focusable) {
                ((Focusable) container).blur();
                return;
            }
            container.getChildren().forEach(MultiField::blur);
        }

        private static Focusable<?> focus(Component container) {
            if (container instanceof Focusable) {
                return (Focusable<?>) container;
            }
            return container.getChildren().map(MultiField::focus).filter(Objects::nonNull).findFirst().orElse(null);
        }

        @Override
        public final void setLabel(String label) {
        }

        @Override
        public final String getLabel() {
            return null;
        }

        @Override
        public Registration addValueChangeListener(ValueChangeListener<? super Event> valueChangeListener) {
            return null;
        }

        protected boolean isValid() {
            return true;
        }
    }

    public static class SField<Type, C extends Component & HasValueAndElement<Event, Type>, Event extends AbstractField.ComponentValueChangeEvent<C, Type>>
            extends MultiField<Event, Type> {

        private C field;

        public SField(C field) {
            this.field = field;
        }
        @Override
        protected final C initComponent() {
            return field;
        }

        @Override
        protected final void loadValue(Type value) {
            field.setValue(value);
        }

        @Override
        protected final Type saveValue(Type value) {
            return field.getValue();
        }

        @Override
        public Registration addValueChangeListener(ValueChangeListener<? super Event> listener) {
            return field.addValueChangeListener(listener);
        }

        public C getField() {
            return field;
        }
    }

    public static abstract class SingleField<T, S extends SingleField<T, S, C, E>, C extends Component & HasValueAndElement<E, T>, E extends AbstractField.ComponentValueChangeEvent<C, T>>
            extends CompositeField<T, S, E, SField<T, C, E>> {

        protected SingleField(C field, T defaultValue) {
            super(new SField<>(field), defaultValue);
        }

        public C getInnerField() {
            return getField().getField();
        }
    }
}