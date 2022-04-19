package ru.peacockTeam.utils;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Processing {

    public void go() throws IOException {
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FiosUtil().getResourceFileStream()));
        Long stringNumber = 0L;
        Map<Long, Set<Long>> stringsMap = new TreeMap<>();
        Long groupNumber = 0L;
        Map<Long, Set<Long>> groupsMap = new TreeMap<>();
        groupsMap.put(groupNumber, new HashSet<>());
        String row;
        while ((row = csvReader.readLine()) != null) {
            Set<Long> stringSet = Arrays.stream(row.split(";"))
                    .map(item -> item.replaceAll("\\\"",""))
                    .filter(item -> !item.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());

            stringsMap.put(stringNumber++, stringSet);
            groupsMap.entrySet().stream()
                    .filter(groupsSetEntry -> groupsSetEntry.getValue().contains(stringSet))// пересечение с группой set-строки
                    .
        }
        csvReader.close();
    }
}
