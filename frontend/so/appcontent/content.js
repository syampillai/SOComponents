import { LitElement, html, css } from 'lit'
import {property, customElement} from 'lit/decorators.js';

export class SOAppContent extends LitElement {

    static get styles () {
        return [
            css`
            .socontent {
              margin: 0px;
              border: 0px;
              padding: 0px;
              width: var(--so-content-width);
              height: var(--so-content-height);
              overflow: auto;
              box-sizing: border-box;
            }
            `
        ];
    }

    render() {
        return html`<div class="socontent" id="${this.idContent}"><slot></slot></div>`;
    }

    static get properties() {
        return {
            speak: String,
        };
    }

    constructor() {
        super();
        this.idContent = null;
    }

    connectedCallback() {
        super.connectedCallback();
        this._boundResizeHandler = this._sendSize.bind(this);
        new ResizeObserver(this._boundResizeHandler).observe(this.shadowRoot.getElementById(this.idContent));
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        window.removeEventListener('resize', this._boundResizeHandler);
    }

    firstUpdated() {
        this.content = this.shadowRoot.getElementById(this.idContent);
        this._sendSize();
    }

    _sendSize() {
        this.$server.resized(this.content.clientWidth, this.content.clientHeight);
    }

    updated(changedProps) {
        if(changedProps.has('speak')) {
            window.speechSynthesis.cancel();
            const utterThis = new SpeechSynthesisUtterance(this.speak);
            this.timer = setInterval(() => {
                window.speechSynthesis.pause();
                window.speechSynthesis.resume();
            }, 10000);
            utterThis.addEventListener("end", (e) => {
                clearInterval(this.timer);
                this.$server.spoken();
            });
            window.speechSynthesis.speak(utterThis);
        }
    }
}
customElements.define('so-app-content', SOAppContent);
