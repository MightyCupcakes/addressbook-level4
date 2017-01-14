package seedu.address.logic.parser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import seedu.address.commons.core.LogsCenter;

public class ParserRegistry {

    private static final ParserRegistry INSTANCE = new ParserRegistry();

    private final Logger logger = LogsCenter.getLogger(ParserRegistry.class);

    private final Map<String, CommandParserInfo> parserRegistry;

    private ParserRegistry() {
        this(ParserRegistry.class.getPackage().getName());
    }

    protected ParserRegistry(String packageName) {
        parserRegistry = Maps.newHashMap();
        getCommandClasses(packageName);
    }

    public static ParserRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the corresponding command parser for the given command word.
     * <br>
     * Returns {@code Optional.empty()} if no such command parser exists.
     */
    public Optional<CommandParser> getCommandParser(String commandWord) {
        CommandParserInfo commandInfo = parserRegistry.get(commandWord);

        if (commandInfo == null) {
            return Optional.empty();
        }

        try {
            CommandParser parser;

            if (commandInfo.passParam) {
                parser = commandInfo.parserClass.getConstructor(String.class).newInstance(commandWord);
            } else {
                parser = commandInfo.parserClass.newInstance();
            }

            return Optional.of(parser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void getCommandClasses(String packageName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ClassPath classPath;

        try {
            classPath = ClassPath.from(loader);
        } catch (IOException e) {
            logger.severe("Unable to load command parsers");
            return;
        }

        Set<ClassInfo> classes = classPath.getTopLevelClassesRecursive(packageName);

        for (ClassInfo classInfo : classes) {
            Class<?> thisClass = classInfo.load();

            if (thisClass.isAnnotationPresent(RegisterParser.class)
                    && CommandParser.class.isAssignableFrom(thisClass)) {
                registerCommandParser(thisClass);
            }
        }
    }

    private void registerCommandParser(Class<?> clazz) {

        CommandParserInfo commandInfo = getCommandInfo(clazz);

        for (String commandWord : commandInfo.commandWords) {
            parserRegistry.put(commandWord, commandInfo);
            logger.info("Command Parser registered for command word " + commandWord);
        }
    }

    private CommandParserInfo getCommandInfo(Class<?> thisClass) {
        Annotation[] annotations = thisClass.getAnnotations();

        RegisterParser registerAnnotation = null;

        for (Annotation thisAnnotation : annotations) {
            if (thisAnnotation instanceof RegisterParser) {
                registerAnnotation = (RegisterParser) thisAnnotation;
                break;
            }
        }

        assert registerAnnotation != null;
        return new CommandParserInfo(registerAnnotation.passCommandWordAsParam(),
                Arrays.asList(registerAnnotation.commandWord()),
                thisClass);
    }

    private static class CommandParserInfo {
        public boolean passParam;
        public Iterable<String> commandWords;
        public Class<? extends CommandParser> parserClass;

        @SuppressWarnings("unchecked")
        public CommandParserInfo(boolean passParam, Iterable<String> commandWords, Class<?> parserClass) {
            this.passParam = passParam;
            this.commandWords = commandWords;
            this.parserClass = (Class<? extends CommandParser>) parserClass;
        }
    }
}
