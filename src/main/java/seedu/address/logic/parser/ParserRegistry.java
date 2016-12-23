package seedu.address.logic.parser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import seedu.address.commons.core.LogsCenter;

public class ParserRegistry {

    private final Logger logger = LogsCenter.getLogger(ParserRegistry.class);
    private static final ParserRegistry INSTANCE = new ParserRegistry();

    private final Map<String, Class<? extends CommandParser>> parserRegistry;

    private ParserRegistry() {
        parserRegistry = Maps.newHashMap();
        getCommandClasses(this.getClass().getPackage().getName());
    }

    public static ParserRegistry getInstance() {
        return INSTANCE;
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
        @SuppressWarnings("unchecked")
        Class<? extends CommandParser> commandParser = (Class<? extends CommandParser>) clazz;

        String commandWord = getCommandWord(commandParser);
        parserRegistry.put(commandWord, commandParser);

        logger.info("Command Parser registered for command word " + commandWord);
    }

    private String getCommandWord(Class<? extends CommandParser> thisClass) {
        Annotation[] annotations = thisClass.getAnnotations();

        RegisterParser registerAnnotation = null;

        for (Annotation thisAnnotation : annotations) {
            if (thisAnnotation instanceof RegisterParser) {
                registerAnnotation = (RegisterParser) thisAnnotation;
                break;
            }
        }

        assert registerAnnotation != null;
        return registerAnnotation.commandWord();
    }
}
