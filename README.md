# SO Components
A set of Vaadin Flow Components and Abstractions

(Sample screenshots are from a commercial application under development that uses this add-on)

## Use Cases
There are 2 different ways of using this add-on.

### Normal Use
It can be used to utilize the components (especially "fields" and "forms") available in this add-on.

The "form" concept here supports a mechanism that doesn't require direct use of Vaadin'n "Binder" classes to create data entry forms.
A hidden binder does all the tricks.

Also, there are two notable classes: DataGrid and TreeDataGrid. These clsses are enhanced
versions of Vaadin's Grid and TreeGrid respectively.

Two layout classes are worth mentioning: ButtonLayout and GridLayout.

A crude version of Dashboard is also available.

### Single Page Application
This is the 2nd use case. Single page applications typically use an "application layout"
with a "menu area" and "content area". The content area displays the current "view". A view may
be anyy sort of information displayed in the content area. It could be a "data entry screen", some
sort of chart or a dashboard. This add-on contains certain classes to create different
"views" including "data entry screens".

Please see the documentation of the Application class to get an idea how to create a single-page
application.

## Documentation
Most classes are documented with some explanation about its usage. Please see the API
documentation.

## Source code
Available at https://github.com/syampillai/SOComponents