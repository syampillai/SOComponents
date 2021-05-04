package com.storedobject.vaadin;

import com.vaadin.flow.component.HasStyle;

/**
 * Interface to denote that some text-input based component can switch on/off spell-check feature.
 *
 * @author Syam
 */
public interface SpellCheck extends HasStyle {

    /**
     * Set spelling check on/off. (Default is off.)
     *
     * @param on True to set it on.
     */
    default void setSpellCheck(boolean on) {
        getElement().setAttribute("spellcheck", "" + on);
    }
}
