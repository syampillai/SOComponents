package com.storedobject.vaadin;

/**
 * Field to edit {@link Boolean} values.
 *
 * @author Syam
 */
public class BooleanField extends CustomField<Boolean> {

    private final ToggleButton checkbox = new ToggleButton();

    /**
     * Constructor.
     */
    public BooleanField() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public BooleanField(String label) {
        this(label, null);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     */
    public BooleanField(Boolean initialValue) {
        this(null, initialValue);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public BooleanField(String label, Boolean initialValue) {
        super(false);
        setLabel(label);
        add(checkbox);
        setValue(initialValue);
        setTabIndex(-1);
    }

    @Override
    public void setValue(Boolean value) {
        super.setValue(value != null && value);
    }

    @Override
    protected Boolean generateModelValue() {
        return checkbox.getValue();
    }

    @Override
    protected void setPresentationValue(Boolean value) {
        checkbox.setValue(value);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        checkbox.setReadOnly(readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        checkbox.setEnabled(enabled);
    }
}