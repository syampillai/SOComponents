package com.storedobject.vaadin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;

/**
 * Interface that handles mouse clicks (typically on {@link Button}).
 * @author Syam
 */
@FunctionalInterface
public interface ClickHandler extends ComponentEventListener<ClickEvent<? extends Component>> {

    /**
     * This method is invoked from {@link #clicked(ClickEvent)} when a component is clicked.
     * @param c Component
     */
    void clicked(Component c);

    /**
     * This method is invoked from {@link #clicked(ClickEvent)} when a component is double clicked.
     * Default implementation invokes {@link #clicked(Component)}.
     * @param c Component
     */
    default void doubleClicked(Component c) {
        clicked(c);
    }

    /**
     * This method is invoked from {@link #clicked(ClickEvent)} when a is right clicked.
     * Default implementation invokes {@link #clicked(Component)}.
     * @param c Component
     */
    default void rightClicked(Component c) {
        clicked(c);
    }

    /**
     * This method is invoked from {@link #onComponentEvent(ClickEvent)} when a component is clicked.
     * Default implementation invokes {@link #clicked(Component)}, {@link #doubleClicked(Component)} or {@link #rightClicked(Component)}.
     * @param event Click event.
     */
    default void clicked(ClickEvent event) {
        Component c = event.getSource();
        if(event.getButton() == 2) {
            rightClicked(c);
        } else {
            if (event.getClickCount() == 1) {
                clicked(c);
            } else {
                doubleClicked(c);
            }
        }
    }

    /**
     * Default implementation invokes {@link #clicked(ClickEvent)}.
     * @param event Click event.
     */
    @Override
    default void onComponentEvent(ClickEvent<? extends Component> event) {
        clicked(event);
    }

    /**
     * Convert a Click Hanlder to a {@link ComponentEventListener}.
     * @param clickHandler Click handler to convert.
     * @param <T> Component type
     * @return Component event handler.
     */
    static <T extends Component> ComponentEventListener<ClickEvent<T>> convert(ClickHandler clickHandler) {
        return e -> { if(clickHandler != null) {
            clickHandler.onComponentEvent(e);
        }};
    }

    /**
     * Create a "click handler" that transfers the "clicks" to to another component.
     * @param clickHandler Click handler whose clicks to be transferred.
     * @param another Component to which clicks to be transferred.
     * @return Newly created click handler.
     */
    static ClickHandler transfer(ClickHandler clickHandler, Component another) {
        return new ClickHandler() {
            @Override
            public void clicked(Component c) {
                if(clickHandler != null) {
                    clickHandler.clicked(another);
                }
            }

            @Override
            public void onComponentEvent(ClickEvent<? extends Component> event) {
                if(clickHandler != null) {
                    clickHandler.onComponentEvent(new ModifiedClickEvent<>(event, another));
                }
            }
        };
    }

    /**
     * Create a click event from another event.
     * @param <C> Component type
     */
    class ModifiedClickEvent<C extends Component> extends ClickEvent<C> {

        /**
         * Constructor.
         * @param original Original event.
         * @param to Component to which the modified event should be attached.
         */
        public ModifiedClickEvent(ClickEvent<C> original, Component to) {
            super(to, original.isFromClient(), original.getScreenX(), original.getScreenY(), original.getClientX(),
                    original.getClientY(), original.getClickCount(), original.getButton(), original.isCtrlKey(),
                    original.isShiftKey(), original.isAltKey(), original.isMetaKey());
        }
    }
}
