package com.airtel.cs.model.response.adjustmentreason;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdjustmentReasonPOJO {
    private String status;
    private String statusCode;
    private List<ReasonDetail> result;
}