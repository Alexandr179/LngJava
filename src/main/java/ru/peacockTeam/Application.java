package ru.peacockTeam;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import ru.peacockTeam.config.ApplicationConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Application {

    public static ApplicationContext CONTEXT;
    public static Set<String> STOP_ROW_SET = new HashSet<>();
    public static long PARSING_TIME;
    public static String SOURCE_LNG_FILE;

    public static void main(String[] args) {
        initApi();
    }

    private static void initApi() {
        CONTEXT = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Environment environment = CONTEXT.getBean(Environment.class);
        SOURCE_LNG_FILE = environment.getProperty("SOURCE_LNG_FILE");
        formStopSet();
        PARSING_TIME = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        try {
            CONTEXT.getBean(Processing.class).process();
            System.out.println("API TIME: " + DurationFormatUtils.formatDuration(PARSING_TIME, "HH:mm:ss,SSS") + " (HH:mm:ss,SSS)");
        } catch (IOException ignored) {
        }
    }

    private static void formStopSet() {
        STOP_ROW_SET.add("");
        STOP_ROW_SET.add("");
        STOP_ROW_SET.add("");
        STOP_ROW_SET.add("");
    }
}
