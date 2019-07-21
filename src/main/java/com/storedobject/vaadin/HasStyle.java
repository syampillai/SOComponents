package com.storedobject.vaadin;

import com.vaadin.flow.dom.ClassList;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;

/**
 * Same as {@link com.vaadin.flow.component.HasStyle} but can be applied to an "internal element" specified.
 *
 * @author Syam
 */
public interface HasStyle extends com.vaadin.flow.component.HasStyle {

    default Element getInternalElement() {
        return getElement();
    }

    @Override
    default void setClassName(String className) {
        getInternalElement().setAttribute("class", className);
    }

    @Override
    default String getClassName() {
        return getInternalElement().getAttribute("class");
    }

    @Override
    default ClassList getClassNames() {
        return getInternalElement().getClassList();
    }

    @Override
    default Style getStyle() {
        return getInternalElement().getStyle();
    }
}
