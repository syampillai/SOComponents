package com.storedobject.vaadin;

import com.storedobject.helper.LitComponent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple component to use as an up/down counter. The counting is done in seconds.
 *
 * @author Syam
 */
@Tag("so-timer")
@JsModule("./so/timer/so-timer.js")
public class TimerComponent extends LitComponent implements HasStyle {

    private final List<Listener> listeners = new ArrayList<>();
    @Id
    private Span span;

    /**
     * Constructor.
     */
    public TimerComponent() {
        executeJS("initComp");
    }

    /**
     * Add a listener to this timer.
     *
     * @param listener Listener.
     * @return Registration that can be used to remove the listener.
     */
    public Registration addListener(Listener listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    @ClientCallable
    private void completed() {
        listeners.forEach(listener -> listener.completed(this));
    }

    /**
     * Count up.
     *
     * @param seconds Seconds
     */
    public void countUp(int seconds) {
        countDown(-seconds);
    }

    /**
     * Count down.
     *
     * @param seconds Seconds
     */
    public void countDown(int seconds) {
        executeJS("count", -seconds);
    }

    /**
     * Abort the timer.
     */
    public void abort() {
        countDown(0);
    }

    /**
     * Set a prefix for the display. This value will be displayed as a prefix to the timer value.
     * <p>For example, you may want to display something like "Waiting for 23 seconds". Here, "Waiting for " is the
     * prefix and " seconds" is the suffix.</p>
     *
     * @param prefix Prefix.
     */
    public void setPrefix(String prefix) {
        executeJS("setDPrefix", prefix);
    }

    /**
     * Set a suffix for the display. This value will be displayed as a prefix to the timer value.
     * <p>For example, you may want to display something like "Waiting for 23 seconds". Here, "Waiting for " is the
     * prefix and " seconds" is the suffix.</p>
     *
     * @param suffix Suffix.
     */
    public void setSuffix(String suffix) {
        executeJS("setDSuffix", suffix);
    }

    @Override
    public Style getStyle() {
        return span.getStyle();
    }

    /**
     * Listener interface for the {@link TimerComponent} class.
     *
     * @author Syam
     */
    @FunctionalInterface
    public interface Listener {
        void completed(TimerComponent timerComponent);
    }
}
