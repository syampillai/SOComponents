package com.storedobject.vaadin;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A grid to be shown in a window with "Proceed/Cancel" buttons to select multiple entries. When the "Proceed" button is
 * pressed, the selected entries are passed to a {@link java.util.function.Consumer} that can be set.
 *
 * @param <T> Type of object in the grid.
 * @author Syam
 */
public class MultiSelectGrid<T> extends SelectGrid<T> {

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     */
    public MultiSelectGrid(Class<T> objectClass, List<T> items) {
        this(objectClass, items, (Consumer<Set<T>>) null);
    }

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     * @param columns Column names of the grid.
     */
    public MultiSelectGrid(Class<T> objectClass, List<T> items, Iterable<String> columns) {
        this(objectClass, items, columns, null);
    }

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     * @param consumer Consumer to consume the selected item.
     */
    public MultiSelectGrid(Class<T> objectClass, List<T> items, Consumer<Set<T>> consumer) {
        this(objectClass, items, null, consumer);
    }

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     * @param columns Column names of the grid.
     * @param consumer Consumer to consume the selected item.
     */
    public MultiSelectGrid(Class<T> objectClass, List<T> items, Iterable<String> columns, Consumer<Set<T>> consumer) {
        //noinspection unchecked
        super(objectClass, items, columns, o -> consumer.accept((Set<T>) o), true);
    }

    /**
     * This method is never called.
     *
     * @param selected Selected item.
     */
    @Override
    protected final void process(T selected) {
        super.process(selected);
    }

    /**
     * This will be invoked when the "Proceed" button is pressed and no "consumer" is set. (View will have already
     * closed before calling this).
     *
     * @param selected Set of selected items.
     */
    protected void process(Set<T> selected) {
    }
}
