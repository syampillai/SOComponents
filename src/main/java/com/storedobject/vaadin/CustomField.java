package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;

/**
 * Slightly enhanced version of Vaadin's {@link com.vaadin.flow.component.customfield.CustomField}.
 *
 * @param <T> Value type
 * @author Syam
 */
public abstract class CustomField<T> extends com.vaadin.flow.component.customfield.CustomField<T> {

    /**
     * Constructor.
     *
     * @param defaultValue Default value (getEmptyValue() will return this)
     */
    public CustomField(T defaultValue) {
        super(defaultValue);
    }

    /**
     * Add fields to the custom field. Any change in the field values will fire a value change event.
     *
     * @param fields Fields to be added
     */
    public void addField(HasValue<?, ?>... fields) {
        addField(null, fields);
    }

    /**
     * Add fields to the container (that may have already added to it earlier) of the custom field.
     * Any change in the field values will fire a value change event.
     *
     * @param container Container to which fields must be added
     * @param fields Fields to be added
     */
    public void addField(HasComponents container, HasValue<?, ?>... fields) {
        for(HasValue<?, ?> field: fields) {
            if(field instanceof Component) {
                if(container == null) {
                    add((Component)field);
                } else {
                    container.add((Component) field);
                }
            }
            field.addValueChangeListener(e -> {
                if(e.isFromClient()) {
                    updateValue();
                }
            });
        }
    }
}