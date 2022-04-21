package ru.peacockTeam.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.peacockTeam.Application.ROW_SET_QUEUE;
import static ru.peacockTeam.Application.STOP_ROW_SET;

@Component
public class QueueProducer implements Runnable {

    @Autowired
    FiosUtil fiosUtil;

    @Override
    public void run() {
        try {
            fillQueue();
        } catch (IOException ignored) {
        }
    }

    private void fillQueue() throws IOException {
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(fiosUtil.getResourceFileStream()));
        String row;
        while ((row = csvReader.readLine()) != null) {
            try {
                ROW_SET_QUEUE.put(getRowSet(row));
            } catch (InterruptedException ignored) {
            }
        }
        csvReader.close();
        try {
            ROW_SET_QUEUE.put(STOP_ROW_SET);
        } catch (InterruptedException ignored) {
        }
    }

    private Set<Long> getRowSet(String row) {
        return Arrays.stream(row.split(";"))
                .map(item -> item.replaceAll("\\\"", ""))
                .filter(item -> !item.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }
}
