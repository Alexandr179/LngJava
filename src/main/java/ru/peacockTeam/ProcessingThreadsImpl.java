package ru.peacockTeam;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static ru.peacockTeam.Application.*;
import static ru.peacockTeam.utils.QueueConsumer.lookTime;

@Component("ProcessingThreadsImpl")
public class ProcessingThreadsImpl implements Processing {

    Graph<Object, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    Long vertexCounter = 0L;
    boolean STOP_API;

    public void process() {
        System.out.println(">> API process..");
        try {
            graph.addVertex(ROW_SET_QUEUE.take());
            while (true) {
                ExecutorService pool = Executors.newFixedThreadPool(EMBEDDED_ROW_SET_THREADS);
                List<Callable<Object>> tasks = new ArrayList<>();
                try {
                    for (int i = 0; i < EMBEDDED_ROW_SET_THREADS; i++) {
                        tasks.add(new Callable<Object>() {
                            public Object call() throws Exception {
                                Set<Long> rowSet = ROW_SET_QUEUE.take();
                                if (rowSet.equals(STOP_ROW_SET)) {
                                    STOP_API = true;
                                    return null;
                                }
                                graph.addVertex(rowSet);
                                vertexCounter++;
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
                                return null;
                            }
                        });
                    }
                    pool.invokeAll(tasks);
                } catch (InterruptedException ignored) {
                } finally {
                    pool.shutdown();
                }
                if (STOP_API) break;
            }
        } catch (InterruptedException ignored) {
        }
    }
}







