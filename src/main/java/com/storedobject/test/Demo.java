package com.storedobject.test;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

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
}
