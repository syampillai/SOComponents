# SO Components
A set of Vaadin Flow Components and Abstractions

https://vaadin.com/directory/component/so-components

Include this in the dependencies section of your POM file:
```xml
  <dependency>
    <groupId>com.storedobject</groupId>
    <artifactId>so-components</artifactId>
    <version>14.0.6</version>
  </dependency>
```
Also, include this in your repositories section:
```xml
<repositories>
    <repository>
        <id>so-maven</id>
        <url>https://storedobject.com/maven</url>
    </repository>
</repositories>
```

## Dependencies
https://vaadin.com/directory/component/so-helper Ver 2.1.2 or later (Helper classes).

https://vaadin.com/directory/component/textfield-formatter Ver 5.4.0 or later (for field formatting features).  

https://vaadin.com/directory/component/multicombobox-component Ver 2.0.0 or later (for token-field implementation).

https://xmlgraphics.apache.org/batik Ver 1.11 (for SVG image manipulation).

## Use Cases
There are 2 different ways of using this add-on.

### Normal Use
It can be used to utilize the components (especially "fields" and "forms") available in this add-on.

The "form" concept here supports a mechanism that doesn't require direct use of Vaadin's "Binder" classes to create data entry forms.
A hidden binder does all the tricks.

Also, there are two notable classes: DataGrid and TreeDataGrid. These classes are enhanced
versions of Vaadin's Grid and TreeGrid respectively.

Two layout classes are worth mentioning: ButtonLayout and GridLayout.

A version of Dashboard is also available.

### Single Page Application
This is the 2nd use case. Single page applications typically use an "application layout"
with a "menu area" and "content area". The content area displays the current "view". A view may
be any sort of information displayed in the content area. It could be a "data entry screen", some
sort of chart or a dashboard. This add-on contains certain classes to create different
"views" including "data entry screens".

Please see the documentation of the Application class to get an idea how to create a single-page
application.

## Documentation
Most classes are documented with some explanation about its usage. Please see the API
documentation.