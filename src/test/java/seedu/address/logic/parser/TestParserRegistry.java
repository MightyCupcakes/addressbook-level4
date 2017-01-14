package seedu.address.logic.parser;

import java.util.Map;

public class TestParserRegistry extends ParserRegistry {
    public TestParserRegistry(String packageName) {
        super(packageName);
    }

    public Map<String, CommandParserInfo> getParserRegistry() {
        return parserRegistry;
    }
}
