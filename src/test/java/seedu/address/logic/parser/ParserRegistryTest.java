package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;

public class ParserRegistryTest {

    private static final String PACKAGE_EMPTY = "seedu.address.logic.parser.emptypackage";
    private static final String PACKAGE_NOT_INSTANCE = "seedu.address.logic.parser.invalidparser";
    private static final String PACKAGE_VALID = "seedu.address.logic.parser.validparser";

    private TestParserRegistry parserRegistry;

    @Before
    public void setUp() {
        parserRegistry = new TestParserRegistry(PACKAGE_VALID);
    }

    @Test
    public void register_emptyPackage_notRegistered() {
        TestParserRegistry testRegistry = new TestParserRegistry(PACKAGE_EMPTY);
        assertEquals(testRegistry.getParserRegistry(), Maps.newHashMap());
    }

    @Test
    public void register_notInstaneOfCommandResult_notRegistered() {
        TestParserRegistry testRegistry = new TestParserRegistry(PACKAGE_NOT_INSTANCE);
        assertEquals(testRegistry.getParserRegistry(), Maps.newHashMap());
    }

    @Test
    public void register_validCommandParser_registered() {
    }

    private static class TestCommand extends Command {

        private String message;

        public TestCommand(String msg) {
            super();
            this.message = msg;
        }

        @Override
        public CommandResult execute() {
            return new CommandResult(message);
        }

    }

    private static class TestParser extends CommandParser {
        @Override
        public Command prepareCommand(String args) {
            return null;
        }
    }
}
