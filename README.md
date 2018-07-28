# SO Components

A set of Vaadin Flow Components and Abstractions

## Components

### BigDecimalField
Field to edit BigDecimal values

### BooleanField
Field to edit Boolean values

### Box
To draw a box around any Vaadin component. new Box(component)

### Button
Enhanced Vaadin Button that takes ClickHandler to handle clicks. Also, it will try to set an Icon based on the text
on the button. (Name resolver on the Icon decides the mapping of the text to icon)

### ButtonIcon
Vaadin Flow wrapper around https://github.com/PolymerElements/paper-icon-button (Web Component License: BSD-3-CLAUSE)

### Card
To make Vaadin component to look like it is embedded in a card. new Card(component)

### ChoiceField
Field to choose an item index from a set of Strings

### ChoicesField
Field to choose zero or more item indices from a set of Strings. The value type of this field is an integer that
represents bit pattern of the chosen indices

### Clickable
Implementation of an slightly enhanced version of com.vaadin.flow.component.ClickNotifier

### ClickHandler
Enahancement to com.vaadin.flow.component.ComponentEventListener for specifically handling mouse clicks

### ComboField
A slightly enhanced ComboBox that internally keeps the items as a list

### DateField
Field to edit java.sql.Date (wrapper around Vaadin's DatePicker)

### DoubleField
Field to edit Double values

### ETextArea
A multi-line text field that can grow.
Vaadin Flow wrapper around https://github.com/PolymerElements/iron-autogrow-textarea (Web Component License: BSD-3-CLAUSE)

### Field
Interface used to represent a field.

### Form
A form with built-in data binder for editing fields (HasValue components) with load(), commit() methods.

### HasElement
Slight enhancements to Vaadin'e Element

### HasIcon
Interface to denote that a comonent has an icon

### HasSquareElement
Interface to denote that a component's shape is a square

### Icon
Enhancements to Vaadin's Icon to handle all Iron icon collections

### ImageButton
Same as Button but no text on it

### IntegerField
Field to edit Integer values

### IPAddressField
Field to edit IP address values

### LabelChoiceField
Field that can be used to choose an item index from list of Strings. Chosen item is shown using a Label

### LabelField
Field that can be used to choose an item from list of items. String value of the chosen item is shown using a Label

### LongField
Field to edit Long values

### MACAddressField
Field to edit MAC address values

### ObjectForm
A form with built-in data binder for editing POJOs with load(), commit() methods.

### PDFViewer
A PDF viewer component that uses browser's native PDF viewer.
Vaadin Flow wrapper around https://github.com/IngressoRapidoWebComponents/pdf-browser-viewer (Web Component License: MIT)

### Scrollable
Make any Vaadin component scrollable by simply implementing this marker interface

### Window
A slightly enhanced version of Dialog that can not be closed by pressing Esc key or clicking outside

## Source code

Available at https://github.com/syampillai/SOComponents

## Code samples
```
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

/**
 * Demo ButtonIcon, Button, Icon, ETextArea, IntegerField, ChoicesField, Form, ObjectForm
 */
@Route("")
public class Demo1 extends VerticalLayout {

    public Demo1() {
        ObjectForm<Person> of = new ObjectForm<>(Person.class);
        Form f = new Form();
        add(new ButtonIcon("device", "bluetooth", e -> {
            Notification.show("Bluetooh clicked!");
        }));
        add(new Button("Save", new Icon("editor", "functions"), e -> {
            Notification.show(f.commit() && of.commit() ? "Saved!" : "Not saved!!");
        }));
        add(new Button("Load", new Icon("editor", "functions"), e -> {
            f.load();
            of.load();
            Notification.show("Loaded!");
        }));
        ETextArea ta = new ETextArea("Text");
        ta.setMinRows(5);
        ta.setMaxRows(10);
        f.addField(ta);
        f.setRequired(ta, "Enter multiple lines");
        add(new ChoicesField("Choose", new String[] { "One", "Two", "Three" }));
        TextField tf = new TextField("Hello");
        f.addField(tf);
        f.setRequired(tf, "Text can not be empty!!!");
        IntegerField i = new IntegerField("Count");
        f.addField(i);
        f.setRequired(i);
        add(f.getComponent());
        f.addValidator(i, v -> v > 10, "Value must be greater that 10");
        add(of.getComponent());
    }

    public static class Person {

        private String firstName = "Me", lastName = "You";

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getName() {
            return firstName + " " + lastName;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
```
```
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

/**
 * Demo PDFViewer
 */
@Route("pdf")
public class Demo2 extends Div {

    public Demo2() {
        PDFViewer pdfViewer = new PDFViewer("http://www.cambridgeenglish.org/images/young-learners-sample-papers-2018-vol1.pdf");
        pdfViewer.setWidth("800px");
        pdfViewer.setHeight("600px");
        add(pdfViewer);
    }
}
```
