package org.alin.chargerhertsmere;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwiftObject {

    private boolean phone;
    private ResponseObject responseObject;
    private List<String> statuses;
}
