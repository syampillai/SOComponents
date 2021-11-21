package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.storedobject.helper.LitComponent;
import com.storedobject.vaadin.util.MediaStreamVariable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamReceiver;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonFactory;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A special {@link Video} class that gets input from the client Computer's camera. The video stream from the
 * camera can be (1) just viewed or (2) streamed to the server and saved. Also, snap-shot of the
 * video stream can be taken as a picture and the resulting image can be just viewed or saved to the server.
 *
 * @author Syam
 */
public class VideoCapture extends Video implements MediaCapture {

    private final Camera camera;
    private boolean frontCamera = false;
    private List<StatusChangeListener> statusChangeListeners;
    private Application application;

    /**
     * Constructor.
     */
    public VideoCapture() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param image Image to be attached for previewing the snap-shots
     */
    public VideoCapture(com.vaadin.flow.component.html.Image image) {
        ID.set(this);
        camera = new Camera();
        getElement().appendChild(camera.getElement());
        camera.getElement().setAttribute("videoid", getId().orElse(""));
        attachImage(image);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(application == null) {
            getApplication();
        }
        camera.previewing = camera.recording = -1;
        setCameraOptions();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        boolean changed = camera.previewing == 1 || camera.recording == 1;
        camera.previewing = camera.recording = -1;
        if(changed) {
            fireStatusChange();
        }
    }

    /**
     * Select the front camera if possible. (This should be done before the {@link VideoCapture} component is added
     * to the layout).
     */
    public void selectFrontCamera() {
        frontCamera = true;
    }

    /**
     * Select the rear camera if possible. (This should be done before the {@link VideoCapture} component is added
     * to the layout).
     */
    public void selectRearCamera() {
        frontCamera = false;
    }

