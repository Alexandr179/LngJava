package ru.peacockTeam.utils;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * https://ask-dev.ru/info/27809/good-java-graph-algorithm-library
 * https://javascopes.com/jgrapht-066252f4/
 * https://code-examples.net/ru/q/c976
 */
public class Processing {

    Graph<Object, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

    public void process() throws IOException {
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FiosUtil().getResourceFileStream()));
        String row;

        graph.addVertex(getRowSet(csvReader.readLine()));
        while ((row = csvReader.readLine()) != null) {
            Set<Long> rowSet = getRowSet(row);
            graph.addVertex(rowSet);

//            for (Object vertex : graph.vertexSet()) {
//                Set<Long> current = new HashSet<>(rowSet);
//                current.retainAll((Set<Long>) vertex);
//                if (!current.isEmpty() && !current.equals(vertex)) {
//                    graph.addEdge(vertex, rowSet);
//                    Set<DefaultEdge> defaultEdges = graph.edgeSet();
//                    defaultEdges.forEach(System.err::println);
//                }
//            }

            graph.vertexSet().parallelStream()
                    .filter(vertex -> {
                        Set<Long> current = new CopyOnWriteArraySet<>(rowSet);
                        current.retainAll((Set<Long>) vertex);
                        return !current.isEmpty() && !current.equals(vertex);
                    })
                    .unordered()
                    .forEach(vertex -> {graph.addEdge(vertex, rowSet);
                        Set<DefaultEdge> defaultEdges = graph.edgeSet();
                        defaultEdges.forEach(System.err::println);});

        }
        csvReader.close();
    }

    private Set<Long> getRowSet(String row) {
        return Arrays.stream(row.split(";"))
                .map(item -> item.replaceAll("\\\"", ""))
                .filter(item -> !item.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }
}


















