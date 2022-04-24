package ru.peacockTeam;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Application {

    public static long PARSING_TIME;
    public static long START_API_TIME;
    public static String SOURCE_LNG_FILE = "lng.csv";

    public static void main(String[] args) {
        START_API_TIME = System.nanoTime();
        try {
            new Processing().process();
            PARSING_TIME = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - START_API_TIME);
            System.out.println("\nAPI TIME: " + DurationFormatUtils.formatDuration(PARSING_TIME, "HH:mm:ss,SSS") + " (HH:mm:ss,SSS)");
        } catch (IOException ignored) {
        }
    }
}
