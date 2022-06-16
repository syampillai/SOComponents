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
        this.utc = true;
        this.showUTC = true;
        this.timer = null;
        this.showDate = true;
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

    setUTC(utc) {
        this.utc = utc;
    }

    showUTC(showUTC) {
        this.showUTC = showUTC;
    }

    showDate(showDate) {
        this.showDate = showDate;
    }

    _currTime() {
        let date = new Date();
        let hh;
        let mm;
        let ss;
        if(this.utc) {
            hh = date.getUTCHours();
            mm = date.getUTCMinutes();
            ss = date.getUTCSeconds();
        } else {
            hh = date.getHours();
            mm = date.getMinutes();
            ss = date.getSeconds();
        }
        let ampm;
        if(this.ampm) {
            ampm = " AM";
            if(hh == 0) {
                hh = 12;
            }
            if(hh > 12) {
                hh = hh - 12;
                ampm = " PM";
            }
        } else {
            ampm = "";
        }
        hh = (hh < 10) ? "0" + hh : hh;
        mm = (mm < 10) ? "0" + mm : mm;
        ss = (ss < 10) ? "0" + ss : ss;
        let dp = "";
        if(this.showDate) {
            if(this.utc) {
                dp = date.toUTCString();
                dp = dp.substring(0, 16);
            } else {
                dp = date.toDateString();
            }
            dp += " ";
        }
        dp += hh + ":" + mm + ":" + ss + ampm;
        if(this.utc && this.showUTC) {
            dp += " UTC";
        }
        this.shadowRoot.getElementById(this.idClock).innerText = dp;
    }
}

customElements.define('so-clock', Clock);
