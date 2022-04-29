package ru.peacockTeam;

import ru.peacockTeam.utils.FiosUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Processing {

    public static String SOURCE_LNG_FILE = "lng_test.csv";
    private final FiosUtil fiosUtil;
    private Integer groupCounter = 1;
    public Map<String, Set<List<String>>> rowsMap = new HashMap<>();
    public Map<String, Set<String>> interceptionsMap = new ConcurrentHashMap<>();
    public List<Set<List<String>>> interceptionRowSetsList = new CopyOnWriteArrayList<>();
    public Map<List<String>, Set<List<String>>> interceptionGroupMap = new HashMap<>();

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
        outputGroups();
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
                }
            }
            Set<List<String>> interceptNextRows = rowsMap.get(key);
            interceptionRowSetsList.add(interceptNextRows);
//            System.out.println("Group<>: " + interceptNextRows);
            interceptionsMap.remove(key);
            rowsMap.remove(key);
        }
    }

    private void outputGroups() {
        Iterator<Set<List<String>>> interceptionRowSetIterator = interceptionRowSetsList.iterator();
        Set<List<String>> currentRowSet = interceptionRowSetIterator.next();// without check on null first..
        while (interceptionRowSetIterator.hasNext()){
            Set<List<String>> rowSet = interceptionRowSetIterator.next();
            boolean isIntercepted = currentRowSet.contains(rowSet);
            if(isIntercepted){
                rowSet.addAll(currentRowSet);
                interceptionRowSetsList.add(rowSet);
                interceptionRowSetsList.remove(rowSet);
            }
        }
        buildGroups(interceptionRowSetsList);
        interceptionGroupMap.values().stream()
                .sorted(Comparator.comparingInt(Set::size))
                .distinct().forEach(item -> System.out.println("\nGroup<" + groupCounter++ + ">: " + item));
    }

    public void buildGroups(List<Set<List<String>>> interceptionRowSetsList) {
        for (Set<List<String>> interceptionRowSet : interceptionRowSetsList) {
            Set<List<String>> iterationRowsSet = new HashSet<>(interceptionRowSet);
            for (List<String> row : iterationRowsSet) {
                Set<List<String>> vertexSet = new HashSet<>();
                Set<List<String>> iterationRowSet = new HashSet<>(vertexSet);
                iterationRowSet.remove(row);
                vertexSet.addAll(iterationRowsSet);
                interceptionGroupMap.merge(row, vertexSet, (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
        }
    }
}
