    private void setCameraOptions() {
        Map<String, Object> previewOpts = new HashMap<>();
        previewOpts.put("audio", false);
        Map<String, Object> videoP = new HashMap<>();
        videoP.put("facingMode", frontCamera ? "user" : "environment");
        previewOpts.put("video", videoP);
        Map<String, Object> recOpts = new HashMap<>();
        recOpts.put("audio", true);
        Map<String, Object> videoR = new HashMap<>();
        videoR.put("facingMode", frontCamera ? "user" : "environment");
        recOpts.put("video", videoR);
        camera.setOptions(previewOpts, recOpts);
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
     * Attach an image to this video camera so that pictures shot can be viewed on that. (Image should be somewhere
     * on the screen when the actual preview is requested on this image using {@link #previewPicture()}).
     *
     * @param image The image component on which picture should be displayed
     */
    public void attachImage(com.vaadin.flow.component.html.Image image) {
        camera.connect(image);
    }

    /**
     * Preview the camera's output on this video component. The output will be visible only if the following conditions
     * are satisfied:<BR>
     * (1) The component is already on the screen (by adding to some layout in your application) and visible.<BR>
     * (2) The camera on the client computer can be accessed (browser will prompt the user for providing access).<BR>
     * (3) Your application is using https protocol and not just http protocol (Modern browsers do not allow camera
     * access when a site is using http protocol).<BR>
     * (4) If recording is not active already.
     */
    public void preview() {
        camera.showPreview();
    }

    /**
     * Take snap-shot from the video stream and show that picture on the already attached image component
     * {@link #attachImage(Image)}. The output will be visible only if the previewing is already active.
     */
    public void previewPicture() {
        camera.takePicture(null, false);
    }

    /**
     * Take snap-shot from the video stream and show that picture on the given image component. The output will be
     * visible only if the previewing
     * is already active and the image is already somewhere on the same screen.
     *
     * @param image The image component on which picture should be displayed
     */
    public void previewPicture(com.vaadin.flow.component.html.Image image) {
        camera.takePicture(image, false);
    }

    /**
     * Take snap-shot from the video stream, show that picture on the already attached image component and also, save
     * the picture via a "data receiver".
     * The output will be visible and saved only if the previewing is already active.
     *
     * @param dataReceiver Data receiver to receive the picture stream
     */
    public void savePicture(DataReceiver dataReceiver) {
        camera.setReceiver(dataReceiver);
        camera.takePicture(null,true);
    }

    /**
     * Take snap-shot from the video stream, show that picture on the given image component and also, save the picture
     * via a "data receiver".
     * The output will be visible and saved only if the previewing is already active.
     *
     * @param image The image component on which picture should be displayed
     * @param dataReceiver Data receiver to receive the picture stream
     */
    public void savePicture(com.vaadin.flow.component.html.Image image, DataReceiver dataReceiver) {
        camera.setReceiver(dataReceiver);
        camera.takePicture(image,true);
    }

    /**
     * Start the recording of the video stream to a "data receiver".
     *
     * @param dataReceiver Data receiver to receive the video stream
     */
    @Override
    public void startRecording(DataReceiver dataReceiver) {
        camera.setReceiver(dataReceiver);
        camera.startRecording();
    }

    /**
     * Check whether recording is in progress or not.
     *
     * @return True or false.
     */
    @Override
    public boolean isRecording() {
        return camera.recording == 1;
    }

    /**
     * Stop the recording of the video stream that may be currently in progress.
     */
    @Override
    public void stopRecording() {
        camera.stopRecording();
    }

    /**
     * Check whether previewing is in progress or not.
     *
     * @return True or false.
     */
    @Override
    public boolean isPreviewing() {
        return camera.previewing == 1;
    }

    /**
     * Stop / switch off the camera (activities such as previewing, recording etc. will be stopped).
     */
    @Override
    public void stopDevice() {
        camera.stopCamera();
    }

    /**
     * Add "status change" listener so that we can monitor recording/previewing status.
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

    @Tag("so-camera")
    @JsModule("./so/media/camera.js")
    private class Camera extends LitComponent {

        private final String STOP_CAMERA = "stopCamera";
        private final String START_RECORDING = "startRecording";
        private final String TAKE_PICTURE = "takePicture";
        private final String PREVIEW_PICTURE = "previewPicture";
        private final String SHOW_PREVIEW = "showPreview";
        private byte previewing = 0, recording = 0;
        private final List<String> commands = new ArrayList<>();
        private volatile boolean done = true;
        private String prevCommand;

        private Camera() {
            ID.set(this);
            getElement().setAttribute("canvasid", "canvas" + ID.newID());
        }

        @ClientCallable
        private void previewingStatus(int status) {
            previewing = (byte) status;
            recording = -1;
            fireStatusChange();
        }

        @ClientCallable
        private void recordingStatus(int status) {
            recording = (byte) status;
            previewing = -1;
            fireStatusChange();
        }

        private void connect(Image image) {
            if (image != null) {
                String iid = image.getId().orElse(null);
                if (iid == null) {
                    ID.set(image);
                    iid = image.getId().orElse(null);
                }
                getElement().setAttribute("imageid", iid);
            }
        }

        private void takePicture(Image image, boolean save) {
            connect(image);
            js(save ? TAKE_PICTURE : PREVIEW_PICTURE);
        }

        private void setOptions(Map<String, Object> previewOptions, Map<String, Object> recordingOptions) {
            JsonFactory factory = new JreJsonFactory();
            getElement().setPropertyJson("previewOptions", toJson(previewOptions, factory));
            getElement().setPropertyJson("recordingOptions", toJson(recordingOptions, factory));
        }

        private JsonValue toJson(Map<String, Object> map, JsonFactory factory) {
            JsonObject obj = factory.createObject();
            for (Map.Entry<String, Object> entry: map.entrySet()) {
                if (entry.getValue() instanceof Boolean) {
                    obj.put(entry.getKey(), factory.create((Boolean) entry.getValue()));
                } else if (entry.getValue() instanceof Double) {
                    obj.put(entry.getKey(), factory.create((Double) entry.getValue()));
                } else if (entry.getValue() instanceof Integer) {
                    obj.put(entry.getKey(), factory.create((Integer) entry.getValue()));
                } else if (entry.getValue() instanceof String) {
                    obj.put(entry.getKey(), factory.create((String) entry.getValue()));
                } else if (entry.getValue() instanceof Map) {
                    //noinspection unchecked
                    obj.put(entry.getKey(), toJson((Map<String, Object>) entry.getValue(), factory));
                } else {
                    throw new IllegalArgumentException();
                }
            }
            return obj;
        }

        private void setReceiver(DataReceiver receiver) {
            getElement().setAttribute("target", new StreamReceiver(getElement().getNode(),
                    "camera" + ID.newID(), new MediaStreamVariable(receiver)));
        }

        private void startRecording() {
            js(START_RECORDING);
        }

        private void stopRecording() {
            js("stopRecording");
        }

        private void stopCamera() {
            js(STOP_CAMERA);
        }

        private void showPreview() {
            js(SHOW_PREVIEW);
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
                if (commands.contains(STOP_CAMERA)) {
                    done();
                    return;
                }
                _js(command);
            }
        }

        private void js(String js) {
            synchronized (commands) {
                if (done) {
                    done = false;
                    _js(js);
                    return;
                }
                commands.add(js);
            }
        }

        private void _js(String js) {
            switch (js) {
                case STOP_CAMERA:
                    if (STOP_CAMERA.equals(prevCommand)) {
                        done();
                        return;
                    }
                    break;
                case SHOW_PREVIEW:
                    if (!VideoCapture.this.isVisible() || previewing == 1 || VideoCapture.this.getParent().isEmpty()) {
                        done();
                        return;
                    }
                    break;
                case TAKE_PICTURE:
                case PREVIEW_PICTURE:
                    if (!VideoCapture.this.isVisible() || previewing != 1) {
                        done();
                        return;
                    }
                    break;
                case START_RECORDING:
                    if (!VideoCapture.this.isVisible() || recording == 1 || VideoCapture.this.getParent().isEmpty()) {
                        done();
                        return;
                    }
                    break;
            }
            executeJS(js);
            prevCommand = js;
        }
    }
}
