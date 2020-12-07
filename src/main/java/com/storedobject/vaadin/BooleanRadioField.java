package com.storedobject.vaadin;

/**
 * Boolean field displayed like a radio field.
 *
 * @author Syam
 */
public class BooleanRadioField extends TranslatedField<Boolean, String> {

    private static final String[] yesNo = new String[] { "Yes", "No" };

    /**
     * Constructor.
     */
    public BooleanRadioField() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param initialValue Initial value.
     */
    public BooleanRadioField(boolean initialValue) {
        this(null, initialValue);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     */
    public BooleanRadioField(String label) {
        this(label, false);
    }

    /**
     * Constructor.
     *
     * @param label Label.
     * @param initialValue Initial value.
     */
    public BooleanRadioField(String label, boolean initialValue) {
        super(new RadioField<>(yesNo), (f, s) -> "Yes".equals(s), (f, b) -> yesNo[b ? 0 : 1]);
        if(label != null) {
            setLabel(label);
        }
        setValue(initialValue);
    }
}
