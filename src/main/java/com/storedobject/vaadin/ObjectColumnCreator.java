package com.storedobject.vaadin;

import com.vaadin.flow.component.grid.ColumnTextAlign;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Interface that defines the "column creator" for {@link DataGrid} and {@link DataTreeGrid}.
 *
 * @param <T> Data type of the grid
 * @author Syam
 */
public interface ObjectColumnCreator<T> {

    /**
     * Create a "column creator" for the given grid.
     *
     * @param grid The grid for which columns need to be created.
     * @return Column creator. The defaul implementation return the self reference.
     */
    default ObjectColumnCreator<T> create(@SuppressWarnings("unused") HasColumns<T> grid) {
        return this;
    }

    /**
     * Get the column names for the the grid.
     *
     * @return Stream of column names.
     */
    default Stream<String> getColumnNames() {
        return null;
    }

    /**
     * Get the method that is used to generate the content of a particular column.
     *
     * @param columnName Name of the column
     * @return Method. Default implemenation returns null.
     */
    default Method getColumnMethod(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    /**
     * Get the funtion that is used to generate the content of a particular column.
     *
     * @param columnName Name of the column
     * @return Function. Default implemenation returns null.
     */
    default Function<T, ?> getColumnFunction(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    /**
     * Get the order of the column to be displayed in the grid. It could be any integer number and lower numbered columns will be on the left side.
     *
     * @param columnName Name of the column
     * @return An integer number representing the order. Default implementation returns {@link Integer#MIN_VALUE}.
     */
    default int getColumnOrder(@SuppressWarnings("unused") String columnName) {
        return Integer.MIN_VALUE;
    }

    /**
     * Get the header text for the given column.
     *
     * @param columnName Name of the column
     * @return Header text. Default implementation returns the value from {@link ApplicationEnvironment#createLabel(String)}.
     */
    default String getColumnCaption(@SuppressWarnings("unused") String columnName) {
        return Objects.requireNonNull(ApplicationEnvironment.get()).createLabel(columnName);
    }

    /**
     * Get the text alignment of a particular column.
     *
     * @param columnName Name of the column
     * @param valueType Value type of the column (If the value tye can not be determined, it will be <code>null</code>)
     * @return Text alignment. Default implemenation returns START if the value type is <code>null</code> or non-numeric.
     */
    default ColumnTextAlign getColumnTextAlign(@SuppressWarnings("unused") String columnName, @SuppressWarnings("unused") Class<?> valueType) {
        if(valueType != null) {
            if(Number.class.isAssignableFrom(valueType) || valueType == int.class || valueType == long.class || valueType == double.class ||  valueType == float.class) {
                return ColumnTextAlign.END;
            }
        }
        return null;
    }

    /**
     * Get the value type of a particular column.
     *
     * @param columnName Name of the column
     * @return Value type. The default implementation returns <code>null</code>.
     */
    default Class<?> getColumnValueType(@SuppressWarnings("unused") String columnName) {
        return null;
    }

    /**
     * This will be invoked when the process of "column creation" is over. Default implementation does nothing.
     */
    default void close() {
    }
}