package com.storedobject.vaadin;

import com.vaadin.flow.shared.Registration;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * A list data structure with a "refresh" listener. The refresh methods of the
 * listeners ({@link RefreshListener}) are called whenever list is changed by adding/deleting/updating items.
 * <p>This class may be used instead of normal {@link List} if you want to use it as data for
 * {@link ListGrid}. The same instance may be used in multiple {@link ListGrid}s and all grids will be
 * simultaneously updated when data is updated programmatically.</p>
 *
 * @param <T> Type of data in the list.
 * @author Syam
 */
public class DataList<T> implements List<T> {

    private final List<T> data;
    private List<RefreshListener<T>> listeners;

    /**
     * Constructor.
     */
    public DataList() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param data for the list.
     */
    public DataList(List<T> data) {
        this.data = data == null ? new ArrayList<>() : data;
    }

    /**
     * Get the data of this list. If the data is directly manipulated, {@link #refresh()} or {@link #refresh(Object)}
     * should be invoked to inform the listeners about the changes.
     *
     * @return Data of this list.
     */
    public List<T> getData() {
        return data;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object item) {
        return data.contains(item);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Nonnull
    @Override
    public <T1> T1[] toArray(@Nonnull T1[] a) {
        //noinspection SuspiciousToArrayCall
        return data.toArray(a);
    }

    @Override
    public boolean add(T item) {
        data.add(item);
        refresh();
        return true;
    }

    @Override
    public boolean remove(Object item) {
        if(data.remove(item)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> collection) {
        return data.containsAll(collection);
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends T> collection) {
        if(data.addAll(collection)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int index, @Nonnull Collection<? extends T> collection) {
        if(data.addAll(index, collection)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> collection) {
        if(data.removeAll(collection)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        if(data.removeIf(filter)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> collection) {
        if(data.retainAll(collection)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        data.clear();
        refresh();
    }

    @Override
    public T get(int index) {
        return data.get(index);
    }

    @Override
    public T set(int index, T item) {
        T d = data.set(index, item);
        refresh(item);
        return d;
    }

    @Override
    public void add(int index, T item) {
        data.add(index, item);
        refresh();
    }

    @Override
    public T remove(int index) {
        T item = data.remove(index);
        if(item != null) {
            refresh();
        }
        return item;
    }

    @Override
    public int indexOf(Object item) {
        return data.indexOf(item);
    }

    @Override
    public int lastIndexOf(Object item) {
        return data.lastIndexOf(item);
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator() {
        return data.listIterator();
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator(int index) {
        return data.listIterator(index);
    }

    @Nonnull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }

    /**
     * Inform the listeners that the items in this list are refreshed due to addition or deletion of items.
     */
    public void refresh() {
        if(listeners != null) {
            listeners.forEach(RefreshListener::refresh);
        }
    }

    /**
     * Inform the listeners that an item of this in this list is refreshed.
     *
     * @param item Item that is changed.
     */
    public void refresh(T item) {
        if(listeners != null) {
            listeners.forEach(listener -> listener.refresh(item));
        }
    }

    /**
     * Add a refresh listener for this list.
     *
     * @param listener Listener to add.
     * @return Registration.
     */
    public Registration addRefreshListener(RefreshListener<T> listener) {
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        data.sort(comparator);
        refresh();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        data.replaceAll(operator);
        refresh();
    }

    /**
     * Definition of refresh listener that can be added to the {@link DataList}.
     * See {@link #addRefreshListener(RefreshListener)}.
     *
     * @param <T> Type of data in the list.
     */
    public interface RefreshListener<T> {

        /**
         * Invoked whenever data in the list is added and/or deleted.
         */
        void refresh();

        /**
         * Invoked whenever data in the list is updated.
         *
         * @param item Item that is updated.
         */
        void refresh(T item);
    }
}