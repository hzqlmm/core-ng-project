package core.framework.impl.log;

import core.framework.api.log.ErrorCode;
import core.framework.api.log.Markers;
import core.framework.api.log.Warning;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * @author neo
 */
public final class LogManager {
    public final String appName;

    private final ThreadLocal<ActionLog> actionLog = new ThreadLocal<>();

    public ActionLogger actionLogger;
    public TraceLogger traceLogger;
    public LogForwarder logForwarder;
    Logger logger;

    public LogManager() {
        this.appName = System.getProperty("core.appName");
    }

    public void begin(String message) {
        ActionLog actionLog = new ActionLog();
        this.actionLog.set(actionLog);

        logger.debug(message);
        logger.debug("[context] id={}", actionLog.id);
    }

    public void end(String message) {
        logger.debug(message);

        ActionLog actionLog = currentActionLog();
        this.actionLog.remove();

        actionLog.end();

        if (actionLogger != null) actionLogger.write(actionLog);
        if (traceLogger != null) traceLogger.write(actionLog);
        if (logForwarder != null) logForwarder.forward(actionLog);
    }

    public void process(LogEvent event) {
        ActionLog actionLog = currentActionLog();
        if (actionLog != null) actionLog.process(event);    // process is called by loggerImpl.log, begin() may not be called before
    }

    public void start() {
        if (logForwarder != null) logForwarder.start();
    }

    public void stop() {
        if (logForwarder != null) logForwarder.stop();
        if (actionLogger != null) actionLogger.close();
    }

    public ActionLog currentActionLog() {
        return actionLog.get();
    }

    public void triggerTraceLog() {
        ActionLog actionLog = currentActionLog();   // actionLog should not be null, logManager.begin should always be called before triggerTraceLog
        actionLog.trace = true;
    }

    public void logError(Throwable e) {
        String errorMessage = e.getMessage();
        String errorCode = e instanceof ErrorCode ? ((ErrorCode) e).errorCode() : e.getClass().getCanonicalName();
        Marker marker = Markers.errorCode(errorCode);
        if (e.getClass().isAnnotationPresent(Warning.class)) {
            logger.warn(marker, errorMessage, e);
        } else {
            logger.error(marker, errorMessage, e);
        }
    }
}
