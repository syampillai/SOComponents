package com.storedobject.vaadin;

/**
 * An interface that generate HTML text.
 *
 * @author Syam
 */
@FunctionalInterface
public interface HTMLGenerator {

    /**
     * HTML character entities
     */
    String[] CHAR_ENTITIES = new String[] {
            "&amp;", "&",
            "&nbsp;", " ",
            "&gt;", ">",
            "&lt;", "<",
            "&quot;", "\"",
            "&apos;", "'",
            "&copy;", "\u0169",
            "&reg;", "\u0174",
    };

    /**
     * Get the HTML text generated. (The generated HTML may not contain outer tags. So, span or div tag may be used to
     * bracket it in order to make it a valid HTML text).
     *
     * @return HTML text generated.
     */
    String getHTML();

    /**
     * Get a printable object that can be used for creating a printable representation of the HTML text.
     *
     * @return The default implementation returns the HTML text getHTML() returns after handling line-breaks and simple ampersand coded characters.
     */
    default Object getPrintText() {
        String s = getHTML();
        for(int i = 2; i < CHAR_ENTITIES.length; i++) {
            s = s.replace(CHAR_ENTITIES[i], CHAR_ENTITIES[i + 1]);
            ++i;
        }
        s = s.replace(CHAR_ENTITIES[0], CHAR_ENTITIES[1]);
        return s.replace("<BR>", "\n").replace("<br>", "\n");
    }

    /**
     * Encode a string so that HTML character entities and newline characters are properly replaced.
     *
     * @param string String to encode
     * @return Encoded string.
     */
    static String encodeHTML(String string) {
        if(string == null) {
            string = "";
        }
        for(int i = 0; i < CHAR_ENTITIES.length; i++) {
            string = string.replace(CHAR_ENTITIES[i], CHAR_ENTITIES[i + 1]);
            ++i;
        }
        return string.replace("\n", "<br>");
    }
}
