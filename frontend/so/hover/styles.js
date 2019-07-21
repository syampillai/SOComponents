const $_documentContainer = document.createElement('template');
$_documentContainer.innerHTML = `
<custom-style>
    <style is="custom-style">

        .so-hover {
            cursor: pointer;
        }

        .so-hover:hover {
            background-color: var(--lumo-primary-color-50pct);
        }

  </style>
</custom-style>`;

document.head.appendChild($_documentContainer.content);
