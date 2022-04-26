package ru.peacockTeam;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.peacockTeam.utils.FiosUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Processing {

    @Autowired
    FiosUtil fiosUtil;

    public Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
    public Map<String, List<List<String>>> groupMap = new HashMap<>();
    private Integer groupCounter = 0;

    public void process() throws IOException {
        System.out.println(">> API process. Building Graph... \t\t\t\t(jgrapht.v1.1.0)");
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String row;
        while ((row = csvReader.readLine()) != null) {
            List<String> rowSet = fiosUtil.getRowSet(row);
            createGraph(rowSet);
        }
        csvReader.close();
        System.out.println(">> Created graph. Graph vertexes are: " + graph.vertexSet().size() +
                "\t\t(unique digits in all row's)");
        boolean isEndOfGroups = outOfGroups();
        while (!isEndOfGroups) {
            isEndOfGroups = outOfGroups();
        }
    }

    public void createGraph(List<String> row) {
        List<List<String>> list = new ArrayList<>();
        list.add(row);
        String initItem = "";
        if (!row.isEmpty()) initItem = row.get(0);
        for (String item : row) {
            if (!item.isEmpty()) {
                graph.addVertex(item);
                groupMap.merge(item, list, (x, y) -> {
                    x.addAll(y);
                    return x.stream().distinct().collect(Collectors.toList());
                });
                if (!initItem.isEmpty() && !initItem.equals(item)) {
                    graph.addEdge(initItem, item);
                    initItem = item;
                }
            }
        }
    }

    public boolean outOfGroups() {
        List<List<String>> groupRows = new LinkedList<>();
        List<String> groupVertex = new ArrayList<>();
        Iterator<String> graphIterator = graph.vertexSet().iterator();
        if (graphIterator.hasNext()) {// getStartVertex
            System.out.println("\nGroup<" + groupCounter++ + ">:" +
                    "\n----------------------------------------------------------------------------");
            BreadthFirstIterator<String, DefaultEdge> breadthFirstIterator = new BreadthFirstIterator<>(graph, graphIterator.next());
            while (breadthFirstIterator.hasNext()) {
                String vertex = breadthFirstIterator.next();
                groupVertex.add(vertex);
            }
            for (String vertex : groupVertex) {
                groupRows.addAll(new ArrayList<>(groupMap.get(vertex)));
            }
        }
        removeGraphEntryOutputRows(groupRows);
        return graph.vertexSet().size() == 0;
    }

    private void removeGraphEntryOutputRows(List<List<String>> groupRows){
        groupRows.stream()
                .peek(row -> row.forEach(vertex -> graph.removeVertex(vertex)))
                .distinct()
                .sorted(Comparator.comparingInt(List::size))
                .forEach(System.out::println);
    }
}