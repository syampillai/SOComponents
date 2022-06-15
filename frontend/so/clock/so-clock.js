import { LitElement, html, css } from 'lit'
import {property, customElement} from 'lit/decorators.js';

export class Clock extends LitElement {

    render() {
        return html`<span id="${this.idClock}"></span>`;
    }

    constructor() {
        super();
        this.idClock = null;
        this.ampm = false;
        this.gmt = true;
        this.timer = null;
    }

    connectedCallback() {
        super.connectedCallback();
        this.$server.ready();
        this.timer = setInterval(() => this._currTime(), 1000);
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        clearInterval(this.timer);
        this.timer = null;
    }

    setAMPM(ampm) {
        this.ampm = ampm;
    }

    setGMT(gmt) {
        this.gmt = gmt;
    }

    _currTime() {
        let date = new Date();
        let hh;
        let mm;
        let ss;
        if(this.gmt) {
            hh = date.getUTCHours();
            mm = date.getUTCMinutes();
            ss = date.getUTCSeconds();
        } else {
            hh = date.getHours();
            mm = date.getMinutes();
            ss = date.getSeconds();
        }
        let session;
        if(this.ampm) {
            session = " AM";
            if(hh == 0) {
                hh = 12;
            }
            if(hh > 12) {
                hh = hh - 12;
                session = " PM";
            }
        } else {
            session = "";
        }
        hh = (hh < 10) ? "0" + hh : hh;
        mm = (mm < 10) ? "0" + mm : mm;
        ss = (ss < 10) ? "0" + ss : ss;
        this.shadowRoot.getElementById(this.idClock).innerText = hh + ":" + mm + ":" + ss + session;
    }
}

customElements.define('so-clock', Clock);
