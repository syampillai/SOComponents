package com.storedobject.vaadin;

import java.lang.annotation.*;

/**
 * Annotation to ignore a getXXX method of a "host" set in an {@link ObjectForm} while building fields.
 *
 * @author Syam
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoField {
}
