package ru.peacockTeam;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.peacockTeam.utils.FiosUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * https://ask-dev.ru/info/27809/good-java-graph-algorithm-library
 * https://javascopes.com/jgrapht-066252f4/
 * https://code-examples.net/ru/q/c976
 */
@Component
public class Processing {

    @Autowired
    FiosUtil fiosUtil;

    Graph<Object, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    AtomicLong vertexCounter = new AtomicLong();

    public void process() throws IOException {
        System.out.println(">> API process..");
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String row;
        while ((row = csvReader.readLine()) != null) {
            Set<String> rowSet = fiosUtil.getRowSet(row);
            parseRow(rowSet);

        }
        csvReader.close();
    }


    public void parseRow(Set<String> rowSet) {
        graph.addVertex(rowSet);
        System.out.println("RowSet: " + rowSet);
//                vertexCounter.incrementAndGet();
//                graph.vertexSet().parallelStream()
//                        .filter(streamVertex -> {
//                            Set<Long> copyRowSet = new HashSet<>(rowSet);
//                            copyRowSet.retainAll((Set<Long>) streamVertex);
//                            return !copyRowSet.isEmpty() && !copyRowSet.equals(streamVertex);
//                        })
//                        .unordered()
//                        .forEach(vertex -> {
//                            graph.addEdge(vertex, rowSet);
//                            Set<DefaultEdge> defaultEdges = graph.edgeSet();
//                            defaultEdges.forEach(edge -> System.out.println("Edge: " + edge + " .." + vertexCounter + " Vertex'es are become."));
//                            lookTime();
//                        });
    }
}







