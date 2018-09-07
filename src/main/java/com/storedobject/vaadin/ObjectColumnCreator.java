package com.storedobject.vaadin;

import java.util.function.Function;
import java.util.stream.Stream;

public interface ObjectColumnCreator<T> {

    default ObjectColumnCreator<T> create(DataGrid<T> dataGrid) {
        return this;
    }

    default Stream<String> getColumnNames() {
        return null;
    }

    default Function<T, ?> getColumnFunction(String columnName) {
        return null;
    }

    default int getColumnOrder(String columnName) {
        return Integer.MAX_VALUE;
    }

    default String getHeader(String columnName) {
        return ApplicationEnvironment.get().createLabel(columnName);
    }

    default void close() {
    }
}