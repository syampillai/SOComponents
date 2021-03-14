package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;

/**
 * A {@link View} that is wrapped in an {@link ExecutableView}.
 * The {@link ExecutableView}'s {@link ExecutableView#getView(boolean)} can return such a wrapped view.
 * However, any {@link Component} may be used inside and it is not necessary to use anything that really
 * implements {@link ExecutableView}.
 *
 * @author Syam
 */
public class WrappedView extends View {

    private final Component executableViewComponent;
    private boolean recursiveClose = false, recursiveAbort = false, recursiveClean = false;

    /**
     * Constructor.
     *
     * @param executableView The {@link ExecutableView} component. (It can be a normal {@link Component} too).
     */
    public WrappedView(Component executableView) {
        this(executableView,
                executableView instanceof ExecutableView ? ((ExecutableView) executableView).getCaption() : null);
    }

    /**
     * Constructor.
     *
     * @param executableView The {@link ExecutableView} component. (It can be a normal {@link Component} too).
     * @param caption Caption for the view.
     */
    public WrappedView(Component executableView, String caption) {
        super(executableView, caption);
        this.executableViewComponent = executableView;
    }

    @Override
    public boolean isCloseable() {
        if(executableViewComponent instanceof ExecutableView) {
            return ((ExecutableView) executableViewComponent).isCloseable();
        }
        return executableViewComponent instanceof CloseableView;
    }

    @Override
    public boolean isHomeView() {
        if(executableViewComponent instanceof ExecutableView) {
            return ((ExecutableView) executableViewComponent).isHomeView();
        }
        return executableViewComponent instanceof HomeView;
    }

    @Override
    public void returnedFrom(View parent) {
        if(executableViewComponent != null) {
            ((ExecutableView) executableViewComponent).returnedFrom(parent);
        }
    }

    @Override
    public void close() {
        if(recursiveClose) {
            return;
        }
        recursiveClose = true;
        super.close();
        if(executableViewComponent instanceof ExecutableView) {
            ((ExecutableView) executableViewComponent).close();
        }
        recursiveClose = false;
    }

    @Override
    public void abort() {
        if(recursiveAbort) {
            return;
        }
        recursiveAbort = true;
        super.abort();
        if(executableViewComponent instanceof ExecutableView) {
            ((ExecutableView) executableViewComponent).abort();
        }
        recursiveAbort = false;
    }

    @Override
    public void clean() {
        if(recursiveClean) {
            return;
        }
        recursiveClean = true;
        super.clean();
        if(executableViewComponent instanceof ExecutableView) {
            ((ExecutableView) executableViewComponent).clean();
        }
        recursiveClean = false;
    }

    @Override
    public String getMenuIconName() {
        if(executableViewComponent instanceof ExecutableView) {
            return ((ExecutableView) executableViewComponent).getMenuIconName();
        }
        return super.getMenuIconName();
    }

    @Override
    public ApplicationMenuItem createMenuItem(Runnable menuAction) {
        if(executableViewComponent instanceof ExecutableView) {
            return ((ExecutableView) executableViewComponent).createMenuItem(menuAction);
        }
        return super.createMenuItem(menuAction);
    }
}
