import { html } from '@polymer/polymer/lib/utils/html-tag.js';

const $_documentContainer = document.createElement('template');
$_documentContainer.innerHTML = `
<dom-module id="so-grid" theme-for="vaadin-grid">
    <template>
        <style>
            :host(.so-grid) [part~="row"][selected] [part~="body-cell"]:not([part~="details-cell"]) {
                background: var(--lumo-primary-color-50pct);
            }
        </style>
    </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);
