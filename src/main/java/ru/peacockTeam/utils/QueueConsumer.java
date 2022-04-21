package ru.peacockTeam.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.peacockTeam.Processing;
import ru.peacockTeam.ProcessingThreadsImpl;

import java.util.concurrent.TimeUnit;

import static ru.peacockTeam.Application.PARSING_TIME;

@Component
public class QueueConsumer implements Runnable {

    @Autowired
    @Qualifier("ProcessingImpl")//  "ProcessingThreadsImpl"  /multithreading/
    Processing processing;

    public static long PROCESS_TIME;

    @Override
    public void run() {
        processing.process();
        lookTime();
    }

    public static void lookTime() {
        PROCESS_TIME = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - PARSING_TIME);
        System.err.println("API_TIME: " + DurationFormatUtils.formatDuration(PROCESS_TIME, "HH:mm:ss,SSS") + " (HH:mm:ss,SSS)");
    }
}