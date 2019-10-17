package com.storedobject.vaadin;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;

/**
 * A button that can be used to toggle "speak" option of the {@link Application}. Clicking this button enables/disables
 * the "speak" option. Once "speak" option is enabled, {@link Application#speak(String)} and {@link View#speak(String)}
 * methods can be used to do "text to speech".
 *
 * @author Syam
 */
@Tag("span")
public class SpeakerButton extends Component implements Application.SpeakerToggledListner {

    private ImageButton icon;
    private Registration registration;

    /**
     * Constructor.
     */
    public SpeakerButton() {
        icon = new ImageButton(VaadinIcon.SOUND_DISABLE, c -> {
            Application a = Application.get();
            if(a != null) {
                a.setSpeaker(!a.isSpeakerOn());
            }
        });
        icon.getElement().setAttribute("onclick", "this.blur()");
        icon.getElement().setAttribute("tabindex", "-1");
        getElement().setAttribute("title", "Toggle speaker output");
        getElement().setAttribute("onclick", "window.speechSynthesis.speak(new SpeechSynthesisUtterance('Speaker output toggled'));this.blur()");
        getElement().appendChild(icon.getElement());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(registration == null) {
            Application a = Application.get();
            if(a != null) {
                registration = a.addSpeakerToggedListener(this);
                speaker(a.isSpeakerOn());
            }
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        if(registration != null) {
            registration.remove();
            registration = null;
        }
    }

    /**
     * Used by the {@link Application} to inform about current status of the "speaker".
     *
     * @param on True if the status is on
     */
    @Override
    public void speaker(boolean on) {
        icon.setIcon(on ? VaadinIcon.VOLUME_UP : VaadinIcon.SOUND_DISABLE);
    }
}