package ru.peacockTeam;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static ru.peacockTeam.Application.ROW_SET_QUEUE;
import static ru.peacockTeam.Application.STOP_ROW_SET;
import static ru.peacockTeam.utils.QueueConsumer.lookTime;

/**
 * https://ask-dev.ru/info/27809/good-java-graph-algorithm-library
 * https://javascopes.com/jgrapht-066252f4/
 * https://code-examples.net/ru/q/c976
 */
@Component("ProcessingImpl")
public class ProcessingImpl implements Processing {

    Graph<Object, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    AtomicLong vertexCounter = new AtomicLong();

    public void process() {
        System.out.println(">> API process..");
        try {
            graph.addVertex(ROW_SET_QUEUE.take());
            while (true) {
                Set<Long> rowSet = ROW_SET_QUEUE.take();
                if (rowSet.equals(STOP_ROW_SET)) break;
                graph.addVertex(rowSet);
                vertexCounter.incrementAndGet();
                graph.vertexSet().parallelStream()
                        .filter(streamVertex -> {
                            Set<Long> copyRowSet = new HashSet<>(rowSet);
                            copyRowSet.retainAll((Set<Long>) streamVertex);
                            return !copyRowSet.isEmpty() && !copyRowSet.equals(streamVertex);
                        })
                        .unordered()
                        .forEach(vertex -> {
                            graph.addEdge(vertex, rowSet);
                            Set<DefaultEdge> defaultEdges = graph.edgeSet();
                            defaultEdges.forEach(edge -> System.out.println("Edge: " + edge + " .." + vertexCounter + " Vertex'es are become."));
                            lookTime();
                        });
            }
        } catch (InterruptedException ignored) {
        }
    }
}







