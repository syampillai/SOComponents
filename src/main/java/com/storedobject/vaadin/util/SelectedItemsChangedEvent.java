package com.storedobject.vaadin.util;

import com.storedobject.vaadin.TokenField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.EventData;
import elemental.json.JsonArray;

/**
 * For internal use only.
 *
 * @author Syam
 */
public class SelectedItemsChangedEvent extends ComponentEvent<TokenField<?>> {

    private JsonArray newItems;

    public SelectedItemsChangedEvent(TokenField<?> source, boolean fromClient,
                                     @EventData(value = "element.selectedItems") JsonArray newItems) {
        super(source, fromClient);
        this.newItems = newItems;
    }

    public JsonArray getNewItems() {
        return newItems;
    }
}