package com.airtel.cs.model.response.hbb;

import com.airtel.cs.model.response.tariffplan.PlanResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HbbLinkedAccountsResponse {

    private String status;
    private Integer statusCode;
    private List<HbbLinkedAccountResult> result;
}
