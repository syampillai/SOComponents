import { LitElement, html, css } from 'lit'
import {property, customElement} from 'lit/decorators.js';

export class SOTimer extends LitElement {

    render() {
        return html`
            <span id="span"></span>
        `;
    }

    connectedCallback() {
        super.connectedCallback();
        this.$server.ready();
    }

    initComp() {
        this.span = this.shadowRoot.getElementById("span");
        this.timer = null;
        this.prefixD = "";
        this.suffixD = "";
    }

    setDPrefix(prefixD) {
        this.prefixD = prefixD;
        console.log("Prefix: " + prefixD);
    }

    setDSuffix(suffixD) {
        this.suffixD = suffixD;
        console.log("Suffix: " + suffixD);
    }

    count(limit) {
        if(this.timer != null) {
            clearInterval(this.timer);
            this.span.textContent = "";
            this.timer = null;
        }
        if(limit == 0) {
            return;
        }
        console.log("Prefix & Suffix are: " + this.prefixD + " & " + this.suffixD);
        this.limit = limit;
        this.up = limit > 0;
        this.span.textContent = this.prefixD + Math.abs(this.up ? 0 : limit) + this.suffixD;
        this.startTime = new Date().getTime();
        this.stopTime = this.startTime + Math.abs(limit) * 1000;
        this.timer = setInterval(() => {
            let now = new Date().getTime();
            let counter;
            if(this.up) {
                counter = now - this.startTime;
            } else {
                counter = this.stopTime - now;
            }
            if(this.stopTime <= now) {
                clearInterval(this.timer);
                this.timer = null;
                if(this.up) {
                    this.span.textContent = this.prefixD + this.limit + this.suffixD;
                } else {
                    this.span.textContent = this.prefixD + "0" + this.suffixD;
                }
                this.$server.completed();
            } else {
                this.span.textContent = this.prefixD + Math.floor(counter/1000) + this.suffixD;
            }
        }, 1000);
    }
}

customElements.define('so-timer', SOTimer);