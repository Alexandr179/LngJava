package ru.peacockTeam;

import ru.peacockTeam.utils.FiosUtil;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import static ru.peacockTeam.utils.FiosUtil.getCurrentTime;

public class Processing {

    public static String SOURCE_LNG_FILE = "lng_test.csv";
    public FiosUtil fiosUtil;
    private Integer groupCounter = 0;
    private Integer rowCounter = 0;
    public volatile BufferedReader csvReader;
    public byte[] resourceFileByteStream;


    Map<String, Set<List<String>>> rowsMap = new HashMap<>();
    Map<String, Set<String>> vertexMap = new HashMap<>();


    public Processing() {
        this.fiosUtil = new FiosUtil();
    }


//    public List<Map<String, List<List<String>>>> groupMapList = new ArrayList<>();

    public void process() throws IOException {
        resourceFileByteStream = fiosUtil.getCopyByteArrayStream(fiosUtil.getResourceFileStream());
        csvReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(resourceFileByteStream)));
        System.out.println(">> API process. Building GroupMap...");
        String rowAsStr;
        while ((rowAsStr = csvReader.readLine()) != null) {
            List<String> row = fiosUtil.getRowSet(rowAsStr);
            if (!row.isEmpty()) buildGroupMap(row);
        }
        csvReader.close();
//        outputGroups();
    }

    public void buildGroupMap(List<String> row) {
        for(String rowItem : row){
            Set<String> vertexSet = new HashSet<>();
            Set<String> iterationRow = new HashSet<>(row);
            iterationRow.remove(rowItem);
            vertexSet.addAll(iterationRow);
            vertexMap.merge(rowItem, vertexSet, (x, y) -> {
                x.addAll(y);
                return x;
            });
        }


//        Set<String> vertexSet = new HashSet<>();
//        for (int i = 1; i < row.size()-1; i++) {
//            vertexSet.add(row.get(i));
//        }
//        vertexMap.putIfAbsent(row.get(0), vertexSet);
//        vertexMap.merge(row.get(0), vertexSet, (x, y) -> {
//            x.addAll(y);
//            return x;
//        });

//        Set<String> vertexSet = new HashSet<>();
//        vertexSet.addAll()
//        for(String vertex : row){
//        vertexMap.merge(vertex, vertexMap.get(vertex), (x, y) -> {
//            x.addAll(y);
//            return x;
//        });
//
//        Set<List<String>> rowSet = new HashSet<>();
//        rowsMap.merge(vertex, rowsMap.get(vertex), (x, y) -> {
//            x.addAll(y);
//            return x;
//        });
        }


//        rowCounter++;
//        Map<String, List<List<String>>> collapsedGroupMap = new HashMap<>();
//        boolean intersection = false;
//        for (String rowItem : row) {
//            Iterator<Map<String, List<List<String>>>> groupMapListIterator = groupMapList.iterator();
//            while (groupMapListIterator.hasNext()) {
//                Map<String, List<List<String>>> groupMap = groupMapListIterator.next();
//                if (groupMap.containsKey(rowItem)) {
//                    groupMap.forEach(collapsedGroupMap::put);
//                    putRowToGroupMap(row, collapsedGroupMap);
//                    groupMapListIterator.remove();
//                    intersection = true;
//                }
//            }
//            if (intersection) groupMapList.add(collapsedGroupMap);
////            System.err.println("DEBUG. FileRow<" + rowCounter + ">, total groups: " + groupMapList.size() + ". Time: " + getCurrentTime() + " [INTERSECTION]");
//        }
//        if (!intersection) putRowToNewGroupMap(row);
//    }

    private void putRowToGroupMap(List<String> row, Map<String, List<List<String>>> newGroupMap) {
        for (String rowItem : row) {
            List<List<String>> groupMapItemValue = new ArrayList<>();
            groupMapItemValue.add(row);
            newGroupMap.put(rowItem, groupMapItemValue);
        }
    }

//    private void putRowToNewGroupMap(List<String> row) {
//        Map<String, List<List<String>>> newGroupMap = new HashMap<>();
//        putRowToGroupMap(row, newGroupMap);
//        groupMapList.add(newGroupMap);
////        System.out.println("DEBUG. FileRow<" + rowCounter + ">, total groups: " + groupMapList.size() + ". Time: " + getCurrentTime());
//    }

//    private void outputGroups() {
//        System.out.println("\nAPI groups number: " + groupMapList.size());
//        for (Map<String, List<List<String>>> groupMap : groupMapList) {
//            System.out.println("\nGroup<" + groupCounter++ + ">:");
//            groupMap.values().stream()
//                    .sorted(Comparator.comparingInt(List::size))
//                    .distinct().forEach(System.out::println);
//        }
//    }
}




