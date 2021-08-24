package com.airtel.cs.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LimitConfigRequest {
    private String featurekey;
    private String dailyLimit;
    private String monthlyLimit;
    private String transactionLimit;
}
