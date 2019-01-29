package com.storedobject.vaadin;

/**
 * {@link Application} can set up an environment that controls certain behaviours of the application.
 * @see Application#createEnvironment()
 * @author Syam
 */
public interface ApplicationEnvironment {

    /**
     * Create the "field creator" for the application when an {@link Object} is edited or viewed in a "data entry form".
     * @return Field creator.
     */
    default ObjectFieldCreator<?> getObjectFieldCreator() {
        return new ObjectFieldCreator() {
        };
    }

    /**
     * Create a "column creator" for the application when an {@link Object} is viewed in a {@link com.vaadin.flow.component.grid.Grid}.
     * @return Column creator.
     */
    default ObjectColumnCreator<?> getObjectColumnCreator() {
        return new ObjectColumnCreator() {
        };
    }

    /**
     * Converts an object into a displable form (String). The default implementation converts it by invoking {@link Object#toString()}.
     * @param any Object to convert
     * @return Converted string.
     */
    default String toString(Object any) {
        if(any == null) {
            return "";
        }
        String s = any.toString();
        return s == null ? "" : s;
    }

    /**
     * Converts an object into a displable form (String). The default implementation converts {@link Exception} to its message and rest of the
     * types to {@link #toString(Object)}.
     * @param any Object to convert
     * @return Converted string.
     */
    default String toDisplay(Object any) {
        if(any == null) {
            return "";
        }
        if(Exception.class.isAssignableFrom(any.getClass())) {
            return toDisplay(((Exception)any).getMessage());
        }
        return toString(any);
    }

    /**
     * Create a label for the attribute name passed. This is typically used for creating labels for field names. THe default
     * implementation converts the name to camel case words. For example, "firstName" will be converted to "First Name".
     * @param attributeName Attribute name
     * @return Label.
     */
    default String createLabel(String attributeName) {
        StringBuilder label = new StringBuilder();
        label.append(attributeName.charAt(0));
        char c;
        for(int i = 1; i < attributeName.length(); i++) {
            c = attributeName.charAt(i);
            if(Character.isUpperCase(c)) {
                label.append(' ');
            }
            label.append(c);
        }
        return label.toString();
    }

    /**
     * Create a label for displaying for a particular class. (A class's name may have to be displayed in some part of the application and this method
     * is invoked for that). The default implementation strips of the "pakage" portion and invokes {@link #createLabel(String)} with the
     * rest of the class name.
     * @param aClass Class to be displayed
     * @return Label.
     */
    default String createLabel(Class<?> aClass) {
        String s = aClass.getName();
        if(s.contains(".")) {
            s = s.substring(s.lastIndexOf('.') + 1);
        }
        if(s.contains("$")) {
            s = s.substring(s.lastIndexOf('$') + 1);
        }
        return createLabel(s);
    }

    /**
     * This method is used to determine the name of the icon to be displayed on a {@link Button} from its label.
     * The default implementation returns certain basic names. For example, for labels like "save", "ok", "yes" etc., it returns "check".
     * @param label Label on the button
     * @return Name of the icon (Icons are created using {@link Icon} class).
     */
    default String getIconName(String label) {
        if(label == null) {
            return null;
        }
        switch (label.toLowerCase()) {
            case "save":
            case "ok":
            case "yes":
                return "check";
            case "cancel":
            case "no":
                return "close";
        }
        return label;
    }

    /**
     * Create a default implementation.
     * @return A default implementation of the "application environment".
     */
    static ApplicationEnvironment get() {
        Application a = Application.get();
        return a == null ? null : a.getEnvironment();
    }
}
