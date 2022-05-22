import { LitElement, html, css } from 'lit'
import {property, customElement} from 'lit/decorators.js';

export class SOAudio extends LitElement {

    render() {
        return html`<span id="${this.spanid}" hidden></span>`;
    }

	static get properties() {
        return {
            spanid: {
                type: String,
                value: "sid"
            },
            audioid: {
                type: String,
                value: "aid"
            },
            recordingOptions: {
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

    _span() {
        return this.shadowRoot.querySelector("#" + this.canvasid);
    }

    _eAudio() {
        return this._element(this.audioid);
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
    }

    stopMic() {
    	this.$server.recordingStatus(-1);
    	this._stopMic();
    	this.$server.done();
    }

    _stopMic() {
        this._eAudio().srcObject = null;
    	this._stopStream(this.stream);
    	this.stream = null;
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
        this._stopMic();
        this._eAudio().srcObject = null;
    	if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    		navigator.mediaDevices.getUserMedia(this.recordingOptions).then(stream => {
    		    this.$server.recordingStatus(1);
    			this.recorder = new MediaRecorder(stream);
    			this.recorder.ondataavailable = e => {
    		        let audio = this._eAudio();
    		        audio.setAttribute("controls", "controls");
    				audio.srcObject = null;
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
}

customElements.define("so-audio", SOAudio);
