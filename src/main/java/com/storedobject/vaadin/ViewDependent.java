package com.storedobject.vaadin;

/**
 * Interface that suggests that a class dependent on some {@link View}.
 *
 * @author Syam
 */
public interface ViewDependent {
    /**
     * Set the dependent view.
     *
     * @param dependent The dependent view.
     */
    void setDependentView(View dependent);

    /**
     * Get the dependent view.
     *
     * @return The dependent view.
     */
    View getDependentView();
}