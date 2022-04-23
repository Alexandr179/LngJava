package ru.peacockTeam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.peacockTeam.utils.FiosUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Processing {

    @Autowired
    FiosUtil fiosUtil;

    List<Map<String, ArrayList<ArrayList<String>>>> groupMapList = new CopyOnWriteArrayList<>();
    Long rowCounter = 0L;

    public void process() throws IOException {
        System.out.println(">> API process. Building GroupMap...");
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String rowAsStr;
        while ((rowAsStr = csvReader.readLine()) != null) {
            List<String> row = fiosUtil.getRowSet(rowAsStr);
            if (!row.isEmpty()) buildGroupMap(row);
        }
        csvReader.close();
        outputGroups();
    }

    private void outputGroups() {
        System.out.println("\nAPI number groups: " + groupMapList.size());
//        int groupCounter = 0;
//        for(Map<String, ArrayList<ArrayList<String>>> groupMap : groupMapList){
//            System.out.println("\nGroup<" + groupCounter++ + "> rows:");
//            ArrayList<ArrayList<String>> rows = groupMap.values().stream()
//                    .distinct()
//                    .max(Comparator.comparing(ArrayList::size)).get();
//            System.out.println(rows);
//        }
    }


    public void buildGroupMap(List<String> row) {
        AtomicBoolean intersection = new AtomicBoolean(false);
        for (String rowItem : row) {
            Iterator<Map<String, ArrayList<ArrayList<String>>>> groupMapListIterator = groupMapList.iterator();
            while (groupMapListIterator.hasNext()) {
                Map<String, ArrayList<ArrayList<String>>> groupMap = groupMapListIterator.next();
                if (groupMap.containsKey(rowItem)) {
                    putRowToGroupMap(row, groupMap);
                    System.out.println("Row: " + rowCounter++ + ", total groups: " + groupMapList.size());
                    intersection.set(true);
                }
            }
        }
        if (!intersection.get()) {
            putRowNewGroupMap(row);
        }
    }

    private void putRowNewGroupMap(List<String> row) {
        Map<String, ArrayList<ArrayList<String>>> newGroupMap = new HashMap<>();
        putRowToGroupMap(row, newGroupMap);
        groupMapList.add(newGroupMap);
        System.out.println("Row: " + rowCounter++ + ", total groups: " + groupMapList.size());
    }

    private void putRowToGroupMap(List<String> row, Map<String, ArrayList<ArrayList<String>>> newGroupMap) {
        for (String rowItem : row) {
            ArrayList<String> copyRow = new ArrayList<>(row);
            ArrayList<ArrayList<String>> groupMapItemValue = new ArrayList<>();
            groupMapItemValue.add(copyRow);
            newGroupMap.put(rowItem, groupMapItemValue);
        }
    }
}




