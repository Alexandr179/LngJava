package ru.peacockTeam;

import org.jgrapht.Graph;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.peacockTeam.utils.FiosUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class Processing {

    /**
     * https://javascopes.com/jgrapht-066252f4/
     * https://www.programcreek.com/java-api-examples/?api=org.jgrapht.traverse.DepthFirstIterator
     * <p>
     * REAL_GRAPH_SAMPLES:                                                                                                               https://jgrapht.org/guide/UserOverview
     * https://programming.vip/docs/using-the-jgrapht-library-to-manipulate-graphs.html
     * ..some other
     * https://stackoverflow.com/questions/57184523/jgrapht-how-to-represent-set-of-vertices-and-edges-as-efficiently-as-possible
     * https://stackoverflow.com/questions/32935692/jgrapht-apply-bfs-to-weightedgraph
     */

    @Autowired
    FiosUtil fiosUtil;

    public Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);//  DefaultDirectedGraph     DefaultDirectedWeightedGraph
    public Map<String, List<List<String>>> groupMap = new HashMap<>();
    public List<List<List<String>>> groupList = new CopyOnWriteArrayList<>();
    private Integer groupCounter = 1;

    public void process() throws IOException {
        System.out.println(">> API process. Building Graph... \t\t\t\t(jgrapht.v1.1.0)");
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String row;
        while ((row = csvReader.readLine()) != null) {
            List<String> rowSet = fiosUtil.getRowSet(row);
            createGraph(rowSet);
        }
        csvReader.close();
        System.out.println(">> Created graph. Graph vertexes are: " + graph.vertexSet().size() + "\t\t(unique digits in all row's)");
        boolean isEndOfGroups = graphIterate(graph);
        while (!isEndOfGroups) {
            isEndOfGroups = graphIterate(graph);
        }
        outOfGroups();
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
        graph.addEdge(row.get(0), row.get(row.size()-1));
    }

    public boolean graphIterate(Graph<String, DefaultEdge> graph) {
        List<List<String>> groupRows = new LinkedList<>();
        List<String> groupVertex = new ArrayList<>();
        Iterator<String> graphIterator = graph.vertexSet().iterator();
        if (graphIterator.hasNext()) {// getStartVertex
            DepthFirstIterator<String, DefaultEdge> depthFirstIterator = new DepthFirstIterator<>(graph, graphIterator.next());//todo: def:  BreadthFirstIterator
            while (depthFirstIterator.hasNext()) {
                String vertex = depthFirstIterator.next();
                groupVertex.add(vertex);
            }
            for (String vertex : groupVertex) {
                groupRows.addAll(new ArrayList<>(groupMap.get(vertex)));
            }
        }
        removeGraphEntrySaveRows(groupRows);
        return graph.vertexSet().size() == 0;
    }

    private void removeGraphEntrySaveRows(List<List<String>> group){
        List<List<String>> rowsGroup = group.stream()
                .peek(row -> row.forEach(vertex -> graph.removeVertex(vertex)))
                .distinct()
                .sorted(Comparator.comparingInt(List::size))
                .collect(Collectors.toList());
        groupList.add(rowsGroup);
    }

    public void outOfGroups(){
        Iterator<List<List<String>>> groupsIterator = groupList.iterator();
        while (groupsIterator.hasNext()){
            List<List<String>> currentGroup = groupsIterator.next();
            groupList.stream()
                    .filter(iterateGroup -> iterateGroup.equals(currentGroup))
                    .peek(iterateGroup -> {
                        iterateGroup.addAll(currentGroup);
                        groupList.remove(currentGroup);
                    });
        }

        for (List<List<String>> rowsGroup : groupList){// todo: print groups
            System.out.println("\nGroup<" + groupCounter++ + ">:" +
                    "\n----------------------------------------------------------------------------");
            rowsGroup.forEach(System.out::println);
        }
    }
}

