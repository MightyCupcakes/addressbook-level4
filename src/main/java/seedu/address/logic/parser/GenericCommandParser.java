package seedu.address.logic.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;

@RegisterParser (
        commandWord =
        {
                ClearCommand.COMMAND_WORD, ListCommand.COMMAND_WORD, ExitCommand.COMMAND_WORD, HelpCommand.COMMAND_WORD
        },
        passCommandWordAsParam = true
        )
public class GenericCommandParser extends CommandParser {

    private static final ImmutableMap<String, Class<? extends Command>> commandMap;

    private String commandWord;

    static {
        Builder<String, Class<? extends Command>> builder = ImmutableMap.builder();

        builder.put(ClearCommand.COMMAND_WORD, ClearCommand.class);
        builder.put(ListCommand.COMMAND_WORD, ListCommand.class);
        builder.put(ExitCommand.COMMAND_WORD, ExitCommand.class);
        builder.put(HelpCommand.COMMAND_WORD, HelpCommand.class);

        commandMap = builder.build();
    }

    public GenericCommandParser() {
        this("");
    }

    public GenericCommandParser(String commandWord) {
        this.commandWord = commandWord;
    }

    @Override
    public Command prepareCommand(String args) {
        if ("".equals(commandWord) || !commandMap.containsKey(commandWord)) {
            return new IncorrectCommand("");
        }

        try {
            return commandMap.get(commandWord).newInstance();
        } catch (Exception e) {
            return new IncorrectCommand("");
        }
    }
}
