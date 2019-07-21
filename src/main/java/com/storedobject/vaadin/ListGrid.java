package com.storedobject.vaadin;

import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.*;

/**
 * A grid that implements a List data model.
 * @param <T> Data type of the grid
 *
 * @author Syam
 */
public class ListGrid<T> extends DataGrid<T> implements List<T> {

    private List<T> data = new ArrayList<>();

    public ListGrid(Class<T> objectClass) {
        this(objectClass, null);
    }

    public ListGrid(Class<T> objectClass, Iterable<String> columns) {
        super(objectClass, columns);
        super.setDataProvider(new ListDataProvider<>(data));
    }

    @Override
    public void setDataProvider(DataProvider<T, ?> dataProvider) {
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

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
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
    public boolean containsAll(Collection<?> collection) {
        return data.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        if(data.addAll(collection)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        if(data.addAll(index, collection)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if(data.removeAll(collection)) {
            refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
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

    @Override
    public ListIterator<T> listIterator() {
        return data.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return data.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }
}
