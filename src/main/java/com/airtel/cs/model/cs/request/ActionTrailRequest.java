package com.airtel.cs.model.cs.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ActionTrailRequest {
    private String msisdn;
    private String eventType;
    private Integer pageSize;
    private Integer pageNumber;
    private Map<String, String> clientInfo;
}
