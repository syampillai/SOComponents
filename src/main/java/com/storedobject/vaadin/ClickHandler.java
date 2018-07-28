package com.storedobject.vaadin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;

@FunctionalInterface
public interface ClickHandler extends ComponentEventListener<ClickEvent<? extends Component>> {

    void clicked(Component c);

    default void doubleClicked(Component c) {
        clicked(c);
    }

    default void rightClicked(Component c) {
        clicked(c);
    }

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

    @Override
    default void onComponentEvent(ClickEvent<? extends Component> event) {
        clicked(event);
    }

    static <T extends Component> ComponentEventListener<ClickEvent<T>> convert(ClickHandler clickHandler) {
        return e -> clickHandler.onComponentEvent(e);
    }

    static ClickHandler transfer(ClickHandler clickHandler, Component another) {
        return new ClickHandler() {
            @Override
            public void clicked(Component c) {
                clickHandler.clicked(another);
            }

            @Override
            public void onComponentEvent(ClickEvent<? extends Component> event) {
                clickHandler.onComponentEvent(new ModifiedClickEvent<>(event, another));
            }
        };
    }

    static class ModifiedClickEvent<C extends Component> extends ClickEvent<C> {

        public ModifiedClickEvent(ClickEvent<C> original, Component to) {
            super(to, original.isFromClient(), original.getScreenX(), original.getScreenY(), original.getClientX(),
                    original.getClientY(), original.getClickCount(), original.getButton(), original.isCtrlKey(),
                    original.isShiftKey(), original.isAltKey(), original.isMetaKey());
        }
    }
}
