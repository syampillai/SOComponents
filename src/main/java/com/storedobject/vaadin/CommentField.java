package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A custom field implementation for managing and editing text comments.
 * The field includes a popover for editing values, with an icon that indicates
 * the state of the field and buttons for saving or cancelling changes.
 *
 * @author Syam
 */
public class CommentField extends CustomField<String> {

    private static String blankColor = "#A9A9A9", activeColor = "var(--so-success-color)";

    /**
     * Represents the layout container for managing and organizing buttons within the CommentField component.
     * This layout is used to house and structure interactive elements like "ok" and "cancel" buttons.
     * It ensures a consistent arrangement of buttons when interacting with the component's UI.
     * The layout is immutable and is initialized when the CommentField is instantiated.
     */
    protected final ButtonLayout buttonLayout = new ButtonLayout();
    /**
     * The "OK" button used within the CommentField component.
     * This button is typically associated with a confirmation or save action
     * for the input provided in the component.
     */
    protected Button ok;
    /**
     * Represents the cancel button in the CommentField component.
     * This variable is used to handle the cancellation events within the
     * comment editing functionality. It allows users to discard any
     * unsaved changes they make to the comment field.
     */
    protected Button cancel;
    private final TextArea textArea = createTextArea();
    private final Icon icon = VaadinIcon.FILE_TEXT.create();
    private final Popover popover = new Pop();
    private List<Consumer<CommentField>> savedListeners, cancelledListeners;

    /**
     * Constructs a new instance of the CommentField with no initial label or value.
     * This constructor initializes the field with a null label and an empty value.
     */
    public CommentField() {
        this(null);
    }

    /**
     * Constructs a CommentField with a specified label.
     *
     * @param label the label to set for the comment field; a null value will not set a label
     */
    public CommentField(String label) {
        this(label, "");
    }

    /**
     * Constructs a CommentField instance with a specified label and initial value.
     *
     * @param label the label to display for the CommentField. If null, no label is set.
     * @param value the initial value of the CommentField. If blank or null, the icon color will be grey.
     */
    public CommentField(String label, String value) {
        super("");
        addValueChangeListener(e -> updateIconAppearance());
        setValue(value);
        if (label != null) {
            setLabel(label);
        }
        icon.setColor("grey");
        icon.getStyle().set("cursor", "pointer");
        add(icon);
        configurePopover();
    }

    /**
     * Sets the color to be used when the comment field value is blank.
     * This method allows defining a visual indicator (like a background color)
     * for the blank state of the comment field.
     *
     * @param blankColor the color value to set for the blank state; must be a valid color format
     */
    public static void setBlankColor(String blankColor) {
        CommentField.blankColor = blankColor;
    }

    /**
     * Sets the active color for the comment field.
     * The active color is used to visually indicate the non-blank state
     * of the comment field, such as highlighting the icon or other UI elements
     * when a value is present.
     *
     * @param activeColor the color value to set for the active state;
     *                    must be a valid color format
     */
    public static void setActiveColor(String activeColor) {
        CommentField.activeColor = activeColor;
    }

    /**
     * Updates the appearance of the icon based on the current state of the comment field.
     * <p></p>
     * The method evaluates whether the comment field's value is blank by invoking the {@code isBlank} method.
     * If the value is determined to be blank, the icon's color is set to the value of {@code blankColor}.
     * Otherwise, the icon's color is set to the value of {@code activeColor}.
     */
    public void updateIconAppearance() {
        icon.setColor(isBlank() ? blankColor : activeColor);
    }

    /**
     * Determines whether the current value of the comment field is blank.
     * This method checks if the underlying value string is empty or contains only whitespace characters.
     *
     * @return {@code true} if the current value is blank, {@code false} otherwise
     */
    protected boolean isBlank() {
        return getValue().isBlank();
    }

    @Override
    protected String generateModelValue() {
        return textArea.getValue();
    }

    @Override
    protected void setPresentationValue(String s) {
        textArea.setValue(s);
    }

    private TextArea createTextArea() {
        TextArea area = new TextArea();
        area.setWidthFull();
        area.setWidth( "350px");
        area.setHeight("150px");
        return area;
    }

