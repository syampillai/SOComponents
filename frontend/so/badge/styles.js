import '@vaadin/vaadin-lumo-styles/badge.js';

const $_documentContainer = document.createElement('template');
$_documentContainer.innerHTML = `
    <custom-style>
        <style include="lumo-badge"></style>
    </custom-style>`;

document.head.appendChild($_documentContainer.content);