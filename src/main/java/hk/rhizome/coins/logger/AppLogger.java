package hk.rhizome.coins.logger;

import org.slf4j.LoggerFactory;

import hk.rhizome.coins.LoggerConfiguration;
import hk.rhizome.coins.logger.LoggerUtils.Level;

/**
 * Application logger.
 */
public class AppLogger {

    private static final Object LOCK = new Object();

    private static AppLogger appLogger;
    private LoggerUtils log;
    private org.slf4j.Logger slf4j;

    private AppLogger() {
        slf4j = LoggerFactory.getLogger(AppLogger.class.getSimpleName());
        log = LoggerUtils.getLogger();
    }

    private AppLogger(LoggerConfiguration configuration) {
        slf4j = LoggerFactory.getLogger(AppLogger.class.getSimpleName());
        log = LoggerUtils.getLogger();
        initializeLogger(configuration);
    }

    private void initializeLogger(LoggerConfiguration configuration) {
        LoggerFormatter formatter = new LoggerFormatter();
        String logLevel = configuration.getLogLevel();
        
        Level level = logLevel != null ? Level.valueOf(logLevel) : null;
        log.initialize(formatter, level);
    }
    
    public static AppLogger initialize() {
        synchronized (LOCK) {
            if (appLogger == null) {
                appLogger = new AppLogger();
            }
        }
        return appLogger;
    }

    public static AppLogger initialize(LoggerConfiguration configuration) {
        synchronized (LOCK) {
            if (appLogger == null) {
                appLogger = new AppLogger(configuration);
            }
        }
        return appLogger;
    }

    public static AppLogger getLogger() {
        return appLogger;
    }

    public void error(Object message) {
        slf4j.error(message.toString());
        if (log.isInitialized()) {
            log.error(message);
        }
    }

    public void error(Object message, Throwable throwable) {
        slf4j.error(message.toString(), throwable);
        if (log.isInitialized()) {
            log.error(message, throwable);
        }
    }

    public void warn(Object message) {
        slf4j.warn(message.toString());
        if (log.isInitialized()) {
            log.warn(message);
        }
    }

    public void warn(Object message, Throwable throwable) {
        slf4j.warn(message.toString(), throwable);
        if (log.isInitialized()) {
            log.warn(message, throwable);
        }
    }

    public void info(Object message) {
        slf4j.info(message.toString());
        if (log.isInitialized()) {
            log.info(message);
        }
    }

    public void info(Object message, Throwable throwable) {
        slf4j.info(message.toString(), throwable);
        if (log.isInitialized()) {
            log.info(message, throwable);
        }
    }

    public void debug(Object message) {
        slf4j.debug(message.toString());
        if (log.isInitialized()) {
            log.debug(message);
        }
    }

    public void debug(Object message, Throwable throwable) {
        slf4j.debug(message.toString(), throwable);
        if (log.isInitialized()) {
            log.debug(message, throwable);
        }
    }
}
