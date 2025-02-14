import { LitElement, html, css } from 'lit'
import {property, customElement} from 'lit/decorators.js';

export class SOPageRoot extends LitElement {

    render() {
        return html`<span id="${this.idContent}"><slot></slot></span>`;
    }

    constructor() {
        super();
        this.idContent = null;
    }

    connectedCallback() {
        super.connectedCallback();
        this.$server.ready();
    }

    firstUpdated() {
        this.sendInfo();
    }

    sendInfo() {
        this.$server.info(window.location.href);
    }
}
customElements.define('so-page-root', SOPageRoot);
