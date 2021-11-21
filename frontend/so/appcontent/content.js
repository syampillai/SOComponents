import { LitElement, html, css, property, customElement } from 'lit-element'

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
        window.addEventListener('resize', this._boundResizeHandler);
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        window.removeEventListener('resize', this._sendSize);
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
            window.speechSynthesis.speak(new SpeechSynthesisUtterance(this.speak));
        }
    }
}
customElements.define('so-app-content', SOAppContent);
