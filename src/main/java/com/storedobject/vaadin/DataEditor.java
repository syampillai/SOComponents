package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

/**
 * A {@link View} that is used for creating a "data entry form" with "Save"/"Cancel" buttons.
 *
 * @param <T> Type of the object to edit
 * @author Syam
 */
public class DataEditor<T> extends AbstractDataEditor<T> {

    /**
     * The "save" button.
     */
    protected Button save;
    /**
     * The "cancel" button.
     */
    protected Button cancel;
    /**
     * The button panel where the buttons are painted.
     */
    protected HasComponents buttonPanel;
    private Consumer<T> saveAction, cancelAction;

    /**
     * Constructor.
     * @param objectClass Object class
     */
    public DataEditor(Class<T> objectClass) {
        this(objectClass, null, null, (Consumer<T>)null, null);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param caption Caption
     */
    public DataEditor(Class<T> objectClass, String caption) {
        this(objectClass, caption, null, null, null, null);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param labelSave Label for "save" button
     * @param labelCancel Label for "cancel" button
     */
    public DataEditor(Class<T> objectClass, String labelSave, String labelCancel) {
        this(objectClass, labelSave, labelCancel, (Consumer<T>)null, null);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param caption Caption
     * @param labelSave Label for "save" button
     * @param labelCancel Label for "cancel" button
     */
    public DataEditor(Class<T> objectClass, String caption, String labelSave, String labelCancel) {
        this(objectClass, caption, labelSave, labelCancel, null, null);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     */
    public DataEditor(Class<T> objectClass, Consumer<T> saveAction) {
        this(objectClass, null, null, saveAction, null);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param caption Caption
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     */
    public DataEditor(Class<T> objectClass, String caption, Consumer<T> saveAction) {
        this(objectClass, caption, null, null, saveAction, null);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param labelSave Label for "save" button
     * @param labelCancel Label for "cancel" button
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     */
    public DataEditor(Class<T> objectClass, String labelSave, String labelCancel, Consumer<T> saveAction) {
        this(objectClass, labelSave, labelCancel, saveAction, null);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param caption Caption
     * @param labelSave Label for "save" button
     * @param labelCancel Label for "cancel" button
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     */
    public DataEditor(Class<T> objectClass, String caption, String labelSave, String labelCancel, Consumer<T> saveAction) {
        this(objectClass, caption, labelSave, labelCancel, saveAction, null);
    }


    /**
     * Constructor.
     * @param objectClass Object class
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     * @param cancelAction Cancel action (Will be called when the "cancel" button is pressed)
     */
    public DataEditor(Class<T> objectClass, Consumer<T> saveAction, Consumer<T> cancelAction) {
        super(objectClass);
        init(null, null, saveAction, cancelAction);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param caption Caption
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     * @param cancelAction Cancel action (Will be called when the "cancel" button is pressed)
     */
    public DataEditor(Class<T> objectClass, String caption, Consumer<T> saveAction, Consumer<T> cancelAction) {
        super(objectClass, caption);
        init(null, null, saveAction, cancelAction);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param labelSave Label for "save" button
     * @param labelCancel Label for "cancel" button
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     * @param cancelAction Cancel action (Will be called when the "cancel" button is pressed)
     */
    public DataEditor(Class<T> objectClass, String labelSave, String labelCancel, Consumer<T> saveAction, Consumer<T> cancelAction) {
        super(objectClass);
        init(labelSave, labelCancel, saveAction, cancelAction);
    }

    /**
     * Constructor.
     * @param objectClass Object class
     * @param caption Caption
     * @param labelSave Label for "save" button
     * @param labelCancel Label for "cancel" button
     * @param saveAction Save action (Will be called when the "save" button is pressed and data is validated)
     * @param cancelAction Cancel action (Will be called when the "cancel" button is pressed)
     */
    public DataEditor(Class<T> objectClass, String caption, String labelSave, String labelCancel, Consumer<T> saveAction, Consumer<T> cancelAction) {
        super(objectClass, caption);
        init(labelSave, labelCancel, saveAction, cancelAction);
    }

    private void init(String labelSave, String labelCancel, Consumer<T> saveAction, Consumer<T> cancelAction) {
        save = new Button(labelSave == null ? "Save" : labelSave, "ok", this).asPrimary();
        cancel = new Button(labelCancel == null ? "Cancel" : labelCancel, "cancel", this);
        this.saveAction = saveAction;
        this.cancelAction = cancelAction;
    }

    @Override
    protected void initUI() {
        buttonPanel = createButtonLayout();
        if(buttonPanel == null) {
            buttonPanel = createDefaultButtonLayout();
        }
        HasComponents v = createLayout();
        if(v == null) {
            v = createDefaultLayout();
        }
        v.add((Component)buttonPanel, getForm().getComponent());
        buildButtons();
        setComponent((Component)v);
    }

    @Override
    public boolean skipFirstFocus(Focusable<?> skipFocus) {
        return skipFocus == save || skipFocus == cancel;
    }

    private HasComponents createDefaultLayout() {
        return new VerticalLayout();
    }

    private HasComponents createDefaultButtonLayout() {
        return new ButtonLayout();
    }

    /**
     * Build the buttons. Button "save" and "cancel" are already created. This method add those to the {@link #buttonPanel}.
     * You can add additional custom buttons and components to the {@link #buttonPanel}.
     */
    protected void buildButtons() {
        buttonPanel.add(save, cancel);
    }

    /**
     * This method is invoked to carry out the "cancel". By default it processes the "cancel action" if defined.
     */
    protected void cancel() {
        abort();
        if(cancelAction != null) {
            cancelAction.accept(getObject());
        }
    }

    /**
     * This method is invoked to carry out the "save". By default it processes the "save action" if defined.
     */
    protected void save() {
        close();
        if(saveAction != null) {
            saveAction.accept(getObject());
        }
    }

    /**
     * Validate data. This method is invoked when the "save" button is pressed. {@link #save()} will be invoked only if this
     * method did not raise any exception.
     * @throws Exception Exception raised will be displayed as a warning message and "data entry form" will not be closed.
     */
    protected void validateData() throws Exception {
    }

    @Override
    public void clicked(Component c) {
        if(c == cancel) {
            cancel();
            return;
        }
        if(c == save) {
            if(commit()) {
                try {
                    validateData();
                } catch (Exception e) {
                    warning(e);
                    return;
                }
                save();
            }
        }
    }
}