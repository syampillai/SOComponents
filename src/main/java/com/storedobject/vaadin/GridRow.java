package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to handle join operations of header and footer row cells.
 * Each cell in the row represented by {@link Cell} has a name and is the same as its column name. If a cell is
 * a merged cell (after a {@link #join(String...)} operation), then, its name will be the column name of the
 * first column of the merged columns.
 *
 * @author Syam
 */
public abstract class GridRow {

    private final Grid<?> grid;
    private final Object row;

    private GridRow(Grid<?> grid, Object row) {
        this.grid = grid;
        this.row = row;
    }

    /**
     * Create a header row.
     *
     * @param grid Grid for which the row needs to be created
     * @param append Whether to append or not.
     * @return The row that is created.
     */
    static GridRow createHeader(Grid<?> grid, boolean append) {
        return new GridHeaderRow(grid, append);
    }

    /**
     * Create a footer row.
     *
     * @param grid Grid for which the row needs to be created
     * @param append Whether to append or not.
     * @return The row that is created.
     */
    static GridRow createFooter(Grid<?> grid, boolean append) {
        return new GridFooterRow(grid, append);
    }

    /**
     * Join all cells starting at a given index and ending at the last column (inclusive).
     * @param startintgIndex Starting index of the column
     * @return The new Cell that is created or null if join operation fails.
     */
    public Cell join(int startintgIndex) {
        return join(startintgIndex, Integer.MAX_VALUE);
    }

    /**
     * Join cells for a range of column indices.
     * @param startintgIndex Starting index of the column
     * @param endingIndex Ending index of the column (exclusive).
     * @return The new Cell that is created or null if join operation fails.
     */
    public Cell join(int startintgIndex, int endingIndex) {
        List<String> names = new ArrayList<>();
        getCells().skip(startintgIndex).limit(endingIndex - startintgIndex).forEach(c -> names.add(c.name));
        return join(names);
    }

    /**
     * Join cells. (If no cell names are passed, all cells in the row are joined).
     *
     * @param names Names of the cells to join (Names should be specified in proper order from left to right)
     * @return The new Cell that is created or null if join operation fails.
     */
    public Cell join(Collection<String> names) {
        if(names == null || names.isEmpty()) {
            return join((String[])null);
        }
        String[] colNames = new String[names.size()];
        names.toArray(colNames);
        return join(colNames);
    }

    /**
     * Join cells. (If no cell names are passed, all cells in the row are joined).
     *
     * @param names Names of the cells to join (Names should be specified in proper order from left to right)
     * @return The new Cell that is created or null if join operation fails.
     */
    public Cell join(String... names) {
        if(names != null && names.length == 1) {
            return getCell(names[0]);
        }
        try {
            List<Cell> cells = new ArrayList<>();
            if(names == null || names.length == 0) {
                getCells().forEach(cells::add);
                if(cells.size() == 1) {
                    return getCell(cells.get(0).name);
                }
            } else {
                Cell cell;
                for(String name: names) {
                    cell = getCell(name);
                    if(cell == null) {
                        return null;
                    }
                    cells.add(cell);
                }
            }
            if(row instanceof HeaderRow) {
                List<HeaderRow.HeaderCell> hcells = cells.stream().map(c -> (HeaderRow.HeaderCell)c.cell).collect(Collectors.toList());
                ((HeaderRow)row).join(hcells);
            } else {
                List<FooterRow.FooterCell> fcells = cells.stream().map(c -> (FooterRow.FooterCell)c.cell).collect(Collectors.toList());
                ((FooterRow)row).join(fcells);
            }
            return names == null || names.length == 0 ? getCells().findAny().orElse(null) : getCell(names[0]);
        } catch (Throwable error) {
            Application a = Application.get();
            if(a != null) {
                a.log(error);
            }
            return null;
        }
    }

    /**
     * Join cells. (If no cells are passed, all cells in the row are joined).
     *
     * @param cells Cells to join (Cells should be specified in proper order from left to right)
     * @return The new Cell that is created or null if join operation fails.
     */
    public Cell joinCells(Cell... cells) {
        if (cells != null && cells.length == 1) {
            return getCell(cells[0].name);
        }
        if(cells == null) {
            return join();
        }
        List<String> names = new ArrayList<>();
        for(Cell c: cells) {
            names.add(c.name);
        }
        return join(names);
    }

    /**
     * Get a specific cell from this row.
     *
     * @param name Name of the cell
     * @return Cell with the given name if it exists, otherwise <code>null</code>.
     */
    public Cell getCell(String name) {
        Object cell = getCell(grid.getColumnByKey(name));
        return cell == null ? null : new Cell(cell, name);
    }

    private Object getCell(Grid.Column<?> column) {
        if(column == null || column.isFrozen()) {
            return null;
        }
        return row instanceof HeaderRow ? ((HeaderRow)row).getCell(column) : ((FooterRow)row).getCell(column);
    }

    /**
     * Get all the cells of this row.
     *
     * @return Stream of cells of this row.
     */
    public Stream<Cell> getCells() {
        ArrayList<Grid.Column<?>> columns = new ArrayList<>();
        Object prev = null, cell;
        for(Object column: grid.getColumns()) {
            cell = getCell((Grid.Column<?>) column);
            if(cell == null || cell == prev) {
                continue;
            }
            prev = cell;
            columns.add((Grid.Column<?>) column);
        }
        return columns.stream().map(c -> new Cell(getCell(c), c.getKey()));
    }

    private static class GridHeaderRow extends GridRow {

        public GridHeaderRow(Grid<?> grid, boolean append) {
            super(grid, append ? grid.appendHeaderRow() : grid.prependHeaderRow());
        }
    }

    private static class GridFooterRow extends GridRow {

        public GridFooterRow(Grid<?> grid, boolean append) {
            super(grid, append ? grid.appendFooterRow() : grid.prependFooterRow());
        }
    }

    /**
     * A class that represents a single cell of the row.
     *
     * @author Syam
     */
    public static class Cell {

        private final String name;
        private final Object cell;

        private Cell(Object cell, String name) {
            this.cell = cell;
            this.name = name;
        }

        /**
         * Get the name of this cell.
         *
         * @return Name
         */
        public String getName() {
            return name;
        }

        /**
         * Set text for this cell to display.
         *
         * @param text Text to set
         * @return Self reference.
         */
        public Cell setText(String text) {
            if(cell instanceof HeaderRow.HeaderCell) {
                ((HeaderRow.HeaderCell) cell).setText(text);
            } else {
                ((FooterRow.FooterCell) cell).setText(text);
            }
            return this;
        }

        /**
         * Set text for this cell to display.
         *
         * @param text Text to set
         * @param textAlign Alignment for the text
         * @return Self reference.
         */
        public Cell setText(String text, ColumnTextAlign textAlign) {
            if(textAlign == null || textAlign.equals(ColumnTextAlign.START)) {
                return setText(text);
            }
            Div div = new Div(new Span(text));
            div.getStyle().set("text-align", textAlign.toString());
            div.setSizeFull();
            return setComponent(div);
        }

        /**
         * Set a component for this cell (Text if any already set will be discarded).
         *
         * @param component Component to set
         * @return Self reference.
         */
        public Cell setComponent(Component component) {
            if(cell instanceof HeaderRow.HeaderCell) {
                ((HeaderRow.HeaderCell) cell).setComponent(component);
            } else {
                ((FooterRow.FooterCell) cell).setComponent(component);
            }
            return this;
        }
    }
}