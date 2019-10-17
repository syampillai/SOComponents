package com.storedobject.vaadin;

import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.storedobject.vaadin.util.ID;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.StreamReceiver;
import com.vaadin.flow.server.StreamVariable;
import com.vaadin.flow.shared.Registration;

import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.JsonFactory;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;

/**
 * Camera class for recording video / capturing images via computer's camera.<BR>
 * This is a modified version of the component released by Pontus Boström.<BR>
 * See https://vaadin.com/directory/component/vcamera/overview<BR>
 * Note: This class is still under development and the API may be changed.
 *
 * @author Syam
 */
@Tag("so-camera")
@JsModule("./so/camera/camera.js")
public class Camera extends PolymerTemplate<TemplateModel> {
    
    public Camera(Map<String, Object> previewOptions, Map<String, Object> recordingOptions, DataReceiver receiver) {
        this();
        setReceiver(receiver);
        setOptions(previewOptions, recordingOptions);
    }

    public Camera() {
        setId("socamera" + ID.newID());
        getElement().setAttribute("contentid", "content" + ID.newID());
        getElement().setAttribute("canvasid", "canvas" + ID.newID());
    }

    /**
     * Show the preview of the camera's video output in the given component.
     *
     * @param video Component on which video output to be shown
     */
    public void show(Video video) {
        if(video == null || !video.getParent().isPresent() || !this.getParent().isPresent()) {
            return;
        }
        String vid = video.getId().orElse(null);
        if(vid == null) {
            vid = getId().orElse(null) + "v" + ID.newID();
            video.setId(vid);
        }
        getElement().setAttribute("videoid", vid);
        showPreview();
    }

    /**
     * Show the preview of the snap-shot picture from camera's video output in the given component.
     *
     * @param image Component on which the image to be shown
     */
    public void show(Image image) {
        if(image == null || !image.getParent().isPresent() || !this.getParent().isPresent()) {
            return;
        }
        String iid = image.getId().orElse(null);
        if(iid == null) {
            iid = getId().orElse(null) + "i" + ID.newID();
            image.setId(iid);
        }
        getElement().setAttribute("imageid", iid);
        takePicture();
    }

    public void setOptions(Map<String, Object> previewOptions, Map<String, Object> recordingOptions) {
        JsonFactory factory = new JreJsonFactory();
        getElement().setPropertyJson("previewOptions", toJson(previewOptions, factory));
        getElement().setPropertyJson("recordingOptions", toJson(recordingOptions, factory));
    }

    private JsonValue toJson(Map<String,Object> map, JsonFactory factory) {
        JsonObject obj = factory.createObject();
        for(Entry<String,Object> entry: map.entrySet()) {
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

    public void setReceiver(DataReceiver receiver) {
        getElement().setAttribute("target", new StreamReceiver(getElement().getNode(), "camera", new CameraStreamVariable(receiver)));
    }

    private void fireFinishedEvent(String mime) {
        fireEvent(new FinishedEvent(this, true, mime));
    }

    public void startRecording() {
        js("startRecording");
    }

    public void stopRecording() {
        js("stopRecording");
    }

    public void stopCamera() {
        js("stopCamera");
    }

    private void takePicture() {
        js("takePicture");
    }

    private void showPreview() {
        js("showPreview");
    }

    private void showPicture() {
        js("showPicture");
    }

    public void showRecording() {
        js("showRecording");
    }

    public Registration addFinishedListener(ComponentEventListener<FinishedEvent> listener) {
        return addListener(FinishedEvent.class, listener);
    }

    private void js(String js) {
        Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "')." + js + "();");
    }

    private class CameraStreamVariable implements StreamVariable {

        String mime;
        DataReceiver receiver;

        public CameraStreamVariable(DataReceiver receiver) {
            this.receiver = receiver;
        }


        @Override
        public OutputStream getOutputStream() {
            return receiver.getOutputStream(mime);
        }

        @Override
        public boolean isInterrupted() {
            return false;
        }

        @Override
        public boolean listenProgress() {
            return false;
        }

        @Override
        public void onProgress(StreamingProgressEvent event) {
        }

        @Override
        public void streamingFailed(StreamingErrorEvent event) {
        }

        @Override
        public void streamingFinished(StreamingEndEvent event) {
            fireFinishedEvent(mime);
        }

        @Override
        public void streamingStarted(StreamingStartEvent event) {
            mime = event.getMimeType();
        }
    }

    @FunctionalInterface
    public interface DataReceiver {
        OutputStream getOutputStream(String mimeType);
    }

    public static class FinishedEvent extends ComponentEvent<Camera> {

        private String mime;

        public FinishedEvent(Camera source, boolean fromClient, String mime) {
            super(source, fromClient);
            this.mime = mime;
        }

        public String getMime() {
            return mime;
        }
    }
}