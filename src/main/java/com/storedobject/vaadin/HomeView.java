package com.storedobject.vaadin;

/**
 * This is a marker interface to denote that a {@link View}, once displayed, should not be removed and should remain
 * as a "home view" (unless it is programmatically removed). If more that one {@link View} are defined as "home views", the one that executed most recently will
 * replace the earlier ones.
 *
 * @author Syam
 */
public interface HomeView {
}
