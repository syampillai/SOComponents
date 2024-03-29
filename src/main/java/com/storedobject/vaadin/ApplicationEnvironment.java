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
        return new ObjectFieldCreator<>() {
        };
    }

    /**
     * Create a "column creator" for the application when an {@link Object} is viewed in a
     * {@link com.vaadin.flow.component.grid.Grid}.
     * @return Column creator.
     */
    default ObjectColumnCreator<?> getObjectColumnCreator() {
        return new ObjectColumnCreator<>() {
        };
    }

    /**
     * Converts an object into a displayable form (String). The default implementation converts it by invoking
     * {@link Object#toString()}.
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
     * Converts an object into a displayable form (String). The default implementation converts {@link Exception} to
     * its message and rest of the types to {@link #toString(Object)}.
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
        if(any instanceof HTMLGenerator) {
            return ((HTMLGenerator) any).getHTML();
        }
        return toString(any);
    }

    /**
     * Create a label for the attribute name passed. This is typically used for creating labels for field names.
     * The default implementation converts the name to camel case words. For example, "firstName" will be converted
     * to "First Name".
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
     * Create a label for displaying a particular class name. (A class's name may have to be displayed in some part
     * of the application and this method is invoked for that). The default implementation strips of the "package"
     * portion and invokes {@link #createLabel(String)} with the rest of the class name.
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
     * The default implementation returns certain basic names. For example, for labels like "save", "ok", "yes" etc.,
     * it returns "check".
     * @param label Label on the button
     * @return Name of the icon (Icons are created using {@link Icon} class).
     */
    default String getIconName(String label) {
        if(label == null || label.contains(":")) {
            return null;
        }
        return switch(label.toLowerCase()) {
            case "save", "ok", "yes" -> "check";
            case "cancel", "no", "clear" -> "close";
            default -> label;
        };
    }

    /**
     * Create a menu item.
     *
     * @param menuLabel Menu label
     * @param icon Icon
     * @param menuAction Action associated with the menu item
     * @return Menu item created.
     */
    default ApplicationMenuItem createMenuItem(String menuLabel, String icon, Runnable menuAction) {
        return MenuItem.create(menuLabel, getIconName(icon), menuAction);
    }

    /**
     * Create a menu item for the View.
     *
     * @param view View for which menu item needs to be created
     * @param menuLabel Menu label
     * @param menuAction Action associated with the menu item
     * @return Menu item created.
     */
    default ApplicationMenuItem createMenuItem(ExecutableView view, String menuLabel, Runnable menuAction) {
        return MenuItem.create(view, menuLabel, menuAction);
    }

    /**
     * Create a menu item.
     *
     * @param menuLabel Menu label
     * @return Menu item created.
     */
    default ApplicationMenuItemGroup createMenuItemGroup(String menuLabel) {
        return MenuItem.createGroup(menuLabel);
    }

    /**
     * Get the menu icon to be used for active (running) {@link View}s.
     *
     * @return Default implementation returns "vaadin:cogs".
     */
    default String getActiveMenuIconName() {
        return "vaadin:cogs";
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
