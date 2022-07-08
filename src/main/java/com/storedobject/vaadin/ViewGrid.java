package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

import java.util.List;

/**
 * A grid to be shown in a window with an "Exit" button for viewing the rows of the grid.
 *
 * @param <T> Type of object in the grid.
 * @author Syam
 */
public class ViewGrid<T> extends ListGrid<T> {

    private String emptyRowsMessage = "No entries found!";
    /**
     * The button layout at the top.
     */
    protected final ButtonLayout buttonLayout = new ButtonLayout();
    /**
     * The "Exit" button.
     */
    protected final Button exit = new Button("Exit", e -> close());

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     */
    public ViewGrid(Class<T> objectClass, List<T> items, String caption) {
        this(objectClass, items, null, caption);
    }

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     * @param columns Column names of the grid.
     */
    public ViewGrid(Class<T> objectClass, List<T> items, Iterable<String> columns, String caption) {
        super(objectClass, items, columns);
        setWidth("60vw");
        setHeight("60vh");
        setCaption(caption);
    }

    @Override
    public final Component createHeader() {
        addExtraButtons();
        buttonLayout.add(exit);
        return buttonLayout;
    }

    /**
     * Add extra buttons if required. You can add anything to {@link #buttonLayout}.
     */
    public void addExtraButtons() {
    }

    @Override
    public void execute(View lock) {
        if(isEmpty()) {
            if(emptyRowsMessage != null) {
                warning(emptyRowsMessage);
            }
            return;
        }
        super.execute(lock);
    }

    @Override
    public final View createView() {
        return new GridView();
    }

    /**
     * Set the message to be displayed when no rows exist while executing.
     * If set to null, no message will be displayed.
     *
     * @param emptyRowsMessage Message to be displayed.
     */
    public void setEmptyRowsMessage(String emptyRowsMessage) {
        this.emptyRowsMessage = emptyRowsMessage;
    }

    private class GridView extends View implements CloseableView, HomeView {

        private GridView() {
            setCaption(ViewGrid.this.getCaption());
            setComponent(new Window(new WindowDecorator(this), ViewGrid.this));
        }
    }
}
