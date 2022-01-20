package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * A grid that implements a {@link List} data model.
 *
 * @param <T> Data type of the grid
 * @author Syam
 */
public class ListGrid<T> extends DataGrid<T> implements List<T> {

    private boolean wrapped;
    private final Refresher refresher = new Refresher();
    private DataList<T> data;

    /**
     * Constructor.
     *
     * @param objectClass Class of objects stored.
     */
    public ListGrid(Class<T> objectClass) {
        this(objectClass, null, null);
    }

    /**
     * Constructor.
     *
     * @param objectClass Class of objects stored.
     * @param columns Column names.
     */
    public ListGrid(Class<T> objectClass, Iterable<String> columns) {
        this(objectClass, null, columns);
    }

    /**
     * Constructor.
     *
     * @param objectClass Class of objects stored.
     * @param data Data to be set. Same data may be used on multiple {@link ListGrid}s.
     */
    public ListGrid(Class<T> objectClass, List<T> data) {
        this(objectClass, data, null);
    }

    /**
     * Constructor.
     *
     * @param objectClass Class of objects stored.
     * @param data Data to be set. Same data may be used on multiple {@link ListGrid}s.
     * @param columns Column names.
     */
    public ListGrid(Class<T> objectClass, List<T> data, Iterable<String> columns) {
        super(objectClass, columns);
        setData(data);
    }

    /**
     * Get the data of this list. If the data is not an instance of {@link DataList} and
     * is directly manipulated, {@link #refresh()} or {@link #refresh(Object)} should be
     * invoked to reflect the changes on the grid.
     *
     * @return Data of this list.
     */
    public List<T> getData() {
        return wrapped ? data.getData() : data;
    }

    /**
     * Set the data of this list.
     *
     * @param data Data to be set. Same data may be used on multiple {@link ListGrid}s.
     */
    public void setData(List<T> data) {
        if(data == null) {
            data = new DataList<>();
        }
        DataList<T> list;
        if(data instanceof DataList) {
            list = (DataList<T>) data;
            wrapped = false;
        } else {
            list = new DataList<>(data);
            wrapped = true;
        }
        this.data = list;
        super.setItems(createListDataProvider(this.data));
        refresher.change();
    }

    /**
     * Create an instance of {@link ListDataProvider} for the given data. The default implementation creates an
     * instance of Vaadin's implementation. However, a modified implementation can be provided instead.
     *
     * @param data Data for which data provider needs to be created.
     * @return An instance of the {@link ListDataProvider}.
     */
    protected ListDataProvider<T> createListDataProvider(DataList<T> data) {
        return new ListDataProvider<>(data);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        refresher.set();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        refresher.remove();
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
        return data.add(item);
    }

    @Override
    public boolean remove(Object item) {
        return data.remove(item);
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> collection) {
        return data.containsAll(collection);
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends T> collection) {
        return data.addAll(collection);
    }

    @Override
    public boolean addAll(int index, @Nonnull Collection<? extends T> collection) {
        return data.addAll(index, collection);
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> collection) {
        return data.removeAll(collection);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return data.removeIf(filter);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> collection) {
        return data.retainAll(collection);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        data.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        data.sort(comparator);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public T get(int index) {
        return data.get(index);
    }

    @Override
    public T set(int index, T item) {
        return data.set(index, item);
    }

    @Override
    public void add(int index, T item) {
        data.add(index, item);
    }

    @Override
    public T remove(int index) {
        return data.remove(index);
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

    @Override
    public ListDataProvider<T> getDataProvider() {
        //noinspection unchecked
        return (ListDataProvider<T>) super.getDataProvider();
    }

    /**
     * Set a filter. (All previously added filters will be cleared).
     *
     * @param filter Filter to set.
     */
    public void setViewFilter(Predicate<T> filter) {
        getDataProvider().setFilter(filter::test);
    }

    /**
     * Add a filter.
     *
     * @param filter Filter to add.
     */
    public void addViewFilter(Predicate<T> filter) {
        if(filter == null) {
            return;
        }
        Predicate<T> old = getDataProvider().getFilter();
        if(old == null) {
            setViewFilter(filter);
            return;
        }
        setViewFilter(item -> old.test(item) && filter.test(item));
    }

    /**
     * Clear all filters.
     */
    public void clearViewFilters() {
        //noinspection ConstantConditions
        setViewFilter(null);
    }

    private class Refresher implements DataList.RefreshListener<T> {

        private Registration registration;

        void change() {
            if(registration != null) {
                registration.remove();
                registration = data.addRefreshListener(this);
                refresh();
            }
        }

        void set() {
            if(registration == null) {
                registration = data.addRefreshListener(this);
                refresh();
            }
        }

        void remove() {
            if(registration != null) {
                registration.remove();
                registration = null;
            }
        }

        @Override
        public void refresh() {
            ListGrid.this.refresh();
        }

        @Override
        public void refresh(T item) {
            ListGrid.this.refresh(item);
        }
    }
}