    private VerticalLayout createPopoverContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(textArea, buttonLayout);
        return layout;
    }

    /**
     * Sets the width of the text area in pixels.
     * This method adjusts the width of the comment field's text area
     * to the specified value, applying the width as a CSS pixel value.
     *
     * @param widthInPixels the desired width of the text area in pixels; must be a non-negative integer
     */
    public void setTextAreaWidth(int widthInPixels) {
        textArea.setWidth(widthInPixels + "px");
    }

    /**
     * Sets the height of the text area within the CommentField.
     * This method adjusts the visual height of the text area to the specified value in pixels.
     *
     * @param heightInPixels the desired height of the text area in pixels;
     *                       must be a non-negative integer
     */
    public void setTextAreaHeight(int heightInPixels) {
        textArea.setHeight(heightInPixels + "px");
    }

    /**
     * Builds and initializes the "OK" and "Cancel" buttons for the CommentField.
     * <pre>
     * The "OK" button is configured to:
     * - Close the associated popover when clicked.
     * - Trigger the `updateValue` method to update the field's value.
     * - Notify all registered saved listeners by invoking their `accept` method with the current instance.
     *
     * The "Cancel" button is configured to:
     * - Close the associated popover when clicked.
     * - Notify all registered cancelled listeners by invoking their `accept` method with the current instance.
     *</pre>
     * Both buttons are styled, with the "OK" button being a primary button and both buttons rendered in a small size.
     * The buttons are added to the `buttonLayout` of the field.
     */
    protected void buildButtons() {
        ok = new Button("OK", e -> {
            popover.close();
            updateValue();
            if(savedListeners != null) {
                savedListeners.forEach(l -> l.accept(this));
            }
        });
        ok.asPrimary().asSmall();
        cancel = new Button("Cancel", e -> {
            popover.close();
            if(cancelledListeners != null) {
                cancelledListeners.forEach(l -> l.accept(this));
            }
        }).asSmall();
        buttonLayout.add(ok, cancel);
    }

    private void configurePopover() {
        icon.getElement().executeJs(
                "this.addEventListener('click', function(e) { e.stopPropagation(); });"
        );
        popover.setTarget(icon);
        popover.setOpenOnClick(true);
        popover.setCloseOnOutsideClick(true);
        popover.setCloseOnEsc(true);
        popover.setPosition(PopoverPosition.BOTTOM);
        popover.addThemeVariants(PopoverVariant.ARROW);
        popover.setModal(false);
        popover.setAutofocus(true);
        popover.add(createPopoverContent());
        popover.addOpenedChangeListener(e -> {
            if(e.isOpened()) {
                textArea.setValue(getValue());
                textArea.focus();
            }
        });
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        textArea.setReadOnly(readOnly || !isEnabled());
        cancel.setVisible(isEnabled() && !readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textArea.setEnabled(isReadOnly() || enabled);
        cancel.setVisible(enabled && !isReadOnly());
    }

    /**
     * Registers a listener that will be invoked when a comment is saved.
     * The provided listener is triggered whenever the "save" action is performed
     * on this comment field. The listener consumes the current instance of
     * {@code CommentField}.
     *
     * @param listener the {@code Consumer} to be invoked when the comment is saved;
     *                 must not be null
     * @return a {@code Registration} object that can be used to remove the listener
     */
    public Registration addSavedListener(Consumer<CommentField> listener) {
        if(savedListeners == null) {
            savedListeners = new ArrayList<>();
        }
        savedListeners.add(listener);
        return () -> savedListeners.remove(listener);
    }

    /**
     * Registers a listener to be notified when the "Cancel" button is clicked in the CommentField.
     *
     * @param listener a {@link Consumer} that accepts the current instance of {@code CommentField},
     *                 invoked when the "Cancel" button is clicked.
     * @return a {@link Registration} object that can be used to remove the registered listener.
     */
    public Registration addCancelledListener(Consumer<CommentField> listener) {
        if(cancelledListeners == null) {
            cancelledListeners = new ArrayList<>();
        }
        cancelledListeners.add(listener);
        return () -> cancelledListeners.remove(listener);
    }

    /**
     * Opens the popover associated with the CommentField.
     * This method triggers the display of the popover, allowing the user
     * to interact with the CommentField, such as entering or modifying
     * text content.
     */
    public void open() {
        popover.open();
    }

    /**
     * Closes the associated popover in the CommentField.
     * This method is responsible for hiding or dismissing the popover
     * element associated with the CommentField. It gets invoked typically
     * when the user interacts with the "Cancel" or "OK" buttons, or in other
     * scenarios where the popover should no longer be displayed.
     */
    public void close() {
        popover.close();
    }

    private class Pop extends Popover {

        @Override
        protected void onAttach(AttachEvent attachEvent) {
            if(ok == null && cancel == null) {
                buildButtons();
            }
            super.onAttach(attachEvent);
        }
    }
}
