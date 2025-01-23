package ui.listeners;


import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;
import org.testng.Reporter;

import java.util.Objects;

public class TestNGReportAppender extends AppenderSkeleton {

    @Override
    protected void append(final LoggingEvent event) {
        MDC.put("id", Thread.currentThread().getId());
        Reporter.log(eventToString(event));
    }

    private String eventToString(final LoggingEvent event) {
        var result = new StringBuilder(layout.format(event));
        if (layout.ignoresThrowable()) {
            var s = event.getThrowableStrRep();
            if (Objects.nonNull(s)) {
                for (String value : s) {
                    result.append(value).append(Layout.LINE_SEP);
                }
            }
        }
        return result.toString();
    }

    @Override
    public void close() {}

    @Override
    public boolean requiresLayout() {
        return true;
    }

}
