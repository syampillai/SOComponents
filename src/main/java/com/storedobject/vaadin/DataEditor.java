package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

public class DataEditor<T> extends AbstractDataEditor<T> {

    protected Button save, cancel;
    protected HasComponents buttonPanel;
    private Consumer<T> saveAction, cancelAction;

    public DataEditor(Class<T> objectClass) {
        this(objectClass, null, null, (Consumer<T>)null, null);
    }

    public DataEditor(Class<T> objectClass, String caption) {
        this(objectClass, caption, null, null, null, null);
    }

    public DataEditor(Class<T> objectClass, String labelSave, String labelCancel) {
        this(objectClass, labelSave, labelCancel, (Consumer<T>)null, null);
    }

    public DataEditor(Class<T> objectClass, String caption, String labelSave, String labelCancel) {
        this(objectClass, caption, labelSave, labelCancel, null, null);
    }


    public DataEditor(Class<T> objectClass, Consumer<T> saveAction) {
        this(objectClass, null, null, saveAction, null);
    }

    public DataEditor(Class<T> objectClass, String caption, Consumer<T> saveAction) {
        this(objectClass, caption, null, null, saveAction, null);
    }

    public DataEditor(Class<T> objectClass, String labelSave, String labelCancel, Consumer<T> saveAction) {
        this(objectClass, labelSave, labelCancel, saveAction, null);
    }

    public DataEditor(Class<T> objectClass, String caption, String labelSave, String labelCancel, Consumer<T> saveAction) {
        this(objectClass, caption, labelSave, labelCancel, saveAction, null);
    }


    public DataEditor(Class<T> objectClass, Consumer<T> saveAction, Consumer<T> cancelAction) {
        super(objectClass);
        init(null, null, saveAction, cancelAction);
    }

    public DataEditor(Class<T> objectClass, String caption, Consumer<T> saveAction, Consumer<T> cancelAction) {
        super(objectClass, caption);
        init(null, null, saveAction, cancelAction);
    }

    public DataEditor(Class<T> objectClass, String labelSave, String labelCancel, Consumer<T> saveAction, Consumer<T> cancelAction) {
        super(objectClass);
        init(labelSave, labelCancel, saveAction, cancelAction);
    }

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

    private HasComponents createDefaultLayout() {
        return new VerticalLayout();
    }

    private HasComponents createDefaultButtonLayout() {
        return new ButtonLayout();
    }

    protected void buildButtons() {
        buttonPanel.add(save, cancel);
    }

    protected void cancel() {
        abort();
        if(cancelAction != null) {
            cancelAction.accept(getObject());
        }
    }

    protected void save() {
        close();
        if(saveAction != null) {
            saveAction.accept(getObject());
        }
    }

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