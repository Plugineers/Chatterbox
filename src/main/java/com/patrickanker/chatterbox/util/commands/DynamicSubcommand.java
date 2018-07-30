package com.patrickanker.chatterbox.util.commands;

import java.lang.annotation.Repeatable;

@Repeatable(DynamicSubcommands.class)
public @interface DynamicSubcommand {

    String[] aliases();

    String permission() default "";
}
