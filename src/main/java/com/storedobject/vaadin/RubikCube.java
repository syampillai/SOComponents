package com.storedobject.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

/**
 * Rubik's Cube (created by wrapping Scarygami's @scarygami/scary-cube).
 *
 * @author Syam
 */
@Tag("scary-cube")
@NpmPackage(value = "@scarygami/scary-cube", version = "2.1.0")
@JsModule("@scarygami/scary-cube/scary-cube.js")
public class RubikCube extends Component implements HasSize {

    /**
     * Constructor.
     */
    public RubikCube() {
        ID.set(this);
        setWidth("100%");
        setHeight("500px");
    }

    /**
     * Do moves.
     *
     * @param moves Moves in SiGN notation
     */
    public void move(String moves) {
        if(moves == null || moves.isEmpty()) {
            return;
        }
        String command = "addMove";
        if(moves.contains(" ")) {
            command += "s";
            while(moves.contains("  ")) {
                moves = moves.replace("  ", " ");
            }
        }
        command += "(\"" + moves + "\")";
        js(command);
    }

    /**
     * Reset the cube.
     */
    public void reset() {
        js("reset()");
    }

    private void js(String command) {
        Application.get().getPage().executeJs("document.getElementById('" + getId().orElse(null) + "')." + command + ";");
    }
}