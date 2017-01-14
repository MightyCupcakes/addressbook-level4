package seedu.address.logic.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the class declaration is intended to be registered as a command parser.
 * The command parser will be registered to the given command word.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterParser {
    public String[] commandWord();
    public boolean passCommandWordAsParam() default false;
}
