package org.alin.chargerhertsmere;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObject {

    private List<Pod> pods;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pod implements Comparator<Pod> {

        private int id;
        private List<Status> statuses;

        @Override
        public int compare(Pod o1, Pod o2) {
            return o1 != null && o2 != null ? o1.id < o2.id ? 1 : -1 : 0;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Status implements Comparator<Status> {
            @JsonProperty("door_id")
            private Integer doorId;
            private String label;
            private ActiveCharge activeCharge;

            @Override
            public int compare(Status o1, Status o2) {
                return o1 != null && o2 != null ? o1.doorId < o2.doorId ? 1 : -1 : 0;
            }

            @Data
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ActiveCharge {
                private boolean canStopCharge;
                private boolean startingCharge;
            }
        }
    }
}
