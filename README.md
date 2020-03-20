# SO Components
A set of Vaadin Flow Components and Abstractions

https://vaadin.com/directory/component/so-components

## Dependencies
https://vaadin.com/directory/component/textfield-formatter Ver 4.1.4 or later (for field formatting features - use versions < 1.3.0 if you don't want this dependency).

https://vaadin.com/directory/component/multiselect-combo-box Ver 2.3.1 or later (for TokensField - use versions < 1.7.0 if you don't want this dependency).

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