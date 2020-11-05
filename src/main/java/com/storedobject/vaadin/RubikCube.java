package com.storedobject.vaadin;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Rubik's Cube (created by wrapping Scarygami's @scarygami/scary-cube).
 * <p>Please note: I have included the JS file directly to handle Polymer version compatibility.</p>
 *
 * @author Syam
 */
@Tag("scary-cube")
@NpmPackage(value = "resize-observer-polyfill", version = "1.5.1")
@JsModule("./so/scarycube/scary-cube.js")
public class RubikCube extends LitTemplate implements HasSize {

    private boolean shuffled = false;
    private List<Consumer<RubikCube>> solvedListeners;

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
    public void move(CharSequence moves) {
        if(moves == null || moves.length() == 0) {
            return;
        }
        boolean many = false;
        StringBuilder command = new StringBuilder();
        char c;
        for(int i = 0; i < moves.length(); i++) {
            c = moves.charAt(i);
            switch(c) {
                case ' ':
                    continue;
                case '\'':
                case '2':
                    command.append(c);
                    continue;
                default:
                    if(i > 0) {
                        command.append(' ');
                        many = true;
                    }
                    command.append(Character.toUpperCase(c));
            }
        }
        getElement().callJsFunction("addMove" + (many ? "s" : ""), command.toString());
    }

    /**
     * Reset the cube.
     */
    public void reset() {
        getElement().callJsFunction("reset");
    }

    /**
     * Shuffle the cube. The cube is considered as shuffled only if it was shuffled with at least 10 moves.
     *
     * @param moves Number of moves to be used for shuffling.
     */
    public void shuffle(int moves) {
        if(moves <= 0) {
            return;
        }
        if(!shuffled) {
            shuffled = moves > 9;
        }
        Random random = new Random();
        StringBuilder m = new StringBuilder();
        int r;
        while(moves-- > 0) {
            r = random.nextInt(9);
            m.append(moveChar(r));
            switch(r) {
                case 6:
                case 7:
                case 8:
                    if(random.nextBoolean()) {
                        m.append('\'');
                    }
                    break;
                default:
                    switch(random.nextInt(3)) {
                        case 0:
                            break;
                        case 1:
                            m.append('\'');
                            break;
                        case 2:
                            m.append('2');
                            break;
                    }
            }
        }
        move(m);
    }

    private char moveChar(int i) {
        switch(i) {
            case 0:
                return 'F';
            case 1:
                return 'R';
            case 2:
                return 'U';
            case 3:
                return 'L';
            case 4:
                return 'B';
            case 5:
                return 'D';
            case 6:
                return 'M';
            case 7:
                return 'E';
            case 8:
                return 'S';
        }
        return 0;
    }

    @ClientCallable
    private void solved() {
        if(shuffled) {
            shuffled = false;
            if(solvedListeners != null) {
                solvedListeners.forEach(s -> s.accept(RubikCube.this));
            }
        }
    }

    /**
     * Add a "solved listener" that will be informed about if the cube is solved. (The cube will be considered
     * solved only if it was previously shuffled).
     *
     * @param solvedListener Solved listener (A {@link Consumer} that consumes a {@link RubikCube}).
     * @return Registration.
     */
    public Registration addSolvedListener(Consumer<RubikCube> solvedListener) {
        if(solvedListeners == null) {
            solvedListeners = new ArrayList<>();
        }
        solvedListeners.add(solvedListener);
        return () -> solvedListeners.remove(solvedListener);
    }
}