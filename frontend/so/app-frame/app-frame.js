import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';
import '@polymer/iron-icons/iron-icons.js';
import '@polymer/paper-icon-button/paper-icon-button.js';
import '@polymer/app-layout/app-drawer-layout/app-drawer-layout.js';
import '@polymer/app-layout/app-drawer/app-drawer.js';
import '@polymer/app-layout/app-scroll-effects/app-scroll-effects.js';
import '@polymer/app-layout/app-header/app-header.js';
import '@polymer/app-layout/app-header-layout/app-header-layout.js';
import '@polymer/app-layout/app-toolbar/app-toolbar.js';

class AppFrame extends PolymerElement {

    static get template() {
        return html`
            <style>
                app-header {
                    background-color: var(--lumo-primary-color);
                    color: var(--lumo-primary-contrast-color);
                }
                app-header paper-icon-button {
                    --paper-icon-button-ink-color: var(--lumo-primary-contrast-color);
                }
                app-drawer-layout {
                    --app-drawer-layout-content-transition: margin 0.2s;
                }
                app-drawer {
                    --app-drawer-content-container: {
                        background-color: var(--lumo-primary-contrast-color);
                    }
                }
            </style>
            <app-header-layout>
                <app-header fixed effects="waterfall" slot="header">
                    <app-toolbar>
                        <paper-icon-button tabindex="-1", on-click="doToggle" icon="menu"></paper-icon-button>
                        <div main-title><slot name="caption"></slot></div>
                        <slot name="alert"></slot>
                        <slot name="user"></slot>
                        <slot name="logout"></slot>
                    </app-toolbar>
                </app-header>
                <app-drawer-layout id="drawerLayout">
                    <app-drawer slot="drawer">
                        <slot name="menu"></slot>
                    </app-drawer>
                    <slot name="content"></slot>
                </app-drawer-layout>
            </app-header-layout>`;
    }

    static get is() { return 'app-frame'; }

    doToggle() {
        if (this.$.drawerLayout.forceNarrow || !this.$.drawerLayout.narrow) {
            this.$.drawerLayout.forceNarrow = !this.$.drawerLayout.forceNarrow;
        } else {
            this.$.drawerLayout.drawer.toggle();
        }
    }

    doOpen() {
        if(!this.$.drawerLayout.narrow) {
            this.$.drawerLayout.drawer.open();
        }
    }

    doClose() {
        this.$.drawerLayout.drawer.close();
    }
}

customElements.define(AppFrame.is, AppFrame);
