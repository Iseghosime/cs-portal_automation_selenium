package com.airtel.cs.model.cs.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferDetailRequest {
    private String msisdn;
    private Boolean requestDADetailsFlag;
}
