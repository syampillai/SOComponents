package com.storedobject.test;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Demo extends Application {

    @Override
    protected ApplicationLayout createLayout() {
        return new AppFrame();
    }

    private static class AppFrame extends ApplicationFrame {

        @Override
        public void drawMenu(Application application) {
            setCaption("Sample Application  Ver 1.0.4");
            addToNavbar(new SpeakerButton());
            ApplicationMenu menu = getMenu();
            ApplicationMenuItem ami;
            ami = application.createMenuItem("One", () -> Notification.show("Hello World!"));
            menu.add(ami);
            ami = application.createMenuItem("Two", () -> Notification.show("Hello World 2!"));
            menu.add(ami);
            ami = application.createMenuItem("Greeting", () -> {
                Application a = Application.get();
                a.speak("Hello, how are you?");
                if(!a.isSpeakerOn()) {
                    Notification.show("Speaker is off! Click on the speaker button to turn it on.");
                }
            });
            menu.add(ami);
            ami = application.createMenuItem("Person Form",
                    () -> new PersonView().execute());
            menu.add(ami);
            ami = application.createMenuItem("Person Form (Cached)", new PersonView("Cached Person Form"));
            menu.add(ami);
            ami = application.createMenuItem("Toggle Compact Mode",
                    () -> ((Demo)Application.get()).toggleCompactTheme());
            menu.add(ami);
        }
    }

    @Route("")
    public static class AppView extends ApplicationView {

        @Override
        protected Application createApplication() {
            return new Demo();
        }
    }

    private static class PersonView extends View implements CloseableView {

        public PersonView() {
            this(null);
        }

        public PersonView(String caption) {
            super(new PersonForm(), caption == null || caption.isBlank() ? "Person Details" : caption);
        }
    }

    private static final String COMPACT_STYLES = """
            --lumo-size-xl: 3rem;
            --lumo-size-l: 2.5rem;
            --lumo-size-m: 2rem;
            --lumo-size-s: 1.75rem;
            --lumo-size-xs: 1.5rem;
            --lumo-font-size: 1rem;
            --lumo-font-size-xxxl: 1.75rem;
            --lumo-font-size-xxl: 1.375rem;
            --lumo-font-size-xl: 1.125rem;
            --lumo-font-size-l: 1rem;
            --lumo-font-size-m: 0.875rem;
            --lumo-font-size-s: 0.8125rem;
            --lumo-font-size-xs: 0.75rem;
            --lumo-font-size-xxs: 0.6875rem;
            --lumo-line-height-m: 1.4;
            --lumo-line-height-s: 1.2;
            --lumo-line-height-xs: 1.1;
            --lumo-space-xl: 1.875rem;
            --lumo-space-l: 1.25rem;
            --lumo-space-m: 0.625rem;
            --lumo-space-s: 0.3125rem;
            --lumo-space-xs: 0.1875rem;
            """;
    private boolean compactTheme = false;

    private void toggleCompactTheme() {
        compactTheme = !compactTheme;
        if(compactTheme) {
            compactModeOn();
        } else {
            compactThemeOff();
        }
        message("Compact Theme: " + (compactTheme ? "On" : "Off"));
    }

    private void compactModeOn() {
        UI ui = getUI();
        Style s = ui.getElement().getStyle();
        try(BufferedReader r = new BufferedReader(new StringReader(Demo.COMPACT_STYLES))) {
            r.lines().forEach(line -> {
                line = line.trim();
                if(line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1);
                }
                int p = line.indexOf(':');
                if(p > 0) {
                    s.set(line.substring(0, p).trim(), line.substring(p + 1).trim());
                }
            });
        } catch (IOException ignored) {
        } catch(Throwable error) {
            warning("Errors while setting custom styles!");
            error(error);
        }
    }

    private void compactThemeOff() {
        UI ui = getUI();
        Style s = ui.getElement().getStyle();
        try(BufferedReader r = new BufferedReader(new StringReader(COMPACT_STYLES))) {
            r.lines().forEach(line -> {
                line = line.trim();
                if(line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1);
                }
                int p = line.indexOf(':');
                if(p >= 0) {
                    line = line.substring(0, p);
                }
                line = line.trim();
                if(!line.isEmpty()) {
                    s.remove(line);
                }
            });
        } catch (IOException ignored) {
        } catch(Throwable error) {
            warning("Errors while removing custom styles!");
            error(error);
        }
    }
}
