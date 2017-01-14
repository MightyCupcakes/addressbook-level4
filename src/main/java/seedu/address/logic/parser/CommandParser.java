package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.parser.ArgumentTokenizer.Prefix;

public abstract class CommandParser {

    public static final Prefix PREFIX_PHONE_NUMBER = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAGS = new Prefix("t/");

    public abstract Command prepareCommand(String args);
}
