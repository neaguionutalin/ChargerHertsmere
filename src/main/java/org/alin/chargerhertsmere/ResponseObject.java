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
    public static class Pod implements Comparable<Pod> {

        private int id;
        private List<Status> statuses;

        @Override
        public int compareTo(Pod o) {
            return Integer.compare(this.id, o.id);
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Status implements Comparable<Status> {
            @JsonProperty("door_id")
            private Integer doorId;
            private String label;
            private ActiveCharge activeCharge;

            @Override
            public int compareTo(Status o) {
                return this.doorId.compareTo(o.doorId);
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
