package com.storedobject.vaadin;

import com.vaadin.flow.component.HasValue;

/**
 * Enahancemet to {@link HasValue.ValueChangeListener} that wraps the changed field ({@link HasValue} and its value into {@link ChangedValues}
 * and invoke (default implementation) {@link #valueChanged(ChangedValues)}.
 * @author Syam
 */
@FunctionalInterface
public interface ValueChangeHandler extends HasValue.ValueChangeListener {

    /**
     * Default implemenation invokes {@link #valueChanged(ChangedValues)}.
     * @param valueChangeEvent Value changed event.
     */
    @Override
    default void valueChanged(HasValue.ValueChangeEvent valueChangeEvent) {
        valueChanged(new ChangedValues(valueChangeEvent));
    }

    /**
     * Invoked by {@link #valueChanged(HasValue.ValueChangeEvent)} when a value of a field ({@link HasValue}) is changed.
     * @param changedValues Change information (field and it value changes) wrapped into {@link ChangedValues}.
     */
    void valueChanged(ChangedValues changedValues);
}