package com.airtel.cs.pojo.response.ticketlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatePOJO {

    private int id;
    private String externalStateName;
    private boolean isReopenedState;
    private InternalState internalState;
    private boolean active;
}

