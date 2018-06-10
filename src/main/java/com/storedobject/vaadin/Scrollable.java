package com.storedobject.vaadin;

public interface Scrollable extends HasElement {

    default void setScrollable(boolean scrollable) {
        if(scrollable) {
            getElement().getStyle().set("overflow", "auto");
        } else if(isScrollable()) {
            getElement().getStyle().remove("overflow");
        }
    }

    default boolean isScrollable() {
        return "auto".equals(getElement().getStyle().get("overflow"));
    }
}
