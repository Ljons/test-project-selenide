package ui.listeners;

import io.qameta.allure.Attachment;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import ui.base.BaseTest;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Log4j
public class StepListener implements StepLifecycleListener {

    static utils.Logger logger = utils.Logger.get(BaseTest.class);
    private Map<Long, StepLogger> logMap          = new HashMap<>();

    @SneakyThrows
    public StepListener() {
    }

    @Override
    public synchronized void afterStepStart(StepResult result) {
        var id = Thread.currentThread().getId();
            logMap.put(id, new StepLogger(true, id));
    }

    @Override
    public synchronized void beforeStepStop(StepResult result) {
        var name = result.getName();
            logMap.get(Thread.currentThread().getId()).stop();
            if (validateStep(result))
                logger.stepInfo(String.format("%s\t- [%s]", name, result.getStatus().name()));
    }

    /**
     * Can be used to check if step logging required.
     * 
     * START/FINISH STEP messages were added, But it not required for all steps.
     * 
     * Right now by this method excluded such messages for HELPER steps and for optional attribute setters in API steps
     * 
     * @param result
     * @return
     */
    private boolean validateStep(StepResult result) {
        var name = result.getName();
        boolean isValid = !name.contains("Logger") && !name.contains("BaseTest") && !name.contains("Selenide");
        if (isValid && result.getParameters().size() > 0) {
            isValid = isValid && !String.valueOf(result.getParameters().get(0).getValue()).equals("null");
        }
        return isValid;
    }

    static class StepLogger {
        private StringWriter   stringWriter;
        private WriterAppender writerAppender;
        private boolean        isLoggerEnabled;

        public StepLogger(boolean isLoggerEnabled, long threadId) {
            this.isLoggerEnabled = isLoggerEnabled;
            if (isLoggerEnabled) {
                Layout layout = Logger.getRootLogger().getAppender("CONSOLE").getLayout();
                stringWriter = new StringWriter();
                writerAppender = new WriterAppender(layout, stringWriter);
                writerAppender.addFilter(getFilter(threadId));
                Logger.getRootLogger().addAppender(writerAppender);
            }
        }

        private Filter getFilter(long id) {
            return new Filter() {
                @Override
                public int decide(LoggingEvent event) {
                    var isValidThread = event.getProperty("id").equals(String.valueOf(id));
                    if (isValidThread)
                        return ACCEPT;
                    return DENY;
                }

            };
        }

        @SneakyThrows
        public void stop() {
            if (isLoggerEnabled) {
                var log = stringWriter.toString();
                if (!log.isEmpty())
                    attachLog(log);
                Logger.getRootLogger().removeAppender(writerAppender);
                stringWriter.close();
            }
        }

        @Attachment(value = "STEP LOG", type = "text/plain")
        public String attachLog(String value) {
            return value;
        }

    }

}
