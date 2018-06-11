package com.patrickanker.chatterbox.util.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicCommand {

    /**
     *
     * List of aliases for the command. Index 0 is the command name & label
     *
     */
    String[] aliases();

    /**
     *
     * Minimum and maximum argument lengths.
     * Index 0 is the minimum, and index 1 is maximum
     * Set either to -1 to have unlimited lower or upper bound, respectively.
     *
     * E.g. bounds = [2, -1] would mean at minimum two arguments long but with no maximum
     *
     */
    int[] bounds();

    /**
     *
     * Prints this out when person does "/<command> ?", "/<command> help", or "/<command> h"
     *
     */
    String help() default "§cNo help provided for this command";

    /**
     *
     * The associated permission string for this command
     *
     */
    String permission() default "";

    /**
     *
     * True if this command only makes sense if sent from a player
     *
     */
    boolean playerOnly() default false;
}
