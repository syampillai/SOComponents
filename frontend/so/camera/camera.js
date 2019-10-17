import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class SOCamera extends PolymerElement {

    static get template() {
        return html`<canvas id="{{ canvasid }}" hidden></canvas>`;
    }

	static get properties() {
        return {
            canvasid: {
                type: String,
                value: "cid"
            },
            videoid: {
                type: String,
                value: "vid"
            },
            imageid: {
                type: String,
                value: "iid"
            },
            previewOptions: {
              	type: Object,
              	value: null
            },
            recordingOptions: {
              	type: Object,
              	value: null
            },
            imageData: {
              	type: Object,
              	value: null
            },
            recordingData: {
              	type: Object,
              	value: null
            },
            target: {
              	type: String,
              	value: ""
            }
        }
    }

    static get is() { return 'so-camera'; }

    _element(elementId) {
        return document.querySelector("#" + elementId);
    }

    _canvas() {
        return this.shadowRoot.querySelector("#" + this.canvasid);
    }

    _eVideo() {
        return this._element(this.videoid);
    }

    _eImage() {
        return this._element(this.imageid);
    }

    disconnectedCallback() {
        super.disconnectedCallback();
    	if(this.recorder != null) {
    		this.recorder.stop();
    	}
    	if(this.stream != null) {
    		this.stream.getTracks().forEach( t=> {
    			t.stop();
    		});
    	}
    }

    takePicture() {
    	let vid = this._eVideo();
    	if (vid == null) {
    		return;
    	}
    	let canvas = this._canvas();
    	let context = canvas.getContext('2d');
    	canvas.height = vid.videoHeight;
    	canvas.width = vid.videoWidth;
    	context.drawImage(vid, 0, 0, vid.videoWidth, vid.videoHeight);
    	let blob = canvas.toBlob(b => {
    		this.imageData = b;
    		this.showPicture();
    		this.saveToServer(b);
    	}, 'image/jpeg', 0.95);
    }

    stopCamera() {
    	this._stopStream(this.stream);
    }

    _stopStream(stream) {
    	if(stream != null) {
    		stream.getTracks().forEach( t=> {
    			t.stop();
    		});
    	}
    }

    saveToServer(data) {
    	let formData = new FormData();
    	formData.append("data", data);
    	fetch(this.target, {
    		method: "post",
    		body: formData
   		}).then(response => console.log(response));
    }

    startRecording() {
    	if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    		navigator.mediaDevices.getUserMedia(this.recordingOptions).then(stream => {
    			this.recorder = new MediaRecorder(stream);
    			this.recorder.ondataavailable = e => {
    				this.recordingData = e.data;
    				let video = this._eVideo();
    				let url = window.URL.createObjectURL(this.recordingData);
    				video.src = url;
    				video.setAttribute("controls", "controls");
    				this.saveToServer(e.data);
    				this._stopStream(stream);
    			}
    			this.recorder.start();
    		});
    	}
    }

    stopRecording() {
    	if(this.recorder != null) {
    		this.recorder.stop();
    	}
    }

    showPreview() {
    	let video = this._eVideo();
    	if(this.stream != null) {
    		this._stopStream(this.stream);
    	}
    	if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    		navigator.mediaDevices.getUserMedia(this.previewOptions).then(stream => {
    			this.stream = stream;
    			video.srcObject = this.stream;
    			video.play();
    		});
    	}
    }

    showPicture() {
        if(this.imageData == null) {
            return;
        }
    	let img = this._eImage();
    	img.src = URL.createObjectURL(this.imageData);
    }

    showRecording() {
    	if(this.recordingData != null) {
    		let video = this._eVideo();
  			video.srcObject = this.recordingData;
  		}
    }
}

customElements.define(SOCamera.is, SOCamera);
