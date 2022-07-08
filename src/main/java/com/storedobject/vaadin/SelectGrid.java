package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A grid to be shown in a window with "Proceed/Cancel" buttons to select an entry. When the "Proceed" button is
 * pressed, the selected entry is passed to a {@link java.util.function.Consumer} that can be set.
 *
 * @param <T> Type of object in the grid.
 * @author Syam
 */
public class SelectGrid<T> extends ListGrid<T> {

    private String emptyRowsMessage = "No entries found!";
    /**
     * The button layout at the top.
     */
    protected final ButtonLayout buttonLayout = new ButtonLayout();
    /**
     * The "Proceed" button.
     */
    protected final Button proceed = new Button("Proceed", e -> act());
    /**
     * The "Cancel" button.
     */
    protected final Button cancel = new Button("Cancel", e -> cancel());
    private final Consumer<Object> consumer;

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     */
    public SelectGrid(Class<T> objectClass, List<T> items) {
        this(objectClass, items, (Consumer<T>) null);
    }

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     * @param columns Column names of the grid.
     */
    public SelectGrid(Class<T> objectClass, List<T> items, Iterable<String> columns) {
        this(objectClass, items, columns, null);
    }

    /**
     * Constructor.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     * @param consumer Consumer to consume the selected item.
     */
    public SelectGrid(Class<T> objectClass, List<T> items, Consumer<T> consumer) {
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
    public SelectGrid(Class<T> objectClass, List<T> items, Iterable<String> columns, Consumer<T> consumer) {
        //noinspection unchecked
        this(objectClass, items, columns, o -> consumer.accept((T)o), false);
    }

    /**
     * For internal use only.
     *
     * @param objectClass Type object in the grid.
     * @param items Items of the grid.
     * @param columns Column names of the grid.
     * @param consumer Consumer to consume the selected item.
     * @param multi Whether multi-select mode or not.
     */
    SelectGrid(Class<T> objectClass, List<T> items, Iterable<String> columns, Consumer<Object> consumer, boolean multi) {
        super(objectClass, items, columns);
        this.consumer = consumer;
        setWidth("60vw");
        setHeight("60vh");
        if(multi) {
            setSelectionMode(SelectionMode.MULTI);
        } else {
            addItemDoubleClickListener(e -> act(e.getItem()));
        }
    }

    @Override
    public final Component createHeader() {
        addExtraButtons();
        buttonLayout.add(proceed, cancel);
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

    private void act() {
        if(getSelectionModel() instanceof GridMultiSelectionModel) {
            if(!validate()) {
                return;
            }
            close();
            Set<T> selected = getSelectedItems();
            if(consumer == null) {
                process(selected);
            } else {
                consumer.accept(selected);
            }
        } else {
            act(getSelected());
        }
    }

    private void act(T selected) {
        if(selected == null) {
            warning("Please select an entry");
            return;
        }
        if(!validate()) {
            return;
        }
        close();
        if(consumer == null) {
            process(selected);
        } else {
            consumer.accept(selected);
        }
    }

    /**
     * This will be invoked when the "Cancel" button is pressed. The default action is to abort the {@link View}.
     */
    protected void cancel() {
        abort();
    }

    /**
     * This method is never called.
     *
     * @param selected Selected item.
     */
    protected void process(Set<T> selected) {
    }

    /**
     * This will be invoked when the "Proceed" button is pressed and no "consumer" is set. (View will have already
     * closed before calling this).
     *
     * @param selected Selected item.
     */
    protected void process(T selected) {
    }

    @Override
    public final View createView() {
        return new SelectView();
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

    /**
     * This method is called just before closing the grid's view and before the selected item/items are handed over
     * to the consumer. If <code>false</code> is returned from this method
     * grid's view will not be closed and consumer will not be fed with the item/items.
     *
     * @return True/false. Default implementation returns <code>true</code>.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean validate() {
        return true;
    }

    private class SelectView extends View {

        private SelectView() {
            super("Select " + (SelectGrid.this instanceof MultiSelectGrid ? "Entries" : "an Entry"));
            setComponent(SelectGrid.this);
            setWindowMode(true);
            String c = SelectGrid.this.getCaption();
            if(c != null && !c.isEmpty()) {
                setCaption(c);
            }
        }
    }
}
