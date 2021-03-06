package core.framework.impl.log;

import core.framework.api.util.Maps;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @author neo
 */
public final class DefaultLoggerFactory implements ILoggerFactory {
    public final LogManager logManager;
    private final Map<String, Logger> loggers = Maps.newConcurrentHashMap();

    public DefaultLoggerFactory() {
        logManager = new LogManager();
        logManager.logger = getLogger(LogManager.class.getName());  // create logger requires logManager, so this is to create logManager first then assign logger
    }

    @Override
    public Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, this::createLogger);
    }

    private Logger createLogger(String name) {
        LogLevel[] levels = logLevel(name);
        return new LoggerImpl(name, logManager, levels[0], levels[1]);
    }

    private LogLevel[] logLevel(String name) {
        if (name.startsWith("org.elasticsearch")
            || name.startsWith("org.mongodb")
            || name.startsWith("org.apache")
            || name.startsWith("org.xnio")) {
            return new LogLevel[]{LogLevel.WARN, LogLevel.INFO};
        }
        return new LogLevel[]{LogLevel.INFO, LogLevel.DEBUG};
    }
}
