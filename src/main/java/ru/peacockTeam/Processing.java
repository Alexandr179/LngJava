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

    List<Map<String, Set<List<String>>>> groupsMapList = new CopyOnWriteArrayList<>();

    public void process() throws IOException {
        System.out.println(">> API process. Building GroupMap...");
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String rowAsStr;
        groupsMapListInit(fiosUtil.getRowSet(csvReader.readLine()));
        while ((rowAsStr = csvReader.readLine()) != null) {
            List<String> row = fiosUtil.getRowSet(rowAsStr);
            if (!row.isEmpty()) buildGroupMap(row);
        }
        csvReader.close();
        outputGroups();
    }

    private void groupsMapListInit(List<String> row) {
        Set<List<String>> rowsList = new HashSet<>();
        Map<String, Set<List<String>>> groupMap = new HashMap<>();

        rowsList.add(row);// without first checked on conditional: !row.isEmpty()
        row.forEach(item -> groupMap.put(item, rowsList));
        groupsMapList.add(groupMap);
    }

    public void buildGroupMap(List<String> row) {
        Iterator<Map<String, Set<List<String>>>> groupsMapListIterator = groupsMapList.iterator();
        while (groupsMapListIterator.hasNext()) {
            Map<String, Set<List<String>>> groupMap = groupsMapListIterator.next();
            AtomicBoolean isInterception = new AtomicBoolean(false);
            for (String rowItem : row) {
                groupMap.keySet().stream()
                        .filter(keyGroupMap -> keyGroupMap.equals(rowItem))
                        .findAny()// merge - найдено совпадение ключа в row{1,2,3}  groupsMapList.groupMap()
                        .ifPresent(keyGroupMap -> {
                            boolean added = groupMap.get(keyGroupMap).add(row);// добавление row в groupMap
                            System.out.println("groupMap<" + keyGroupMap + ">: " + row);
                            isInterception.set(true);
                        });
                if(isInterception.get()) break;//NO ADD TO groupMap ..
            }
            if (!isInterception.get()) {
                Map<String, Set<List<String>>> nextGroupMap = new HashMap<>();
                nextGroupMap.put(row.get(0), Collections.singleton(row));
                System.err.println("NEW! groupMap<" + row.get(0) + ">: " + row);
                groupsMapList.add(nextGroupMap);// // добавление row в groupsMapList.newGroupMap (* groupsMapList - итерация через iterator)
            }
        }
    }

    public void outputGroups() {
        System.err.println("API number groups: " + groupsMapList.size());
    }


//        groupMap.merge(item, rowList, (x, y) -> {// https://devmark.ru/article/java-map-new-methods
//            x.addAll(y);
//            return x;
//        });

}



