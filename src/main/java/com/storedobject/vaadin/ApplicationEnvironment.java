package com.storedobject.vaadin;

public interface ApplicationEnvironment {

    default ObjectFieldCreator<?> getObjectFieldCreator() {
        return new ObjectFieldCreator() {
        };
    }

    default ObjectColumnCreator<?> getObjectColumnCreator() {
        return new ObjectColumnCreator() {
        };
    }

    default String toString(Object any) {
        if(any == null) {
            return "";
        }
        String s = any.toString();
        return s == null ? "" : s;
    }

    default String toDisplay(Object any) {
        if(any == null) {
            return "";
        }
        if(any.getClass() == Exception.class) {
            return ((Exception)any).getMessage();
        }
        return toString(any);
    }

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

    default String getIconName(String label) {
        if(label == null) {
            return label;
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

    public static ApplicationEnvironment get() {
        return Application.get().getEnvironment();
    }
}
