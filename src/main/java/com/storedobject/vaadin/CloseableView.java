package com.storedobject.vaadin;

/**
 * In order to display a "closeable vew", {@link View} creates a special "menu item" with a "Close" icon
 * and the {@link View} can be closed by clicking on that icon. A {@link View} can be made "closeable" by
 * implementing this marker interface by itself or its component ({@link View#getComponent()}).
 *
 * @author Syam
 */
public interface CloseableView {
}