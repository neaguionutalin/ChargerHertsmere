package org.alin.chargerhertsmere;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private static final Integer QUEUE_SIZE = 30;
    @Getter
    private final List<String> queue = new ArrayList<>();

    public void addEntry(ResponseObject responseObject) {
        if (queue.size() == QUEUE_SIZE) shiftQueueElements();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Time: %s. ", Instant.now()));
        for (int i = 0; i < responseObject.getPods().size(); i++) {
            stringBuilder.append(String.format("%s - %s, %s - %s. %n", responseObject.getPods().get(i).getStatuses().get(0).getDoorId(),
                    responseObject.getPods().get(i).getStatuses().get(0).getLabel().charAt(0),
                    responseObject.getPods().get(i).getStatuses().get(1).getDoorId(),
                    responseObject.getPods().get(i).getStatuses().get(1).getLabel().charAt(0)));
        }
        queue.add(stringBuilder.toString());
    }

    private void shiftQueueElements() {
        for (int i = 1; i < QUEUE_SIZE; i++) {
            queue.set(i - 1, queue.get(i));
        }
        queue.remove(queue.get(QUEUE_SIZE - 1));
    }
}
