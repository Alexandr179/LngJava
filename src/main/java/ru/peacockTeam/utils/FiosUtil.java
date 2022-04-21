package ru.peacockTeam.utils;

import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.peacockTeam.Application.SOURCE_LNG_FILE;

@Component
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
}
