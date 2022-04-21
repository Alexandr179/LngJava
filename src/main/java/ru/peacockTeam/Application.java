package ru.peacockTeam;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import ru.peacockTeam.config.ApplicationConfig;
import ru.peacockTeam.utils.QueueConsumer;
import ru.peacockTeam.utils.QueueProducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

public class Application {

    public static ApplicationContext CONTEXT;
    public static ExecutorService EXECUTOR_SERVICE;
    public static int COUNT_EXECUTOR_SERVICE_THREADS;
    public static int EMBEDDED_ROW_SET_THREADS;
    public static Integer QUEUE_CAPACITY;
    public static BlockingQueue<Set<Long>> ROW_SET_QUEUE;
    public static Set<Long> STOP_ROW_SET = new HashSet<>();
    public static long PARSING_TIME;
    public static String SOURCE_LNG_FILE;

    public static void main(String[] args) {
        initApi();
    }

    private static void initApi() {
        CONTEXT = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Environment environment = CONTEXT.getBean(Environment.class);
        COUNT_EXECUTOR_SERVICE_THREADS = Integer.parseInt(Objects.requireNonNull(environment.getProperty("COUNT_EXECUTOR_SERVICE_THREADS")));
        EMBEDDED_ROW_SET_THREADS = Integer.parseInt(Objects.requireNonNull(environment.getProperty("EMBEDDED_ROW_SET_THREADS")));
        QUEUE_CAPACITY = Integer.parseInt(Objects.requireNonNull(environment.getProperty("QUEUE_CAPACITY")));
        ROW_SET_QUEUE = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        SOURCE_LNG_FILE = environment.getProperty("SOURCE_LNG_FILE");
        formStopSet();
        PARSING_TIME = System.nanoTime();
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(COUNT_EXECUTOR_SERVICE_THREADS);
        EXECUTOR_SERVICE.submit(CONTEXT.getBean(QueueConsumer.class));
        EXECUTOR_SERVICE.submit(CONTEXT.getBean(QueueProducer.class));
        EXECUTOR_SERVICE.shutdown();
        try {
            EXECUTOR_SERVICE.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ignored) {
        }
    }

    private static void formStopSet() {
        STOP_ROW_SET.add(1l);
        STOP_ROW_SET.add(1l);
        STOP_ROW_SET.add(1l);
        STOP_ROW_SET.add(1l);
    }
}
