package com.storedobject.vaadin;

import com.storedobject.vaadin.util.Data;
import com.vaadin.flow.component.HasValue;

/**
 * Class to represent a "data entry form". A "field" in the form is a {@link HasValue}. Form internally keeps {@link Data} that is a structure
 * that keeps data for all the "fields" of the form.
 * <p>A form is not used directly in most cases. Instead, a {@link View} derived from {@link AbstractDataForm} is used where a form is
 * already embedded. All overridable methods in the form can be defined in {@link AbstractDataForm} too.</p>
 *
 * @author Syam
 */
public class Form extends AbstractForm<Object> {

    public Form() {
        super(Object.class);
    }
}