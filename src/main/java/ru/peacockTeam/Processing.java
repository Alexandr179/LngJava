package ru.peacockTeam;

import ru.peacockTeam.utils.FiosUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Processing {

    public static String SOURCE_LNG_FILE = "lng_test.csv";
    private final FiosUtil fiosUtil;
    private Integer groupCounter = 1;
    public Map<String, Set<List<String>>> rowsMap = new HashMap<>();
    public Map<String, Set<String>> interceptionsMap = new ConcurrentHashMap<>();
    public List<List<String>> groupList = new LinkedList<>();

    public Processing() {
        this.fiosUtil = new FiosUtil();
    }

    public void process() throws IOException {
        byte[] resourceFileByteStream = fiosUtil.getCopyByteArrayStream(fiosUtil.getResourceFileStream());
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resourceFileByteStream)));
        System.out.println(">> API process. Building GroupMaps...");
        String rowAsStr;
        while ((rowAsStr = csvReader.readLine()) != null) {
            List<String> row = fiosUtil.getRowSet(rowAsStr);
            if (!row.isEmpty()) buildMaps(row);
        }
        csvReader.close();
        collapseMaps();
//        outputGroups();
    }

    public void buildMaps(List<String> row) {
        for (String rowItem : row) {
            Set<String> vertexSet = new HashSet<>();
            Set<String> iterationRow = new HashSet<>(row);
            iterationRow.remove(rowItem);
            vertexSet.addAll(iterationRow);
            interceptionsMap.merge(rowItem, vertexSet, (x, y) -> {
                x.addAll(y);
                return x;
            });
            Set<List<String>> rowSet = new HashSet<>();
            rowSet.add(row);
            rowsMap.merge(rowItem, rowSet, (x, y) -> {
                x.addAll(y);
                return x;
            });
        }
    }

    private void collapseMaps() {
        Iterator<Map.Entry<String, Set<String>>> interceptionsMapIterator = interceptionsMap.entrySet().iterator();
        while (interceptionsMapIterator.hasNext()) {
            boolean isIntercept = false;
            Map.Entry<String, Set<String>> entryIntspMap = interceptionsMapIterator.next();
            String key = entryIntspMap.getKey();
            for (String iteratedValue : entryIntspMap.getValue()) {
                if (interceptionsMap.containsKey(iteratedValue)) {
                    Set<String> collapseIntersections = interceptionsMap.get(key);
                    interceptionsMap.merge(iteratedValue, collapseIntersections, (x, y) -> {
                        x.addAll(y);
                        return x;
                    });
                    Set<List<String>> collapseRows = rowsMap.get(key);
                    rowsMap.merge(iteratedValue, collapseRows, (x, y) -> {
                        x.addAll(y);
                        return x;
                    });
                    isIntercept = true;
                }
            }
            System.out.println("Group<>: " + rowsMap.get(key) + ", interception: " + isIntercept);
            interceptionsMap.remove(key);
            rowsMap.remove(key);
        }
    }

    private void outputGroups() {
        for (Map.Entry<String, Set<List<String>>> entry : rowsMap.entrySet()) {
            System.out.println("Group<" + groupCounter++ + ">:" +
                    "\n---------------------------------------------------");
            entry.getValue().stream()
                    .sorted(Comparator.comparingInt(List::size))
                    .distinct().forEach(System.out::println);
        }
    }
}




