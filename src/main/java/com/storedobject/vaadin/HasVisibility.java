package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * Interface that determines whether an object (typically a "field") has visibility or not. It doesn't have to be a
 * {@link com.vaadin.flow.component.Component} with a {@link com.vaadin.flow.component.Tag} for having the visibility.
 * However, {@link com.vaadin.flow.component.Component} should be already considered as having this feature even though
 * it's not implementing it explicitly. So, some crude work-around is required to use this properly - see the
 * static methods {@link HasVisibility#setVisible(boolean, Object)} and {@link HasVisibility#isVisible(Object)}
 * <p>Note: If you are wondering what is the use of this, please study the code of {@link AbstractDataForm}.</p>
 * 
 * @author Syam
 */
public interface HasVisibility {

    /**
     * Set the field visible/hidden.
     * 
     * @param visible True if visible, otherwise, false.
     */
    void setVisible(boolean visible);

    /**
     * Is this field visible or not.
     * 
     * @return True/false.
     */
    boolean isVisible();

    /**
     * Set the visibility of "something".
     * 
     * @param visible Visibility.
     * @param object Object for which visibility to be set.
     */
    static void setVisible(boolean visible, Object object) {
        if(object instanceof Component c) {
            c.setVisible(visible);
        } else if(object instanceof HasVisibility hv) {
            hv.setVisible(visible);
        }
    }

    /**
     * Check if an object is visible or not.
     * 
     * @param object Object to check.
     * @return True/false. (If the object is not of type {@link Component} or {@link HasVisibility}, it will always 
     * return <code>false</code>.
     */
    static boolean isVisible(Object object) {
        if(object instanceof Component c) {
            return c.isVisible();
        } else if(object instanceof HasVisibility hv) {
            return hv.isVisible();
        }
        return false;
    }
}
