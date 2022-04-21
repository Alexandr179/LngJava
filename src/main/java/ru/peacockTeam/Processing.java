package ru.peacockTeam;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.peacockTeam.utils.FiosUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

/**
 * https://javascopes.com/jgrapht-066252f4/
 * https://www.programcreek.com/java-api-examples/?api=org.jgrapht.traverse.DepthFirstIterator
 */
@Component
public class Processing {

    @Autowired
    FiosUtil fiosUtil;

    Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
    Map<String, List<List<String>>> groupMap = new HashMap<>();

    Integer startVertexCounter = 0;

    public void process() throws IOException {
        System.out.println(">> API process. Building Graph...");
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String row;
        while ((row = csvReader.readLine()) != null) {
            List<String> rowSet = fiosUtil.getRowSet(row);
            createGraph(rowSet);
        }
        csvReader.close();
        startVertexCounter = graph.vertexSet().size();
        outputGroups(graph, groupMap);
//        imaginaryGraph(graph);// from no_big_size graph
    }

    private void sleepPaarSecunde() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    public void imaginaryGraph(Graph<String, DefaultEdge> graph) throws IOException {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/main/resources/graph.png");
        ImageIO.write(image, "PNG", imgFile);
        System.err.println("Imaginary Graph to graph.png");
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
                    return x;
                });
//                if (groupMap.get(item).size() > 1) System.out.println("INTERSECTION. GroupMap, key<" + item + ">: " + groupMap.get(item));
                if (!initItem.isEmpty() && !initItem.equals(item)) {
                    DefaultEdge defaultEdge = graph.addEdge(initItem, item);
                    initItem = item;
//                    System.err.println("Edge: " + defaultEdge);
                }
            }
        }
    }

    Long vertexCounter = 0L;
    List<String> vertexes = new LinkedList<>();

    public void outputGroups(Graph<String, DefaultEdge> graph, Map<String, List<List<String>>> groupMap) {
        // todo: getting all the paths
        int groupsCounter = 0;
//        while (graph.vertexSet().size() != 0){
            Iterator<String> iter = new DepthFirstIterator<>(graph);
            while (iter.hasNext()) {
                String vertex = iter.next();
                groupMap.get(vertex).forEach(row -> System.out.println("Row: " + row + ". Vertex<" + vertex + "> number: " + vertexCounter++));//.sort(Comparator.comparingInt(List::size));

                Set<DefaultEdge> defaultEdges = graph.edgesOf(vertex);
                graph.removeAllEdges(defaultEdges);
            }
            groupsCounter ++;
//        }
        System.err.println("Vertex graph start_number: " + startVertexCounter);
        System.err.println("Vertex graph number: " + graph.vertexSet().size());
        System.err.println("API number groups: " + groupsCounter);

        //  todo: getting all the paths between 2 random countries
//        AllDirectedPaths<String, DefaultEdge> paths = new AllDirectedPaths<>(graph);
//        List<GraphPath<String, DefaultEdge>> longestPath = paths.getAllPaths("", "", true, null);
//        GraphPath<String, DefaultEdge> obj = null;
//        double maxlenght=0;
//        for( GraphPath<String, DefaultEdge> pa :longestPath ) {
//            if(pa.getLength()>maxlenght)
//                obj= pa;
//        }

        //  todo: Subgraph
//        StrongConnectivityAlgorithm<String, DefaultEdge> scAlg = new KosarajuStrongConnectivityInspector<>(graph);
//        List<Graph<String, DefaultEdge>> stronglyConnectedComponents = scAlg.getStronglyConnectedComponents();
//        System.err.println("API count groups are: " + stronglyConnectedComponents.size());
    }



}







