import { LitElement, html, css } from 'lit'
import {property, customElement} from 'lit/decorators.js';

export class SOCamera extends LitElement {

    render() {
        return html`<canvas id="${this.canvasid}" hidden></canvas>`;
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

    connectedCallback() {
        super.connectedCallback();
        this.$server.ready();
    }

    disconnectedCallback() {
        super.disconnectedCallback();
    	if(this.recorder != null) {
    		this.recorder.stop();
    		this.recorder = null;
    	}
    	if(this.stream != null) {
    		this.stream.getTracks().forEach( t=> {
    			t.stop();
    		});
    		this.stream = null;
    	}
    	if(this.altStream != null) {
    		this.altStream.getTracks().forEach( t=> {
    			t.stop();
    		});
    		this.altStream = null;
    	}
    	let vid = this._eVideo();
    	if (vid != null) {
    		vid.srcObject = null;
    	}
    }

    previewPicture() {
        this._takePicture(false);
    }

    takePicture() {
        this._takePicture(true);
    }

    _takePicture(save) {
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
    		if(save) {
    		    this.saveToServer(b);
    		}
    	}, 'image/jpeg', 0.95);
    	this.$server.done();
    }

    stopCamera() {
    	this.$server.previewingStatus(-1);
    	this._stopCamera();
    	this.$server.done();
    }

    _stopCamera() {
        this._eVideo().srcObject = null;
    	this._stopStream(this.stream);
    	this.stream = null;
    	this._stopStream(this.altStream);
    	this.altStream = null;
    	this._stopRecording();
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
        this._stopCamera();
        this._eVideo().srcObject = null;
    	if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    		navigator.mediaDevices.getUserMedia(this.recordingOptions).then(stream => {
    		    this.$server.recordingStatus(1);
    		    let v = this._eVideo();
    		    this.altStream = new MediaStream(stream);
    		    this.altStream.getAudioTracks().forEach(a => this.altStream.removeTrack(a));
    		    v.srcObject = this.altStream;
                v.play();
    			this.recorder = new MediaRecorder(stream);
    			this.recorder.ondataavailable = e => {
    			    this._stopStream(this.altStream);
    			    this.altStream = null;
    		        let video = this._eVideo();
    		        video.setAttribute("controls", "controls");
    				video.srcObject = null;
    				this.recordingData = e.data;
    				this.saveToServer(e.data);
    				this.stream = stream;
    			}
    			this.recorder.start();
    			this.$server.done();
    		}).catch(() => { this.$server.recordingStatus(-1); this.$server.done(); });
    	} else {
    	    this.$server.recordingStatus(-1);
    	    this.$server.done();
    	}
    }

    stopRecording() {
        this._stopRecording();
    	this.$server.done();
    }

    _stopRecording() {
    	if(this.recorder != null) {
    		this.recorder.stop();
    		this.recorder = null;
    		this.$server.recordingStatus(-1);
    	}
    }

    showPreview() {
    	let video = this._eVideo();
    	this._stopStream(this.stream);
    	this.stream = null;
    	this._stopRecording();
    	if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    		navigator.mediaDevices.getUserMedia(this.previewOptions).then(stream => {
    			this.stream = stream;
    			video.srcObject = this.stream;
    			video.play().then(_ => { this.$server.previewingStatus(1); this.$server.done(); }).catch(err => { this.$server.previewingStatus(-1); this.$server.done(); });
    		}).catch(err => { this.$server.previewingStatus(-1); this.$server.done(); });
    	} else {
    	    this.$server.previewingStatus(-1);
    	    this.$server.done();
    	}
    }

    showPicture() {
        this._showPicture(3000);
    }

    _showPicture(retry) {
        if(this.imageData == null) {
            return;
        }
    	let img = this._eImage();
    	if(img == null) {
    	    if(retry <= 0) {
    	        this.$server.done();
                return;
    	    }
    	    retry -= 1000;
    	    setTimeout(function() {
    	        this._showPicture(retry);
    	    }.bind(this), 1000, retry);
    	    return;
    	}
    	let url = URL.createObjectURL(this.imageData);
    	img.onload = function() { URL.revokeObjectURL(url); };
    	img.src = url;
    	this.$server.done();
    }
}

customElements.define("so-camera", SOCamera);
