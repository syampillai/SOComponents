package com.storedobject.vaadin;

import com.storedobject.vaadin.util.ClickNotifier;
import com.storedobject.vaadin.util.ElementClick;
import com.vaadin.flow.component.*;
import com.vaadin.flow.shared.Registration;

/**
 * A wrapped class to make any Vaadin component "clickable".
 * <p>Example: Component c; new Clickable(c);</p>
 *
 * @param <T> Component type
 * @author Syam
 */
public class Clickable<T extends Component> extends Composite<T> implements ClickNotifier {

    private T component;
    private ElementClick click;

    /**
     * Constructor.
     * @param component Component to make it clickable
     */
    public Clickable(T component) {
        this(component,null);
    }

    /**
     * Constructor.
     * @param component Component to make it clickable
     * @param clickHandler Click handler
     */
    public Clickable(T component, ClickHandler clickHandler) {
        this.component = component;
        click = new ElementClick(component);
        if(clickHandler != null) {
            addClickHandler(clickHandler);
        }
    }

    @Override
    protected T initContent() {
        return component;
    }

    @Override
    public Registration addClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        return click.addClickListener(listener);
    }

    @Override
    public void removeClickListener(ComponentEventListener<ClickEvent<Component>> listener) {
        click.removeClickListener(listener);
    }

    /**
     * Add a click handler.
     *
     * @param clickHandler Click handler to add
     * @return Registration.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Registration addClickHandler(ClickHandler clickHandler) {
        if(clickHandler == null) {
            return null;
        }
        return addClickListener(ClickHandler.convert(clickHandler));
    }
}