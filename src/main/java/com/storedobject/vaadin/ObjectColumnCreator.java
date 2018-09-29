package com.storedobject.vaadin;

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ObjectColumnCreator<T> {

    default ObjectColumnCreator<T> create(@SuppressWarnings("unused") DataGrid<T> dataGrid) {
        return this;
    }

    default Stream<String> getColumnNames() {
        return null;
    }

    default Method getColumnMethod(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    default Function<T, ?> getColumnFunction(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    default int getColumnOrder(@SuppressWarnings("unused") String columnName) {
        return Integer.MAX_VALUE;
    }

    default String getHeader(@SuppressWarnings("unused") String columnName) {
        return ApplicationEnvironment.get().createLabel(columnName);
    }

    default void close() {
    }
}