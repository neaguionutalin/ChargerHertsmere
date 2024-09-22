package org.alin.chargerhertsmere;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

  private static final Integer QUEUE_SIZE = 10;
  @Getter private final List<String> queue = new ArrayList<>();

  public void addEntry(ResponseObject responseObject) {
    if (queue.size() == QUEUE_SIZE) shiftQueueElements();
    queue.add(
        String.format(
            "Time: %s. Charger %s is %s. Charger %s is %s.",
            Instant.now(),
            responseObject.getPods().get(0).getStatuses().get(0).getDoorId(),
            responseObject.getPods().get(0).getStatuses().get(0).getLabel(),
            responseObject.getPods().get(0).getStatuses().get(1).getDoorId(),
            responseObject.getPods().get(0).getStatuses().get(1).getLabel()));
  }

  private void shiftQueueElements() {
    for (int i = 1; i < QUEUE_SIZE; i++) {
      queue.set(i - 1, queue.get(i));
    }
    queue.remove(queue.get(QUEUE_SIZE-1));
  }
}
