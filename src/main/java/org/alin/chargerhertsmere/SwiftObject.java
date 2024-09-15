package org.alin.chargerhertsmere;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwiftObject {

    private boolean phone;
    private ResponseObject responseObject;
}
