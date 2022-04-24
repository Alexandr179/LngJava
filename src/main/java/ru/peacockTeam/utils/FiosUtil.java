package ru.peacockTeam.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static ru.peacockTeam.Application.SOURCE_LNG_FILE;
import static ru.peacockTeam.Application.START_API_TIME;

public class FiosUtil {

    public InputStream getResourceFileStream(){
        return this.getClass().getClassLoader().getResourceAsStream(SOURCE_LNG_FILE);
    }

    public List<String> getRowSet(String row) {
        return Arrays.stream(row.split(";"))
                .map(item -> item.replaceAll("\\\"", ""))
                .filter(item -> !item.isEmpty())
                .collect(Collectors.toList());
    }

    public static String getCurrentTime(){
        return DurationFormatUtils.formatDuration(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - START_API_TIME), "HH:mm:ss,SSS");
    }
}
