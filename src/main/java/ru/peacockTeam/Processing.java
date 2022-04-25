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

/**
 * https://javascopes.com/jgrapht-066252f4/
 * https://www.programcreek.com/java-api-examples/?api=org.jgrapht.traverse.DepthFirstIterator
 * <p>
 * REAL_GRAPH_SAMPLES:
 * https://programming.vip/docs/using-the-jgrapht-library-to-manipulate-graphs.html
 * ..some other
 * https://stackoverflow.com/questions/57184523/jgrapht-how-to-represent-set-of-vertices-and-edges-as-efficiently-as-possible
 * https://stackoverflow.com/questions/32935692/jgrapht-apply-bfs-to-weightedgraph
 */
@Component
public class Processing {

    @Autowired
    FiosUtil fiosUtil;

    public Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
    public Map<String, List<List<String>>> groupMap = new HashMap<>();
    private Integer groupCounter = 0;

    public void process() throws IOException {
        System.out.println(">> API process. Building Graph...");
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String row;
        while ((row = csvReader.readLine()) != null) {
            List<String> rowSet = fiosUtil.getRowSet(row);
            createGraph(rowSet);
        }
        csvReader.close();
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
//                System.out.println("Vertex: " + item);
                groupMap.merge(item, list, (x, y) -> {// https://devmark.ru/article/java-map-new-methods
                    x.addAll(y);
                    return x.stream().distinct().collect(Collectors.toList());
                });
//                if (groupMap.get(item).size() > 1) System.out.println("INTERSECTION. GroupMap, key<" + item + ">: " + groupMap.get(item));
                if (!initItem.isEmpty() && !initItem.equals(item)) {
                    DefaultEdge defaultEdge = graph.addEdge(initItem, item);
                    initItem = item;
//                    System.out.println("Edge: " + defaultEdge);
                }
            }
        }
    }

    public boolean outOfGroups() {
        List<List<String>> groupRows = new LinkedList<>();
        List<String> groupVertex = new ArrayList<>();
        Iterator<String> graphIterator = graph.vertexSet().iterator();
        if (graphIterator.hasNext()) {// getStartVertex
            System.out.println("\nGroup<" + groupCounter++ + ">:");
            BreadthFirstIterator<String, DefaultEdge> breadthFirstIterator = new BreadthFirstIterator<>(graph, graphIterator.next());
            while (breadthFirstIterator.hasNext()) {
                String vertex = breadthFirstIterator.next();
                groupVertex.add(vertex);
            }
            for (String vertex : groupVertex) {
                groupRows.addAll(new ArrayList<>(groupMap.get(vertex)));
            }
        }
//        System.out.println("Vertex count: " + graph.vertexSet().size());
        removeGraphEntryOutputRows(groupRows);
//        System.out.println("Vertex count (after clear): " + graph.vertexSet().size());
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







