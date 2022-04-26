package ru.peacockTeam;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import ru.peacockTeam.config.ApplicationConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Application {

    public static ApplicationContext CONTEXT;
    public static long PARSING_TIME;
    public static long START_API_TIME;
    public static String SOURCE_LNG_FILE;

    public static void main(String[] args) {
        START_API_TIME = System.nanoTime();
        CONTEXT = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Environment environment = CONTEXT.getBean(Environment.class);
        SOURCE_LNG_FILE = environment.getProperty("SOURCE_LNG_FILE");
        try {
            CONTEXT.getBean(Processing.class).process();
            PARSING_TIME = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - START_API_TIME);
            System.out.println("\nAPI TIME: " + DurationFormatUtils.formatDuration(PARSING_TIME, "HH:mm:ss,SSS") + " (HH:mm:ss,SSS)");
        } catch (IOException ignored) {
        }
    }
}
