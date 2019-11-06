package com.storedobject.vaadin;

import com.storedobject.vaadin.util.MediaStreamVariable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.StreamReceiver;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.JsonFactory;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A special {@link Audio} class that gets input from the client Computer's mic. The audio stream from the
 * mic can be streamed to the server and saved.
 *
 * @author Syam
 */
public class AudioCapture extends Audio implements MediaCapture {

    private final Mic mic;
    private List<StatusChangeListener> statusChangeListeners;
    private Application application;

    /**
     * Constructor.
     */
    public AudioCapture() {
        ID.set(this);
        mic = new Mic();
        embedMic();
        mic.getElement().setAttribute("audioid", getId().orElse(""));
    }

    private void embedMic() {
        getElement().appendChild(mic.getElement());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(application == null) {
            getApplication();
        }
        mic.recording = -1;
        Map<String, Object> recOpts = new HashMap<>();
        recOpts.put("video", false);
        recOpts.put("audio", true);
        mic.setOptions(recOpts);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        boolean changed = mic.recording == 1;
        mic.recording = -1;
        if(changed) {
            fireStatusChange();
        }
    }

    /**
     * Get the {@link Application} to which this is attached.
     *
     * @return The application.
     */
    @Override
    public Application getApplication() {
        if(application == null) {
            application = Application.get();
        }
        return application;
    }

    /**
     * Start the recording of the audio stream to a "data receiver".
     *
     * @param dataReceiver Data receiver to receive the audio stream
     */
    @Override
    public void startRecording(DataReceiver dataReceiver) {
        mic.setReceiver(dataReceiver);
        mic.startRecording();
    }

    /**
     * Check whether recording is in progress or not.
     *
     * @return True or false.
     */
    @Override
    public boolean isRecording() {
        return mic.recording == 1;
    }

    /**
     * Stop the recording of the audio stream that may be currently in progress.
     */
    @Override
    public void stopRecording() {
        mic.stopRecording();
    }

    /**
     * Stop / switch off the mic (activities such as recording etc. will be stopped).
     */
    @Override
    public void stopDevice() {
        mic.stopMic();
    }

    /**
     * Add "status change" listener so that we can monitor recording status.
     *
     * @param listener Listener to add
     * @return Registration that can be used to remove the listener.
     */
    @Override
    public Registration addStatusChangeListener(StatusChangeListener listener) {
        if(listener == null) {
            return null;
        }
        if(statusChangeListeners == null) {
            statusChangeListeners = new ArrayList<>();
        }
        statusChangeListeners.add(listener);
        return () -> statusChangeListeners.remove(listener);
    }

    private void fireStatusChange() {
        if(statusChangeListeners != null) {
            statusChangeListeners.forEach(l -> l.statusChanged(this));
        }
    }

    @Tag("so-audio")
    @JsModule("./so/media/audio.js")
    private class Mic extends PolymerTemplate<TemplateModel> {

        private final String STOP_MIC = "stopMic";
        private final String START_RECORDING = "startRecording";
        private byte recording = 0;
        private final List<String> commands = new ArrayList<>();
        private volatile boolean done = true;
        private String prevCommand;

        private Mic() {
            ID.set(this);
            getElement().setAttribute("spanid", "span" + ID.newID());
        }

        @ClientCallable
        private void recordingStatus(int status) {
            recording = (byte)status;
            fireStatusChange();
        }

        private void setOptions(Map<String, Object> recordingOptions) {
            JsonFactory factory = new JreJsonFactory();
            getElement().setPropertyJson("recordingOptions", toJson(recordingOptions, factory));
        }

        private JsonValue toJson(Map<String,Object> map, JsonFactory factory) {
            JsonObject obj = factory.createObject();
            for(Map.Entry<String,Object> entry: map.entrySet()) {
                if(entry.getValue() instanceof Boolean) {
                    obj.put(entry.getKey(), factory.create((Boolean)entry.getValue()));
                } else if(entry.getValue() instanceof Double) {
                    obj.put(entry.getKey(), factory.create((Double)entry.getValue()));
                } else if(entry.getValue() instanceof Integer) {
                    obj.put(entry.getKey(), factory.create((Integer)entry.getValue()));
                } else if(entry.getValue() instanceof String) {
                    obj.put(entry.getKey(), factory.create((String)entry.getValue()));
                } else if(entry.getValue() instanceof Map) {
                    //noinspection unchecked
                    obj.put(entry.getKey(), toJson((Map<String,Object>)entry.getValue(), factory));
                } else {
                    throw new IllegalArgumentException();
                }
            }
            return obj;
        }

        private void setReceiver(DataReceiver receiver) {
            getElement().setAttribute("target", new StreamReceiver(getElement().getNode(), "mic" + ID.newID(), new MediaStreamVariable(receiver)));
        }

        private void startRecording() {
            js(START_RECORDING);
        }

        private void stopRecording() {
            js("stopRecording");
        }

        private void stopMic() {
            js(STOP_MIC);
        }

        @ClientCallable
        private void done() {
            synchronized (commands) {
                if (commands.isEmpty()) {
                    done = true;
                    return;
                }
                done = false;
                String command = commands.remove(0);
                if(commands.contains(STOP_MIC)) {
                    done();
                    return;
                }
                _js(command);
            }
        }

        private void js(String js) {
            synchronized (commands) {
                if(done) {
                    done = false;
                    _js(js);
                    return;
                }
                commands.add(js);
            }
        }

        private void _js(String js) {
            switch (js) {
                case STOP_MIC:
                    if(STOP_MIC.equals(prevCommand)) {
                        done();
                        return;
                    }
                    break;
                case START_RECORDING:
                    if(!AudioCapture.this.isVisible() || recording == 1 || !AudioCapture.this.getParent().isPresent()) {
                        done();
                        return;
                    }
                    break;
            }
            getApplication().getPage().executeJs("let v=document.getElementById('" + getId().orElse(null) +
                    "');if(v!=null){v." + js + "();return 0;}else return 1;").
                    then(Integer.class, r -> {
                        if (r == 1) {
                            lost(js);
                        } else {
                            prevCommand = js;
                        }
                    }, err -> getApplication().log(err));
        }

        private void lost(String pendingCommand) {
            embedMic();
            _js(pendingCommand);
        }
    }
}
