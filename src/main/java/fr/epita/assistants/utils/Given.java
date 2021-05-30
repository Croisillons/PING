package fr.epita.assistants.utils;

/**
 * All classes with this annotation are provided by the assistants.
 */
public @interface Given {

    /**
     * Will this file be overridden at compilation time by the moulinette?
     * Note: changing this field locally will not change the moulinette's
     * behavior, this is an indication of intent, not something you can
     * unilaterally change.
     *
     * @return `true` if the file will be overridden, false otherwise.
     */
    boolean overridden() default true;
}
